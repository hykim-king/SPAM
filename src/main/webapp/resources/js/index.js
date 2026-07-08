/*
 * SPAM 공통/메인 JS
 *
 * [변경] 검색 검증, 카테고리 메가 메뉴, TOP 이동, 개인정보처리방침 팝업, 배너 버튼 기본 동작을 담당한다.
 */
(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        bindSearchValidation();
        bindScrollTop();
        bindMobileMenu();
        bindHeroButtons();
        bindCategoryMegaMenu();
        bindPrivacyModal();
    });

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

    function bindScrollTop() {
        var topButtons = document.querySelectorAll('.js-scroll-top');

        topButtons.forEach(function (button) {
            button.addEventListener('click', function (event) {
                event.preventDefault();
                window.scrollTo({ top: 0, behavior: 'smooth' });
            });
        });
    }

    function bindMobileMenu() {
        var button = document.querySelector('.mobile-menu-button');
        var categoryNav = document.querySelector('.category-nav');

        if (!button || !categoryNav) return;

        button.addEventListener('click', function () {
            categoryNav.classList.toggle('is-open');
            categoryNav.scrollIntoView({ behavior: 'smooth', block: 'start' });
        });
    }

    function bindHeroButtons() {
        var arrows = document.querySelectorAll('.hero-arrow');
        var dots = document.querySelectorAll('.hero-dots span');
        var currentIndex = 0;

        if (arrows.length === 0 || dots.length === 0) return;

        function renderDots() {
            dots.forEach(function (dot, index) {
                dot.classList.toggle('active', index === currentIndex);
            });
        }

        arrows.forEach(function (arrow) {
            arrow.addEventListener('click', function () {
                if (arrow.classList.contains('hero-arrow-left')) {
                    currentIndex = (currentIndex - 1 + dots.length) % dots.length;
                } else {
                    currentIndex = (currentIndex + 1) % dots.length;
                }
                renderDots();
            });
        });
    }

    function bindCategoryMegaMenu() {
        var nav = document.querySelector('.js-category-nav');
        var toggle = document.querySelector('.js-category-toggle');
        var links = document.querySelectorAll('.category-link[data-category]');
        var panels = document.querySelectorAll('.mega-panel[data-panel]');

        if (!nav || !toggle || panels.length === 0) return;

        function activatePanel(category) {
            panels.forEach(function (panel) {
                panel.classList.toggle('is-active', panel.getAttribute('data-panel') === category);
            });
        }

        links.forEach(function (link) {
            link.addEventListener('mouseenter', function () {
                activatePanel(link.getAttribute('data-category'));
            });

            link.addEventListener('focus', function () {
                activatePanel(link.getAttribute('data-category'));
            });
        });

        toggle.addEventListener('click', function () {
            var isOpen = nav.classList.toggle('is-open');
            toggle.setAttribute('aria-expanded', isOpen ? 'true' : 'false');
        });

        document.addEventListener('click', function (event) {
            if (!nav.contains(event.target)) {
                nav.classList.remove('is-open');
                toggle.setAttribute('aria-expanded', 'false');
            }
        });

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape') {
                nav.classList.remove('is-open');
                toggle.setAttribute('aria-expanded', 'false');
            }
        });
    }

    function bindPrivacyModal() {
        var modal = document.getElementById('privacyPolicyModal');
        var openButtons = document.querySelectorAll('.js-privacy-open');
        var closeButtons = document.querySelectorAll('.js-privacy-close');

        if (!modal || openButtons.length === 0) return;

        function openModal() {
            modal.classList.add('is-open');
            modal.setAttribute('aria-hidden', 'false');
            document.body.classList.add('no-scroll');
        }

        function closeModal() {
            modal.classList.remove('is-open');
            modal.setAttribute('aria-hidden', 'true');
            document.body.classList.remove('no-scroll');
        }

        openButtons.forEach(function (button) {
            button.addEventListener('click', openModal);
        });

        closeButtons.forEach(function (button) {
            button.addEventListener('click', closeModal);
        });

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && modal.classList.contains('is-open')) {
                closeModal();
            }
        });
    }
}());
