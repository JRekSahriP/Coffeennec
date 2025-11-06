package com.coffeennec.game.time;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class FennecCooldowns {
	private static Map<String, Cooldown> cooldowns = new HashMap<>();
	
	public static void putCooldown(String key, long duration) {
		cooldowns.computeIfAbsent(key, k -> new Cooldown(duration));
	}
	
	public static void remove(String key) {
		cooldowns.remove(key);
	}
	
	public static void removeIf(Predicate<Cooldown> condition) {
		cooldowns.entrySet().removeIf(e -> condition.test(e.getValue()));
	}
	
	public static void removeAll() {
		cooldowns.clear();
	}
	
	public static Cooldown get(String key) {
		return cooldowns.get(key);
	}
	
	public static int size() {
		return cooldowns.size();
	}
}
