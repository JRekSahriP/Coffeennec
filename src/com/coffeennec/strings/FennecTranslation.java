package com.coffeennec.strings;

import java.util.HashMap;
import java.util.Map;

public class FennecTranslation {
	
	// (language, key, text)
	private static Map<String, Map<String, String>> translations = new HashMap<>();
	private static String currentLang = "en";
	
	public static void setLanguage(String language) {
		currentLang = language;
	}
	
	public static void set(String key, String text) {
		set(currentLang, key, text);
	}
	public static void set(String language, String key, String text) {
		translations.putIfAbsent(language, new HashMap<>());
		translations.get(language).put(key, text);
	}
	
	public static String get(String key) {
		return get(currentLang, key);
	}
	public static String get(String language, String key) {
		return translations.get(language).get(key);
	}
	
	public static void clear() {
		translations.clear();
	}
}