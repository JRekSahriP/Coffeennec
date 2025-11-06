package com.coffeennec.game.time;

public class Cooldown {
	private long duration;
	private long timer;
	private long pausedTimer;
	
	private boolean isPaused;
	
	public Cooldown(long duration) {
		this.duration = duration;
		this.timer = 0l;
		this.isPaused = false;
		this.pausedTimer = 0l;
	}
	
	
	public boolean isInCooldown() {
		if (isPaused) {
			return duration - pausedTimer <= 0;
		}
		return (System.currentTimeMillis() - timer) < duration;
	}
	
	public void use() {
		timer = System.currentTimeMillis();
	}
	
	public void reset() {
		timer = 0l;
	}
	
	public void addTime(long amount) {
		timer += amount;
	}
	
	public void pauseTimer(boolean paused) {
		isPaused = paused;
		
		if (isPaused) {
			pausedTimer = System.currentTimeMillis();
		} else {
			timer += (System.currentTimeMillis() - pausedTimer);
		}
	}
	
	public long getRemainingTime() {
		if (isPaused) {
			return duration - (pausedTimer - timer);
		}
		
		long remaining = duration - (System.currentTimeMillis() - timer);
		return remaining > 0 ? remaining : 0l;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public long getTimer() {
		return timer;
	}
	
}
