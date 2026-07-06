# 계좌/자산 관리 모듈 README

## 목차

1. 모듈 개요
    - 주요 기능

2. 주요 파일 설명
    - Controller
    - Domain
    - Mapper
    - Service
    - JSP
    - SQL

3. USER_ACCOUNT 테이블 기준
    - 주요 컬럼
    - 제약조건

4. 로그인 세션 저장 방식

5. 계좌 등록/수정 정책
    - 입력값 검증
    - 중복 계좌 기준
    - 본인 계좌 확인

6. 잔액 처리 정책
    - 신규 계좌 시작 잔액
    - 충전
    - 차감
    - 총 보유 자산

7. 계좌 삭제 정책

8. 거래내역 연동 참고

9. 테스트 파일
    - AccountMapperJUnit.java
    - AccountServiceJUnit.java

10. 접근 URL

11. 팀원 연동 참고

12. 테스트 실행 전 확인사항

13. Git 커밋 메시지 예시

---

## 1. 모듈 개요

SPAM 프로젝트의 계좌/자산 관리 기능을 담당하는 모듈이다.

회원이 본인의 계좌 정보를 등록하고, 계좌별 잔액을 충전/차감하며, 전체 보유 자산을 확인할 수 있도록 구성한다.

### 주요 기능

- **계좌 관리 기능**
    - 계좌 등록
    - 로그인 회원의 계좌 목록 조회
    - 계좌 단건 조회
    - 계좌 기본정보 수정
    - 계좌 삭제

- **자산 관리 기능**
    - 계좌 잔액 충전
    - 계좌 잔액 차감
    - 로그인 회원의 총 보유 자산 조회

- **검증 기능**
    - 은행명 필수값 검증
    - 계좌번호 필수값 검증
    - 동일 회원의 중복 계좌 등록 방지
    - 본인 계좌만 조회/수정/삭제 가능
    - 0원 이하 금액 차단
    - 소수 금액 차단
    - 잔액 부족 시 차감 차단

- **테스트**
    - Mapper JUnit 테스트
    - Service JUnit 테스트

---

## 2. 주요 파일 설명

### Controller

- **AccountController.java**
    - 계좌/자산 화면 요청 처리
    - 계좌 목록, 등록 화면, 수정 화면 요청 처리
    - 계좌 등록, 수정, 삭제 요청 처리
    - 잔액 충전, 잔액 차감 요청 처리
    - 세션의 `loginUser`를 기준으로 로그인 여부와 회원번호를 확인

### Domain

- **AccountVO.java**
    - 계좌/자산 정보 VO
    - `USER_ACCOUNT` 테이블과 매핑되는 계좌 정보 객체
    - 금액 계산 오류를 줄이기 위해 잔액은 `BigDecimal` 타입으로 관리

### Mapper

- **AccountMapper.java**
    - MyBatis Mapper 인터페이스
    - 계좌 등록, 조회, 수정, 삭제, 잔액 변경, 총 자산 조회 메서드 정의

- **accountMapper.xml**
    - 계좌/자산 관리 SQL 작성 파일
    - `ACCOUNT_ID + USER_NUM` 조건으로 본인 계좌만 조회되도록 처리
    - 잔액 차감 시 `BALANCE >= amount` 조건으로 음수 잔액 방지

### Service

- **AccountService.java**
    - 계좌/자산 관리 Service 인터페이스
    - Controller에서 호출할 기능 단위 메서드 정의

- **AccountServiceImpl.java**
    - 계좌/자산 관리 비즈니스 로직 처리
    - 입력값 공백 제거, 필수값 검증, 중복 검사, 본인 계좌 확인 처리
    - 등록/수정/삭제/충전/차감 기능에 트랜잭션 적용

### JSP

- **account_list.jsp**
    - 계좌/자산 목록 화면
    - 총 보유 자산 표시
    - 계좌별 수정, 충전, 차감, 삭제 기능 제공

- **account_form.jsp**
    - 계좌 등록/수정 화면
    - 등록 모드와 수정 모드를 `mode` 값으로 구분
    - 수정 시 잔액은 직접 수정하지 않고 목록 화면의 충전/차감 기능으로만 변경

### SQL

