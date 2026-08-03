package org.coffeennec.game.contexts;

public abstract class DefaultGameContext implements GameContext {
	private double deltaTime;
	
	@Override
	public double getDeltaTime() {
		return deltaTime;
	}
	
	@Override
	public void setDeltaTime(double deltaTime) {
		this.deltaTime = deltaTime;
	}
}
