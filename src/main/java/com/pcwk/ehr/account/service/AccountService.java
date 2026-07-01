package com.pcwk.ehr.account.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.user.domain.UserVO;

public interface AccountService {

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
	int doUpdate(AccountVO param);
	
	/**
	 * 단건 삭제
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	int doDelete(AccountVO param);
	
	/**
	 * 단건조회
	 * 
	 * @param param
	 * @return T(성공)/null(실패)
	 */
	UserVO doSelectOne(AccountVO param) throws EmptyResultDataAccessException;
	
	/**
	 * 회원가입
	 * @param param
	 * @return 1(성공)/0(실패)
	 */
	public int doSave(UserVO param);

}
