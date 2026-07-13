# 중고거래 사이트 상품관리 기능 README

## 1. 기능 개요

이 프로젝트의 상품관리 기능은 중고거래 사이트에서 사용자가 상품을 등록하고, 목록 조회, 상세 조회, 수정, 삭제, 거래상태 변경, 채팅 수 증감, 상품 이미지 업로드, 카테고리 선택을 처리하기 위한 Spring MVC 기반 기능입니다.

전체 구조는 다음과 같은 흐름으로 동작합니다.

```text
사용자 요청
   ↓
ProductController / CategoryService 호출
   ↓
ProductService / CategoryService
   ↓
ProductServiceImpl / CategoryServiceImpl
   ↓
ProductMapper / ProductImageMapper / CategoryMapper
   ↓
MyBatis XML Mapper
   ↓
Oracle DB
```

상품 등록 시에는 상품 기본 정보가 먼저 `PRODUCT` 테이블에 저장되고, 첨부된 이미지 파일은 서버 폴더에 저장된 뒤 이미지 정보가 `PRODUCT_IMAGE` 테이블에 저장됩니다.

---

## 2. 주요 패키지 및 파일 역할

### 2.1 Category 관련 파일

| 파일 | 역할 |
|---|---|
| `CategoryVO.java` | 카테고리 정보를 담는 VO 객체 |
| `CategoryMapper.java` | 카테고리 DB 접근 메서드 정의 |
| `CategoryService.java` | 카테고리 서비스 인터페이스 |
| `CategoryServiceImpl.java` | 카테고리 서비스 구현체 |
| `categoryMapper.xml` | MyBatis 카테고리 SQL 작성 파일 |

카테고리는 대분류, 중분류, 소분류 구조를 가지고 있습니다. `CategoryVO`에는 카테고리 번호, 부모 카테고리 번호, 카테고리명, 카테고리 레벨, 정렬순서, 생성일, 수정일이 들어갑니다.

```text
대분류(category_level = 1)
   ↓
중분류(parent_category_no = 대분류 번호)
   ↓
소분류(parent_category_no = 중분류 번호)
```

### 2.2 Product 관련 파일

| 파일 | 역할 |
|---|---|
| `ProductController.java` | 상품 관련 요청을 받는 Controller |
| `ProductVO.java` | 상품 정보를 담는 VO 객체 |
| `ProductService.java` | 상품 서비스 인터페이스 |
| `ProductServiceImpl.java` | 상품 등록, 조회, 수정, 삭제 등 실제 비즈니스 로직 |
| `ProductMapper.java` | 상품 DB 접근 메서드 정의 |
| `productMapper.xml` | 상품 관련 SQL 작성 파일 |
| `ProductServiceTest.java` | 상품 서비스 테스트 코드 |

`ProductVO`는 상품 번호, 판매자 번호, 카테고리 번호, 상품명, 설명, 상품 상태, 가격, 지역, 거래상태, 관리자 숨김 여부, 조회수, 찜 수, 채팅 수 등을 관리합니다. 또한 상세 조회 시 카테고리 경로 표시를 위해 `largeName`, `middleName`, `smallName` 필드를 가지고 있고, 이미지 목록을 담기 위해 `imageList`를 포함합니다.

### 2.3 ProductImage 관련 파일

| 파일 | 역할 |
|---|---|
| `ProductImageVO.java` | 상품 이미지 정보를 담는 VO 객체 |
| `ProductImageMapper.java` | 상품 이미지 DB 접근 메서드 정의 |
| `productImageMapper.xml` | 상품 이미지 관련 SQL 작성 파일 |

`ProductImageVO`는 이미지 번호, 상품 번호, 원본 파일명, 서버 저장 파일명, 파일 경로, 이미지 순서, 대표 이미지 여부를 관리합니다.

---

## 3. Controller 흐름

`ProductController`는 `/product` 경로를 기준으로 상품 기능 요청을 처리합니다.

