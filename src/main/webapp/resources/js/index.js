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
        bindSpamNoticeModal();
        renderProductDates();
    });

    function parseProductDate(value) {
        var match = String(value || '').trim().match(
            /^(\d{4})-(\d{2})-(\d{2})(?:[ T](\d{2}):(\d{2})(?::(\d{2}))?)?$/
        );
        if (!match) return null;

        var date = new Date(
            Number(match[1]),
            Number(match[2]) - 1,
            Number(match[3]),
            Number(match[4] || 0),
            Number(match[5] || 0),
            Number(match[6] || 0)
        );
        return Number.isNaN(date.getTime()) ? null : date;
    }

    function formatProductDate(date, now) {
        var sameYear = date.getFullYear() === now.getFullYear();
        var baseText = sameYear
            ? (date.getMonth() + 1) + '월 ' + date.getDate() + '일'
            : date.getFullYear() + '년 ' + (date.getMonth() + 1) + '월 ' + date.getDate() + '일';
        var elapsedMinutes = Math.floor((now.getTime() - date.getTime()) / (60 * 1000));
        var elapsedHours = Math.floor(elapsedMinutes / 60);

        if (elapsedMinutes < 0 || elapsedMinutes > 48 * 60) return baseText;
        if (elapsedMinutes <= 120) {
            return elapsedHours === 0
                ? baseText + ' (' + Math.max(elapsedMinutes, 0) + '분 전)'
                : baseText + ' (' + elapsedHours + '시간 ' + (elapsedMinutes % 60) + '분 전)';
        }
        return baseText + ' (' + elapsedHours + '시간 전)';
    }

    function renderProductDates() {
        var now = new Date();
        document.querySelectorAll('.js-product-date').forEach(function (element) {
            var rawValue = element.getAttribute('data-product-date');
            var date = parseProductDate(rawValue);
            if (!date) return;
            element.textContent = formatProductDate(date, now);
            element.setAttribute('datetime', date.toISOString());
            element.setAttribute('title', rawValue);
        });
    }

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

        // 2026-07-14 [수정] 전체 메뉴 클릭은 링크 이동에 맡기고 hover/focus만 메가 메뉴를 연다.

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


    var spamNoticeModal = null;
    var spamNoticeAction = null;

    /**
     * 2026-07-13 [추가] 별도 안내 페이지 대신 현재 화면 위에 공통 안내 모달을 표시한다.
     * type: login | forbidden | ready
     */
    function openSpamNoticeModal(type, options) {
        options = options || {};
        spamNoticeModal = spamNoticeModal || document.getElementById('spamNoticeModal');
        if (!spamNoticeModal) return;

        var title = spamNoticeModal.querySelector('#spamNoticeTitle');
        var message = spamNoticeModal.querySelector('#spamNoticeMessage');
        var actions = spamNoticeModal.querySelector('.spam-confirm-actions');
        var cancelButton = spamNoticeModal.querySelector('.spam-confirm-cancel');
        var okButton = spamNoticeModal.querySelector('.spam-confirm-ok');
        var loginUrl = options.loginUrl || spamNoticeModal.getAttribute('data-login-url');

        var presets = {
            login: {
                title: '로그인이 필요한 서비스입니다.',
                message: '마이페이지, 상품 등록, 채팅, 신고 등은 로그인 후 이용할 수 있습니다.',
                okText: '로그인',
                cancelText: '취소',
                single: false
            },
            forbidden: {
                title: '접근 권한이 없습니다.',
                message: '현재 계정으로는 해당 메뉴를 이용할 수 없습니다.',
                okText: '확인',
                cancelText: '취소',
                single: true
            },
            ready: {
                title: '서비스 준비중',
                message: '아직 완성되지 않은 기능입니다.\n빠르게 이용할 수 있도록 준비하겠습니다.',
                okText: '확인',
                cancelText: '취소',
                single: true
            }
        };

        var preset = presets[type] || presets.ready;
        title.textContent = options.title || preset.title;
        message.textContent = options.message || preset.message;
        okButton.textContent = options.okText || preset.okText;
        cancelButton.textContent = options.cancelText || preset.cancelText;
        actions.classList.toggle('is-single', options.single === true || (options.single !== false && preset.single));

        spamNoticeAction = function () {
            if (typeof options.onConfirm === 'function') {
                options.onConfirm();
                return;
            }
            if (type === 'login' && loginUrl) {
                window.location.href = loginUrl;
            }
        };

        spamNoticeModal.classList.add('is-open');
        spamNoticeModal.setAttribute('aria-hidden', 'false');
        document.body.classList.add('no-scroll');
        okButton.focus();
    }

    function closeSpamNoticeModal() {
        spamNoticeModal = spamNoticeModal || document.getElementById('spamNoticeModal');
        if (!spamNoticeModal) return;

        spamNoticeModal.classList.remove('is-open');
        spamNoticeModal.setAttribute('aria-hidden', 'true');
        document.body.classList.remove('no-scroll');
        spamNoticeAction = null;
    }

    function bindSpamNoticeModal() {
        spamNoticeModal = document.getElementById('spamNoticeModal');
        if (!spamNoticeModal) return;

        document.querySelectorAll('[data-spam-modal]').forEach(function (trigger) {
            if (trigger.dataset.spamModalBound === 'true') return;
            trigger.dataset.spamModalBound = 'true';

            trigger.addEventListener('click', function (event) {
                var triggerType = trigger.getAttribute('data-spam-modal');
                if (triggerType !== 'login' && triggerType !== 'forbidden' && triggerType !== 'ready') return;

                event.preventDefault();
                openSpamNoticeModal(triggerType, {
                    title: trigger.getAttribute('data-modal-title') || '',
                    message: trigger.getAttribute('data-modal-message') || '',
                    loginUrl: trigger.getAttribute('data-login-url') || ''
                });
            });
        });

        spamNoticeModal.querySelectorAll('.js-spam-notice-close').forEach(function (button) {
            button.addEventListener('click', closeSpamNoticeModal);
        });

        var okButton = spamNoticeModal.querySelector('.js-spam-notice-ok');
        if (okButton) {
            okButton.addEventListener('click', function () {
                var action = spamNoticeAction;
                closeSpamNoticeModal();
                if (action) action();
            });
        }

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && spamNoticeModal.classList.contains('is-open')) {
                closeSpamNoticeModal();
            }
        });

        var params = new URLSearchParams(window.location.search);
        var modalType = params.get('modal');
        if (modalType === 'login' || modalType === 'forbidden' || modalType === 'ready') {
            openSpamNoticeModal(modalType);

            // 새로고침할 때 모달이 반복 노출되지 않도록 modal 파라미터만 주소에서 제거한다.
            params.delete('modal');
            var cleanQuery = params.toString();
            var cleanUrl = window.location.pathname + (cleanQuery ? '?' + cleanQuery : '') + window.location.hash;
            window.history.replaceState({}, document.title, cleanUrl);
        }
    }

    window.SPAMModal = {
        login: function (options) { openSpamNoticeModal('login', options); },
        forbidden: function (options) { openSpamNoticeModal('forbidden', options); },
        ready: function (options) { openSpamNoticeModal('ready', options); },
        close: closeSpamNoticeModal
    };

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
