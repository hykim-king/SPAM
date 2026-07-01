package com.pcwk.ehr.time.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pcwk.ehr.time.domain.TimeVO;

@Repository
public class TimeDaoImpl {

	final Logger log = LogManager.getLogger(getClass());

	final String NAMESPACE = "com.pcwk.ehr.time";
	final String DOT = ".";

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	public TimeDaoImpl() {
		super();
		log.debug("---------------------------");
		log.debug("TimeDaoImpl()*");
		log.debug("---------------------------");		
		
	}
	
	
	public TimeVO doSelectOne(TimeVO parameter) {
		TimeVO outVO = null;
		//com.pcwk.ehr.time.doSelectOne
		String statement = NAMESPACE + DOT +"doSelectOne";
		log.debug("1.parameter: "+parameter);
		log.debug("2.statement: "+statement);
		
		
		outVO = sqlSessionTemplate.selectOne(statement, parameter);
		log.debug("3.outVO: "+outVO);
		return outVO;
	}
}
