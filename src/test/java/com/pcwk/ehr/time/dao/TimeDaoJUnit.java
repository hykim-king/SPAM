package com.pcwk.ehr.time.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//스프링 테스트(spring-test) 컨텍스트 프레임워크의 JUnit확장 기능

import com.pcwk.ehr.time.domain.TimeVO;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class TimeDaoJUnit {
	
	final Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	TimeDaoImpl dao;
	
	TimeVO  time01;
	
	
	@BeforeEach
	void setUp() throws Exception {
		log.debug("*****************************");
		log.debug("*@BeforeEach*");
		log.debug("*****************************");	
		
		time01 =new TimeVO("pcwk", "4321a", "사용하지 않음!");
	}
	
	@Test
	void doSelectOne() {
		log.debug("---------------------------");
		log.debug("*doSelectOne()*");
		log.debug("---------------------------");	
		
		TimeVO outVO = dao.doSelectOne(time01);
		assertNotNull(outVO);
		
		log.debug("outVO:{}",outVO);
	}
	
	
	@Disabled
	@Test
	void beans() {
		log.debug("---------------------------");
		log.debug("*beans()*");
		log.debug("---------------------------");
		
		assertNotNull(dao);
		log.debug("dao: " + dao);		
	}

}
