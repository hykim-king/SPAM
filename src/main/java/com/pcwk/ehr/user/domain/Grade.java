package com.pcwk.ehr.user.domain;

public enum Grade {
	
	GOLD(3,null), SILVER(2,GOLD), BASIC(1,SILVER);
	
	private final int value;
	private final Grade nextLevel; //다음등급
	
	private Grade(int value, Grade nextLevel) {
		this.value= value;
		this.nextLevel = nextLevel;
	}
	
	//BASIC  -> 1
	//SILVER -> 2
	//GOLD   -> 3
	public int getValue() {
		return value;
	}
		
	public Grade getNextLevel() {
		return nextLevel;
	}

	public static Grade valueOf(int value) {
		switch (value) {
		case 1:
			return BASIC;
		case 2:
			return SILVER;
		case 3:
			return GOLD;
		default:
			throw new AssertionError("Unknown Value: "+value);
		}
	}

}
