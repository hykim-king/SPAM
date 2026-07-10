(function () {
    'use strict';

    function qs(selector, root) {
        return (root || document).querySelector(selector);
    }

    function qsa(selector, root) {
        return Array.prototype.slice.call((root || document).querySelectorAll(selector));
    }

    function bindFilterToggle() {
        var panel = qs('.product-filter-panel');
        var toggle = qs('.js-product-filter-toggle');
        if (!panel || !toggle) return;

        toggle.addEventListener('click', function () {
            var opened = panel.classList.toggle('is-open');
            toggle.setAttribute('aria-expanded', opened ? 'true' : 'false');
        });
    }

    function bindSortAutoSubmit() {
        var select = qs('.js-product-sort');
        if (!select || !select.form) return;

        select.addEventListener('change', function () {
            select.form.submit();
        });
    }

    function bindGallery() {
        var mainImage = qs('.js-product-main-image');
        var thumbs = qsa('.js-product-gallery-thumb');
        if (!mainImage || thumbs.length === 0) return;

        thumbs.forEach(function (button) {
            button.addEventListener('click', function () {
                var src = button.getAttribute('data-image-src');
                var alt = button.getAttribute('data-image-alt') || '상품 이미지';
                if (!src) return;

                mainImage.src = src;
                mainImage.alt = alt;
                thumbs.forEach(function (item) {
                    item.classList.toggle('is-active', item === button);
                    item.setAttribute('aria-pressed', item === button ? 'true' : 'false');
                });
            });
        });
    }

    function bindImagePreview() {
        var input = qs('.js-product-file-input');
        var preview = qs('.js-product-upload-preview');
        if (!input || !preview) return;

        input.addEventListener('change', function () {
            preview.innerHTML = '';
            var files = Array.prototype.slice.call(input.files || []).slice(0, 5);

            files.forEach(function (file, index) {
                if (!file.type || file.type.indexOf('image/') !== 0) return;

                var reader = new FileReader();
                reader.onload = function (event) {
                    var figure = document.createElement('figure');
                    var image = document.createElement('img');
                    var caption = document.createElement('figcaption');

                    image.src = event.target.result;
                    image.alt = index === 0 ? '대표 이미지 미리보기' : '추가 이미지 미리보기';
                    caption.textContent = index === 0 ? '대표 · ' + file.name : file.name;

                    figure.appendChild(image);
                    figure.appendChild(caption);
                    preview.appendChild(figure);
                };
                reader.readAsDataURL(file);
            });
        });
    }

    function option(label, value) {
        var item = document.createElement('option');
        item.textContent = label;
        item.value = value;
        return item;
    }

    function resetSelect(select, placeholder) {
        if (!select) return;
        select.innerHTML = '';
        select.appendChild(option(placeholder, ''));
        select.disabled = true;
    }

    function fillSelect(select, list, placeholder) {
        resetSelect(select, placeholder);
        (list || []).forEach(function (item) {
            select.appendChild(option(item.categoryName, item.categoryNo));
        });
        select.disabled = !(list && list.length);
    }

    function bindCategorySelects() {
        var large = qs('.js-category-large');
        var middle = qs('.js-category-middle');
        var small = qs('.js-category-small');
        var hidden = qs('.js-category-value');
        var endpoint = large ? large.getAttribute('data-child-url') : '';
        if (!large || !middle || !small || !hidden || !endpoint) return;

        function setDeepestValue() {
            hidden.value = small.value || middle.value || large.value || hidden.getAttribute('data-original-value') || '';
        }

        function loadChildren(parentNo, target, placeholder) {
            if (!parentNo) {
                resetSelect(target, placeholder);
                setDeepestValue();
                return Promise.resolve([]);
            }

            target.disabled = true;
            return fetch(endpoint + '?parentCategoryNo=' + encodeURIComponent(parentNo), {
                headers: { 'Accept': 'application/json' }
            })
                .then(function (response) {
                    if (!response.ok) throw new Error('카테고리 조회 실패');
                    return response.json();
                })
                .then(function (list) {
                    fillSelect(target, list, placeholder);
                    setDeepestValue();
                    return list;
                })
                .catch(function () {
                    resetSelect(target, '불러오기 실패');
                    setDeepestValue();
                    return [];
                });
        }

        large.addEventListener('change', function () {
            hidden.removeAttribute('data-original-value');
            resetSelect(middle, '중분류 선택');
            resetSelect(small, '소분류 선택');
            setDeepestValue();
            loadChildren(large.value, middle, '중분류 선택');
        });

        middle.addEventListener('change', function () {
            hidden.removeAttribute('data-original-value');
            resetSelect(small, '소분류 선택');
            setDeepestValue();
            loadChildren(middle.value, small, '소분류 선택');
        });

        small.addEventListener('change', function () {
            hidden.removeAttribute('data-original-value');
            setDeepestValue();
        });

        setDeepestValue();
    }

    function bindProductFormValidation() {
        qsa('.js-product-form').forEach(function (form) {
            form.addEventListener('submit', function (event) {
                var category = qs('.js-category-value', form);
                var price = qs('input[name="price"]', form);

                if (category && !category.value) {
                    event.preventDefault();
                    alert('카테고리를 선택해주세요.');
                    var large = qs('.js-category-large', form);
                    if (large) large.focus();
                    return;
                }

                if (price && Number(price.value) < 0) {
                    event.preventDefault();
                    alert('가격은 0원 이상으로 입력해주세요.');
                    price.focus();
                }
            });
        });
    }

    function postForm(url, data) {
        var body = new FormData();
        Object.keys(data).forEach(function (key) {
            body.append(key, data[key]);
        });

        return fetch(url, {
            method: 'POST',
            body: body
        }).then(function (response) {
            if (!response.ok) throw new Error('요청 실패');
            return response.text();
        });
    }

    function bindDeleteButtons() {
        qsa('.js-product-delete').forEach(function (button) {
            button.addEventListener('click', function () {
                var productNo = button.getAttribute('data-product-no');
                var sellerNo = button.getAttribute('data-seller-no');
                var deleteUrl = button.getAttribute('data-delete-url');
                var redirectUrl = button.getAttribute('data-redirect-url');
                if (!productNo || !deleteUrl) return;

                if (!window.confirm('상품을 삭제하시겠습니까? 삭제 후에는 복구할 수 없습니다.')) return;

                postForm(deleteUrl, {
                    productNo: productNo,
                    sallerNo: sellerNo || ''
                })
                    .then(function (result) {
                        if (String(result).trim() !== '1') throw new Error('삭제 실패');
                        alert('상품이 삭제되었습니다.');
                        window.location.href = redirectUrl;
                    })
                    .catch(function () {
                        alert('상품 삭제 중 오류가 발생했습니다.');
                    });
            });
        });
    }

    function bindStatusButtons() {
        qsa('.js-product-status').forEach(function (button) {
            button.addEventListener('click', function () {
                var statusText = button.getAttribute('data-status-text') || '선택한 상태';
                if (!window.confirm('거래 상태를 ' + statusText + '(으)로 변경하시겠습니까?')) return;

                postForm(button.getAttribute('data-status-url'), {
                    productNo: button.getAttribute('data-product-no'),
                    sallerNo: button.getAttribute('data-seller-no'),
                    status: button.getAttribute('data-status')
                })
                    .then(function (result) {
                        if (String(result).trim() !== '1') throw new Error('상태 변경 실패');
                        window.location.reload();
                    })
                    .catch(function () {
                        alert('거래 상태 변경 중 오류가 발생했습니다.');
                    });
            });
        });
    }

    function bindChatButton() {
        var button = qs('.js-product-chat');
        if (!button) return;

        button.addEventListener('click', function () {
            var login = button.getAttribute('data-login');
            if (login !== 'true') {
                window.location.href = button.getAttribute('data-login-url');
                return;
            }

            postForm(button.getAttribute('data-chat-url'), {
                productNo: button.getAttribute('data-product-no'),
                sellerNo: button.getAttribute('data-seller-no')
            })
                .then(function (text) {
                    var room = JSON.parse(text);
                    var target = button.getAttribute('data-chat-view-url');
                    if (room && room.chatRoomNo) {
                        target += '?chatRoomNo=' + encodeURIComponent(room.chatRoomNo);
                    }
                    window.location.href = target;
                })
                .catch(function () {
                    alert('채팅방을 여는 중 오류가 발생했습니다.');
                });
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        bindFilterToggle();
        bindSortAutoSubmit();
        bindGallery();
        bindImagePreview();
        bindCategorySelects();
        bindProductFormValidation();
        bindDeleteButtons();
        bindStatusButtons();
        bindChatButton();
    });
}());
