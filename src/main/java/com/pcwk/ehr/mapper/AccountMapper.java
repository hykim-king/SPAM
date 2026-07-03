package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.account.domain.AccountVO;
import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.WorkDiv;

@Mapper
public interface AccountMapper extends WorkDiv<AccountVO> {

	
	List<AccountVO> doRetrieveANSI (DTO param);
	
	int saveAll(int saveCount);
	List<AccountVO> getAllMember();
	int totalCnt();
	int deleteAll();
}
