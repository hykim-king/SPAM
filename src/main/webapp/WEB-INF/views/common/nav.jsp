<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!--
    [추가] 공통 카테고리 내비게이션
    [수정 필요] 카테고리 테이블 연동 후 고정 메뉴를 DB 조회 결과로 교체 가능
-->
<nav class="category-nav" aria-label="상품 카테고리">
    <div class="category-inner">
        <a class="active" href="${CP}/product/list.do">전체</a>
        <a href="${CP}/product/list.do?category=digital">디지털/가전</a>
        <a href="${CP}/product/list.do?category=fashion">패션/잡화</a>
        <a href="${CP}/product/list.do?category=beauty">뷰티/미용</a>
        <a href="${CP}/product/list.do?category=living">생활/가구</a>
        <a href="${CP}/product/list.do?category=baby">유아동</a>
        <a href="${CP}/product/list.do?category=sports">스포츠/레저</a>
        <a href="${CP}/product/list.do?category=book">도서/티켓</a>
        <a href="${CP}/product/list.do?category=etc">기타</a>
        <button type="button" class="category-more" aria-label="전체 카테고리 보기">☰</button>
    </div>
</nav>
