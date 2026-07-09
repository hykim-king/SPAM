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
                </div>
            </section>
        </main>

        <jsp:include page="../common/footer.jsp" />
        <jsp:include page="../common/floatingBar.jsp" />
        <jsp:include page="../common/mobileBottomNav.jsp" />
    </div>

    <script>
        const ctx = '${CP}';
        let currentRoomNo = null;
        let pollTimer = null;

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
                        div.innerHTML =
                            '<div class="rname">채팅방 #' + room.chatRoomNo + '</div>' +
                            '<div class="rsub">상품번호: ' + (room.productNo ?? '-') + '</div>';
                        div.onclick = () => openRoom(room.chatRoomNo, div);
                        box.appendChild(div);
                    });
                })
                .catch(err => {
                    document.getElementById('roomList').innerHTML =
                        '<div class="room-empty">목록을 불러오지 못했습니다.<br>로그인 상태를 확인하세요.</div>';
                    console.error(err);
                });
        }

        function openRoom(roomNo, el) {
            currentRoomNo = roomNo;
            document.querySelectorAll('.room-item').forEach(r => r.classList.remove('active'));
            if (el) el.classList.add('active');
            document.getElementById('currentRoomName').textContent = '채팅방 #' + roomNo;
            document.getElementById('exitBtn').style.display = 'inline-block';

            loadMessages();
            if (pollTimer) clearInterval(pollTimer);
            pollTimer = setInterval(loadMessages, 3000);
        }

        function loadMessages() {
            if (currentRoomNo == null) return;
            fetch(ctx + '/chat/getMessageList.do?chatRoomNo=' + currentRoomNo)
                .then(res => res.json())
                .then(msgs => {
                    const box = document.getElementById('msgList');
                    if (!msgs || msgs.length === 0) {
                        box.innerHTML = '<div class="msg-placeholder">아직 메시지가 없습니다.</div>';
                        return;
                    }
                    box.innerHTML = '';
                    msgs.forEach(m => {
                        const div = document.createElement('div');
                        div.className = 'msg-bubble';
                        div.innerHTML =
                            '<div class="who">사용자 ' + (m.senderNo ?? '') + '</div>' +
                            (m.message ?? m.content ?? '');
                        box.appendChild(div);
                    });
                    box.scrollTop = box.scrollHeight;
                })
                .catch(err => console.error(err));
        }

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
                alert(result === 'Success' ? '나갔습니다.' : '실패: ' + result);
                if (pollTimer) clearInterval(pollTimer);
                currentRoomNo = null;
                document.getElementById('currentRoomName').textContent = '채팅방을 선택하세요';
                document.getElementById('exitBtn').style.display = 'none';
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