- **user_account_ddl.sql**
    - `SEQ_USER_ACCOUNT` 시퀀스 생성
    - `USER_ACCOUNT` 테이블 생성
    - PK, FK, CHECK, UNIQUE 제약조건 생성
    - 회원번호 조회용 인덱스 생성

---

## 3. USER_ACCOUNT 테이블 기준

현재 계좌/자산 소스코드는 `USER_ACCOUNT` 테이블을 기준으로 동작한다.

### 주요 컬럼

```text
ACCOUNT_ID   = 계좌고유번호, PK, SEQ_USER_ACCOUNT.NEXTVAL 사용
USER_NUM     = 회원번호, USER_INFO.USER_NUM 참조
BANK_NAME    = 은행명, 필수값, 최대 20자
ACCOUNT_NUM  = 계좌번호, 필수값, 최대 30자
BALANCE      = 잔액, 기본값 0, 0 이상만 허용
CREATE_DT    = 등록일, 기본값 SYSDATE
UPDATE_DT    = 수정일, 계좌정보 수정 또는 잔액 변경 시 갱신
```

현재 소스코드는 위 컬럼을 기준으로 매핑되어 있으며, 계좌 사용 여부 컬럼은 Service, VO, Mapper XML에서 사용하지 않는다.

### 제약조건

```text
PK_USER_ACCOUNT       = ACCOUNT_ID 기본키
FK_USER_ACCOUNT_USER  = USER_NUM 외래키, USER_INFO.USER_NUM 참조
CK_USER_ACCOUNT_BALANCE = BALANCE >= 0
UK_USER_ACCOUNT_NUM   = USER_NUM + BANK_NAME + ACCOUNT_NUM 중복 방지
```

`USER_ACCOUNT`는 회원별 계좌 정보를 저장한다.

동일 회원은 같은 은행명과 같은 계좌번호를 중복 등록할 수 없다.

다른 회원은 같은 은행명과 같은 계좌번호를 사용할 수 있다.

---

## 4. 로그인 세션 저장 방식

회원관리 모듈에서 로그인 성공 시 `HttpSession`에 로그인 회원 정보를 저장한다.

```java
session.setAttribute("loginUser", loginUser);
```

계좌/자산 모듈은 같은 세션 키를 사용한다.

```java
private static final String SESSION_LOGIN_USER = "loginUser";
```

계좌 기능에서 로그인 회원 정보가 필요한 경우 다음과 같이 사용한다.

```java
UserVO loginUser = (UserVO) session.getAttribute("loginUser");

if (loginUser == null) {
    return "redirect:/user/login.do";
}

Long userNum = loginUser.getUserNum();
```

화면에서 `userNum` 값이 넘어오더라도 Controller에서 세션의 회원번호로 다시 세팅한다.

```java
account.setUserNum(loginUser.getUserNum());
```

DB에는 `UserVO` 전체가 아니라 회원번호인 `USER_NUM`을 저장한다.

---

## 5. 계좌 등록/수정 정책

계좌 등록과 수정은 `AccountServiceImpl`에서 공통 검증을 거친다.

### 입력값 검증

계좌 저장 시 아래 값을 검증한다.

```text
USER_NUM     = 필수
ACCOUNT_ID   = 수정 시 필수
BANK_NAME    = 필수, 20자 이하
ACCOUNT_NUM  = 필수, 30자 이하
```

은행명과 계좌번호는 저장 전에 앞뒤 공백을 제거한다.

```text
"  국민은행  " → "국민은행"
" 123-456 " → "123-456"
```

공백 제거 후 빈 문자열이면 `null`로 처리하고 필수값 검증에서 차단한다.

### 중복 계좌 기준

중복 계좌는 아래 조합으로 판단한다.

```text
USER_NUM + BANK_NAME + ACCOUNT_NUM
```

계좌 등록 시 같은 회원에게 이미 같은 은행명과 계좌번호가 있으면 등록하지 않는다.

계좌 수정 시에는 자기 자신의 `ACCOUNT_ID`는 중복 검사에서 제외한다.

### 본인 계좌 확인

계좌 단건 조회, 수정, 삭제, 충전, 차감은 모두 `ACCOUNT_ID`와 `USER_NUM`을 함께 사용한다.

