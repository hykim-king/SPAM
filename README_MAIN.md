# 메인 및 공통 화면 README

## 목차

1. [모듈 개요](#1-모듈-개요)
   - [주요 기능](#주요-기능)

2. [주요 파일 설명](#2-주요-파일-설명)
   - [Controller](#controller)
   - [JSP](#jsp)
   - [CSS](#css)
   - [JavaScript](#javascript)
   - [Image](#image)

3. [공통 레이아웃 구성](#3-공통-레이아웃-구성)

4. [메인 화면 구성](#4-메인-화면-구성)

5. [서비스 안내 화면 구성](#5-서비스-안내-화면-구성)

6. [플레이스홀더 화면 구성](#6-플레이스홀더-화면-구성)

7. [접근 URL](#7-접근-url)

8. [팀원 연동 참고](#8-팀원-연동-참고)

---

## 1. 모듈 개요

SPAM 프로젝트의 메인 화면과 공통 레이아웃을 담당하는 영역이다.

메인 페이지, 서비스 안내 페이지, 공통 헤더, 네비게이션, 푸터, 모바일 하단 네비게이션, 플로팅 바, 공통 상품 카드 화면 구성을 포함한다.

### 주요 기능

| 구분 | 기능 |
|---|---|
| 메인 화면 | 프로젝트 진입 화면 제공 |
| 메인 화면 | 주요 서비스 소개 영역 제공 |
| 메인 화면 | 안전거래 안내 영역 제공 |
| 메인 화면 | 상품 카드 공통 UI 표시 |
| 공통 레이아웃 | Header 제공 |
| 공통 레이아웃 | Navigation 제공 |
| 공통 레이아웃 | Footer 제공 |
| 공통 레이아웃 | Mobile Bottom Navigation 제공 |
| 공통 레이아웃 | Floating Bar 제공 |
| 서비스 안내 | SPAM 서비스 사용 안내 화면 제공 |
| 플레이스홀더 | 아직 구현되지 않은 페이지의 임시 화면 제공 |
| 반응형 화면 | PC, 태블릿, 모바일 화면 대응 |

---

## 2. 주요 파일 설명

### Controller

| 파일명 | 설명 |
|---|---|
| `PlaceholderPageController.java` | 아직 구현되지 않은 기능 페이지의 임시 화면 요청 처리 |
| `ServiceGuideController.java` | 서비스 안내 화면 요청 처리 |

---

### JSP

| 파일명 | 설명 |
|---|---|
| `index.jsp` | 메인 화면 JSP |
| `service_info.jsp` | 서비스 안내 화면 JSP |
| `header.jsp` | 공통 헤더 JSP |
| `nav.jsp` | 공통 네비게이션 JSP |
| `footer.jsp` | 공통 푸터 JSP |
| `mobileBottomNav.jsp` | 모바일 하단 네비게이션 JSP |
| `floatingBar.jsp` | 공통 플로팅 바 JSP |
| `productCard.jsp` | 상품 카드 공통 JSP |
| `chat_view.jsp` | 채팅 화면 레이아웃 연동 |
| `report_list.jsp` | 신고 목록 화면 레이아웃 연동 |
| `user_login.jsp` | 로그인 화면 공통 레이아웃 연동 |
| `user_join.jsp` | 회원가입 화면 공통 레이아웃 연동 |
| `user_mypage.jsp` | 마이페이지 화면 공통 레이아웃 연동 |
| `user_update.jsp` | 회원정보 수정 화면 공통 레이아웃 연동 |
| `user_list.jsp` | 관리자 회원목록 화면 공통 레이아웃 연동 |
| `user_detail.jsp` | 관리자 회원상세 화면 공통 레이아웃 연동 |

---

### CSS

| 파일명 | 설명 |
|---|---|
| `index.css` | 메인 화면 스타일 |
| `member.css` | 회원 화면 스타일 및 공통 레이아웃 보정 |

---

### JavaScript

| 파일명 | 설명 |
|---|---|
| `index.js` | 메인 화면 동작 처리 |
| `member.js` | 회원 화면 동작 처리 및 공통 UI 연동 |

---

### Image

| 파일명 | 설명 |
|---|---|
| `hero-main-final.svg` | 메인 히어로 배너 이미지 |
| `hero-safe-final.svg` | 안전거래 안내 배너 이미지 |
| `safe-guide.svg` | 서비스 안전가이드 이미지 |

---

## 3. 공통 레이아웃 구성

공통 레이아웃은 여러 화면에서 반복되는 UI를 JSP 조각으로 분리하여 관리한다.

| 공통 파일 | 역할 |
|---|---|
| `header.jsp` | 로고, 상단 영역, 공통 헤더 구성 |
| `nav.jsp` | 주요 메뉴 이동 네비게이션 구성 |
| `footer.jsp` | 하단 정보 영역 구성 |
| `mobileBottomNav.jsp` | 모바일 화면 하단 고정 메뉴 구성 |
| `floatingBar.jsp` | 빠른 이동 또는 보조 기능 플로팅 버튼 구성 |
| `productCard.jsp` | 상품 목록에서 재사용하는 상품 카드 UI 구성 |

공통 JSP는 각 화면에서 include하여 사용한다.

```jsp
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
```

---

## 4. 메인 화면 구성

메인 화면은 사용자가 SPAM 서비스에 처음 진입했을 때 보여주는 대표 화면이다.

| 영역 | 설명 |
|---|---|
| 히어로 영역 | 서비스 대표 문구와 메인 배너 표시 |
| 서비스 소개 영역 | SPAM 서비스의 핵심 기능 안내 |
| 안전거래 안내 영역 | 안전한 거래를 위한 안내 문구와 이미지 표시 |
| 상품 카드 영역 | 상품 카드 공통 UI 확인 및 상품 목록 연결을 위한 영역 |
| 반응형 영역 | PC, 태블릿, 모바일 화면 크기에 맞춰 레이아웃 조정 |

---

## 5. 서비스 안내 화면 구성

서비스 안내 화면은 SPAM 서비스 사용 방법과 주요 기능을 설명하는 화면이다.

| 구분 | 설명 |
|---|---|
| 서비스 소개 | SPAM 프로젝트의 서비스 목적 안내 |
| 이용 흐름 | 회원가입, 상품 등록, 채팅, 거래 흐름 안내 |
| 안전거래 안내 | 거래 시 주의사항과 안전 기능 안내 |
| 공통 레이아웃 | header, nav, footer, floating bar 연동 |

---

## 6. 플레이스홀더 화면 구성

플레이스홀더 화면은 아직 구현되지 않은 메뉴 또는 추후 개발 예정인 페이지에 임시로 연결되는 화면이다.

| 구분 | 설명 |
|---|---|
| 목적 | 미구현 페이지 접근 시 빈 화면 대신 안내 화면 제공 |
| Controller | `PlaceholderPageController.java` |
| 사용 위치 | 개발 중인 메뉴, 임시 연결 페이지, 추후 구현 예정 화면 |
| 장점 | 전체 메뉴 흐름을 먼저 확인할 수 있음 |

---

## 7. 접근 URL

프로젝트 Context Path가 `/ehr`인 경우 브라우저에서는 앞에 `/ehr`을 붙여 접근한다.

| 구분 | Controller 기준 URL | 브라우저 접근 예시 |
|---|---|---|
| 메인 화면 | `/main.do` | `http://localhost:8080/ehr/main.do` |
| 서비스 안내 | `/service/info.do` | `http://localhost:8080/ehr/service/info.do` |
| 로그인 화면 | `/user/login.do` | `http://localhost:8080/ehr/user/login.do` |
| 회원가입 화면 | `/user/join.do` | `http://localhost:8080/ehr/user/join.do` |
| 마이페이지 | `/user/mypage.do` | `http://localhost:8080/ehr/user/mypage.do` |
| 관리자 회원목록 | `/admin/user/list.do` | `http://localhost:8080/ehr/admin/user/list.do` |

---

## 8. 팀원 연동 참고

공통 레이아웃은 다른 기능 화면에서도 재사용할 수 있다.

### JSP include 예시

```jsp
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<!-- 각 기능별 본문 영역 -->

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
<%@ include file="/WEB-INF/views/common/floatingBar.jsp" %>
<%@ include file="/WEB-INF/views/common/mobileBottomNav.jsp" %>
```

### 기능별 연동 예시

| 기능 | 연동 대상 |
|---|---|
| 회원관리 | 로그인, 회원가입, 마이페이지, 관리자 회원관리 화면에 공통 레이아웃 적용 |
| 상품관리 | 상품 목록, 상품 상세, 상품 등록 화면에 공통 레이아웃 및 상품 카드 UI 적용 가능 |
| 채팅관리 | 채팅 화면에 header, nav, footer 적용 |
| 거래내역 관리 | 거래내역 화면에 공통 레이아웃 적용 가능 |
| 신고관리 | 신고 목록 화면에 공통 레이아웃 적용 |

---
