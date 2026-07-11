package com.pcwk.ehr.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.user.domain.UserSearchDTO;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;
import com.pcwk.ehr.user.util.PasswordUtil;

/**
 * 담당 핵심 정책:
 * 1. 회원가입 시 필수값을 검사합니다.
 * 2. 팀 공통코드 기준으로 회원권한/회원상태를 저장합니다.
 *    - USER_ROLE   : 01 일반회원, 02 관리자
 *    - USER_STATUS : 01 정상, 02 탈퇴, 03 휴면, 04 정지
 * 3. 01/03/04 상태 회원 기준으로 아이디/전화번호/이메일 중복을 막습니다.
 * 4. 탈퇴 회원의 아이디는 재가입에 사용할 수 없고, 전화번호/이메일은 재사용 가능하도록 처리합니다.
 * 5. 비밀번호는 평문으로 저장하지 않고 SHA-256 해시값으로 저장합니다.
 * 6. 회원탈퇴는 DELETE가 아니라 USER_STATUS='02' 상태 변경으로 처리합니다.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    // 회원권한 공통코드: 01=일반회원, 02=관리자
    private static final String ROLE_USER = "01";
    private static final String ROLE_ADMIN = "02";

    // 회원상태 공통코드: 01=정상, 02=탈퇴, 03=휴면, 04=정지
    private static final String STATUS_ACTIVE = "01";
    private static final String STATUS_WITHDRAWN = "02";
    private static final String STATUS_DORMANT = "03";
    private static final String STATUS_BLOCKED = "04";

    @Autowired
    private UserMapper userMapper;

    /**
     * 회원가입 처리
     *
     * 처리 흐름:
     * 1. 입력값 앞뒤 공백 정리
     * 2. 필수값 검증
     * 3. 아이디 중복 검사
     * 4. 전화번호 중복 검사
     * 5. 이메일 중복 검사
     * 6. 비밀번호 해시 처리
     * 7. 기본 권한/상태 세팅
     * 8. USER_INFO INSERT
     *
     * 재가입 정책:
     * - 아이디는 탈퇴 회원까지 포함해서 중복을 막습니다.
     * - 전화번호와 이메일은 02 탈퇴 회원을 제외하고 중복을 검사합니다.
     */
    @Override
    @Transactional
    public int join(UserVO user) {
        // 사용자가 입력한 문자열 앞뒤 공백을 제거 
        normalizeUserInput(user);

        // 회원가입에 꼭 필요한 값이 있는지 확인 
        // 필수값이 없으면 IllegalArgumentException을 발생 
        validateJoinUser(user);

        // 아이디, 전화번호, 이메일 중복 검사 
        if (userMapper.countByUserId(user.getUserId()) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (userMapper.countByPhoneNum(user.getPhoneNum()) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
        }

        if (!isBlank(user.getEmail()) && userMapper.countByEmail(user.getEmail()) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 해시 처리
        user.setPassword(PasswordUtil.hash(user.getPassword()));

        // 닉네임이 비어 있으면 이름을 닉네임으로 사용
        if (isBlank(user.getNickname())) {
            user.setNickname(user.getUserName());
        }

        // 일반 회원가입은 무조건 01 일반회원 권한으로 시작
        user.setUserRole(ROLE_USER);

        // 신규 가입 회원은 01 정상 상태로 시작
        user.setUserStatus(STATUS_ACTIVE);

        return userMapper.insertUser(user);
    }

    /**
     * 로그인 처리
     *
     * 처리 흐름:
     * 1. 아이디/비밀번호 입력 여부 확인
     * 2. USER_ID로 회원 조회
     * 3. 비밀번호 해시 비교
     * 4. 회원상태 확인
     * 5. 세션 저장용으로 password 제거 후 반환
     */
    @Override
    public UserVO login(String userId, String password) {
        // 로그인 화면에서 아이디나 비밀번호가 비었을 경우 예외 처리
        if (isBlank(userId) || isBlank(password)) {
            throw new IllegalArgumentException("아이디와 비밀번호를 입력하세요.");
        }

        // USER_ID로 회원정보를 조회
        UserVO savedUser = userMapper.selectByUserId(userId.trim());

        // 조회 결과 없을 때
        if (savedUser == null) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 사용자가 입력한 평문 비밀번호를 해시 처리한 뒤, DB에 저장된 해시 비밀번호와 비교
        if (!PasswordUtil.matches(password, savedUser.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 02 탈퇴 회원은 로그인 금지
        if (STATUS_WITHDRAWN.equals(savedUser.getUserStatus())) {
            throw new IllegalStateException("탈퇴 처리된 회원입니다.");
        }

        // 03 휴면 회원은 로그인 금지
        if (STATUS_DORMANT.equals(savedUser.getUserStatus())) {
            throw new IllegalStateException("휴면 상태의 회원입니다.");
        }

        // 04 정지 회원은 로그인 금지
        if (STATUS_BLOCKED.equals(savedUser.getUserStatus())) {
            throw new IllegalStateException("관리자에 의해 정지된 회원입니다.");
        }

        // 현재 허용하는 로그인 상태는 01 정상 상태만 허용
        if (!STATUS_ACTIVE.equals(savedUser.getUserStatus())) {
            throw new IllegalStateException("정상 상태의 회원이 아닙니다.");
        }

        // 세션에 비밀번호 해시값까지 저장하지 않도록 제거
        savedUser.setPassword(null);

        return savedUser;
    }

    /**
     * 회원번호 기준 단건 조회
     */
    @Override
    public UserVO getUser(Long userNum) {

    	if (userNum == null) {
            throw new IllegalArgumentException("회원번호가 없습니다.");
        }

        // USER_NUM으로 USER_INFO를 조회
        UserVO user = userMapper.selectByUserNum(userNum);

        if (user == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        // 화면 표시용 객체에도 비밀번호 해시값은 넘기지 않음
        user.setPassword(null);

        return user;
    }

    /**
     * 회원정보 수정
     *
     * 수정 가능 항목:
     * - 이름
     * - 닉네임
     * - 전화번호
     * - 이메일
     * - 생년월일
     *
     * 중복 정책:
     * - 전화번호/이메일 수정 시 자기 자신은 제외하고 검사
     * - 02 탈퇴 회원은 중복 검사 대상에서 제외
     */
    @Override
    @Transactional
    public int updateUser(UserVO user) {

        normalizeUserInput(user);

        if (user == null || user.getUserNum() == null) {
            throw new IllegalArgumentException("수정할 회원번호가 없습니다.");
        }

        validateUserName(user.getUserName());

        if (isBlank(user.getPhoneNum())) {
            throw new IllegalArgumentException("전화번호는 필수입니다.");
        }

        // 중복 검사
        if (userMapper.countByPhoneNumForUpdate(user) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
        }

        // 입력했을 때만 중복 검사
        if (!isBlank(user.getEmail()) && userMapper.countByEmailForUpdate(user) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임이 비어 있으면 이름을 닉네임으로 사용
        if (isBlank(user.getNickname())) {
            user.setNickname(user.getUserName());
        }

        return userMapper.updateUser(user);
    }

    /**
     * 비밀번호 변경
     */
    @Override
    @Transactional
    public int updatePassword(Long userNum, String currentPassword, String newPassword) {

        if (userNum == null) {
            throw new IllegalArgumentException("회원번호가 없습니다.");
        }

        if (isBlank(currentPassword) || isBlank(newPassword)) {
            throw new IllegalArgumentException("현재 비밀번호와 새 비밀번호를 입력하세요.");
        }

        // DB에서 현재 회원의 저장된 비밀번호 해시값 조회
        UserVO savedUser = userMapper.selectByUserNum(userNum);

        if (savedUser == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        // 현재 비밀번호가 맞는지 먼저 확인
        if (!PasswordUtil.matches(currentPassword, savedUser.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호를 해시 처리한 뒤 update용 UserVO에 담음
        UserVO updateUser = new UserVO();
        updateUser.setUserNum(userNum);
        updateUser.setPassword(PasswordUtil.hash(newPassword));

        // PASSWORD와 UPDATE_DT를 변경
        return userMapper.updatePassword(updateUser);
    }

    /**
     * 회원탈퇴
     *
     * 탈퇴 방식:
     * - DELETE FROM USER_INFO를 하지 않고,
     *   USER_STATUS='02', WITHDRAW_DT=SYSDATE로 변경
     *
     * 재가입 정책:
     * - 탈퇴 회원의 USER_ID는 재사용 불가
     * - 탈퇴 회원의 PHONE_NUM/EMAIL은 중복 검사에서 제외
     */
    @Override
    @Transactional
    public int withdrawUser(Long userNum, String password) {

    	if (userNum == null) {
            throw new IllegalArgumentException("회원번호가 없습니다.");
        }

        if (isBlank(password)) {
            throw new IllegalArgumentException("비밀번호를 입력하세요.");
        }

        // 현재 회원의 저장된 비밀번호 해시값을 조회
        UserVO savedUser = userMapper.selectByUserNum(userNum);

        if (savedUser == null) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        // 이미 탈퇴된 회원을 다시 탈퇴 처리하지 않도록 함
        if (STATUS_WITHDRAWN.equals(savedUser.getUserStatus())) {
            throw new IllegalStateException("이미 탈퇴 처리된 회원입니다.");
        }

        // 사용자가 입력한 비밀번호가 현재 비밀번호와 같은지 확인
        if (!PasswordUtil.matches(password, savedUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // USER_STATUS, WITHDRAW_DT, UPDATE_DT 변경
        return userMapper.withdrawUser(userNum);
    }

    /**
     * 관리자 회원목록 조회
     */
    @Override
    public List<UserVO> getUserList(UserSearchDTO searchDTO) {
        // searchDTO가 null이면 기본 검색조건을 새로 만듬
        if (searchDTO == null) {
            searchDTO = new UserSearchDTO();
        }

        return userMapper.selectUserList(searchDTO);
    }

    /**
     * 관리자 회원목록 총 건수 조회
     */
    @Override
    public int getUserTotalCount(UserSearchDTO searchDTO) {

    	if (searchDTO == null) {
            searchDTO = new UserSearchDTO();
        }

        return userMapper.selectUserTotalCount(searchDTO);
    }

    /**
     * 관리자 회원상태 변경
     */
    @Override
    @Transactional
    public int changeUserStatus(Long userNum, String userStatus) {

        if (userNum == null) {
            throw new IllegalArgumentException("회원번호가 없습니다.");
        }

        // 허용된 상태값만 저장되도록 함
        if (!STATUS_ACTIVE.equals(userStatus)
                && !STATUS_WITHDRAWN.equals(userStatus)
                && !STATUS_DORMANT.equals(userStatus)
                && !STATUS_BLOCKED.equals(userStatus)) {
            throw new IllegalArgumentException("허용되지 않은 회원상태입니다.");
        }

        // Mapper에 전달할 update용 UserVO 생성
        UserVO user = new UserVO();
        user.setUserNum(userNum);
        user.setUserStatus(userStatus);

        return userMapper.updateUserStatus(user);
    }

    /**
     * 관리자 회원권한 변경
     */
    @Override
    @Transactional
    public int changeUserRole(Long userNum, String userRole) {

        if (userNum == null) {
            throw new IllegalArgumentException("회원번호가 없습니다.");
        }

        // 허용된 권한값만 저장 가능
        if (!ROLE_USER.equals(userRole) && !ROLE_ADMIN.equals(userRole)) {
            throw new IllegalArgumentException("허용되지 않은 회원권한입니다.");
        }

        // Mapper에 전달할 update용 UserVO 생성
        UserVO user = new UserVO();
        user.setUserNum(userNum);
        user.setUserRole(userRole);

        return userMapper.updateUserRole(user);
    }

    // 회원가입 필수값 검증 메서드
    private void validateJoinUser(UserVO user) {

        if (user == null) {
            throw new IllegalArgumentException("회원정보가 없습니다.");
        }

        if (isBlank(user.getUserId())) {
            throw new IllegalArgumentException("아이디는 필수입니다.");
        }

        if (isBlank(user.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        validateUserName(user.getUserName());

        if (isBlank(user.getPhoneNum())) {
            throw new IllegalArgumentException("전화번호는 필수입니다.");
        }
    }


    // 이름 형식 검증: 한글/영문만 허용하고 숫자, 특수문자, 내부 공백은 금지
    private void validateUserName(String userName) {
        if (isBlank(userName)) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        if (!userName.matches("^[가-힣a-zA-Z]+$")) {
            throw new IllegalArgumentException("이름은 한글 또는 영문만 입력할 수 있습니다.");
        }
    }

    // 사용자 입력값 앞뒤 공백을 정리
    private void normalizeUserInput(UserVO user) {

        if (user == null) {
            return;
        }

        user.setUserId(trimToNull(user.getUserId()));
        user.setPassword(trimToNull(user.getPassword()));
        user.setUserName(trimToNull(user.getUserName()));
        user.setNickname(trimToNull(user.getNickname()));
        user.setPhoneNum(trimToNull(user.getPhoneNum()));
        user.setEmail(trimToNull(user.getEmail()));
    }

    // 문자열 앞뒤 공백 제거 후, 빈 문자열이면 null로 변경
    private String trimToNull(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }

    // 문자열이 null이거나 공백인지 확인
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
