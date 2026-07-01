package com.pcwk.ehr.cmn;

public class MessageVO {

	private String id;
	private String message;
	public MessageVO() {
		super();
	}
	public MessageVO(String id, String message) {
		super();
		this.id = id;
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "MessageVO [id=" + id + ", message=" + message + "]";
	}
	/**
	 * 	MessageVO 클래스는 우리 프로젝트에서 사용자와 시스템 간의 소통 창구 역할을 하는 클래스입니다.
		이 파일은 복잡한 데이터가 아니라, '처리 결과'를 딱 2가지로 명확하게 전달하기 위해 만들어졌습니다.

		1. 왜 사용하는 걸까요?
			보통 회원 가입, 삭제, 수정 같은 작업을 수행하고 나면 화면에서 사용자에게 결과를 알려줘야 합니다.
			이때 성공인지 실패인지, 그리고 어떤 메시지를 보여줄지를 한 번에 담아서 보내기 위해 이 클래스를 사용합니다.

			id: 보통 이 값에는 결과 코드(예: "1"은 성공, "0"은 실패 등)를 담습니다.
			message: 사용자에게 실제로 보여줄 알림 문구(예: "회원가입이 완료되었습니다.", "비밀번호가 일치하지 않습니다.")를 담습니다.

		2. 이 클래스의 장점 (실무적인 활용)
			이 방식이 좋은 이유는 컨트롤러에서 화면으로 결과를 보낼 때 일관성을 유지할 수 있기 때문입니다.
			- 비동기 통신(AJAX)의 핵심: 대장이 나중에 웹 화면에서 페이지 전환 없이 팝업창을 띄우거나 알림을 줄 때, 서버는 이 MessageVO를 JSON 형태로 변환해서 응답해 줍니다.
			- 코드의 표준화: 모든 서비스의 결과값을 항상 이 클래스에 담아서 보내기로 규칙을 정해두면, 화면(JSP/JavaScript) 쪽에서는 항상 id와 message만 확인하면 되니 에러 처리가 훨씬 편해집니다.

		3. 간단한 예시 (이렇게 쓰일 거예요)
			서비스 로직에서 회원가입을 처리한 뒤 결과값을 만들 때 아마 이렇게 작성할 겁니다:
			Java
			// 예시: 성공했을 경우
				return new MessageVO("1", "회원 가입 성공!");

			// 예시: 실패했을 경우
				return new MessageVO("0", "중복된 아이디가 존재합니다.");
		
			DTO가 '데이터 그릇'이라면, MessageVO는 '결과 안내장'이라고 생각하면 되겠네요!
	 */
}
