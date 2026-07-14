# 상품관리 모듈 README

## 1. 모듈 개요

SPAM 프로젝트의 상품 등록, 수정, 삭제, 검색, 카테고리 조회, 이미지 관리 기능을 담당한다.

### 구현 기능

- 상품 전체 목록 및 상세 조회
- 상품명·설명·카테고리명·지역 통합 검색
- 대분류·중분류·소분류별 상품 조회
- 지역·가격대·거래상태 필터
- 최신순·인기순·추천순·가격순·조회수순 정렬
- 실제 DB 기준 페이징
- 로그인 회원 상품 등록
- 판매자 본인 상품 수정·삭제·거래상태 변경
- 상품 이미지 최대 5장 등록 및 교체
- 상품 목록 대표 이미지 조회
- 내가 등록한 상품 및 판매자별 상품 조회
- 메인화면 인기·추천·최신 상품 DB 연동

---

## 2. 기준 DB 컬럼

### PRODUCT

| 컬럼 | 설명 |
|---|---|
| `PRODUCT_NO` | 상품번호 |
| `USER_NUM` | 판매자 회원번호 |
| `CATEGORY_NO` | 상품 소분류 번호 |
| `PRODUCT_TITLE` | 상품명 |
| `PRODUCT_CONTENT` | 상품 설명 |
| `PRODUCT_CONDITION` | 상품 상태 |
| `PRICE` | 가격 |
| `LOCATION` | 거래지역 |
| `STATUS` | 거래상태 |
| `ADMIN_HIDE_YN` | 관리자 숨김 여부 |
| `HIDE_REASON` | 숨김 사유 |
| `VIEW_COUNT` | 조회수 |
| `CREATE_DT` | 등록일 |
| `MODIFY_DT` | 수정일 |
| `LIKE_CNT` | 찜 수 |
| `CHAT_CNT` | 채팅 수 |

거래상태는 다음 코드를 사용한다.

| 코드 | 의미 |
|---|---|
| `01` | 판매중 |
| `02` | 예약중 |
| `03` | 판매완료 |

<<<<<<< HEAD
=======
흐름은 다음과 같습니다.

```text
1. /product/updateStatus.do 요청
2. ProductController.updateStatus() 실행
3. ProductServiceImpl.updateStatus() 실행
4. checkOwner()로 본인 상품인지 확인
5. status 값이 01, 02, 03 중 하나인지 검증
6. ProductMapper.updateStatus() 실행
7. PRODUCT 테이블의 STATUS, MODIFY_DT 수정
```

---

## 10. 채팅 수 증가/감소 흐름

채팅방이 생성되거나 삭제되는 상황을 기준으로 상품의 채팅 수를 관리합니다.

| 요청 URL | 기능 |
|---|---|
| `/product/plusChatCnt.do` | 채팅 수 1 증가 |
| `/product/minusChatCnt.do` | 채팅 수 1 감소 |

감소 SQL은 `chat_cnt > 0` 조건을 사용하여 채팅 수가 음수가 되지 않도록 막습니다.

---

## 11. 카테고리 조회 흐름

상품 등록/수정 화면에서는 카테고리를 단계적으로 선택할 수 있습니다.

### 대분류 조회

```text
1. /product/saveForm.do 또는 /product/updateForm.do 요청
2. categoryService.doRetrieveParent() 호출
3. CATEGORY 테이블에서 category_level = 1인 데이터 조회
4. 등록/수정 화면에 categoryList 전달
```

### 자식 카테고리 조회

```text
1. 사용자가 대분류 또는 중분류 선택
2. AJAX로 /product/categoryChild.do?parentCategoryNo=번호 요청
3. categoryService.doRetrieveChild(parentCategoryNo) 호출
4. CATEGORY 테이블에서 parent_category_no가 일치하는 데이터 조회
5. JSON으로 자식 카테고리 목록 반환
```

이 구조를 사용하면 대분류 선택 후 중분류를 불러오고, 중분류 선택 후 소분류를 불러올 수 있습니다.

---

## 12. DB 테이블 관계

```text
CATEGORY
   ↑
   │ category_no
   │
PRODUCT
   ↑
   │ product_no
   │
PRODUCT_IMAGE
```

### PRODUCT

상품의 기본 정보를 저장합니다.

주요 컬럼:

```text
PRODUCT_NO
User_Num
CATEGORY_NO
PRODUCT_TITLE
PRODUCT_CONTENT
PRODUCT_CONDITION
PRICE
LOCATION
STATUS
ADMIN_HIDE_YN
VIEW_COUNT
LIKE_CNT
CHAT_CNT
CREATE_DT
MODIFY_DT
```

>>>>>>> origin/feature/product
### PRODUCT_IMAGE

