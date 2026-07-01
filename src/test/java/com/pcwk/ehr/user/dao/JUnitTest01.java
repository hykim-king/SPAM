package com.pcwk.ehr.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JUnitTest01 {
	int x;
	int y;

	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("*****************************");
		System.out.println("*@BeforeEach*");
		System.out.println("*****************************");
		x = 15;
		y = 17;
	}

	@AfterEach
	public void tearDown() throws Exception {
		System.out.println("*****************************");
		System.out.println("*@AfterEach*");
		System.out.println("*****************************");
	}

	@Test
	public void substractTest() {
		System.out.println("---------------------");
		System.out.println("@Test substractTest()");
		System.out.println("---------------------");
		x = x + 1;
		int result = y - x;
		assertTrue(result == 1);
	}

	@Test
	public void addTest() {
		System.out.println("---------------------");
		System.out.println("@Test addTest()");
		System.out.println("---------------------");

		System.out.println("x: " + x);
		int result = x + y;
		// 기대 값, 실제결과
		assertEquals(32, result);
	}

}