| 요청 URL | Method | 기능 | 반환 View 또는 결과 |
|---|---|---|---|
| `/product/list.do` | GET | 상품 목록 조회 | `product/productList` |
| `/product/view.do` | GET | 상품 상세 조회 | `product/productView` |
| `/product/saveForm.do` | GET | 상품 등록 폼 이동 | `product/productSave` |
| `/product/updateForm.do` | GET | 상품 수정 폼 이동 | `product/productUpdate` |
| `/product/categoryChild.do` | GET | 자식 카테고리 조회 | JSON |
| `/product/doSave.do` | POST | 상품 등록 | 목록으로 redirect |
| `/product/doUpdate.do` | POST | 상품 수정 | 상세 페이지로 redirect |
| `/product/doDelete.do` | POST | 상품 삭제 | 성공 건수 반환 |
| `/product/updateStatus.do` | POST | 거래상태 변경 | 성공 건수 반환 |
| `/product/plusChatCnt.do` | POST | 채팅 수 증가 | 성공 건수 반환 |
| `/product/minusChatCnt.do` | POST | 채팅 수 감소 | 성공 건수 반환 |

---

## 4. 상품 등록 흐름

상품 등록은 이미지 파일까지 함께 처리할 수 있습니다.

```text
1. 사용자가 상품 등록 폼에서 상품 정보와 이미지 파일 선택
2. /product/doSave.do 요청 발생
3. ProductController.doSave() 실행
4. ProductService.doInsert(product, files) 호출
5. ProductServiceImpl.doInsert() 실행
6. 상품 필수값 검증
7. 거래상태, 조회수, 찜 수, 채팅 수, 숨김 여부 기본값 세팅
8. ProductMapper.doInsert(product) 실행
9. PRODUCT 테이블에 상품 등록
10. 생성된 productNo를 기준으로 이미지 파일 처리
11. 이미지 파일을 D:\SPAM\Upload 폴더에 저장
12. PRODUCT_IMAGE 테이블에 이미지 정보 저장
13. 상품 목록 페이지로 redirect
```

### 등록 시 기본값

| 항목 | 기본값 | 의미 |
|---|---|---|
| `status` | `01` | 판매중 |
| `viewCount` | `0` | 조회수 |
| `likeCnt` | `0` | 찜 수 |
| `chatCnt` | `0` | 채팅 수 |
| `adminHideYn` | `N` | 관리자 숨김 아님 |

### 이미지 저장 방식

이미지는 실제 파일과 DB 정보가 분리되어 관리됩니다.

```text
실제 파일 저장 위치: D:\SPAM\Upload
DB 저장 경로: /upload/변경된파일명
```

파일명 중복을 막기 위해 `UUID`를 사용하여 서버 저장 파일명을 새로 만듭니다.

첫 번째 이미지는 대표 이미지로 처리됩니다.

```text
첫 번째 이미지: THUMBNAL_YN = 'Y'
나머지 이미지: THUMBNAL_YN = 'N'
```

> 주의: 코드와 DB 컬럼명이 `THUMBNAL_YN`, `thumbnalYn`으로 되어 있습니다. 일반적인 영어 spelling은 `thumbnail`이지만, 현재 DB 컬럼명이 이 이름이라면 코드도 동일하게 유지해야 합니다.

---

## 5. 상품 목록 조회 흐름

```text
1. 사용자가 /product/list.do 접속
2. ProductController.list() 실행
3. productService.doRetrieve() 호출
4. ProductServiceImpl.doRetrieve() 실행
5. productMapper.doRetrieve() 호출
6. productMapper.xml의 doRetrieve SQL 실행
7. ADMIN_HIDE_YN = 'N'인 상품만 조회
8. CREATE_DT 기준 최신순 정렬
9. model에 list 저장
10. product/productList.jsp로 이동
```

목록 조회에서는 관리자에 의해 숨김 처리되지 않은 상품만 노출됩니다.

```sql
WHERE ADMIN_HIDE_YN = 'N'
ORDER BY CREATE_DT DESC
```

---

## 6. 상품 상세 조회 흐름

