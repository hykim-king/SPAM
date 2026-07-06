package com.pcwk.ehr.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.user.domain.UserSearchDTO;
import com.pcwk.ehr.user.domain.UserVO;

public interface UserMapper {

    /*
     * 회원가입 INSERT
     * - Service에서 필수값 검증, 중복검사, 비밀번호 해시 처리 후 호출
     * - USER_NUM은 XML에서 SEQ_USER_INFO.NEXTVAL로 생성합
     */
    int insertUser(UserVO user);

    /*
     * 회원번호 기준 단건 조회
     * - 마이페이지 조회
     * - 회원정보 수정 화면 조회
     * - 관리자 회원상세 조회
     * - 비밀번호 변경/회원탈퇴 시 현재 회원 확인
     */
    UserVO selectByUserNum(@Param("userNum") Long userNum);

    /*
     * 아이디 기준 단건 조회
     * - 로그인 처리에서 사용
     * - 탈퇴 회원도 조회해야 "탈퇴 처리된 회원입니다." 같은 메시지를 줄 수 있으므로
     *   이 조회는 USER_STATUS 조건을 걸지 않음
     */
    UserVO selectByUserId(@Param("userId") String userId);

    /*
     * 회원가입 아이디 중복 검사
     *
     * 현재 정책:
     * - 01 정상/03 휴면/04 정지 회원의 아이디는 중복 불가
     * - 02 탈퇴 회원의 아이디는 재가입 가능
     *
     * 따라서 SQL에서는 USER_STATUS <> '02' 조건 사용
     */
    int countByUserId(@Param("userId") String userId);

    /*
     * 회원가입 전화번호 중복 검사
     *
     * 현재 정책:
     * - 01 정상/03 휴면/04 정지 회원의 전화번호는 중복 불가
     * - 02 탈퇴 회원의 전화번호는 재가입 가능
     */
    int countByPhoneNum(@Param("phoneNum") String phoneNum);

    /*
     * 회원가입 이메일 중복 검사
     *
     * Service에서 이메일이 비어 있지 않을 때만 호출
     * 현재 정책은 아이디/전화번호와 동일하게 02 탈퇴 회원은 중복 검사에서 제외
     */
    int countByEmail(@Param("email") String email);

    /*
     * 회원정보 수정 시 전화번호 중복 검사
     *
     * 수정 시에는 자기 자신의 전화번호는 허용
     * - USER_NUM != #{userNum} : 자기 자신 제외
     * - USER_STATUS <> '02' : 02 탈퇴 회원 제외
     */
    int countByPhoneNumForUpdate(UserVO user);

    /*
     * 회원정보 수정 시 이메일 중복 검사
     *
     * 전화번호 수정 검사와 같이 자기 자신, 탈퇴 회원 제외
     */
    int countByEmailForUpdate(UserVO user);

    /*
     * 일반 회원정보 수정
     * - 이름
     * - 닉네임
     * - 전화번호
     * - 이메일
     * - 생년월일
     */
    int updateUser(UserVO user);

    /*
     * 비밀번호 변경
     * - Service에서 현재 비밀번호 검증 후 새 비밀번호를 해시 처리해 전달
     */
    int updatePassword(UserVO user);

    /*
     * 회원탈퇴
     * - USER_STATUS를 02 탈퇴로 바꾸고 WITHDRAW_DT를 저장
     */
    int withdrawUser(@Param("userNum") Long userNum);

    /*
     * 관리자 회원목록 조회
     * - 검색 조건과 페이징 조건은 UserSearchDTO에 담겨 들어옴
     */
    List<UserVO> selectUserList(UserSearchDTO searchDTO);

    /*
     * 관리자 회원목록 총 건수 조회
     * - 페이징에서 전체 회원 수를 보여주기 위해 사용
     */
    int selectUserTotalCount(UserSearchDTO searchDTO);

    /*
     * 관리자 회원상태 변경
     * - 01 정상 / 02 탈퇴 / 03 휴면 / 04 정지 중 하나로 변경
     */
    int updateUserStatus(UserVO user);

    /*
     * 관리자 회원권한 변경
     * - 01 일반회원 / 02 관리자 중 하나로 변경
     */
    int updateUserRole(UserVO user);
}
