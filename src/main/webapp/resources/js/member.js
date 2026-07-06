(function () {
    'use strict';

    function byId(id) {
        return document.getElementById(id);
    }

    function isBlank(value) {
        return value == null || String(value).trim() === '';
    }

    function focusAndAlert(element, message) {
        alert(message);
        if (element && typeof element.focus === 'function') {
            element.focus();
        }
    }

    function normalizePhoneValue(value) {
        var numbers = String(value || '').replace(/[^0-9]/g, '').slice(0, 11);

        if (numbers.length <= 3) {
            return numbers;
        }

        if (numbers.length <= 7) {
            return numbers.slice(0, 3) + '-' + numbers.slice(3);
        }

        return numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7);
    }

    function bindPhoneFormat() {
        var phoneInputs = document.querySelectorAll('[data-format="phone"]');

        phoneInputs.forEach(function (input) {
            input.addEventListener('input', function () {
                input.value = normalizePhoneValue(input.value);
            });
        });
    }

    function bindJoinForm() {
        var form = byId('joinForm');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            var userId = byId('userId');
            var password = byId('password');
            var passwordConfirm = byId('passwordConfirm');
            var userName = byId('userName');
            var phoneNum = byId('phoneNum');

            if (isBlank(userId.value)) {
                event.preventDefault();
                focusAndAlert(userId, '아이디를 입력하세요.');
                return;
            }

            if (isBlank(password.value)) {
                event.preventDefault();
                focusAndAlert(password, '비밀번호를 입력하세요.');
                return;
            }

            if (password.value.length < 4) {
                event.preventDefault();
                focusAndAlert(password, '비밀번호는 4자 이상 입력하세요.');
                return;
            }

            if (password.value !== passwordConfirm.value) {
                event.preventDefault();
                focusAndAlert(passwordConfirm, '비밀번호와 비밀번호 확인 값이 다릅니다.');
                return;
            }

            if (isBlank(userName.value)) {
                event.preventDefault();
                focusAndAlert(userName, '이름을 입력하세요.');
                return;
            }

            if (isBlank(phoneNum.value)) {
                event.preventDefault();
                focusAndAlert(phoneNum, '전화번호를 입력하세요.');
            }
        });
    }

    function bindLoginForm() {
        var form = byId('loginForm');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            var userId = byId('loginUserId');
            var password = byId('loginPassword');

            if (isBlank(userId.value)) {
                event.preventDefault();
                focusAndAlert(userId, '아이디를 입력하세요.');
                return;
            }

            if (isBlank(password.value)) {
                event.preventDefault();
                focusAndAlert(password, '비밀번호를 입력하세요.');
            }
        });
    }

    function bindUpdateForm() {
        var form = byId('updateForm');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            var userName = byId('updateUserName');
            var phoneNum = byId('updatePhoneNum');

            if (isBlank(userName.value)) {
                event.preventDefault();
                focusAndAlert(userName, '이름을 입력하세요.');
                return;
            }

            if (isBlank(phoneNum.value)) {
                event.preventDefault();
                focusAndAlert(phoneNum, '전화번호를 입력하세요.');
            }
        });
    }

    function bindPasswordForm() {
        var form = byId('passwordForm');
        if (!form) return;

        form.addEventListener('submit', function (event) {
            var currentPassword = byId('currentPassword');
            var newPassword = byId('newPassword');
            var newPasswordConfirm = byId('newPasswordConfirm');

            if (isBlank(currentPassword.value)) {
                event.preventDefault();
                focusAndAlert(currentPassword, '현재 비밀번호를 입력하세요.');
                return;
            }

            if (isBlank(newPassword.value)) {
                event.preventDefault();
                focusAndAlert(newPassword, '새 비밀번호를 입력하세요.');
                return;
            }

            if (newPassword.value.length < 4) {
                event.preventDefault();
                focusAndAlert(newPassword, '새 비밀번호는 4자 이상 입력하세요.');
                return;
            }

            if (newPassword.value !== newPasswordConfirm.value) {
                event.preventDefault();
                focusAndAlert(newPasswordConfirm, '새 비밀번호와 확인 값이 다릅니다.');
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

        if (form) {
            form.addEventListener('submit', function (event) {
                if (!confirmWithdraw()) {
                    event.preventDefault();
                }
            });
        }
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
    };

    window.confirmWithdraw = function () {
        var password = byId('withdrawPassword');

        if (!password || isBlank(password.value)) {
            focusAndAlert(password, '회원탈퇴를 진행하려면 비밀번호를 입력하세요.');
            return false;
        }

        return confirm('정말 탈퇴하시겠습니까?\n탈퇴 후에는 로그인이 제한됩니다.');
    };

    document.addEventListener('DOMContentLoaded', function () {
        bindPhoneFormat();
        bindJoinForm();
        bindLoginForm();
        bindUpdateForm();
        bindPasswordForm();
        bindWithdrawForm();
        bindSearchForm();
    });
}());
