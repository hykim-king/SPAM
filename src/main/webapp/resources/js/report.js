(function () {
    'use strict';

    function bindReportForm(form) {
        var reasonSelect = form.querySelector('.js-report-reason-select');
        var reasonText = form.querySelector('.js-report-reason-text');
        var count = form.querySelector('.js-report-char-count');
        var submit = form.querySelector('.js-report-submit');
        if (!reasonSelect || !reasonText || !count || !submit) return;

        function renderState() {
            var length = reasonText.value.length;
            count.textContent = String(length);
            count.parentElement.classList.toggle('is-warning', length >= 900);
            submit.disabled = !reasonSelect.value || reasonText.value.trim().length === 0 || length > 1000;
        }

        reasonSelect.addEventListener('change', function () {
            if (!reasonSelect.value) {
                reasonText.value = '';
            } else if (reasonSelect.value === '기타 (직접 입력)') {
                reasonText.value = '';
                reasonText.placeholder = '신고 사유를 구체적으로 작성해주세요.';
            } else {
                reasonText.value = '[' + reasonSelect.value + ']\n';
                reasonText.placeholder = '추가적인 상세 내용을 작성해주세요.';
            }
            reasonText.focus();
            renderState();
        });

        reasonText.addEventListener('input', renderState);

        form.addEventListener('submit', function (event) {
            renderState();
            if (submit.disabled) {
                event.preventDefault();
                return;
            }

            var message = form.getAttribute('data-confirm-message') || '신고서를 제출하시겠습니까?';
            if (!window.confirm(message)) event.preventDefault();
        });

        var cancel = form.querySelector('.js-report-cancel');
        if (cancel) {
            cancel.addEventListener('click', function () {
                window.history.back();
            });
        }

        renderState();
    }

    function bindReportTabs() {
        var tabs = document.querySelectorAll('[data-report-tab]');
        var panels = document.querySelectorAll('.report-tab-panel');
        if (tabs.length === 0 || panels.length === 0) return;

        tabs.forEach(function (tab) {
            tab.addEventListener('click', function () {
                tabs.forEach(function (item) {
                    var active = item === tab;
                    item.classList.toggle('is-active', active);
                    item.setAttribute('aria-selected', active ? 'true' : 'false');
                });
                panels.forEach(function (panel) {
                    panel.classList.toggle('is-active', panel.id === tab.getAttribute('data-report-tab'));
                });
            });
        });
    }

    function bindHistoryButtons() {
        document.querySelectorAll('.js-history-back').forEach(function (button) {
            button.addEventListener('click', function () {
                if (window.history.length > 1) {
                    window.history.back();
                    return;
                }
                window.location.href = button.getAttribute('data-fallback-url') || './myReportList.do';
            });
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        document.querySelectorAll('.js-report-form').forEach(bindReportForm);
        bindReportTabs();
        bindHistoryButtons();
    });
}());
