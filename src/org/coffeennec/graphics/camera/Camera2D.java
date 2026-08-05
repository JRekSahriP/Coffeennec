package org.coffeennec.graphics.camera;

import java.awt.Dimension;

import org.coffeennec.game.contexts.RenderContext;
import org.coffeennec.game.interfaces.Renderable;
import org.coffeennec.graphics.buffers.CoffeeBuffer;
import org.coffeennec.graphics.buffers.CoffeeRenderer;
import org.coffeennec.math.Point2D;

public class Camera2D implements Renderable {
	private CoffeeBuffer buffer;
	private Point2D position;
	private Dimension size;
	private float zoom;
	
	public Camera2D(int width, int height) {
		this.buffer = new CoffeeBuffer(width, height);
		this.position = Point2D.zero();
		this.size = new Dimension(width, height);
		this.zoom = 100f;
	}
	
	@Override
	public void render(RenderContext ctx) {
		CoffeeRenderer r = ctx.getRenderer();
		float zoom = this.getZoom() / 100f;
		
		int centerX = this.getWidth() / 2;
		int centerY = this.getHeight() / 2;

		int zoomOffsetX = (int) (centerX - (centerX * zoom));
		int zoomOffsetY = (int) (centerY - (centerY * zoom));

		for (int y = 0; y < r.getHeight(); y++) {
			for (int x = 0; x < r.getWidth(); x++) {
				int srcX = (int) ((x - zoomOffsetX) / zoom);
				int srcY = (int) ((y - zoomOffsetY) / zoom);
				
				if (!this.buffer.boundaryCheckScreen(srcX, srcY)) {
					continue;
				}
				
				int color = this.buffer.get(srcX, srcY);
				r.setPixel(x, y, color);
			}
		}
	}
	
	public void renderAndClear(RenderContext ctx) {
		this.render(ctx);
		this.clearBuffer();
	}
	
	public void clearBuffer() {
		this.buffer.fill(0xFFFFFFFF);
	}
	
	public CoffeeBuffer getBuffer() {
		return this.buffer;
	}
	
	
	public void setSize(int width, int height) {
		this.setSize(new Dimension(width, height));
	}
	public void setSize(Dimension size) {
		this.size = size;
	}
	public void setWidth(int width) {
		this.size.width = width;
	}
	public void setheight(int height) {
		this.size.height = height;
	}
	
	public void setPosition(int x, int y) {
		this.setPosition(new Point2D(x, y));
	}
	public void setPosition(Point2D point) {
		this.position = point;
	}
	public void setX(int x) {
		this.position.x = x;
	}
	public void setY(int y) {
		this.position.y = y;
	}
	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
	

	
	public Dimension getSize() {
		return this.size;
	}
	public int getWidth() {
		return this.size.width;
	}
	public int getHeight() {
		return this.size.height;
	}
	
	public Point2D getPosition() {
		return this.position;
	}
	public float getX() {
		return this.position.x;
	}
	public float getY() {
		return this.position.y;
	}
	
	public float getZoom() {
		return this.zoom;
	}

}