```text
1. 사용자가 상품 상세 페이지 요청
2. /product/view.do?productNo=상품번호 요청 발생
3. ProductController.view() 실행
4. productService.doSelectOne(product) 호출
5. ProductServiceImpl.doSelectOne() 실행
6. ProductMapper.doSelectOne()으로 상품 상세 정보 조회
7. 판매자 본인이 아닌 경우 조회수 증가
8. ProductImageMapper.doRetrieveByProduct(productNo)로 이미지 목록 조회
9. ProductVO.imageList에 이미지 목록 저장
10. model에 product 저장
11. product/productView.jsp로 이동
```

상세 조회 SQL에서는 상품 정보와 함께 카테고리 경로도 조인하여 가져옵니다.

```text
대분류명 largeName
중분류명 middleName
소분류명 smallName
```

따라서 상세 화면에서는 다음과 같은 카테고리 경로를 출력할 수 있습니다.

```text
디지털기기 > 휴대폰 > 아이폰
```

---

## 7. 상품 수정 흐름

```text
1. 사용자가 수정 버튼 클릭
2. /product/updateForm.do 요청
3. 기존 상품 정보와 대분류 카테고리 목록 조회
4. 수정 폼 출력
5. 사용자가 수정 내용 입력 후 /product/doUpdate.do 요청
6. ProductController.doUpdate() 실행
7. ProductServiceImpl.doUpdate() 실행
8. checkOwner()로 본인 상품인지 확인
9. validateForSave()로 필수값 검증
10. ProductMapper.doUpdate() 실행
11. PRODUCT 테이블 수정
12. 수정된 상품 상세 페이지로 redirect
```

수정은 판매자 본인만 가능하도록 `checkOwner()`에서 DB 상품 정보의 `sallerNo`와 요청한 `sallerNo`를 비교합니다.

---

## 8. 상품 삭제 흐름

```text
1. 사용자가 삭제 요청
2. /product/doDelete.do 요청
3. ProductController.doDelete() 실행
4. ProductServiceImpl.doDelete() 실행
5. checkOwner()로 본인 상품인지 확인
6. ProductMapper.doDelete() 실행
7. PRODUCT 테이블에서 상품 삭제
8. 성공 건수 반환
```

현재 서비스 코드에는 `ProductImageMapper.doDeleteByProduct()` 호출이 들어가 있지 않습니다. 따라서 상품 삭제 시 이미지 테이블 데이터까지 함께 삭제하려면 서비스 코드에 이미지 삭제 로직을 추가하는 것이 좋습니다.

예시:

```java
productImageMapper.doDeleteByProduct(product.getProductNo());
productMapper.doDelete(product);
```

단, 실제 이미지 파일까지 삭제하려면 DB 삭제뿐만 아니라 `D:\SPAM\Upload`에 저장된 파일도 함께 삭제하는 로직이 필요합니다.

---

## 9. 거래상태 변경 흐름

거래상태는 다음 3가지 값만 허용됩니다.

| 코드 | 의미 |
|---|---|
| `01` | 판매중 |
| `02` | 예약중 |
| `03` | 판매완료 |

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

### PRODUCT_IMAGE

상품에 연결된 이미지 정보를 저장합니다.

주요 컬럼:

```text
IMAGE_NO
PRODUCT_NO
ORIGIN_NAME
CHANGE_NAME
FILE_PATH
IMAGE_ORDER
THUMBNAL_YN
CRATE_DT
```

### CATEGORY

상품 분류 정보를 저장합니다.

주요 컬럼:

```text
CATEGORY_NO
PARENT_CATEGORY_NO
CATEGORY_NAME
CATEGORY_LEVEL
SORT_ORDER
CREATE_DT
UPDATE_DT
```

---

## 13. MyBatis Mapper 설명

### productMapper.xml

상품 등록, 수정, 삭제, 상세 조회, 목록 조회, 조회수 증가, 거래상태 변경, 채팅 수 증감 SQL을 담당합니다.

특히 상품 등록 시 `selectKey`를 사용하여 `SEQ_PRODUCT.NEXTVAL`로 상품 번호를 먼저 생성합니다.

