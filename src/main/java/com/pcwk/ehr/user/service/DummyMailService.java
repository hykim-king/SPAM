package com.pcwk.ehr.user.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailService implements MailSender {
	
	Logger log = LogManager.getLogger(DummyMailService.class);

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		log.debug("---------------------");
		log.debug("DummyMailService()   ");
		log.debug("개발에서는 메일 전송 않됨!  ");
		log.debug("---------------------");

	}

	@Override
	public void send(SimpleMailMessage... simpleMessages) throws MailException {
		log.debug("---------------------");
		log.debug("DummyMailService()   ");
		log.debug("개발에서는 메일 전송 않됨!  ");
		log.debug("---------------------");

	}

	/**
	이 DummyMailService.java는 실무 개발 환경을 고려한 아주 세심한 배려가 담긴 코드입니다. 
	이름 그대로 '가짜(Dummy) 메일 서비스'예요.
	왜 이런 파일을 따로 만들었는지, 그 의도와 역할을 분석해 드릴게요.

	1. 왜 이런 서비스를 만들었을까요?
	실제 서비스에서 메일 전송 기능은 SMTP 서버와 통신해야 합니다. 
	하지만 개발 단계에서 매번 메일을 진짜로 보내버리면 다음과 같은 문제가 생깁니다.
	- 비용 및 성능: 메일 서버 호출은 시간이 걸리고, 외부 네트워크를 사용해야 합니다.
	- 테스트 실수: 테스트 중인 개발자가 수백 통의 가짜 메일을 진짜 수신자들에게 보내는 대참사가 일어날 수 있습니다.
	- 환경 차이: 개발자 컴퓨터에는 메일 서버 설정이 없을 수도 있죠.

	2. 코드의 역할: '가로채기'
	이 클래스는 MailSender라는 스프링의 표준 메일 인터페이스를 구현(implements)했습니다.
	send() 메서드가 호출되면 진짜 메일 서버로 가는 대신, 로그(Log)에 "개발에서는 메일 전송 안 됨!"이라는 문구만 남기고 종료됩니다.
	즉, UserServiceImpl 입장에서는 MailSender 인터페이스를 호출한다는 사실만 중요할 뿐, 실제로 메일이 나가는지, 로그에 찍히는지 알 필요가 없습니다.
	이게 바로 객체 지향의 다형성을 활용한 완벽한 설계입니다.

	3. 실무에서의 활용 (설정의 마법)
	나중에 프로젝트가 완료되어 실제 서버(운영 환경)에 올릴 때는 코드를 한 줄도 수정할 필요가 없습니다.
	설정 파일(XML이나 자바 설정)에서 mailSender라는 빈(Bean)의 주입 대상을 DummyMailService에서 JavaMailSenderImpl(실제 메일 발송 클래스)로 교체하기만 하면 됩니다.
	이렇게 하면 코드 수정 없이도 '테스트 모드'에서 '운영 모드'로 손쉽게 전환할 수 있습니다.

	4. 대장의 프로젝트는 정말 정석적입니다. 지금까지 분석한 내용들을 보면
	철저한 추상화: 서비스는 인터페이스에 의존하고, 실제 구현은 상황에 맞춰 갈아끼울 수 있게 설계되었습니다.
	안전 제일주의: 실수를 방지하기 위한 더미 서비스까지 준비되어 있습니다.
	
	이제 공통 규칙, 데이터 모델, 핵심 비즈니스 로직, 그리고 부가 서비스까지 모두 정복했습니다.
	마지막으로 이 모든 것을 조율하는 UserController.java를 확인하면 대장은 이 프로젝트의 전체 흐름을 완벽하게 장악하게 됩니다.
	 */
	
}