```text
ACCOUNT_ID = 요청 계좌번호
USER_NUM   = 세션 로그인 회원번호
```

따라서 다른 회원의 `ACCOUNT_ID`를 알고 있어도 조회하거나 수정할 수 없다.

---

## 6. 잔액 처리 정책

잔액은 계좌 기본정보 수정과 분리해서 처리한다.

### 신규 계좌 시작 잔액

신규 계좌 등록 시 시작 잔액은 항상 0원이다.

화면이나 테스트에서 `balance` 값이 넘어오더라도 Service에서 0원으로 고정한다.

```java
account.setBalance(BigDecimal.ZERO);
```

### 충전

충전은 `deposit()`에서 처리한다.

```text
BALANCE = BALANCE + amount
```

충전 금액은 0보다 큰 정수만 허용한다.

충전 성공 시 `UPDATE_DT`를 `SYSDATE`로 변경한다.

### 차감

차감은 `withdraw()`에서 처리한다.

```text
BALANCE = BALANCE - amount
```

차감 금액은 0보다 큰 정수만 허용한다.

SQL에서 아래 조건을 함께 사용해 잔액 부족을 막는다.

```sql
AND BALANCE >= #{amount}
```

차감할 금액이 현재 잔액보다 크면 UPDATE 건수가 0건이 되고 Service에서 예외를 발생시킨다.

### 총 보유 자산

총 보유 자산은 로그인 회원의 모든 계좌 잔액 합계로 계산한다.

```sql
SELECT NVL(SUM(BALANCE), 0)
FROM USER_ACCOUNT
WHERE USER_NUM = #{userNum}
```

조회 결과가 `null`이면 Service에서 0원으로 반환한다.

---

## 7. 계좌 삭제 정책

계좌 삭제는 `deleteAccount()`에서 처리한다.

현재 구현은 잔액이 0원인 계좌만 삭제할 수 있다.

잔액이 남아 있는 계좌를 삭제하면 총 보유 자산 데이터가 사라지므로 Service에서 먼저 차단한다.

```text
BALANCE > 0 → 삭제 불가
BALANCE = 0 → 삭제 가능
```

Mapper에서도 한 번 더 `BALANCE = 0` 조건을 걸어 안전하게 처리한다.

```sql
DELETE FROM USER_ACCOUNT
WHERE ACCOUNT_ID = #{accountId}
  AND USER_NUM = #{userNum}
  AND BALANCE = 0
```

---

## 8. 거래내역 연동 참고

잔액 변경 시에는 거래내역 관리 모듈과 연동해야 한다.

현재 소스코드에는 거래내역 모듈이 합쳐질 위치를 `AccountServiceImpl.deposit()`와 `AccountServiceImpl.withdraw()` 내부 주석으로 남겨두었다.

### 충전 거래내역 예시

```text
TX_TYPE     = CHARGE
RECEIVER_NO = userNum
AMOUNT      = amount
STATUS      = COMPLETE
```

### 차감 거래내역 예시

```text
TX_TYPE   = WITHDRAW
SENDER_NO = userNum
AMOUNT    = amount
STATUS    = COMPLETE
```

거래내역 모듈이 합쳐지면 해당 위치에서 `TransactionMapper` 또는 거래내역 Service를 호출하면 된다.

---

## 9. 테스트 파일

### AccountMapperJUnit.java

Mapper SQL 동작을 직접 확인하는 테스트 파일이다.

검증 항목은 다음과 같다.

- `AccountMapper` Bean 주입 확인
- 계좌 INSERT 후 회원번호 기준 목록 조회
- 본인 계좌 단건 조회
- 다른 회원번호로 타인 계좌 조회 불가 확인
- 동일 회원의 은행명 + 계좌번호 중복 검사
- 수정 시 자기 자신은 중복 검사에서 제외되는지 확인
- 계좌 기본정보 수정
- 잔액 0원 계좌 삭제
- 잔액이 남은 계좌 삭제 실패
- 잔액 증가 SQL 확인
- 잔액 감소 SQL 확인
- 잔액 부족 시 감소 실패 확인
- 총 보유 자산 조회

### AccountServiceJUnit.java

Service 정책과 예외 처리를 확인하는 테스트 파일이다.

검증 항목은 다음과 같다.

