package com.pcwk.ehr.user.service;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.user.domain.UserVO;

public interface UserService {
	
	/**
	 * 목록조회
	 * @param param
	 * @return List<T>
	 */
	List<UserVO> doRetrieve(DTO param);
	
	/**
	 * 수정
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	int doUpdate(UserVO param);
	
	/**
	 * 단건 삭제
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	int doDelete(UserVO param);
	
	/**
	 * 단건조회
	 * 
	 * @param param
	 * @return T(성공)/null(실패)
	 */
	UserVO doSelectOne(UserVO param) throws EmptyResultDataAccessException;

	
	/**
	 * 회원 전체 등업
	 */
	public void upgradeLevels() throws SQLException;
	
	/**
	 * 회원가입
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	public int doSave(UserVO param);
	
}

