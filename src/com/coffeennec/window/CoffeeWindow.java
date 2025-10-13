package com.coffeennec.window;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

public abstract class CoffeeWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private CoffeePanel panel;
	
	public CoffeeWindow() {
		super("Coffeennec Window");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panel = new CoffeePanel(this);	
		panel.pauseLoop();
		this.add(panel);
		this.setWindowSize(600, 600);
	
		
		this.setLocationRelativeTo(null);
		
		
		initializer(); 
		config();

		panel.continueLoop();
		this.setVisible(true);
		this.validate();
		this.repaint();
	}
	
	protected abstract void initializer();
	protected abstract void config();
	protected abstract void update();
	protected abstract void draw(Graphics g);
	
	
	
	public void setWindowSize(int width, int height) {
		this.setWindowSize(new Dimension(width, height));
	}
	public void setWindowSize(Dimension size) {
		this.setSize(size);
		this.panel.setPreferredSize(size);
		this.panel.setSize(size);
		this.pack();
	}


	public CoffeePanel getPanel() {
		return this.panel;
	}
	
}
