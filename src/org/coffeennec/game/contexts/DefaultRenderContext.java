package org.coffeennec.game.contexts;

import org.coffeennec.graphics.buffers.CoffeeBuffer;

public abstract class DefaultRenderContext implements RenderContext {
	private CoffeeBuffer buffer;

	protected DefaultRenderContext(CoffeeBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public CoffeeBuffer getBuffer() {
		return buffer;
	}
	
	@Override
	public void setBuffer(CoffeeBuffer buffer) {
		this.buffer = buffer;
	}

}
