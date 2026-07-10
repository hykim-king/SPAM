/*
 * SPAM 공통/메인 JS
 * - 검색 검증
 * - 배너 슬라이드
 * - 카테고리 메가 메뉴
 * - TOP 이동
 * - 개인정보처리방침 팝업
 */
(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', function () {
        bindSearchValidation();
        bindScrollTop();
        bindMobileMenu();
        bindHeroSlider();
        bindCategoryMegaMenu();
        bindPrivacyModal();
        bindCommonLogoutConfirm();
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
        document.querySelectorAll('.js-scroll-top').forEach(function (button) {
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
            categoryNav.classList.toggle('is-mobile-open');
            categoryNav.scrollIntoView({ behavior: 'smooth', block: 'start' });
        });
    }

    function bindHeroSlider() {
        var banners = document.querySelectorAll('.hero-banner[data-hero-index]');
        var arrows = document.querySelectorAll('.hero-arrow');
        var dots = document.querySelectorAll('.hero-dots button');
        var currentIndex = 0;

        if (banners.length === 0 || dots.length === 0) return;

        function renderHero(nextIndex) {
            currentIndex = (nextIndex + banners.length) % banners.length;

            banners.forEach(function (banner, index) {
                banner.classList.toggle('is-active', index === currentIndex);
            });

            dots.forEach(function (dot, index) {
                dot.classList.toggle('active', index === currentIndex);
                dot.setAttribute('aria-pressed', index === currentIndex ? 'true' : 'false');
            });
        }

        arrows.forEach(function (arrow) {
            arrow.addEventListener('click', function () {
                renderHero(arrow.classList.contains('hero-arrow-left') ? currentIndex - 1 : currentIndex + 1);
            });
        });

        dots.forEach(function (dot, index) {
            dot.addEventListener('click', function () {
                renderHero(index);
            });
        });

        renderHero(0);
    }

    function bindCategoryMegaMenu() {
        var nav = document.querySelector('.js-category-nav');
        var mega = document.getElementById('categoryMegaMenu');
        var toggle = document.querySelector('.js-category-toggle');
        var links = Array.prototype.slice.call(document.querySelectorAll('.category-link[data-category]'));
        var panels = Array.prototype.slice.call(document.querySelectorAll('.mega-panel[data-panel]'));
        var openTimer = null;
        var closeTimer = null;
        var activeMode = null;
        var activeCategory = null;
        var openDelay = 300;
        var closeDelay = 120;

        if (!nav || !mega || !toggle || panels.length === 0) return;

        function clearOpenTimer() {
            if (openTimer) {
                window.clearTimeout(openTimer);
                openTimer = null;
            }
        }

        function clearCloseTimer() {
            if (closeTimer) {
                window.clearTimeout(closeTimer);
                closeTimer = null;
            }
        }

        function clearAllTimers() {
            clearOpenTimer();
            clearCloseTimer();
        }

        function clearActiveNav() {
            toggle.classList.remove('is-active');
            links.forEach(function (link) {
                link.classList.remove('is-active');
            });
        }

        function activateSinglePanel(category) {
            panels.forEach(function (panel) {
                panel.classList.toggle('is-active', panel.getAttribute('data-panel') === category);
            });
        }

        function activateAllPanels() {
            panels.forEach(function (panel) {
                panel.classList.add('is-active');
            });
        }

        function clampMegaLeft(target, mode) {
            var rect = target.getBoundingClientRect();
            var width = mode === 'all' ? Math.min(1120, window.innerWidth - 32) : 650;
            var minLeft = 20;
            var maxLeft = Math.max(minLeft, window.innerWidth - width - 20);
            var left = Math.min(Math.max(Math.round(rect.left), minLeft), maxLeft);

            nav.style.setProperty('--mega-left', left + 'px');
            nav.style.setProperty('--mega-width', width + 'px');
        }

        function openMenu(mode, category, target) {
            clearAllTimers();

            activeMode = mode;
            activeCategory = category || null;

            clearActiveNav();
            clampMegaLeft(target || toggle, mode);

            nav.classList.add('is-open');
            nav.classList.toggle('is-all-open', mode === 'all');
            toggle.setAttribute('aria-expanded', 'true');

            if (mode === 'all') {
                toggle.classList.add('is-active');
                activateAllPanels();
                return;
            }

            var activeLink = links.find(function (link) {
                return link.getAttribute('data-category') === category;
            });

            if (activeLink) {
                activeLink.classList.add('is-active');
            }

            activateSinglePanel(category || 'digital');
        }

        function scheduleOpen(mode, category, target) {
            clearOpenTimer();
            clearCloseTimer();

            var sameMenu = nav.classList.contains('is-open') &&
                activeMode === mode &&
                (mode === 'all' || activeCategory === category);

            if (sameMenu) return;

            var delay = nav.classList.contains('is-open') ? 0 : openDelay;

            openTimer = window.setTimeout(function () {
                openMenu(mode, category, target);
            }, delay);
        }

        function closeMenu() {
            clearOpenTimer();
            clearCloseTimer();

            closeTimer = window.setTimeout(function () {
                nav.classList.remove('is-open', 'is-all-open');
                clearActiveNav();
                toggle.setAttribute('aria-expanded', 'false');
                activateSinglePanel('digital');
                activeMode = null;
                activeCategory = null;
            }, closeDelay);
        }

        toggle.addEventListener('mouseenter', function () {
            scheduleOpen('all', null, toggle);
        });

        toggle.addEventListener('focus', function () {
            scheduleOpen('all', null, toggle);
        });

        toggle.addEventListener('click', function (event) {
            event.preventDefault();

            if (nav.classList.contains('is-open') && nav.classList.contains('is-all-open')) {
                closeMenu();
                return;
            }

            openMenu('all', null, toggle);
        });

        links.forEach(function (link) {
            link.addEventListener('mouseenter', function () {
                scheduleOpen('single', link.getAttribute('data-category'), link);
            });

            link.addEventListener('focus', function () {
                scheduleOpen('single', link.getAttribute('data-category'), link);
            });
        });

        nav.addEventListener('mouseenter', function () {
            clearCloseTimer();
        });

        mega.addEventListener('mouseenter', function () {
            clearCloseTimer();
        });

        nav.addEventListener('mouseleave', closeMenu);

        document.addEventListener('click', function (event) {
            if (!nav.contains(event.target)) {
                closeMenu();
            }
        });

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape') {
                closeMenu();
            }
        });

        window.addEventListener('resize', function () {
            if (!nav.classList.contains('is-open')) return;

            var target = activeMode === 'all'
                ? toggle
                : links.find(function (link) {
                    return link.getAttribute('data-category') === activeCategory;
                });

            clampMegaLeft(target || toggle, activeMode || 'single');
        });
    }


    var commonConfirmModal = null;
    var commonConfirmResolve = null;

    function buildCommonConfirmModal() {
        if (commonConfirmModal) return commonConfirmModal;
        commonConfirmModal = document.createElement('div');
        commonConfirmModal.className = 'spam-confirm-modal';
        commonConfirmModal.setAttribute('aria-hidden', 'true');
        commonConfirmModal.innerHTML = '' +
            '<div class="spam-confirm-backdrop" data-confirm-cancel="true"></div>' +
            '<section class="spam-confirm-card" role="dialog" aria-modal="true" aria-labelledby="commonConfirmTitle">' +
            '  <div class="spam-confirm-icon">!</div>' +
            '  <h2 id="commonConfirmTitle">확인</h2>' +
            '  <p class="spam-confirm-message">진행하시겠습니까?</p>' +
            '  <div class="spam-confirm-actions">' +
            '    <button type="button" class="spam-confirm-cancel" data-confirm-cancel="true">취소</button>' +
            '    <button type="button" class="spam-confirm-ok">확인</button>' +
            '  </div>' +
            '</section>';
        document.body.appendChild(commonConfirmModal);
        commonConfirmModal.addEventListener('click', function (event) {
            if (event.target.matches('[data-confirm-cancel]')) closeCommonConfirm(false);
            if (event.target.classList.contains('spam-confirm-ok')) closeCommonConfirm(true);
        });
        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && commonConfirmModal.classList.contains('is-open')) closeCommonConfirm(false);
        });
        return commonConfirmModal;
    }

    function closeCommonConfirm(result) {
        if (!commonConfirmResolve) return;
        var resolve = commonConfirmResolve;
        commonConfirmResolve = null;
        commonConfirmModal.classList.remove('is-open');
        commonConfirmModal.setAttribute('aria-hidden', 'true');
        document.body.classList.remove('no-scroll');
        resolve(result);
    }

    function commonConfirm(title, message, okText) {
        var modal = buildCommonConfirmModal();
        modal.querySelector('#commonConfirmTitle').textContent = title || '확인';
        modal.querySelector('.spam-confirm-message').textContent = message || '진행하시겠습니까?';
        modal.querySelector('.spam-confirm-ok').textContent = okText || '확인';
        modal.classList.add('is-open');
        modal.setAttribute('aria-hidden', 'false');
        document.body.classList.add('no-scroll');
        return new Promise(function (resolve) { commonConfirmResolve = resolve; });
    }

    function bindCommonLogoutConfirm() {
        document.querySelectorAll('.js-confirm-logout').forEach(function (link) {
            if (link.dataset.confirmBound === 'true') return;
            link.dataset.confirmBound = 'true';
            link.addEventListener('click', function (event) {
                event.preventDefault();
                commonConfirm('로그아웃', '로그아웃 하시겠습니까?', '로그아웃').then(function (ok) {
                    if (ok) window.location.href = link.href;
                });
            });
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
            if (event.key === 'Escape' && modal.classList.contains('is-open')) closeModal();
        });
    }
}());
