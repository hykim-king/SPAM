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
import com.pcwk.ehr.user.domain.Grade;
import com.pcwk.ehr.user.domain.UserVO;
import com.pcwk.ehr.user.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	Logger log = LogManager.getLogger(getClass());

	// 매직넘버 해결으 위한 상수
	// BASIC -> SILVER로 등럽 하기 위한 로그인 최소 횟수
	public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;

	// SILVER -> GOLD로 등업 하기 위한 추천 최소 횟수
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	@Qualifier("dummyMailService")
	private MailSender mailSender;

	
	public UserServiceImpl() {
		super();
		log.debug("-----------------------------");
		log.debug("UserServiceImpl()");
		log.debug("-----------------------------");
	}
		

	
	/**
	 * 등업 mail전송
	 * @param member
	 */
	public void sendUpgradeEMail(UserVO member) {
				
		try {
			//보내는 사람
			//받는 사람
			//제목
			//내용
			
			SimpleMailMessage message = new SimpleMailMessage();
			
			//보내는 사람
			message.setFrom("tnals5035@naver.com");
			
			//받는 사람
			message.setTo(member.getEmail());
			
			//제목
			message.setSubject("EHR 사이트 등업 안내");
			
			//내용
			//내용:{이름}님의 등급이 {등급}로 등업되었습니다.
			String content = member.getName()+"님의 등급이 "+member.getGrade()+"로 등업되었습니다.";
			message.setText(content);
			mailSender.send(message);
			
		} catch (Exception e) {
			log.debug("┌──────────────────────────┐");
			log.debug("│sendUpgradeMail Exception │" + e.getMessage());
			log.debug("└──────────────────────────┘");
		}
	}

	@Override
	public int doSave(UserVO param) {

		if (null == param.getGrade()) {
			param.setGrade(Grade.BASIC);
		}

		return userMapper.doSave(param);
	}

	public boolean canUpgradeLevel(UserVO member) {
		// 현재 Grade 파악
		// 등업조건

		Grade currenGrade = member.getGrade();

		switch (currenGrade) {
		case BASIC:
			return member.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER;
		case SILVER:
			return member.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("등급을 알 수 없습니다.(" + currenGrade + ")");
		}
	}

	/**
	 * 다음 단계의 Grade, 등업
	 * 
	 * @param member
	 */
	public void upgradeLevel(UserVO member) {
		// 다음 단계의 Grade
		member.upgradeLevel();
//		if (Grade.BASIC == member.getGrade()) {
//			member.setGrade(Grade.SILVER);
//		} else if (Grade.SILVER == member.getGrade()) {
//			member.setGrade(Grade.GOLD);
//		}

		// 등업
		int flag = userMapper.doUpdate(member);
		if(1==flag) {
			sendUpgradeEMail(member);//메일전송
		}
	}

	@Override
	public void upgradeLevels() throws SQLException {
		// 1. 전체 회원 조회
		// 2. 등업 대상자 선정
		// 3. 등업
		
		// 1.
		List<UserVO> allUser = userMapper.getAllMember();
		// 2.
		for (UserVO user : allUser) {
			// ---------------------------
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

}
