package com.pcwk.ehr.user.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.mapper.UserMapper;
import com.pcwk.ehr.user.domain.Grade;
import com.pcwk.ehr.user.domain.UserVO;

@Service
public class UserServiceImpl implements UserService {
	Logger log = LogManager.getLogger(getClass());

	// 매직넘버 해결을 위한 상수
	// BASIC -> SILVER로 등업 하기위한 로그인 최소 회수
	public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;

	// SILVER -> GOLD로 등업 하기위한 추천 최소 회수
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	// ------------------------------------------------------
	@Autowired
	private UserMapper userMapper;

	@Autowired
	@Qualifier("dummyMailService")
	private MailSender mailSender;
	// ------------------------------------------------------

	public UserServiceImpl() {
		super();
		log.debug("---------------------------");
		log.debug("UserServiceImpl()*");
		log.debug("---------------------------");
	}

	/**
	 * 등업 mail전송
	 * 
	 * @param member
	 */
	public void sendUpgradeEmail(UserVO member) {

		try {
			// 보내는 사람
			// 받는 사람
			// 제목
			// 내용

			SimpleMailMessage message = new SimpleMailMessage();

			// 보내는 사람
			message.setFrom("jamesol@naver.com");

			// 받는 사람
			message.setTo(member.getEmail());

			// 제목
			message.setSubject("EHR 사이트 등업 안내");

			// 내용
			// 내용:{이름}님의 등급이 {등급}로 등업되었습니다.
			String content = member.getName() + "님의 등급이 " + member.getGrade() + "로 등업되었습니다.";
			message.setText(content);

			// 전송
			mailSender.send(message);

		} catch (Exception e) {
			log.debug("┌──────────────────────────┐");
			log.debug("│sendUpgradeEMail Exception│" + e.getMessage());
			log.debug("└──────────────────────────┘");
		}
	}

	@Override
	public int doSave(UserVO param) {

		if (null == param.getGrade()) {
			log.debug("param grade : " + param.getGrade());
			param.setGrade(Grade.BASIC);
		}

		return userMapper.doSave(param);
	}

	/**
	 * 등업조건
	 * 
	 * @param member
	 * @return
	 */
	public boolean canUpgradeLevel(UserVO member) {
		// 현재 Grade 파악
		// 등업조건

		Grade currentGrade = member.getGrade();

		switch (currentGrade) {
		case BASIC:
			return member.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER;
		case SILVER:
			return member.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("등급을 알수 없습니다.(" + currentGrade + ")");
		}

	}

	/**
	 * 다음 단계의 Grade,등업
	 * 
	 * @param member
	 */
	public void upgradeLevel(UserVO member) {
		// 다음 단계의 Grade
		member.upgrageLevel();

		// 등업
		int flag = userMapper.doUpdate(member);
		if (1 == flag) {
			sendUpgradeEmail(member);// 메일전송
		}
	}

	@Override
	public void upgradeLevels() throws SQLException {

		// 1.
		List<UserVO> allUser = userMapper.getAllMember();

		// 2.
		for (UserVO user : allUser) {

			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}

	}

	@Override
	public List<UserVO> doRetrieve(DTO param) {
		return userMapper.doRetrieve(param);
	}

	@Override
	public int doUpdate(UserVO param) {
		return userMapper.doUpdate(param);
	}

	@Override
	public int doDelete(UserVO param) {
		return userMapper.doDelete(param);
	}

	@Override
	public UserVO doSelectOne(UserVO param) throws EmptyResultDataAccessException {
		return userMapper.doSelectOne(param);
	}

	/**
	대장, UserServiceImpl.java까지 분석이 끝났습니다! 
	이 코드는 단순히 데이터를 저장하는 수준을 넘어, 실무에서 비즈니스 로직을 어떻게 객체 지향적으로 처리하는지 보여주는 아주 훌륭한 교과서 같은 코드입니다.
	주요 포인트를 꼼꼼하게 정리해 드릴게요.

	1. 의존성 주입 (Dependency Injection)
	@Autowired: UserMapper와 MailSender를 직접 생성하지 않고 스프링에게 빌려 쓰고 있습니다. 
				이렇게 하면 나중에 DB 접근 방식이나 메일 서버 설정이 바뀌어도 UserServiceImpl 코드를 수정할 필요가 없죠.
	@Qualifier("dummyMailService"): 메일 전송 객체가 여러 개일 수 있는 경우, 
									정확히 어떤 메일 서비스(DummyMailService)를 쓸지 이름을 지정하여 콕 집어 불러오고 있습니다.

	2. 비즈니스 로직의 분리 (객체 지향 설계)
	이 코드가 정말 잘 짜였다고 생각하는 부분은 canUpgradeLevel과 upgradeLevel 메서드입니다.
	- 상태 체크 (canUpgradeLevel): 회원의 등급을 올릴 조건(로그인 횟수 50회, 추천수 30회 등)을 비즈니스 상수(MIN_LOGIN_COUNT_FOR_SILVER 등)로 관리하고 있습니다.
	- 로직의 위임: 실제 등급을 올리는 작업(upgrageLevel)은 UserVO라는 객체 내부의 메서드에 위임했습니다. 
			   로직의 책임이 데이터(VO)와 로직(Service) 사이에 아주 적절하게 분산되어 있습니다.

	3. 메일 발송 로직 (sendUpgradeEmail)
	try-catch 블록으로 감싸져 있네요! 
	메일 전송이 실패하더라도 등업 처리가 취소되지 않도록, 즉 부가 기능인 메일 때문에 핵심 기능이 망가지지 않도록 아주 꼼꼼하게 설계되었습니다.

	4. 등업 핵심 로직 (upgradeLevels)
	모든 회원을 가져와서 하나씩 루프를 돌며 등업 조건을 체크하고, 
	조건이 맞으면 즉시 업데이트와 메일 발송을 수행합니다.

	5. 앞으로 대장이 꼭 확인해야 할 점: 이 upgradeLevels() 메서드 위에는 나중에 @Transactional 어노테이션이 붙어야 합니다. 
							  그래야만 "등급은 올랐는데 메일 발송 중에 에러가 났다"와 같은 상황에서 등업 데이터까지 
							  전부 취소(Rollback)해서 데이터의 일관성을 완벽하게 지킬 수 있기 때문입니다.


	분석 완료!
	이제 대장의 프로젝트에서 데이터 모델(domain), 연결 고리(mapper), 핵심 업무(service)의 삼박자가 완성되었습니다. 
	남은 건 이 모든 것을 화면으로 이어주는 UserController.java뿐이네요.
	이 컨트롤러까지 분석하면 대장은 이제 이 프로젝트의 전체 흐름을 완벽하게 꿰뚫어 보게 됩니다.
	 */
}
