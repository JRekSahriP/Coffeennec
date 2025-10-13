package com.coffeennec.window;

import java.awt.Graphics;

import javax.swing.JPanel;

public class CoffeePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private Thread thread;
	private volatile boolean running;
	private volatile boolean paused;
	
	
	private CoffeeWindow window;
	private int maxFPS;
	private double tickTime;
	private int FPS;
	
	public CoffeePanel(CoffeeWindow window) {
		running = true;
		paused = false;
		
		setMaxFPS(60);
		
		FPS = 0;
		
		this.window = window;
		initThread();
	}
	
	private void initThread() {
		(this.thread = new Thread(() -> this.run())).start();
	}
	
	private void run() {
		long lastTime = System.nanoTime();
		double delta = 0.0;
		long timer = System.currentTimeMillis();
		int frames = 0;

		while (running) {
			long atual = System.nanoTime();
			delta += (atual - lastTime) / tickTime;
			lastTime = atual;
			
			if (!paused) {
				if (delta >= 1) {
					update();
					repaint();
					frames++;
					delta--;
				}
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				FPS = frames;
				timer += 1000;
				frames = 0;
			}
		}
	}
	
	private void update() {
		window.update();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		window.draw(g);
	}
	
	public void pauseLoop() {
		paused = true;
	}
	
	public void continueLoop() {
		paused = false;
	}
	
	public void stopLoop() {
		running = false;
	}
	
	public int getFPS() {
		return FPS;
	}
	public void setMaxFPS(int fps) {
		maxFPS = fps;
		tickTime = 1_000_000_000.0 / maxFPS;
	}
	
	
}
