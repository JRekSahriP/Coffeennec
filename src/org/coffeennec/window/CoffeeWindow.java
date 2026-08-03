package org.coffeennec.window;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.coffeennec.graphics.buffers.CoffeeBuffer;
import org.coffeennec.input.FennecCursor;
import org.coffeennec.input.FennecKeys;

public abstract class CoffeeWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private CoffeePanel panel;
	
	public CoffeeWindow() {
		super("Coffeennec Window");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panel = new CoffeePanel(this);	
		this.panel.pauseLoop();
		this.add(this.panel);
		this.setWindowSize(600, 600);
	
		
		this.addFennecKeys();
		this.addFennecCursor();
		
		this.initializer(); 
		this.config();

		this.setLocationRelativeTo(null);
		this.panel.continueLoop();
		this.setVisible(true);
		this.validate();
		this.repaint();
	}
	
	protected abstract void initializer();
	protected abstract void config();
	protected abstract void update();
	protected abstract void draw(CoffeeBuffer b);
	
	
	
	private void addFennecKeys() {
		this.addKeyListener(FennecKeys.getInstance());
	}
	private void addFennecCursor() {
		FennecCursor fc = FennecCursor.getInstance();
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
