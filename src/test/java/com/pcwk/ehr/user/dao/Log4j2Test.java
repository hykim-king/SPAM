package com.pcwk.ehr.user.dao;



import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Log4j2Test {

	Logger  log = LogManager.getLogger(Log4j2Test.class);
	
	@BeforeEach
	public void setUp() throws Exception {
		log.debug("*****************************");
		log.debug("*@BeforeEach*");
		log.debug("*****************************");		
	}

	@AfterEach
	public void tearDown() throws Exception {
		log.debug("*****************************");
		log.debug("*@AfterEach*");
		log.debug("*****************************");			
	}

	@Test
	public void test() {
		log.debug("---------------------");
		log.debug("@Test test()");
		log.debug("---------------------");	
		
		log.debug("debug 메시지");
		log.info("info 메시지");
		log.warn("warn 메시지");
		log.error("error 메시지");

	}

}
