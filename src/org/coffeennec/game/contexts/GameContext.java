package org.coffeennec.game.contexts;

import org.coffeennec.input.FennecCursor;
import org.coffeennec.input.FennecKeys;

public interface GameContext {
	
	double getDeltaTime();
	void setDeltaTime(double deltaTime);
	
	default FennecKeys getKeys() {
		return FennecKeys.getInstance();
	}

	default FennecCursor getCursor() {
		return FennecCursor.getInstance();
	}
	
	default void updateState() {
		this.getKeys().update();
		this.getCursor().update();
	}
}
