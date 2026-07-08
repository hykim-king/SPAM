/*
 * SPAM 메인화면 JS
 *
 * [추가] 검색어 검증, TOP 이동, 모바일 메뉴 기본 동작을 담당한다.
 * [수정 필요] 상품관리/검색 API 연동 후 검색 조건과 자동완성 기능을 추가할 수 있다.
 */
(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        bindSearchValidation();
        bindScrollTop();
        bindMobileMenu();
        bindHeroButtons();
    });

    /** 검색어 없이 검색했을 때 빈 목록 페이지로 가지 않도록 안내 */
    function bindSearchValidation() {
        var searchForm = document.querySelector('.search-form');
        var searchInput = document.getElementById('mainSearchWord');

        if (!searchForm || !searchInput) return;

        searchForm.addEventListener('submit', function (event) {
            if (searchInput.value.trim().length === 0) {
                event.preventDefault();
                alert('검색어를 입력해주세요.');
                searchInput.focus();
            }
        });
    }

    /** PC 플로팅바 TOP 버튼 */
    function bindScrollTop() {
        var topButtons = document.querySelectorAll('.js-scroll-top');

        topButtons.forEach(function (button) {
            button.addEventListener('click', function (event) {
                event.preventDefault();
                window.scrollTo({ top: 0, behavior: 'smooth' });
            });
        });
    }

    /** 모바일 메뉴 버튼: 현재는 카테고리 nav 위치로 이동 */
    function bindMobileMenu() {
        var button = document.querySelector('.mobile-menu-button');
        var categoryNav = document.querySelector('.category-nav');

        if (!button || !categoryNav) return;

        button.addEventListener('click', function () {
            categoryNav.scrollIntoView({ behavior: 'smooth', block: 'start' });
        });
    }

    /** 배너 좌우 버튼: 실제 슬라이더 붙이기 전 임시 안내 */
    function bindHeroButtons() {
        var arrows = document.querySelectorAll('.hero-arrow');

        arrows.forEach(function (arrow) {
            arrow.addEventListener('click', function () {
                alert('배너 슬라이드는 추후 이벤트/추천 배너 데이터와 연결하면 됩니다.');
            });
        });
    }
})();
