package com.coffeennec.window;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

import com.coffeennec.input.FennecCursor;
import com.coffeennec.input.FennecKeys;

public abstract class CoffeeWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private CoffeePanel panel;
	
	public CoffeeWindow() {
		super("Coffeennec Window");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panel = new CoffeePanel(this);	
		this.panel.pauseLoop();
		this.add(panel);
		this.setWindowSize(600, 600);
	
		
		this.setLocationRelativeTo(null);
		
		this.addFennecKeys();
		this.addFennecCursor();
		
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
	
	
	
	private void addFennecKeys() {
		this.addKeyListener(new FennecKeys());
	}
	private void addFennecCursor() {
		FennecCursor fc = new FennecCursor();
		this.panel.addMouseListener(fc);
		this.panel.addMouseMotionListener(fc);
		this.panel.addMouseWheelListener(fc);
	}

	
	
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
