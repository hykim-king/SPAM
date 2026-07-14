<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="CP" value="${pageContext.request.contextPath}" scope="request" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>채팅 | SPAM</title>
    <link rel="stylesheet" href="${CP}/resources/css/index.css">
    <link rel="stylesheet" href="${CP}/resources/css/member.css">
    <link rel="stylesheet" href="${CP}/resources/css/chat.css">
    <script defer src="${CP}/resources/js/index.js"></script>
</head>
<body>
    <div class="page-shell" id="top">
        <jsp:include page="../common/header.jsp" />
        <jsp:include page="../common/nav.jsp" />

        <main class="member-page-shell">
            <header class="page-header">
                <div>
                    <h1 class="page-title">채팅</h1>
                </div>
                <nav class="header-actions">
                    <a class="btn outline" href="${CP}/main.do">메인</a>
                    <a class="btn" href="${CP}/product/list.do">상품 보기</a>
                </nav>
            </header>

            <section class="chat-wrap" aria-label="채팅 화면">
                <div class="room-panel">
                    <h2>
                        내 채팅방
                        <button class="refresh-btn" type="button" onclick="loadRooms()">새로고침</button>
                    </h2>
                    <div class="room-list" id="roomList">
                        <div class="room-empty">불러오는 중...</div>
                    </div>
                </div>

                <div class="msg-panel">
                    <div class="msg-header">
                        <span id="currentRoomName">채팅방을 선택하세요</span>
                        <button class="exit-btn" id="exitBtn" type="button" style="display:none" onclick="exitRoom()">나가기</button>
                    </div>
                    <div class="msg-list" id="msgList">
                        <div class="msg-placeholder">왼쪽에서 채팅방을 선택하면 대화가 표시됩니다.</div>
                    </div>

                    <!-- 판매자 나감 안내 (기본 숨김) -->
                    <div class="chat-notice" id="chatNotice" style="display:none">
                        판매자가 채팅을 종료했습니다. 더 이상 메시지를 보낼 수 없습니다.
                    </div>

                    <!-- 메시지 입력 영역 (방 선택 전엔 숨김) -->
                    <div class="msg-input" id="msgInput" style="display:none">
                        <input type="text" id="msgText" placeholder="메시지를 입력하세요"
                               autocomplete="off" onkeydown="if(event.key==='Enter'){sendMessage();}">
                        <button class="send-btn" type="button" onclick="sendMessage()">전송</button>
                    </div>
                </div>
            </section>
        </main>

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>

    <script>
        const ctx = '${CP}';

        // 로그인한 내 회원번호 (내 메시지 구분용). 세션에서 심어줌.
        const myUserNo = '${sessionScope.loginUser.userNum}';

        let currentRoomNo = null;   // 지금 열려있는 방 번호
        let socket = null;          // 웹소켓 연결

        /* ------------------ 채팅방 목록 ------------------ */
        function loadRooms() {
            fetch(ctx + '/chat/getRoomList.do')
                .then(res => res.json())
                .then(rooms => {
                    const box = document.getElementById('roomList');
                    if (!rooms || rooms.length === 0) {
                        box.innerHTML = '<div class="room-empty">참여 중인 채팅방이 없습니다.</div>';
                        return;
                    }
                    box.innerHTML = '';
                    rooms.forEach(room => {
                        const div = document.createElement('div');
                        div.className = 'room-item';
                        div.dataset.roomNo = room.chatRoomNo;
                        div.dataset.sellerNo = (room.sellerNo ?? '');
                        div.dataset.sellerExitYn = (room.sellerExitYn ?? 'N');
                        div.dataset.productTitle = (room.productTitle ?? '상품');
                        div.dataset.productNo = (room.productNo ?? '');
                        // 방 이름(상대 닉네임)도 저장해두기 — openRoom에서 씀
                        div.dataset.oppNick = (room.opponentNick ?? '상대방');
                        div.dataset.oppNo = (room.opponentNo ?? '');
                        div.innerHTML =
                            '<div class="rname">' + (room.opponentNick ?? '상대방') + '</div>' +
                            '<div class="rsub">' + (room.productTitle ?? '-') + ' · ' + (room.location ?? '') + '</div>' +
                            '<div class="rdate">' + (room.lastMsgDt ?? '') + '</div>';
                        div.onclick = () => openRoom(room.chatRoomNo, div);
                        box.appendChild(div);
                    });
                })
                .catch(err => {
                    document.getElementById('roomList').innerHTML =
                        '<div class="room-empty">목록을 불러오지 못했습니다.</div>';
                    console.error(err);
                });
        }

        /* ------------------ 방 열기 ------------------ */
        function openRoom(roomNo, el) {
            currentRoomNo = roomNo;

            document.querySelectorAll('.room-item').forEach(r => r.classList.remove('active'));
            if (el) el.classList.add('active');

            const oppNick = el ? (el.dataset.oppNick || '상대방') : '상대방';
            const productTitle = el ? (el.dataset.productTitle || '상품') : '상품';
            const productNo = el ? (el.dataset.productNo || '') : '';
            const oppNo = el ? (el.dataset.oppNo || '') : '';

            document.getElementById('currentRoomName').innerHTML =
                '<a class="room-link room-link-title" href="' + ctx + '/product/view.do?productNo=' + productNo + '">'
                    + productTitle + '</a>'
                + ' · '
                + '<a class="room-link room-link-seller" href="' + ctx + '/product/seller.do?userNum=' + oppNo + '">'
                    + oppNick + '</a>';
                    
            document.getElementById('exitBtn').style.display = 'inline-block';

            // 판매자가 나간 방인지 판단 (내가 구매자이고 + 판매자 EXIT = Y)
            const sellerNo = el ? el.dataset.sellerNo : '';
            const sellerExitYn = el ? el.dataset.sellerExitYn : 'N';
            const iAmBuyer = (String(sellerNo) !== String(myUserNo)); // 내가 판매자가 아니면 구매자
            const sellerLeft = (sellerExitYn === 'Y');

            if (iAmBuyer && sellerLeft) {
                // 판매자 나감 -> 안내 표시, 입력 잠금
                document.getElementById('chatNotice').style.display = 'block';
                document.getElementById('msgInput').style.display = 'none';
            } else {
                // 정상 -> 입력 가능
                document.getElementById('chatNotice').style.display = 'none';
                document.getElementById('msgInput').style.display = 'flex';
            }

            // 1) 기존 메시지 먼저 HTTP로 한 번 불러오기
            loadMessages();

            // 2) 웹소켓 연결 (실시간 수신용)
            connectSocket(roomNo);
        }

        /* ------------------ 기존 메시지 로드 (HTTP, 최초 1회) ------------------ */
        function loadMessages() {
            if (currentRoomNo == null) return;
            fetch(ctx + '/chat/getMessageList.do?chatRoomNo=' + currentRoomNo)
                .then(res => res.json())
                .then(msgs => {
                    const box = document.getElementById('msgList');
                    box.innerHTML = '';
                    if (!msgs || msgs.length === 0) {
                        box.innerHTML = '<div class="msg-placeholder">아직 메시지가 없습니다.</div>';
                        return;
                    }
                    msgs.forEach(appendMessage);
                    box.scrollTop = box.scrollHeight;
                })
                .catch(err => console.error(err));
        }

        /* ------------------ 웹소켓 연결 ------------------ */
		let heartbeatTimer = null;   // 하트비트 타이머
		let reconnectTimer = null;   // 재연결 타이머
		let manualClose = false;     // 내가 일부러 닫은 건지 표시(나가기/방전환)
		
		function connectSocket(roomNo) {
		    // 기존 연결/타이머 정리
		    manualClose = true;                      // 아래 close는 의도된 종료
		    if (socket) { socket.close(); socket = null; }
		    if (heartbeatTimer) { clearInterval(heartbeatTimer); heartbeatTimer = null; }
		    if (reconnectTimer) { clearTimeout(reconnectTimer); reconnectTimer = null; }
		
		    const proto = (location.protocol === 'https:') ? 'wss://' : 'ws://';
		    const url = proto + location.host + ctx + '/ws/chat.do?roomNo=' + roomNo;
		
		    socket = new WebSocket(url);
		    manualClose = false;                     // 새 연결 시작 — 이제 끊기면 비정상
		
		    socket.onopen = function() {
		        console.log('웹소켓 연결됨');
		        // 하트비트 시작: 25초마다 ping 신호
		        heartbeatTimer = setInterval(function() {
		            if (socket && socket.readyState === WebSocket.OPEN) {
		                socket.send(JSON.stringify({ type: 'ping' }));
		            }
		        }, 25000);
		    };

		    socket.onmessage = function(event) {
		        const msg = JSON.parse(event.data);
		        if (msg.type === 'pong') return;     // 서버 응답 신호는 화면에 안 그림
		        const box = document.getElementById('msgList');
		        const ph = box.querySelector('.msg-placeholder');
		        if (ph) ph.remove();
		        appendMessage(msg);
		        box.scrollTop = box.scrollHeight;
		    };
		
		    socket.onclose = function() {
		        console.log('웹소켓 연결 종료');
		        if (heartbeatTimer) { clearInterval(heartbeatTimer); heartbeatTimer = null; }
		        // 내가 일부러 닫은 게 아니고, 아직 이 방을 보고 있으면 재연결 시도
		        if (!manualClose && currentRoomNo === roomNo) {
		            reconnectTimer = setTimeout(function() {
		                console.log('재연결 시도...');
		                connectSocket(roomNo);
		            }, 2000);   // 2초 뒤 재연결
		        }
		    };
		
		    socket.onerror = function(err) {
		        console.error('웹소켓 오류', err);
		        // 오류 나면 소켓이 곧 close됨 → onclose에서 재연결 처리
		    };
		}

        /* ------------------ 메시지 전송 ------------------ */
        function sendMessage() {
            const input = document.getElementById('msgText');
            const text = input.value.trim();
            if (text === '') return;

            if (!socket || socket.readyState !== WebSocket.OPEN) {
                alert('연결이 준비되지 않았습니다. 잠시 후 다시 시도하세요.');
                return;
            }

            const payload = {
                chatRoomNo: currentRoomNo,
                chatContent: text
            };
            socket.send(JSON.stringify(payload));

            input.value = '';
            input.focus();
        }

        /* ------------------ 메시지 한 줄 그리기 ------------------ */
        function appendMessage(m) {
            const box = document.getElementById('msgList');
            const div = document.createElement('div');

            const mine = (String(m.senderNo) === String(myUserNo));
            div.className = mine ? 'msg-bubble mine' : 'msg-bubble';

            div.innerHTML =
            	'<div class="who">' + (m.senderNick ?? ('사용자 ' + m.senderNo)) + '</div>' +
                '<div class="text"></div>' +
                '<div class="time">' + (m.sendDt ?? '') + '</div>';
            div.querySelector('.text').textContent = (m.chatContent ?? '');

            box.appendChild(div);
        }

        /* ------------------ 방 나가기 ------------------ */
        function exitRoom() {
            if (currentRoomNo == null) return;
            if (!confirm('채팅방에서 나가시겠습니까?')) return;

            fetch(ctx + '/chat/exitRoom.do', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'chatRoomNo=' + currentRoomNo
            })
            .then(res => res.text())
            .then(result => {
                if (result === 'NEED_LOGIN') {
                    if (window.SPAMModal && typeof window.SPAMModal.login === 'function') {
                        window.SPAMModal.login({ loginUrl: ctx + '/user/login.do' });
                    } else {
                        location.href = ctx + '/user/login.do';
                    }
                    return;
                }
                alert(result === 'Success' ? '나갔습니다.' : '실패: ' + result);

                if (socket) { manualClose = true; socket.close(); socket = null; }
                currentRoomNo = null;
                document.getElementById('currentRoomName').textContent = '채팅방을 선택하세요';
                document.getElementById('exitBtn').style.display = 'none';
                document.getElementById('msgInput').style.display = 'none';
                document.getElementById('chatNotice').style.display = 'none';
                document.getElementById('msgList').innerHTML =
                    '<div class="msg-placeholder">왼쪽에서 채팅방을 선택하세요.</div>';
                loadRooms();
            })
            .catch(err => console.error(err));
        }

        loadRooms();
    </script>
</body>
</html>