```xml
<selectKey keyProperty="productNo" resultType="int" order="BEFORE">
    SELECT SEQ_PRODUCT.NEXTVAL FROM DUAL
</selectKey>
```

이렇게 생성된 `productNo`는 상품 이미지 저장 시 외래키로 사용됩니다.

### productImageMapper.xml

상품 이미지 등록, 특정 상품의 이미지 목록 조회, 특정 상품의 이미지 전체 삭제 SQL을 담당합니다.

이미지 목록은 `IMAGE_ORDER` 기준으로 정렬됩니다.

### categoryMapper.xml

대분류 조회, 자식 카테고리 조회, 전체 카테고리 조회 SQL을 담당합니다.

---

## 14. 테스트 코드 설명

`ProductServiceTest`는 Spring 테스트 환경에서 상품 서비스 기능을 확인하기 위한 코드입니다.

포함된 테스트:

| 테스트 메서드 | 설명 |
|---|---|
| `doInsert()` | 상품 등록 테스트 |
| `doRetrieve()` | 상품 목록 조회 테스트 |
| `doSelectOne()` | 상품 상세 조회 및 카테고리 경로 확인 테스트 |

현재 `doInsert()`와 `doRetrieve()`에는 `@Disabled`가 붙어 있어 실행되지 않고, `doSelectOne()`만 실행됩니다.

테스트할 때는 실제 DB에 존재하는 상품 번호로 `productNo` 값을 변경해야 합니다.

---

## 15. 전체 기능 요약

| 기능 | 구현 여부 | 설명 |
|---|---|---|
| 상품 등록 | 구현 | 상품 정보와 이미지 파일 등록 가능 |
| 상품 목록 조회 | 구현 | 숨김 처리되지 않은 상품만 조회 |
| 상품 상세 조회 | 구현 | 상품 정보, 카테고리 경로, 이미지 목록 조회 |
| 상품 수정 | 구현 | 판매자 본인만 수정 가능 |
| 상품 삭제 | 구현 | 판매자 본인만 삭제 가능 |
| 거래상태 변경 | 구현 | 판매중, 예약중, 판매완료 상태 변경 |
| 채팅 수 증가/감소 | 구현 | 채팅방 생성/삭제 시 카운트 변경 가능 |
| 카테고리 대분류 조회 | 구현 | 등록/수정 폼에서 사용 |
| 카테고리 자식 조회 | 구현 | AJAX로 중분류/소분류 조회 가능 |
| 이미지 업로드 | 구현 | 파일 저장 후 DB에 이미지 정보 저장 |

---

## 16. 보완하면 좋은 부분

### 16.1 Mapper namespace 확인 필요

일부 XML 파일의 namespace가 Java Mapper 패키지명과 다르게 보입니다.

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

---

## 17. 실행 전 확인사항

프로젝트 실행 전 다음 항목을 확인해야 합니다.

```text
1. Oracle DB 접속 정보가 올바른지 확인
2. PRODUCT, PRODUCT_IMAGE, CATEGORY 테이블이 존재하는지 확인
3. SEQ_PRODUCT, SEQ_PRODUCT_IMAGE 시퀀스가 존재하는지 확인
4. MyBatis mapper XML namespace가 Java Mapper와 일치하는지 확인
5. D:\SPAM\Upload 폴더에 쓰기 권한이 있는지 확인
6. 웹에서 /upload/** 경로로 이미지 파일 접근이 가능하도록 resource mapping 설정 확인
7. multipartResolver 설정이 되어 있는지 확인
```

---

## 18. 핵심 정리

이 상품관리 기능은 Spring MVC 구조를 기준으로 다음 역할이 분리되어 있습니다.

```text
Controller: 요청/응답 처리
Service: 비즈니스 로직 처리
Mapper: DB 접근 메서드 정의
XML Mapper: 실제 SQL 작성
VO: 데이터 전달 객체
```

상품 등록 시에는 상품 정보와 이미지가 함께 처리되고, 상세 조회 시에는 상품 정보, 카테고리 경로, 이미지 목록이 함께 조회됩니다. 전체적으로 중고거래 사이트의 상품관리 기본 기능을 구현한 구조입니다.
