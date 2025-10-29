package com.coffeennec;

public final class Coffeennec {
	public static void init(Class<?> mainClass, String...args) {
		//TODO Use args in the future here
		
		try {
			mainClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
