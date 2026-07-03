package com.pcwk.ehr.account.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.mapper.AccountMapper;

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
	public List<AccountVO> doRetrieve(DTO param) {
		return accountMapper.doRetrieve(param);
	}

	@Override
	public int doUpdate(AccountVO param) {
		return accountMapper.doUpdate(param);
	}

	@Override
	public int doDelete(AccountVO param) {
		return accountMapper.doDelete(param);
	}

	@Override
	public AccountVO doSelectOne(AccountVO param) throws EmptyResultDataAccessException {
		return accountMapper.doSelectOne(param);
	}

	@Override
	public int doSave(AccountVO param) {
		return accountMapper.doSave(param);
	}

}