| 컬럼 | 설명 |
|---|---|
| `IMAGE_NO` | 이미지번호 |
| `PRODUCT_NO` | 상품번호 |
| `ORIGIN_NAME` | 원본 파일명 |
| `CHANGE_NAME` | 서버 저장 파일명 |
| `FILE_PATH` | 웹 접근 경로 |
| `IMAGE_ORDER` | 이미지 순서 |
| `THUMBNAIL_YN` | 대표 이미지 여부 |
| `CREATE_DT` | 등록일 |

### CATEGORY

| 컬럼 | 설명 |
|---|---|
| `CATEGORY_NO` | 카테고리번호 |
| `PARENT_CATEGORY_NO` | 상위 카테고리번호 |
| `CATEGORY_NAME` | 카테고리명 |
| `CATEGORY_LEVEL` | 1:대분류, 2:중분류, 3:소분류 |
| `SORT_ORDER` | 표시 순서 |
| `USE_YN` | 사용 여부 |
| `CREATE_DT` | 등록일 |

상품에는 사용 중인 소분류 번호만 저장한다. 대분류 또는 중분류 검색 시 Oracle 계층형 조회로 모든 하위 카테고리 상품을 조회한다.

---

## 3. 주요 파일

### Java

| 파일 | 역할 |
|---|---|
| `ProductController.java` | 상품 요청, 로그인 세션 판매자 검증, 화면 이동 |
| `ProductVO.java` | 상품 정보 |
| `ProductImageVO.java` | 상품 이미지 정보 |
| `ProductSearchDTO.java` | 검색·필터·정렬·페이징 조건 |
| `ProductService.java` | 상품 서비스 인터페이스 |
| `ProductServiceImpl.java` | 상품 및 이미지 처리, 소유권 검증 |
| `ProductMapper.java` | 상품 DB 메서드 |
| `CategoryVO.java` | 카테고리 정보 |
| `CategoryServiceImpl.java` | 사용 중인 카테고리 조회 |

### MyBatis

| 파일 | 역할 |
|---|---|
| `productMapper.xml` | 상품 CRUD, 검색, 계층 카테고리, 정렬, 페이징 |
| `productImageMapper.xml` | 이미지 등록·조회·삭제 |
| `categoryMapper.xml` | 대분류·자식·전체·단건 카테고리 조회 |

### JSP 및 JavaScript

| 파일 | 역할 |
|---|---|
| `productList.jsp` | 전체 목록, 검색 필터, 정렬, 페이징 |
| `productView.jsp` | 상품 상세, 판매자 정보, 채팅·신고·관리 기능 |
| `productSave.jsp` | 상품 등록 |
| `productUpdate.jsp` | 상품 및 이미지 수정 |
| `productMyList.jsp` | 내가 등록한 상품 관리 |
| `productSeller.jsp` | 판매자 프로필 및 판매 상품 |
| `product.js` | 카테고리 AJAX, 이미지 미리보기, 상태·삭제·채팅 처리 |

---

## 4. 접근 URL

| URL | Method | 기능 |
|---|---|---|
| `/product/list.do` | GET | 상품 목록 및 검색 |
| `/product/view.do?productNo=번호` | GET | 상품 상세 |
| `/product/saveForm.do` | GET | 상품 등록 화면 |
| `/product/doSave.do` | POST | 상품 등록 |
| `/product/updateForm.do?productNo=번호` | GET | 상품 수정 화면 |
| `/product/doUpdate.do` | POST | 상품 수정 |
| `/product/doDelete.do` | POST | 상품 삭제 |
| `/product/updateStatus.do` | POST | 거래상태 변경 |
| `/product/categoryChild.do?parentCategoryNo=번호` | GET | 자식 카테고리 JSON |
| `/product/myList.do` | GET | 내가 등록한 상품 |
| `/product/seller.do?userNum=번호` | GET | 판매자 상품 목록 |
| `/product/popular.do` | GET | 인기순 목록으로 이동 |
| `/product/recommend.do` | GET | 추천순 목록으로 이동 |
| `/product/latest.do` | GET | 최신순 목록으로 이동 |

---

## 5. 상품 검색 조건

`ProductSearchDTO`가 다음 요청값을 받는다.

| 파라미터 | 설명 |
|---|---|
| `searchWord` | 상품명·설명·카테고리명·지역 검색 |
| `categoryNo` | 선택한 카테고리 및 하위 카테고리 검색 |
| `location` | 거래지역 검색 |
| `priceRange` | 가격대 검색 |
| `status` | 거래상태 검색 |
| `sort` | 정렬 방식 |
| `pageNo` | 현재 페이지 |
| `pageSize` | 페이지당 상품 수 |

<<<<<<< HEAD
관리자가 숨긴 상품은 모든 일반 상품 목록과 상세 조회에서 제외한다.
=======
예를 들어 Java 파일은 다음 패키지를 사용합니다.

