package org.coffeennec.game.contexts;

import org.coffeennec.graphics.buffers.CoffeeBuffer;
import org.coffeennec.graphics.buffers.CoffeeRenderer;

public interface RenderContext {
	CoffeeBuffer getBuffer();
	void setBuffer(CoffeeBuffer buffer);

	default CoffeeRenderer getRenderer() {
		return this.getBuffer().getRenderer();
	}
	
}
