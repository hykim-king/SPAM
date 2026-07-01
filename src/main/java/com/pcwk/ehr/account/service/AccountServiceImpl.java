package com.pcwk.ehr.account.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.account.mapper.AccountMapper;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.user.domain.UserVO;

@Service
public class AccountServiceImpl implements AccountService {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	private AccountMapper accountMapper;
	
		
	public AccountServiceImpl() {
		super();
		log.debug("-----------------------------");
		log.debug("AccountServiceImpl()");
		log.debug("-----------------------------");
	}

	@Override
	public List<UserVO> doRetrieve(DTO param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int doUpdate(AccountVO param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int doDelete(AccountVO param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserVO doSelectOne(AccountVO param) throws EmptyResultDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int doSave(UserVO param) {
		
		return 0;
	}

}
