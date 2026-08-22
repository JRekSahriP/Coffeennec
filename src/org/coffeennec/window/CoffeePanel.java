package org.coffeennec.window;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import org.coffeennec.game.contexts.GameContext;
import org.coffeennec.game.contexts.RenderContext;
import org.coffeennec.graphics.buffers.CoffeeBuffer;

public class CoffeePanel<G extends GameContext, R extends RenderContext> extends JPanel {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private Thread thread;
	private volatile boolean running;
	private volatile boolean paused;
	
	
	private CoffeeWindow<G, R> window;
	private CoffeeBuffer buffer;
	
	private int maxFPS;
	private double tickTime;
	private int FPS;
	
	public CoffeePanel(CoffeeWindow<G, R> window) {
		this.running = true;
		this.paused = true;
		
		this.setMaxFPS(60);
		
		this.FPS = 0;
		
		this.window = window;
		
		this.buffer = new CoffeeBuffer(this.window.getWidth(), this.window.getHeight());
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				buffer = new CoffeeBuffer(getWidth(), getHeight());
				if (window.getRenderContext() != null) {
					window.getRenderContext().setBuffer(buffer);
				}
			}
		});
		
		this.initThread();
	}
	
	private void initThread() {
		(this.thread = new Thread(() -> this.run())).start();
	}
	
	private void run() {
		long lastTime = System.nanoTime();
		double delta = 0.0;
		long timer = System.currentTimeMillis();
		int frames = 0;

		while (this.running) {
			long atual = System.nanoTime();
			
			if (!this.paused) {
				delta += (atual - lastTime) / this.tickTime;

				if (delta >= 1) {
					this.update(delta);
					this.repaint();
					frames++;
					delta--;
				}
			}

			lastTime = atual;
			
			if (System.currentTimeMillis() - timer >= 1000) {
				this.FPS = frames;
				timer += 1000;
				frames = 0;
			}
		}
	}
	
	private void update(double delta) {
		G context = this.window.getGameContext();
		context.setDeltaTime(delta);
		this.window.update(context);
		context.updateState();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		R context = this.window.getRenderContext();
		this.window.draw(context);
		g.drawImage(context.getBuffer().toImage(), 0, 0, null);
	}
	
	public void pauseLoop() {
		this.paused = true;
	}
	
	public void continueLoop() {
		this.paused = false;
	}
	
	public void stopLoop() {
		this.running = false;
	}
	
	public int getFPS() {
		return FPS;
	}
	public void setMaxFPS(int fps) {
		this.maxFPS = fps;
		this.tickTime = 1_000_000_000.0 / this.maxFPS;
	}
	
	public CoffeeBuffer getBuffer() {
		return buffer;
	}
	
	public void setBuffer(CoffeeBuffer buffer) {
		this.buffer = buffer;
	}
	
}