```java
package com.pcwk.ehr.category.mapper;
package com.pcwk.ehr.product.mapper;
```

그런데 XML에서는 다음처럼 되어 있습니다.

```xml
<mapper namespace="com.pcwk.ehr.mapper.CategoryMapper">
<mapper namespace="com.pcwk.ehr.mapper.ProductImageMapper">
```

MyBatis에서 인터페이스 Mapper와 XML Mapper를 연결하려면 namespace가 Mapper 인터페이스의 전체 경로와 일치해야 합니다.

권장 수정:

```xml
<mapper namespace="com.pcwk.ehr.category.mapper.CategoryMapper">
<mapper namespace="com.pcwk.ehr.product.mapper.ProductImageMapper">
```

`productMapper.xml`은 이미 다음처럼 올바르게 되어 있습니다.

```xml
<mapper namespace="com.pcwk.ehr.product.mapper.ProductMapper">
```

### 16.2 상품 삭제 시 이미지 삭제 로직 추가

현재 `ProductImageMapper`에는 특정 상품 이미지 전체 삭제 메서드가 있지만, `ProductServiceImpl.doDelete()`에서는 호출하지 않고 있습니다.

상품 삭제 시 이미지 DB 데이터와 실제 파일을 함께 삭제하면 데이터가 더 깔끔하게 관리됩니다.

### 16.3 파일 업로드 경로 설정 분리

현재 업로드 경로가 코드에 직접 작성되어 있습니다.

```java
private static final String UPLOAD_PATH = "D:\\SPAM\\Upload";
```

운영 환경에서는 `properties` 또는 `yml` 설정 파일로 분리하는 것이 좋습니다.

예시:

```properties
file.upload.path=D:/SPAM/Upload
```

### 16.4 이미지 파일 확장자 검증 추가

현재는 업로드된 파일의 확장자를 그대로 사용합니다. 보안을 위해 이미지 확장자만 허용하는 검증을 추가하는 것이 좋습니다.

예시 허용 확장자:

```text
.jpg
.jpeg
.png
.gif
.webp
```

### 16.5 `sallerNo` 오타 확인

코드에서는 `sallerNo`라는 필드명을 사용하고 있습니다. 실제 의미는 판매자 번호이므로 일반적으로는 `sellerNo`가 맞습니다.

다만 이미 DB 컬럼이나 기존 코드가 `User_Num`, `sallerNo` 기준으로 작성되어 있다면 전체를 한 번에 수정해야 합니다.
>>>>>>> origin/feature/product

---

## 6. 로그인 및 권한 처리

- 상품 등록·수정·삭제·상태 변경은 로그인 회원만 가능하다.
- `USER_NUM`은 hidden input이나 요청값을 신뢰하지 않고 `sessionScope.loginUser.userNum`으로 서버에서 설정한다.
- 수정·삭제·상태 변경 전 DB 상품의 `USER_NUM`과 로그인 회원번호를 비교한다.
- 상품 상세에서 판매자 본인이 조회한 경우 조회수를 증가시키지 않는다.
- 채팅방 생성 시에도 판매자 번호를 요청값이 아닌 DB 상품 정보에서 다시 확인한다.

---

## 7. 이미지 처리

- 실제 저장 폴더: `D:\\SPAM\\Upload`
- 웹 접근 URL: `/upload/저장파일명`
- 최대 5장
- JPG, JPEG, PNG, GIF, WEBP만 허용
- 첫 번째 이미지의 `THUMBNAIL_YN`을 `Y`로 저장
- 수정 시 새 이미지를 선택하면 기존 이미지 전체를 교체
- 상품 삭제 시 `PRODUCT_IMAGE` 데이터를 먼저 삭제한 뒤 상품과 실제 파일을 삭제

`servlet-context.xml`의 `/upload/**` 리소스 경로와 서비스의 `UPLOAD_PATH`가 동일해야 한다.

---

## 8. 메인화면 연동

`MainController`는 더미 상품을 사용하지 않는다.

- 인기 상품: 조회수·찜·채팅 기준
- 오늘의 추천: 찜·채팅·조회수 가중치 기준
- 최신 상품: 등록일 기준
- 각 영역 최대 4개
- 판매중 상품만 노출

---

## 9. 주요 테스트 URL

```text
/ehr/product/list.do
/ehr/product/list.do?searchWord=아이폰
/ehr/product/list.do?categoryNo=1
/ehr/product/list.do?categoryNo=11
/ehr/product/list.do?categoryNo=101
/ehr/product/list.do?location=서울&status=01&sort=priceLow
/ehr/product/saveForm.do
/ehr/product/myList.do
```

카테고리 테스트 기준:

- `1`: 디지털/가전 전체 하위 상품
- `11`: 휴대폰/태블릿 전체 하위 상품
- `101`: 스마트폰 상품

