<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="CP" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 상세</title>

<style>
    body {
        margin: 0;
        font-family: Arial, sans-serif;
        background: #f5f6f8;
        color: #222;
    }

    .container {
        width: 1000px;
        margin: 40px auto;
        background: white;
        border-radius: 16px;
        padding: 35px;
        box-shadow: 0 4px 16px rgba(0,0,0,0.08);
    }

    .back {
        display: inline-block;
        margin-bottom: 25px;
        text-decoration: none;
        color: #555;
    }

    .detail-wrap {
        display: flex;
        gap: 40px;
    }

    .image-area {
        width: 430px;
    }

    .main-image {
        width: 430px;
        height: 430px;
        border-radius: 14px;
        background: #eee;
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;
        color: #777;
    }

    .main-image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .sub-images {
        display: flex;
        gap: 10px;
        margin-top: 12px;
        flex-wrap: wrap;
    }

    .sub-images img {
        width: 76px;
        height: 76px;
        border-radius: 8px;
        object-fit: cover;
        border: 1px solid #ddd;
    }

    .info-area {
        flex: 1;
    }

    .category {
        font-size: 14px;
        color: #777;
        margin-bottom: 12px;
    }

    .title {
        font-size: 30px;
        font-weight: bold;
        margin-bottom: 16px;
    }

    .price {
        font-size: 32px;
        color: #ff7a00;
        font-weight: bold;
        margin-bottom: 20px;
    }

    .status {
        display: inline-block;
        padding: 7px 13px;
        border-radius: 20px;
        background: #eef0f3;
        margin-bottom: 22px;
        font-size: 14px;
    }

    .info-list {
        border-top: 1px solid #eee;
        border-bottom: 1px solid #eee;
        padding: 18px 0;
        margin-bottom: 25px;
        line-height: 2;
        color: #555;
    }

    .content {
        margin-top: 35px;
        padding-top: 25px;
        border-top: 1px solid #eee;
    }

    .content h3 {
        margin-top: 0;
    }

    .content-box {
        min-height: 150px;
        line-height: 1.8;
        white-space: pre-line;
        color: #333;
    }

    .button-area {
        display: flex;
        gap: 10px;
        margin-top: 25px;
        flex-wrap: wrap;
    }

    button, .btn {
        border: none;
        padding: 12px 18px;
        border-radius: 8px;
        cursor: pointer;
        text-decoration: none;
        font-size: 15px;
        font-weight: bold;
    }

    .btn-chat {
        background: #ff7a00;
        color: white;
    }

    .btn-edit {
        background: #333;
        color: white;
    }

    .btn-delete {
        background: #e74c3c;
        color: white;
    }

    .btn-list {
        background: #ddd;
        color: #333;
    }
</style>

<script>
    function deleteProduct() {
        if (!confirm("정말 삭제하시겠습니까?")) {
            return;
        }

        const formData = new FormData();
        formData.append("productNo", "${product.productNo}");

        /*
            로그인 기능이 있다면 아래 sallerNo는
            sessionScope.loginUser.userNum 또는 프로젝트에서 쓰는 로그인 회원번호로 바꿔야 합니다.
        */
        formData.append("sallerNo", "${product.sallerNo}");

        fetch("${CP}/product/doDelete.do", {
            method: "POST",
            body: formData
        })
        .then(response => response.text())
        .then(result => {
            if (result.trim() === "1") {
                alert("삭제되었습니다.");
                location.href = "${CP}/product/list.do";
            } else {
                alert("삭제에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error(error);
            alert("삭제 중 오류가 발생했습니다.");
        });
    }

    function updateStatus(status) {
        const formData = new FormData();
        formData.append("productNo", "${product.productNo}");
        formData.append("sallerNo", "${product.sallerNo}");
        formData.append("status", status);

        fetch("${CP}/product/updateStatus.do", {
            method: "POST",
            body: formData
        })
        .then(response => response.text())
        .then(result => {
            if (result.trim() === "1") {
                alert("거래 상태가 변경되었습니다.");
                location.reload();
            } else {
                alert("상태 변경에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error(error);
            alert("상태 변경 중 오류가 발생했습니다.");
        });
    }
</script>
</head>

<body>

<div class="container">

    <a class="back" href="${CP}/product/list.do">← 목록으로</a>

    <c:if test="${empty product}">
        <h2>상품 정보를 찾을 수 없습니다.</h2>
    </c:if>

    <c:if test="${not empty product}">
        <div class="detail-wrap">

            <div class="image-area">
                <div class="main-image">
                    <c:choose>
                        <c:when test="${not empty product.imageList}">
                            <img src="${CP}${product.imageList[0].filePath}" alt="상품 이미지">
                        </c:when>
                        <c:otherwise>
                            등록된 이미지가 없습니다.
                        </c:otherwise>
                    </c:choose>
                </div>

                <c:if test="${not empty product.imageList}">
                    <div class="sub-images">
                        <c:forEach var="img" items="${product.imageList}">
                            <img src="${CP}${img.filePath}" alt="${img.originName}">
                        </c:forEach>
                    </div>
                </c:if>
            </div>

            <div class="info-area">

                <div class="category">
                    ${product.largeName}
                    <c:if test="${not empty product.middleName}"> &gt; ${product.middleName}</c:if>
                    <c:if test="${not empty product.smallName}"> &gt; ${product.smallName}</c:if>
                </div>

                <div class="title">${product.productTitle}</div>

                <div class="price">
                    <fmt:formatNumber value="${product.price}" pattern="#,###" />원
                </div>

                <div class="status">
                    <c:choose>
                        <c:when test="${product.status eq '01'}">판매중</c:when>
                        <c:when test="${product.status eq '02'}">예약중</c:when>
                        <c:when test="${product.status eq '03'}">판매완료</c:when>
                        <c:otherwise>상태 미정</c:otherwise>
                    </c:choose>
                </div>

                <div class="info-list">
                    판매지역: ${product.location}<br>
                    상품상태: ${product.productCondition}<br>
                    조회수: ${product.viewCount}<br>
                    찜 수: ${product.likeCnt}<br>
                    채팅 수: ${product.chatCnt}<br>
                    등록일: ${product.createDt}
                </div>

                <div class="button-area">
                    <button type="button" class="btn-chat">채팅하기</button>

                    <a class="btn btn-edit"
                       href="${CP}/product/updateForm.do?productNo=${product.productNo}&sallerNo=${product.sallerNo}">
                        수정
                    </a>

                    <button type="button" class="btn-delete" onclick="deleteProduct()">삭제</button>

                    <button type="button" class="btn-list" onclick="updateStatus('01')">판매중</button>
                    <button type="button" class="btn-list" onclick="updateStatus('02')">예약중</button>
                    <button type="button" class="btn-list" onclick="updateStatus('03')">판매완료</button>
                </div>
            </div>
        </div>

        <div class="content">
            <h3>상품 설명</h3>
            <div class="content-box">
                ${product.productContent}
            </div>
        </div>
    </c:if>

</div>

</body>
</html>