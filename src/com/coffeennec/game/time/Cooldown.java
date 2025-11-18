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
		if (this.isPaused) {
			return this.duration - this.pausedTimer <= 0;
		}
		return (System.currentTimeMillis() - this.timer) < this.duration;
	}
	
	public void use() {
		this.timer = System.currentTimeMillis();
	}
	
	public void reset() {
		this.timer = 0l;
	}
	
	public void addTime(long amount) {
		this.timer += amount;
	}
	
	public void pauseTimer(boolean paused) {
		this.isPaused = paused;
		
		if (this.isPaused) {
			this.pausedTimer = System.currentTimeMillis();
		} else {
			this.timer += (System.currentTimeMillis() - this.pausedTimer);
		}
	}
	
	public long getRemainingTime() {
		if (this.isPaused) {
			return this.duration - (this.pausedTimer - this.timer);
		}
		
		long remaining = this.duration - (System.currentTimeMillis() - this.timer);
		return remaining > 0 ? remaining : 0l;
	}
	
	public boolean isPaused() {
		return this.isPaused;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public long getDuration() {
		return this.duration;
	}
	
	public long getTimer() {
		return this.timer;
	}
	
}
