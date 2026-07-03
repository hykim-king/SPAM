package com.pcwk.ehr.user.service;

import java.util.List;

import com.pcwk.ehr.user.domain.UserSearchDTO;
import com.pcwk.ehr.user.domain.UserVO;

public interface UserService {

    /*
     * 회원가입
     * - 아이디/전화번호/이메일 중복 검사
     * - 비밀번호 해시 처리
     * - 기본 권한/상태 세팅
     * - USER_INFO INSERT
     */
    int join(UserVO user);

    /*
     * 로그인
     * - 아이디 조회
     * - 비밀번호 검증
     * - 회원상태 검증
     */
    UserVO login(String userId, String password);

    /*
     * 회원번호로 회원 단건 조회
     * - 마이페이지
     * - 회원정보 수정 화면
     * - 관리자 회원상세 화면
     */
    UserVO getUser(Long userNum);

    /*
     * 회원정보 수정
     * - 전화번호/이메일 중복 검사 포함
     */
    int updateUser(UserVO user);

    /*
     * 비밀번호 변경
     * - 현재 비밀번호 확인 후 새 비밀번호로 변경
     */
    int updatePassword(Long userNum, String currentPassword, String newPassword);

    /*
     * 회원탈퇴
     * - 비밀번호 확인 후 USER_STATUS를 02 탈퇴로 변경
     */
    int withdrawUser(Long userNum, String password);

    /*
     * 관리자 회원목록 조회
     */
    List<UserVO> getUserList(UserSearchDTO searchDTO);

    /*
     * 관리자 회원목록 총 건수 조회
     */
    int getUserTotalCount(UserSearchDTO searchDTO);

    /*
     * 관리자 회원상태 변경
     */
    int changeUserStatus(Long userNum, String userStatus);

    /*
     * 관리자 회원권한 변경
     */
    int changeUserRole(Long userNum, String userRole);
}
