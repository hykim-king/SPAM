/**
 * 
 */
package com.pcwk.ehr.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.cmn.DTO;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.user.domain.UserVO;

/**
 * @author user
 *
 */
@Mapper // MyBatis 매퍼 인터페이스임을 선언
public interface UserMapper extends WorkDiv<UserVO> {
	
	List<UserVO> doRetrieveANSI (DTO param);
	
	int saveAll(int saveCount);
	List<UserVO> getAllMember();
	int totalCnt();
	int deleteAll();	
	
}

