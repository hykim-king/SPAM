<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>채팅</title>
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body {
        font-family: "Malgun Gothic", "Apple SD Gothic Neo", sans-serif;
        background: #eef1f5;
        color: #1f2733;
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .chat-wrap {
        width: 900px;
        height: 620px;
        display: flex;
        background: #fff;
        border-radius: 14px;
        overflow: hidden;
        box-shadow: 0 10px 40px rgba(0,0,0,.12);
    }

    /* 왼쪽: 채팅방 목록 */
    .room-panel {
        width: 280px;
        border-right: 1px solid #e5e9ef;
        display: flex;
        flex-direction: column;
    }
    .room-panel h2 {
        font-size: 16px;
        padding: 18px 20px;
        border-bottom: 1px solid #e5e9ef;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .refresh-btn {
        font-size: 12px;
        color: #4a6cf7;
        background: none;
        border: none;
        cursor: pointer;
    }
    .room-list { flex: 1; overflow-y: auto; }
    .room-item {
        padding: 14px 20px;
        border-bottom: 1px solid #f0f2f6;
        cursor: pointer;
        transition: background .15s;
    }
    .room-item:hover { background: #f5f7fb; }
    .room-item.active { background: #eaf0ff; border-left: 3px solid #4a6cf7; }
    .room-item .rname { font-weight: 600; font-size: 14px; }
    .room-item .rsub { font-size: 12px; color: #8a94a6; margin-top: 4px; }
    .room-empty { padding: 20px; font-size: 13px; color: #a0a8b8; text-align: center; }

    /* 오른쪽: 메시지 영역 */
    .msg-panel { flex: 1; display: flex; flex-direction: column; }
    .msg-header {
        padding: 18px 20px;
        border-bottom: 1px solid #e5e9ef;
        font-size: 15px;
        font-weight: 600;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .exit-btn {
        font-size: 12px;
        color: #e5484d;
        background: none;
        border: 1px solid #f3c6c7;
        border-radius: 6px;
        padding: 5px 10px;
        cursor: pointer;
    }
    .msg-list {
        flex: 1;
        overflow-y: auto;
        padding: 20px;
        background: #f7f9fc;
        display: flex;
        flex-direction: column;
        gap: 10px;
    }
    .msg-bubble {
        max-width: 65%;
        padding: 10px 14px;
        border-radius: 12px;
        font-size: 14px;
        line-height: 1.4;
        background: #fff;
        border: 1px solid #e5e9ef;
        align-self: flex-start;
        word-break: break-word;
    }
    .msg-bubble.mine {
        background: #4a6cf7;
        color: #fff;
        border-color: #4a6cf7;
        align-self: flex-end;
    }
    .msg-bubble .who { font-size: 11px; opacity: .7; margin-bottom: 3px; }
    .msg-placeholder {
        margin: auto;
        color: #a0a8b8;
        font-size: 14px;
    }
</style>
</head>
<body>
<div class="chat-wrap">

    <!-- 채팅방 목록 -->
    <div class="room-panel">
        <h2>
            내 채팅방
            <button class="refresh-btn" onclick="loadRooms()">새로고침</button>
        </h2>
        <div class="room-list" id="roomList">
            <div class="room-empty">불러오는 중...</div>
        </div>
    </div>

    <!-- 메시지 -->
    <div class="msg-panel">
        <div class="msg-header">
            <span id="currentRoomName">채팅방을 선택하세요</span>
            <button class="exit-btn" id="exitBtn" style="display:none" onclick="exitRoom()">나가기</button>
        </div>
        <div class="msg-list" id="msgList">
            <div class="msg-placeholder">왼쪽에서 채팅방을 선택하면 대화가 표시됩니다.</div>
        </div>
    </div>

</div>

<script>
    const ctx = '${pageContext.request.contextPath}';
    let currentRoomNo = null;
    let pollTimer = null;

    // 1. 채팅방 목록 조회 (GET /getRoomList.do)
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

    // 2. 방 선택 → 메시지 조회 (GET /getMessageList.do)
    function openRoom(roomNo, el) {
        currentRoomNo = roomNo;
        document.querySelectorAll('.room-item').forEach(r => r.classList.remove('active'));
        if (el) el.classList.add('active');
        document.getElementById('currentRoomName').textContent = '채팅방 #' + roomNo;
        document.getElementById('exitBtn').style.display = 'inline-block';

        loadMessages();
        // 폴링: 3초마다 메시지 갱신
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
                    // senderNo 등 실제 필드명은 VO에 맞게 조정 필요
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

    // 3. 방 나가기 (POST /exitRoom.do)
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

    // 처음 로딩 시 방 목록 가져오기
    loadRooms();
</script>
</body>
</html>