- `AccountService` Bean 주입 확인
- 계좌 등록 시 시작 잔액 0원 고정
- 계좌 등록 시 은행명과 계좌번호 앞뒤 공백 제거
- 동일 회원의 중복 계좌 등록 방지
- 로그인 회원의 계좌 목록 조회
- 본인 계좌만 단건 조회 가능
- 다른 회원번호로 타인 계좌 조회 차단
- 계좌 기본정보 수정
- 수정 시 중복 계좌 방지
- 잔액 충전
- 잔액 차감
- 잔액 부족 시 차감 실패
- 0원, 음수, 소수 금액 차단
- 잔액 0원 계좌 삭제 가능
- 잔액이 남은 계좌 삭제 차단

테스트는 `@Transactional`과 `@Rollback`을 사용해 테스트 데이터가 DB에 남지 않도록 구성한다.

---

## 10. 접근 URL

```text
계좌 목록 화면      = GET  /account/list.do
계좌 등록 화면      = GET  /account/add.do
계좌 등록 처리      = POST /account/add.do
계좌 수정 화면      = GET  /account/update.do?accountId=1
계좌 수정 처리      = POST /account/update.do
계좌 삭제 처리      = POST /account/delete.do
잔액 충전 처리      = POST /account/deposit.do
잔액 차감 처리      = POST /account/withdraw.do
```

로그인하지 않은 사용자가 계좌 URL에 접근하면 로그인 화면으로 이동한다.

```text
/user/login.do
```

---

## 11. 팀원 연동 참고

다른 기능에서 로그인 회원의 계좌나 자산 정보가 필요한 경우 세션의 `loginUser`에서 회원번호를 꺼내 사용한다.

```java
UserVO loginUser = (UserVO) session.getAttribute("loginUser");
Long userNum = loginUser.getUserNum();
```

### 기능별 연동 예시

- **회원관리**
    - 로그인 성공 시 `loginUser` 세션 저장
    - 계좌 모듈은 `loginUser.getUserNum()`을 사용

- **거래내역 관리**
    - 충전/차감 성공 후 거래내역 INSERT 필요
    - `userNum`, `amount`, `accountId` 값을 연동에 사용

- **상품 거래**
    - 구매 확정 또는 정산 처리 시 구매자/판매자 자산 변동 가능
    - 자산 변동 시 `deposit()` 또는 `withdraw()` 흐름과 같은 정책 적용 필요

- **마이페이지**
    - 계좌/자산 관리 화면으로 이동하는 링크 연결 가능
    - 접근 URL은 `/account/list.do` 사용

---

## 12. 테스트 실행 전 확인사항

계좌/자산 테스트 실행 전 아래 내용을 확인한다.

- `USER_INFO` 테이블이 존재해야 한다.
- `SEQ_USER_INFO` 시퀀스가 존재해야 한다.
- `USER_ACCOUNT` 테이블이 존재해야 한다.
- `SEQ_USER_ACCOUNT` 시퀀스가 존재해야 한다.
- `root-context.xml`의 DB 계정, 비밀번호, URL이 실제 DB와 일치해야 한다.
- Mapper Scan 경로가 `com.pcwk.ehr` 하위 패키지를 포함해야 한다.
- Component Scan 경로가 `AccountServiceImpl`을 포함해야 한다.
- 테스트 실행 계정에 `USER_INFO`, `USER_ACCOUNT` 테이블이 생성되어 있어야 한다.

계좌/자산 테스트만 실행할 때는 아래 명령어를 사용한다.

```bash
mvn -Dtest=AccountMapperJUnit,AccountServiceJUnit test
```

전체 테스트를 실행할 때는 아래 명령어를 사용한다.

```bash
mvn clean test
```

Eclipse에서는 테스트 파일을 우클릭한 뒤 아래 메뉴로 실행한다.

```text
Run As → JUnit Test
```

---

## 13. Git 커밋 메시지 예시

```text
feat(account): 계좌/자산 관리 모듈 추가
```

```text
test(account): 계좌 Mapper와 Service JUnit 테스트 추가
```

```text
docs(account): 계좌/자산 모듈 README 정리
```

```text
fix(account): 잔액 부족 시 차감 방지 로직 수정
```
