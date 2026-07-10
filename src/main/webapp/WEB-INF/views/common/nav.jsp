<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty CP}">
    <c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
</c:if>

<!-- 공통 카테고리 내비게이션
    - 기존 좌측 '전체' 텍스트를 햄버거 아이콘 메뉴로 변경
    - hover 시 하단 메가 메뉴 노출, 햄버거 클릭 시 고정
    - 대분류/중분류/소분류 링크는 모두 상품 목록 조회 URL로 연결
-->
<nav class="category-nav js-category-nav" aria-label="상품 카테고리">
    <div class="category-inner">
        <button type="button" class="category-all-button js-category-toggle" aria-expanded="false" aria-controls="categoryMegaMenu">
            <span class="hamburger-icon" aria-hidden="true"><span></span><span></span><span></span></span>
            <span class="category-all-text">전체</span>
        </button>

        <a class="category-link" data-category="digital" href="${CP}/product/list.do?categoryNo=1">디지털/가전</a>
        <a class="category-link" data-category="fashion" href="${CP}/product/list.do?categoryNo=2">패션/잡화</a>
        <a class="category-link" data-category="beauty" href="${CP}/product/list.do?categoryNo=3">뷰티/미용</a>
        <a class="category-link" data-category="living" href="${CP}/product/list.do?categoryNo=4">생활/가구</a>
        <a class="category-link" data-category="baby" href="${CP}/product/list.do?categoryNo=5">유아동</a>
        <a class="category-link" data-category="sports" href="${CP}/product/list.do?categoryNo=6">스포츠/레저</a>
        <a class="category-link" data-category="book" href="${CP}/product/list.do?categoryNo=7">도서/티켓</a>
        <a class="category-link" data-category="etc" href="${CP}/product/list.do?categoryNo=8">기타</a>
    </div>

    <div class="category-mega" id="categoryMegaMenu">
        <div class="category-mega-inner">
            <div class="mega-summary">
                <strong>전체 카테고리</strong>
                <p>대분류에 마우스를 올리면 관련 중분류와 소분류를 바로 확인할 수 있습니다.</p>
                <a href="${CP}/product/list.do">전체 상품 보기</a>
            </div>

            <div class="mega-panels">
                <section class="mega-panel is-active" data-panel="digital">
                    <div class="mega-content">
                        <div class="mega-heading">
                            <a href="${CP}/product/list.do?categoryNo=1">디지털/가전</a>
                            <p>노트북, 휴대폰, 생활가전까지 필요한 전자제품을 찾아보세요.</p>
                        </div>
                        <div class="mega-columns">
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=11">휴대폰/태블릿</a>
                                <a href="${CP}/product/list.do?categoryNo=101">스마트폰</a>
                                <a href="${CP}/product/list.do?categoryNo=102">태블릿</a>
                                <a href="${CP}/product/list.do?categoryNo=103">스마트워치</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=12">PC/주변기기</a>
                                <a href="${CP}/product/list.do?categoryNo=104">노트북</a>
                                <a href="${CP}/product/list.do?categoryNo=105">모니터</a>
                                <a href="${CP}/product/list.do?categoryNo=106">키보드/마우스</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=13">생활가전</a>
                                <a href="${CP}/product/list.do?categoryNo=107">오디오</a>
                                <a href="${CP}/product/list.do?categoryNo=108">카메라</a>
                                <a href="${CP}/product/list.do?categoryNo=109">주방가전</a>
                            </div>
                        </div>
                    </div>
                    <a class="mega-visual" href="${CP}/product/list.do?categoryNo=1">
                        <img src="${CP}/resources/images/category/category_digital.svg" alt="디지털/가전 아이콘">
                        <span>전자기기 모아보기</span>
                    </a>
                </section>

                <section class="mega-panel" data-panel="fashion">
                    <div class="mega-content">
                        <div class="mega-heading">
                            <a href="${CP}/product/list.do?categoryNo=2">패션/잡화</a>
                            <p>옷, 신발, 가방, 액세서리를 취향별로 둘러보세요.</p>
                        </div>
                        <div class="mega-columns">
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=14">의류</a>
                                <a href="${CP}/product/list.do?categoryNo=110">아우터</a>
                                <a href="${CP}/product/list.do?categoryNo=111">상의</a>
                                <a href="${CP}/product/list.do?categoryNo=112">하의</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=15">신발/가방</a>
                                <a href="${CP}/product/list.do?categoryNo=113">스니커즈</a>
                                <a href="${CP}/product/list.do?categoryNo=114">가방</a>
                                <a href="${CP}/product/list.do?categoryNo=115">지갑</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=16">액세서리</a>
                                <a href="${CP}/product/list.do?categoryNo=116">시계</a>
                                <a href="${CP}/product/list.do?categoryNo=117">주얼리</a>
                                <a href="${CP}/product/list.do?categoryNo=118">모자</a>
                            </div>
                        </div>
                    </div>
                    <a class="mega-visual" href="${CP}/product/list.do?categoryNo=2">
                        <img src="${CP}/resources/images/category/category_fashion.svg" alt="패션/잡화 아이콘">
                        <span>패션 상품 보기</span>
                    </a>
                </section>

                <section class="mega-panel" data-panel="beauty">
                    <div class="mega-content">
                        <div class="mega-heading">
                            <a href="${CP}/product/list.do?categoryNo=3">뷰티/미용</a>
                            <p>화장품, 향수, 미용기기를 필요한 조건으로 찾아보세요.</p>
                        </div>
                        <div class="mega-columns">
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=17">화장품</a>
                                <a href="${CP}/product/list.do?categoryNo=119">베이스</a>
                                <a href="${CP}/product/list.do?categoryNo=120">색조</a>
                                <a href="${CP}/product/list.do?categoryNo=121">스킨케어</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=18">향수/바디</a>
                                <a href="${CP}/product/list.do?categoryNo=122">향수</a>
                                <a href="${CP}/product/list.do?categoryNo=123">바디케어</a>
                                <a href="${CP}/product/list.do?categoryNo=124">헤어케어</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=19">미용기기</a>
                                <a href="${CP}/product/list.do?categoryNo=125">드라이기</a>
                                <a href="${CP}/product/list.do?categoryNo=126">고데기</a>
                                <a href="${CP}/product/list.do?categoryNo=127">미용소품</a>
                            </div>
                        </div>
                    </div>
                    <a class="mega-visual" href="${CP}/product/list.do?categoryNo=3">
                        <img src="${CP}/resources/images/category/category_beauty.svg" alt="뷰티/미용 아이콘">
                        <span>뷰티 상품 보기</span>
                    </a>
                </section>

                <section class="mega-panel" data-panel="living">
                    <div class="mega-content">
                        <div class="mega-heading">
                            <a href="${CP}/product/list.do?categoryNo=4">생활/가구</a>
                            <p>자취방, 집 꾸미기, 생활용품을 합리적으로 준비하세요.</p>
                        </div>
                        <div class="mega-columns">
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=20">가구</a>
                                <a href="${CP}/product/list.do?categoryNo=128">테이블</a>
                                <a href="${CP}/product/list.do?categoryNo=129">의자</a>
                                <a href="${CP}/product/list.do?categoryNo=130">수납장</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=21">인테리어</a>
                                <a href="${CP}/product/list.do?categoryNo=131">조명</a>
                                <a href="${CP}/product/list.do?categoryNo=132">패브릭</a>
                                <a href="${CP}/product/list.do?categoryNo=133">식물/화분</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=22">생활용품</a>
                                <a href="${CP}/product/list.do?categoryNo=134">주방용품</a>
                                <a href="${CP}/product/list.do?categoryNo=135">청소용품</a>
                                <a href="${CP}/product/list.do?categoryNo=136">정리수납</a>
                            </div>
                        </div>
                    </div>
                    <a class="mega-visual" href="${CP}/product/list.do?categoryNo=4">
                        <img src="${CP}/resources/images/category/category_living.svg" alt="생활/가구 아이콘">
                        <span>생활 상품 보기</span>
                    </a>
                </section>

                <section class="mega-panel" data-panel="baby">
                    <div class="mega-content">
                        <div class="mega-heading">
                            <a href="${CP}/product/list.do?categoryNo=5">유아동</a>
                            <p>아이 성장 단계에 맞는 육아용품과 장난감을 찾아보세요.</p>
                        </div>
                        <div class="mega-columns">
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=23">유아의류</a>
                                <a href="${CP}/product/list.do?categoryNo=137">아기옷</a>
                                <a href="${CP}/product/list.do?categoryNo=138">아동복</a>
                                <a href="${CP}/product/list.do?categoryNo=139">유아신발</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=24">육아용품</a>
                                <a href="${CP}/product/list.do?categoryNo=140">유모차</a>
                                <a href="${CP}/product/list.do?categoryNo=141">카시트</a>
                                <a href="${CP}/product/list.do?categoryNo=142">식기/수유</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=25">장난감/도서</a>
                                <a href="${CP}/product/list.do?categoryNo=143">장난감</a>
                                <a href="${CP}/product/list.do?categoryNo=144">그림책</a>
                                <a href="${CP}/product/list.do?categoryNo=145">교구</a>
                            </div>
                        </div>
                    </div>
                    <a class="mega-visual" href="${CP}/product/list.do?categoryNo=5">
                        <img src="${CP}/resources/images/category/category_baby.svg" alt="유아동 아이콘">
                        <span>유아동 상품 보기</span>
                    </a>
                </section>

                <section class="mega-panel" data-panel="sports">
                    <div class="mega-content">
                        <div class="mega-heading">
                            <a href="${CP}/product/list.do?categoryNo=6">스포츠/레저</a>
                            <p>운동, 캠핑, 취미 생활을 위한 상품을 모았습니다.</p>
                        </div>
                        <div class="mega-columns">
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=26">운동용품</a>
                                <a href="${CP}/product/list.do?categoryNo=146">홈트레이닝</a>
                                <a href="${CP}/product/list.do?categoryNo=147">자전거</a>
                                <a href="${CP}/product/list.do?categoryNo=148">골프</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=27">캠핑/아웃도어</a>
                                <a href="${CP}/product/list.do?categoryNo=149">텐트</a>
                                <a href="${CP}/product/list.do?categoryNo=150">캠핑의자</a>
                                <a href="${CP}/product/list.do?categoryNo=151">랜턴</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=28">취미</a>
                                <a href="${CP}/product/list.do?categoryNo=152">보드게임</a>
                                <a href="${CP}/product/list.do?categoryNo=153">악기</a>
                                <a href="${CP}/product/list.do?categoryNo=154">수집품</a>
                            </div>
                        </div>
                    </div>
                    <a class="mega-visual" href="${CP}/product/list.do?categoryNo=6">
                        <img src="${CP}/resources/images/category/category_sports.svg" alt="스포츠/레저 아이콘">
                        <span>스포츠 상품 보기</span>
                    </a>
                </section>

                <section class="mega-panel" data-panel="book">
                    <div class="mega-content">
                        <div class="mega-heading">
                            <a href="${CP}/product/list.do?categoryNo=7">도서/티켓</a>
                            <p>책, 공연, 전시, 문화생활 관련 상품을 확인하세요.</p>
                        </div>
                        <div class="mega-columns">
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=29">도서</a>
                                <a href="${CP}/product/list.do?categoryNo=155">소설</a>
                                <a href="${CP}/product/list.do?categoryNo=156">수험서</a>
                                <a href="${CP}/product/list.do?categoryNo=157">IT/개발</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=30">티켓</a>
                                <a href="${CP}/product/list.do?categoryNo=158">공연</a>
                                <a href="${CP}/product/list.do?categoryNo=159">전시</a>
                                <a href="${CP}/product/list.do?categoryNo=160">스포츠경기</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=31">문구</a>
                                <a href="${CP}/product/list.do?categoryNo=161">노트</a>
                                <a href="${CP}/product/list.do?categoryNo=162">펜/필기구</a>
                                <a href="${CP}/product/list.do?categoryNo=163">데스크용품</a>
                            </div>
                        </div>
                    </div>
                    <a class="mega-visual" href="${CP}/product/list.do?categoryNo=7">
                        <img src="${CP}/resources/images/category/category_book.svg" alt="도서/티켓 아이콘">
                        <span>도서/티켓 보기</span>
                    </a>
                </section>

                <section class="mega-panel" data-panel="etc">
                    <div class="mega-content">
                        <div class="mega-heading">
                            <a href="${CP}/product/list.do?categoryNo=8">기타</a>
                            <p>카테고리로 나누기 애매한 다양한 물건도 편하게 등록하세요.</p>
                        </div>
                        <div class="mega-columns">
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=32">반려동물</a>
                                <a href="${CP}/product/list.do?categoryNo=164">용품</a>
                                <a href="${CP}/product/list.do?categoryNo=165">장난감</a>
                                <a href="${CP}/product/list.do?categoryNo=166">의류</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=33">쿠폰/교환권</a>
                                <a href="${CP}/product/list.do?categoryNo=167">카페</a>
                                <a href="${CP}/product/list.do?categoryNo=168">외식</a>
                                <a href="${CP}/product/list.do?categoryNo=169">상품권</a>
                            </div>
                            <div>
                                <a class="middle-link" href="${CP}/product/list.do?categoryNo=34">기타</a>
                                <a href="${CP}/product/list.do?categoryNo=170">무료나눔</a>
                                <a href="${CP}/product/list.do?categoryNo=171">한정판</a>
                                <a href="${CP}/product/list.do?categoryNo=172">그 외</a>
                            </div>
                        </div>
                    </div>
                    <a class="mega-visual" href="${CP}/product/list.do?categoryNo=8">
                        <img src="${CP}/resources/images/category/category_etc.svg" alt="기타 아이콘">
                        <span>기타 상품 보기</span>
                    </a>
                </section>
            </div>
        </div>
    </div>
</nav>
