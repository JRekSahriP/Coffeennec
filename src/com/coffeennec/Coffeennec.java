package com.coffeennec;

import java.lang.reflect.InvocationTargetException;

public final class Coffeennec {
	public static void init(Class<?> mainClass, String...args) {
		//TODO Use args in the future here
		
		try {
			mainClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException |
				NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
