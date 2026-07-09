# 회원관리 모듈 README

## 목차

1. [모듈 개요](#1-모듈-개요)
   - [주요 기능](#주요-기능)

2. [주요 파일 설명](#2-주요-파일-설명)
   - [Controller](#controller)
   - [Domain](#domain)
   - [Mapper](#mapper)
   - [Service](#service)
   - [Util](#util)
   - [JSP](#jsp)

3. [공통코드 기준](#3-공통코드-기준)
   - [USER_ROLE](#user_role)
   - [USER_STATUS](#user_status)

4. [로그인 세션 저장 방식](#4-로그인-세션-저장-방식)

5. [재가입 정책](#5-재가입-정책)
   - [중복 검사 기준](#중복-검사-기준)

6. [관리자 로그인](#6-관리자-로그인)

7. [테스트 파일](#7-테스트-파일)
   - [UserMapperJUnit.java](#usermapperjunitjava)
   - [UserServiceJUnit.java](#userservicejunitjava)
   - [UserControllerJUnit.java](#usercontrollerjunitjava)

8. [접근 URL](#8-접근-url)

9. [팀원 연동 참고](#9-팀원-연동-참고)

10. [Git 커밋 메시지 예시](#10-git-커밋-메시지-예시)

---

## 1. 모듈 개요

SPAM 프로젝트의 회원관리 기능을 담당하는 모듈이다.

### 주요 기능

| 구분 | 기능 |
|---|---|
| 일반 회원 기능 | 회원가입 |
| 일반 회원 기능 | 로그인 |
| 일반 회원 기능 | 로그아웃 |
| 일반 회원 기능 | 마이페이지 |
| 일반 회원 기능 | 회원정보 수정 |
| 일반 회원 기능 | 비밀번호 변경 |
| 일반 회원 기능 | 회원탈퇴 |
| 관리자 기능 | 회원목록 조회 |
| 관리자 기능 | 회원상세 조회 |
| 관리자 기능 | 회원상태 변경 |
| 관리자 기능 | 회원권한 변경 |
| 중복 검사 기능 | 아이디 중복 검사 |
| 중복 검사 기능 | 전화번호 중복 검사 |
| 중복 검사 기능 | 이메일 중복 검사 |
| 재가입 정책 | 탈퇴 회원의 아이디, 전화번호, 이메일 재사용 허용 |
| 테스트 | Mapper JUnit 테스트 |
| 테스트 | Service JUnit 테스트 |
| 테스트 | Controller JUnit 테스트 |

---

## 2. 주요 파일 설명

### Controller

| 파일명 | 설명 |
|---|---|
| `UserController.java` | 일반 회원 화면 요청 처리 |
| `UserController.java` | 회원가입, 로그인, 로그아웃 처리 |
| `UserController.java` | 마이페이지, 회원정보 수정, 비밀번호 변경, 회원탈퇴 처리 |
| `AdminUserController.java` | 관리자 회원관리 요청 처리 |
| `AdminUserController.java` | 회원목록 조회, 회원상세 조회 처리 |
| `AdminUserController.java` | 회원상태 변경, 회원권한 변경 처리 |

### Domain

| 파일명 | 설명 |
|---|---|
| `UserVO.java` | 회원 정보 VO |
| `UserVO.java` | `USER_INFO` 테이블과 매핑되는 회원 정보 객체 |
| `UserSearchDTO.java` | 관리자 회원목록 검색 DTO |
| `UserSearchDTO.java` | 페이징, 검색 조건 처리에 사용 |

### Mapper

| 파일명 | 설명 |
|---|---|
| `UserMapper.java` | MyBatis Mapper 인터페이스 |
| `UserMapper.java` | 회원 SQL 호출 메서드 정의 |
| `userMapper.xml` | 회원관리 SQL 작성 파일 |
| `userMapper.xml` | 회원가입, 로그인, 중복 검사, 회원 수정, 관리자 조회 SQL 포함 |

### Service

| 파일명 | 설명 |
|---|---|
| `UserService.java` | 회원관리 Service 인터페이스 |
| `UserServiceImpl.java` | 회원가입, 로그인, 수정, 탈퇴 등 비즈니스 로직 처리 |
| `UserServiceImpl.java` | 중복 검사 및 비밀번호 암호화 처리 |

### Util

| 파일명 | 설명 |
|---|---|
| `PasswordUtil.java` | 비밀번호 SHA-256 암호화 처리 |

### JSP

| 파일명 | 설명 |
|---|---|
| `user_join.jsp` | 회원가입 화면 |
| `user_login.jsp` | 로그인 화면 |
| `user_mypage.jsp` | 마이페이지 화면 |
| `user_update.jsp` | 회원정보 수정 화면 |
| `user_list.jsp` | 관리자 회원목록 화면 |
| `user_detail.jsp` | 관리자 회원상세 화면 |

---

## 3. 공통코드 기준

회원 권한과 회원 상태는 팀 공통코드 기준으로 저장한다.

### USER_ROLE

| 코드 | 의미 |
|---|---|
| `01` | 일반회원 |
| `02` | 관리자 |

### USER_STATUS

| 코드 | 의미 |
|---|---|
| `01` | 정상 |
| `02` | 탈퇴 |
| `03` | 휴면 |
| `04` | 정지 |

---

## 4. 로그인 세션 저장 방식

로그인 성공 시 `HttpSession`에 로그인 회원 정보를 저장한다.

```java
session.setAttribute("loginUser", loginUser);
```

세션에는 회원번호만 저장하는 것이 아니라 `UserVO` 객체 전체를 저장한다.

다른 컨트롤러에서 로그인 회원 정보가 필요한 경우 다음과 같이 사용한다.

```java
UserVO loginUser = (UserVO) session.getAttribute("loginUser");

if (loginUser == null) {
    // 로그인하지 않은 상태 처리
}

Integer userNum = loginUser.getUserNum();
```

채팅, 상품, 거래 등 다른 기능에서 회원 FK가 필요한 경우에는 `loginUser.getUserNum()`을 사용한다.

```java
Integer senderNo = loginUser.getUserNum();
```

DB에는 `UserVO` 전체가 아니라 회원번호인 `USER_NUM`을 저장한다.

---

## 5. 재가입 정책

탈퇴 회원은 아이디, 전화번호, 이메일을 다시 사용할 수 있다.

### 중복 검사 기준

| 회원 상태 코드 | 상태 | 중복 검사 포함 여부 | 기준 |
|---|---|---|---|
| `01` | 정상 | 포함 | 중복 검사 포함 |
| `02` | 탈퇴 | 제외 | 아이디, 전화번호, 이메일 재사용 가능 |
| `03` | 휴면 | 포함 | 중복 검사 포함 |
| `04` | 정지 | 포함 | 중복 검사 포함 |

중복 검사 SQL은 탈퇴 회원을 제외한다.

```sql
USER_STATUS <> '02'
```

---

## 6. 관리자 로그인

관리자 계정은 다음 조건을 만족해야 한다.

| 컬럼 | 조건 |
|---|---|
| `USER_ROLE` | `02` |
| `USER_STATUS` | `01` |

관리자로 로그인하면 관리자 회원목록 화면으로 이동한다.

```text
/admin/user/list.do
```

일반 회원으로 로그인하면 마이페이지로 이동한다.

```text
/user/mypage.do
```

---

## 7. 테스트 파일

### UserMapperJUnit.java

| 테스트 항목 |
|---|
| Mapper Bean 주입 확인 |
| 회원 INSERT / SELECT 확인 |
| 아이디 중복 검사 확인 |
| 전화번호 중복 검사 확인 |
| 이메일 중복 검사 확인 |
| 관리자 회원목록 조회 SQL 확인 |

### UserServiceJUnit.java

| 테스트 항목 |
|---|
| Service Bean 주입 확인 |
| 회원가입 후 로그인 확인 |
| 중복 가입 방지 확인 |
| 탈퇴 계정 로그인 차단 확인 |
| 탈퇴 회원 재가입 가능 확인 |
| 회원정보 수정 중복 검사 확인 |
| 관리자 회원목록 조회 확인 |

### UserControllerJUnit.java

| 테스트 항목 |
|---|
| 로그인 화면 요청 확인 |
| 회원가입 화면 요청 확인 |
| 회원가입 POST 검증 확인 |
| 로그인 성공 후 redirect 확인 |
| 관리자 회원목록 접근 확인 |

---

## 8. 접근 URL

| 구분 | URL |
|---|---|
| 로그인 화면 | `/user/login.do` |
| 회원가입 화면 | `/user/join.do` |
| 마이페이지 | `/user/mypage.do` |
| 회원정보 수정 | `/user/update.do` |
| 관리자 회원목록 | `/admin/user/list.do` |
| 관리자 회원상세 | `/admin/user/detail.do` |

---

## 9. 팀원 연동 참고

다른 기능에서 로그인 회원번호가 필요한 경우 다음 방식으로 사용한다.

```java
UserVO loginUser = (UserVO) session.getAttribute("loginUser");
Integer userNum = loginUser.getUserNum();
```

### 기능별 회원번호 사용 예시

| 기능 | 사용 컬럼 |
|---|---|
| 상품 등록 | `SELLER_NO` |
| 채팅방 생성 | `SELLER_NO` |
| 채팅방 생성 | `BUYER_NO` |
| 채팅 메시지 | `SENDER_NO` |
| 찜 | `USER_NO` |
| 거래 | `BUYER_NO` |
| 거래 | `SELLER_NO` |
| 신고 | `REPORTER_NO` |
| 신고 | `TARGET_USER_NO` |

---

## 10. Git 커밋 메시지 예시

| 구분 | 커밋 메시지 예시 |
|---|---|
| 기능 추가 | `feat: 회원가입 기능 추가` |
| 기능 추가 | `feat: 로그인 기능 추가` |
| 기능 추가 | `feat: 관리자 회원목록 조회 기능 추가` |
| 기능 수정 | `fix: 회원 중복 검사 조건 수정` |
| 기능 수정 | `fix: 탈퇴 회원 재가입 조건 수정` |
| 화면 수정 | `style: 회원가입 화면 스타일 수정` |
| 문서 수정 | `docs: 회원관리 README 수정` |
| 테스트 | `test: 회원관리 JUnit 테스트 추가` |

---
