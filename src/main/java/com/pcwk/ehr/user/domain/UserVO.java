package com.pcwk.ehr.user.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class UserVO implements Serializable {

    /*
     * Serializable을 구현한 클래스에는 serialVersionUID를 두는 것이 안전
     * 지금 프로젝트에서 세션에 UserVO를 저장하므로, WAS가 세션 직렬화를 할 수도 있음
     */
    private static final long serialVersionUID = 1L;

    /*
     * USER_NUM
     * - 회원을 구분하는 내부 고유번호
     * - DB에서는 PK이며 SEQ_USER_INFO.NEXTVAL로 생성
     * - 사용자가 직접 입력하거나 수정하는 값 아님
     */
    private Long userNum;

    /*
     * USER_ID
     * - 01 정상/03 휴먼/04 정지 회원 기준 중복될 수 없음
     * - 02 탈퇴 회원의 아이디는 재가입 시 다시 사용할 수 있도록 중복 검사 대상에서 제외
     */
    private String userId;

    /*
     * PASSWORD
     * - 회원가입 화면에서는 평문 비밀번호가 잠깐 들어옴
     * - Service에서 PasswordUtil.hash()를 통해 SHA-256 해시값으로 바꾼 뒤 DB에 저장
     * - 로그인 성공 후 반환되는 UserVO에서는 null로 제거
     */
    private String password;

    /*
     * USER_NAME
     * - NOT NULL
     */
    private String userName;

    /*
     * NICKNAME
     * - 비어 있으면 Service에서 USER_NAME과 같은 값으로 기본 세팅
     */
    private String nickname;

    /*
     * PHONE_NUM
     * - 01 정상/03 휴먼/04 정지 회원 기준 중복 불가능
     * - 02 탈퇴 회원의 전화번호는 재가입 시 다시 사용할 수 있도록 중복 검사 대상에서 제외
     */
    private String phoneNum;

    /*
     * EMAIL
     * - 입력한 경우 01 정상/03 휴먼/04 정지 회원 기준 중복 불가능
     * - 02 탈퇴 회원의 이메일은 재가입 시 다시 사용할 수 있도록 중복 검사 대상에서 제외
     */
    private String email;

    /*
     * BIRTH_DT
     * - JSP의 <input type="date">는 yyyy-MM-dd 형식으로 값을 보냄
     * - @DateTimeFormat 지정으로 Spring이 문자열을 Date로 변환하도록 함
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDt;

    /*
     * USER_ROLE
     * - 01: 일반회원
     * - 02: 관리자
     */
    private String userRole;

    /*
     * USER_STATUS
     * - 01: 정상
     * - 02: 탈퇴
     * - 03: 휴먼
     * - 04: 정지
     */
    private String userStatus;

    /*
     * CREATE_DT
     * - DB INSERT 시 SYSDATE로 저장
     */
    private Date createDt;

    /*
     * UPDATE_DT
     * - 회원정보 수정, 비밀번호 변경, 상태/권한 변경 시 SYSDATE로 갱신
     */
    private Date updateDt;

    /*
     * WITHDRAW_DT
     * - 회원탈퇴 처리 시 SYSDATE로 저장
     */
    private Date withdrawDt;

    public UserVO() {
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDt() {
        return birthDt;
    }

    public void setBirthDt(Date birthDt) {
        this.birthDt = birthDt;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Date getWithdrawDt() {
        return withdrawDt;
    }

    public void setWithdrawDt(Date withdrawDt) {
        this.withdrawDt = withdrawDt;
    }

    /*
     * password는 해시값도 로그에 남기지 않는 편이 안전하기 때문에 출력하지 않음
     */
    @Override
    public String toString() {
        return "UserVO{" +
                "userNum=" + userNum +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", birthDt=" + birthDt +
                ", userRole='" + userRole + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", createDt=" + createDt +
                ", updateDt=" + updateDt +
                ", withdrawDt=" + withdrawDt +
                '}';
    }
}
