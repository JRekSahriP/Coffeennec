package com.coffeennec.window;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.coffeennec.graphics.buffers.CoffeeBuffer;
import com.coffeennec.input.FennecCursor;
import com.coffeennec.input.FennecKeys;

public class CoffeePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private Thread thread;
	private volatile boolean running;
	private volatile boolean paused;
	
	
	private CoffeeWindow window;
	private CoffeeBuffer buffer;
	
	private int maxFPS;
	private double tickTime;
	private int FPS;
	
	public CoffeePanel(CoffeeWindow window) {
		this.running = true;
		this.paused = false;
		
		this.setMaxFPS(60);
		
		this.FPS = 0;
		
		this.window = window;
		this.initThread();
	}
	
	private void initThread() {
		(this.thread = new Thread(() -> this.run())).start();
	}
	
	private void run() {
		this.buffer = new CoffeeBuffer(this.window.getWidth(), this.window.getHeight());
		
		long lastTime = System.nanoTime();
		double delta = 0.0;
		long timer = System.currentTimeMillis();
		int frames = 0;

		while (this.running) {
			long atual = System.nanoTime();
			delta += (atual - lastTime) / this.tickTime;
			lastTime = atual;
			
			if (!this.paused) {
				if (delta >= 1) {
					this.update();
					this.repaint();
					this.updateStates();
					frames++;
					delta--;
				}
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				this.FPS = frames;
				timer += 1000;
				frames = 0;
			}
		}
	}
	
	private void update() {
		this.window.update();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.window.draw(this.buffer);
		g.drawImage(this.buffer.toImage(), 0, 0, null);
	}
	
	private void updateStates() {
		FennecKeys.update();
		FennecCursor.update();
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
		return this.FPS;
	}
	public void setMaxFPS(int fps) {
		this.maxFPS = fps;
		this.tickTime = 1_000_000_000.0 / this.maxFPS;
	}
	
	
}
