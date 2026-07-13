(function () {
    'use strict';

    var confirmModal = null;
    var confirmState = null;

    function byId(id) {
        return document.getElementById(id);
    }

    function isBlank(value) {
        return value == null || String(value).trim() === '';
    }

    function setFieldError(id, message) {
        var input = byId(id);
        var messageEl = document.querySelector('[data-error-for="' + id + '"]');

        if (input) input.classList.toggle('is-invalid', !!message);
        if (messageEl) messageEl.textContent = message || '';
    }

    function clearFormErrors(form) {
        if (!form) return;

        form.querySelectorAll('.is-invalid').forEach(function (el) {
            el.classList.remove('is-invalid');
        });

        form.querySelectorAll('.field-message').forEach(function (el) {
            el.textContent = '';
        });
    }

    function focusField(id) {
        var input = byId(id);
        if (input && typeof input.focus === 'function') input.focus();
    }

    function validateName(value) {
        return /^[가-힣a-zA-Z]+$/.test(String(value || '').trim());
    }

    function normalizePhoneValue(value) {
        var numbers = String(value || '').replace(/[^0-9]/g, '').slice(0, 11);

        if (numbers.length <= 3) return numbers;
        if (numbers.length <= 7) return numbers.slice(0, 3) + '-' + numbers.slice(3);
        return numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7);
    }

    function buildConfirmModal() {
        if (confirmModal) return confirmModal;

        confirmModal = document.createElement('div');
        confirmModal.className = 'spam-confirm-modal';
        confirmModal.setAttribute('aria-hidden', 'true');
        confirmModal.innerHTML = '' +
            '<div class="spam-confirm-backdrop" data-confirm-cancel="true"></div>' +
            '<section class="spam-confirm-card" role="dialog" aria-modal="true" aria-labelledby="spamConfirmTitle">' +
            '  <div class="spam-confirm-icon">!</div>' +
            '  <h2 id="spamConfirmTitle">확인</h2>' +
            '  <p class="spam-confirm-message">진행하시겠습니까?</p>' +
            '  <div class="spam-confirm-actions">' +
            '    <button type="button" class="spam-confirm-cancel" data-confirm-cancel="true">취소</button>' +
            '    <button type="button" class="spam-confirm-ok">확인</button>' +
            '  </div>' +
            '</section>';

        document.body.appendChild(confirmModal);

        confirmModal.addEventListener('click', function (event) {
            if (event.target.matches('[data-confirm-cancel]')) resolveConfirm(false);
            if (event.target.classList.contains('spam-confirm-ok')) resolveConfirm(true);
        });

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && confirmModal.classList.contains('is-open')) {
                resolveConfirm(false);
            }
        });

        return confirmModal;
    }

    function resolveConfirm(result) {
        if (!confirmState) return;

        var resolver = confirmState.resolve;
        confirmState = null;
        confirmModal.classList.remove('is-open');
        confirmModal.setAttribute('aria-hidden', 'true');
        document.body.classList.remove('no-scroll');
        resolver(result);
    }

    function spamConfirm(options) {
        var modal = buildConfirmModal();
        var title = options && options.title ? options.title : '확인';
        var message = options && options.message ? options.message : '진행하시겠습니까?';
        var okText = options && options.okText ? options.okText : '확인';
        var cancelText = options && options.cancelText ? options.cancelText : '취소';

        modal.querySelector('#spamConfirmTitle').textContent = title;
        modal.querySelector('.spam-confirm-message').textContent = message;
        modal.querySelector('.spam-confirm-ok').textContent = okText;
        modal.querySelector('.spam-confirm-cancel').textContent = cancelText;

        modal.classList.add('is-open');
        modal.setAttribute('aria-hidden', 'false');
        document.body.classList.add('no-scroll');
        modal.querySelector('.spam-confirm-cancel').focus();

        return new Promise(function (resolve) {
            confirmState = { resolve: resolve };
        });
    }

    function submitAfterConfirm(form, options) {
        if (form.dataset.confirmed === 'true') return true;
        spamConfirm(options).then(function (ok) {
            if (!ok) return;
            form.dataset.confirmed = 'true';
            if (typeof form.requestSubmit === 'function') {
                form.requestSubmit();
            } else {
                form.submit();
            }
        });
        return false;
    }

    function bindPhoneFormat() {
        document.querySelectorAll('[data-format="phone"]').forEach(function (input) {
            input.addEventListener('input', function () {
                input.value = normalizePhoneValue(input.value);
            });
        });
    }

    function bindJoinForm() {
        var form = byId('joinForm');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            clearFormErrors(form);

            var userId = byId('userId');
            var password = byId('password');
            var passwordConfirm = byId('passwordConfirm');
            var userName = byId('userName');
            var phoneNum = byId('phoneNum');
            var birthDt = byId('birthDt');

            if (isBlank(userId.value)) {
                event.preventDefault();
                setFieldError('userId', '아이디를 입력하세요.');
                focusField('userId');
                return;
            }

            if (isBlank(password.value)) {
                event.preventDefault();
                setFieldError('password', '비밀번호를 입력하세요.');
                focusField('password');
                return;
            }

            if (password.value.length < 4) {
                event.preventDefault();
                setFieldError('password', '비밀번호는 4자 이상 입력하세요.');
                focusField('password');
                return;
            }

            if (password.value !== passwordConfirm.value) {
                event.preventDefault();
                setFieldError('passwordConfirm', '비밀번호와 비밀번호 확인 값이 다릅니다.');
                focusField('passwordConfirm');
                return;
            }

            if (isBlank(userName.value)) {
                event.preventDefault();
                setFieldError('userName', '이름을 입력하세요.');
                focusField('userName');
                return;
            }

            if (!validateName(userName.value)) {
                event.preventDefault();
                setFieldError('userName', '이름은 한글 또는 영문만 입력하세요.');
                focusField('userName');
                return;
            }

            if (isBlank(phoneNum.value)) {
                event.preventDefault();
                setFieldError('phoneNum', '전화번호를 입력하세요.');
                focusField('phoneNum');
                return;
            }

            if (birthDt && !isBlank(birthDt.value) && !/^\d{4}-\d{2}-\d{2}$/.test(birthDt.value)) {
                event.preventDefault();
                setFieldError('birthDt', 'YYYY-MM-DD 형식으로 입력하세요.');
                focusField('birthDt');
            }
        });
    }

    function bindLoginForm() {
        var form = byId('loginForm');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            clearFormErrors(form);

            var userId = byId('loginUserId');
            var password = byId('loginPassword');

            if (isBlank(userId.value)) {
                event.preventDefault();
                setFieldError('loginUserId', '아이디를 입력하세요.');
                focusField('loginUserId');
                return;
            }

            if (isBlank(password.value)) {
                event.preventDefault();
                setFieldError('loginPassword', '비밀번호를 입력하세요.');
                focusField('loginPassword');
            }
        });
    }

    function bindUpdateForm() {
        var form = byId('updateForm');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            clearFormErrors(form);

            var userName = byId('updateUserName');
            var phoneNum = byId('updatePhoneNum');
            var birthDt = byId('updateBirthDt');

            if (isBlank(userName.value)) {
                event.preventDefault();
                setFieldError('updateUserName', '이름을 입력하세요.');
                focusField('updateUserName');
                return;
            }

            if (!validateName(userName.value)) {
                event.preventDefault();
                setFieldError('updateUserName', '이름은 한글 또는 영문만 입력하세요.');
                focusField('updateUserName');
                return;
            }

            if (isBlank(phoneNum.value)) {
                event.preventDefault();
                setFieldError('updatePhoneNum', '전화번호를 입력하세요.');
                focusField('updatePhoneNum');
                return;
            }

            if (birthDt && !isBlank(birthDt.value) && !/^\d{4}-\d{2}-\d{2}$/.test(birthDt.value)) {
                event.preventDefault();
                setFieldError('updateBirthDt', 'YYYY-MM-DD 형식으로 입력하세요.');
                focusField('updateBirthDt');
                return;
            }

            if (!submitAfterConfirm(form, {
                title: '회원정보 수정',
                message: '입력한 정보로 회원정보를 수정하시겠습니까?',
                okText: '수정하기'
            })) {
                event.preventDefault();
            }
        });
    }

    function bindPasswordForm() {
        var form = byId('passwordForm');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            clearFormErrors(form);

            var currentPassword = byId('currentPassword');
            var newPassword = byId('newPassword');
            var newPasswordConfirm = byId('newPasswordConfirm');

            if (isBlank(currentPassword.value)) {
                event.preventDefault();
                setFieldError('currentPassword', '현재 비밀번호를 입력하세요.');
                focusField('currentPassword');
                return;
            }

            if (isBlank(newPassword.value)) {
                event.preventDefault();
                setFieldError('newPassword', '새 비밀번호를 입력하세요.');
                focusField('newPassword');
                return;
            }

            if (newPassword.value.length < 4) {
                event.preventDefault();
                setFieldError('newPassword', '새 비밀번호는 4자 이상 입력하세요.');
                focusField('newPassword');
                return;
            }

            if (newPassword.value !== newPasswordConfirm.value) {
                event.preventDefault();
                setFieldError('newPasswordConfirm', '새 비밀번호와 확인 값이 다릅니다.');
                focusField('newPasswordConfirm');
                return;
            }

            if (!submitAfterConfirm(form, {
                title: '비밀번호 변경',
                message: '비밀번호를 변경하시겠습니까?',
                okText: '변경하기'
            })) {
                event.preventDefault();
            }
        });
    }

    function bindWithdrawForm() {
        var showButton = byId('showWithdrawButton');
        var hideButton = byId('hideWithdrawButton');
        var form = byId('withdrawForm');

        if (showButton) {
            showButton.addEventListener('click', function () {
                showWithdrawArea();
            });
        }

        if (hideButton) {
            hideButton.addEventListener('click', function () {
                hideWithdrawArea();
            });
        }

        if (!form) return;

        form.addEventListener('submit', function (event) {
            var password = byId('withdrawPassword');
            setFieldError('withdrawPassword', '');

            if (!password || isBlank(password.value)) {
                event.preventDefault();
                setFieldError('withdrawPassword', '회원탈퇴를 진행하려면 비밀번호를 입력하세요.');
                focusField('withdrawPassword');
                return;
            }

            if (!submitAfterConfirm(form, {
                title: '회원탈퇴',
                // 2026-07-13 [수정] 탈퇴 후 동일 정보 재가입 가능 정책 반영
                message: '회원탈퇴 시 기존 계정으로 로그인할 수 없습니다. 재가입 시 새로운 회원번호가 발급됩니다. 정말 탈퇴하시겠습니까?',
                okText: '회원탈퇴'
            })) {
                event.preventDefault();
            }
        });
    }

    function bindSearchForm() {
        var searchDiv = byId('searchDiv');
        var searchWord = byId('searchWord');
        if (!searchDiv || !searchWord) return;

        function updatePlaceholder() {
            var labelMap = {
                userId: '아이디를 입력하세요.',
                userName: '이름을 입력하세요.',
                nickname: '닉네임을 입력하세요.',
                phoneNum: '전화번호를 입력하세요.',
                email: '이메일을 입력하세요.',
                userStatus: '상태 코드를 입력하세요. 예: 01, 02, 03, 04'
            };
            searchWord.placeholder = labelMap[searchDiv.value] || '검색어를 입력하세요.';
        }

        searchDiv.addEventListener('change', updatePlaceholder);
        updatePlaceholder();
    }

    function bindLogoutConfirm() {
        document.querySelectorAll('.js-confirm-logout').forEach(function (link) {
            if (link.dataset.confirmBound === 'true') return;
            link.dataset.confirmBound = 'true';
            link.addEventListener('click', function (event) {
                event.preventDefault();
                spamConfirm({
                    title: '로그아웃',
                    message: '로그아웃 하시겠습니까?',
                    okText: '로그아웃'
                }).then(function (ok) {
                    if (ok) window.location.href = link.href;
                });
            });
        });
    }

    function bindAdminChangeConfirm() {
        document.querySelectorAll('.admin-change-form').forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!submitAfterConfirm(form, {
                    title: form.querySelector('.btn') && form.querySelector('.btn').textContent.indexOf('권한') !== -1 ? '회원권한 변경' : '회원상태 변경',
                    message: form.getAttribute('data-confirm-message') || '선택한 값으로 변경하시겠습니까?',
                    okText: '변경하기'
                })) {
                    event.preventDefault();
                }
            });
        });
    }

    // 2026-07-13 [수정] 관리자 상세뿐 아니라 회원목록 검색 select에도 동일한 UI를 적용한다.
    function bindCustomSelects() {
        document.querySelectorAll('.admin-change-form select.select, .admin-filter-box select.select').forEach(function (select) {
            if (select.dataset.customized === 'true') return;
            select.dataset.customized = 'true';
            select.classList.add('is-custom-hidden');

            var wrapper = document.createElement('div');
            wrapper.className = 'spam-select';
            wrapper.setAttribute('tabindex', '0');

            var current = document.createElement('button');
            current.className = 'spam-select-current';
            current.type = 'button';

            var list = document.createElement('div');
            list.className = 'spam-select-options';

            function selectedText() {
                return select.options[select.selectedIndex] ? select.options[select.selectedIndex].textContent : '';
            }

            function render() {
                current.textContent = selectedText();
                list.innerHTML = '';
                Array.prototype.forEach.call(select.options, function (option) {
                    var item = document.createElement('button');
                    item.type = 'button';
                    item.className = 'spam-select-option';
                    item.textContent = option.textContent;
                    item.dataset.value = option.value;
                    item.classList.toggle('is-selected', option.selected);
                    item.addEventListener('click', function () {
                        select.value = option.value;
                        select.dispatchEvent(new Event('change', { bubbles: true }));
                        wrapper.classList.remove('is-open');
                        render();
                    });
                    list.appendChild(item);
                });
            }

            current.addEventListener('click', function () {
                document.querySelectorAll('.spam-select.is-open').forEach(function (other) {
                    if (other !== wrapper) other.classList.remove('is-open');
                });
                wrapper.classList.toggle('is-open');
            });

            wrapper.addEventListener('keydown', function (event) {
                if (event.key === 'Escape') wrapper.classList.remove('is-open');
            });

            document.addEventListener('click', function (event) {
                if (!wrapper.contains(event.target)) wrapper.classList.remove('is-open');
            });

            wrapper.appendChild(current);
            wrapper.appendChild(list);
            select.insertAdjacentElement('afterend', wrapper);
            render();
        });
    }

    window.showWithdrawArea = function () {
        var startArea = byId('withdrawStartArea');
        var confirmArea = byId('withdrawConfirmArea');
        var password = byId('withdrawPassword');

        if (startArea) startArea.classList.add('hidden');
        if (confirmArea) confirmArea.classList.remove('hidden');
        if (password) password.focus();
    };

    window.hideWithdrawArea = function () {
        var startArea = byId('withdrawStartArea');
        var confirmArea = byId('withdrawConfirmArea');
        var password = byId('withdrawPassword');

        if (confirmArea) confirmArea.classList.add('hidden');
        if (startArea) startArea.classList.remove('hidden');
        if (password) password.value = '';
        setFieldError('withdrawPassword', '');
    };

    document.addEventListener('DOMContentLoaded', function () {
        bindPhoneFormat();
        bindJoinForm();
        bindLoginForm();
        bindUpdateForm();
        bindPasswordForm();
        bindWithdrawForm();
        bindSearchForm();
        bindLogoutConfirm();
        bindAdminChangeConfirm();
        bindCustomSelects();
    });
}());
