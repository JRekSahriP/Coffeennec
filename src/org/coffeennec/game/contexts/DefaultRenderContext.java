package org.coffeennec.game.contexts;

import org.coffeennec.graphics.buffers.CoffeeBuffer;
import org.coffeennec.graphics.buffers.CoffeeRenderer;

public abstract class DefaultRenderContext implements RenderContext {
	private CoffeeBuffer buffer;
	private CoffeeRenderer renderer;

	protected DefaultRenderContext(CoffeeBuffer buffer) {
		this.setBuffer(buffer);
	}

	@Override
	public CoffeeBuffer getBuffer() {
		return this.buffer;
	}

	@Override
	public void setBuffer(CoffeeBuffer buffer) {
		this.buffer = buffer;
		this.renderer = new CoffeeRenderer(buffer);
	}

	@Override
	public CoffeeRenderer getRenderer() {
		return this.renderer;
	}

}
