package org.coffeennec.window;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.coffeennec.game.contexts.GameContext;
import org.coffeennec.game.contexts.RenderContext;
import org.coffeennec.graphics.buffers.CoffeeBuffer;
import org.coffeennec.input.FennecCursor;
import org.coffeennec.input.FennecKeys;

public abstract class CoffeeWindow<G extends GameContext, R extends RenderContext> extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private CoffeePanel<G, R> panel;
	private final G gameContext;
	private final R renderContext;
	
	public CoffeeWindow() {
		super("Coffeennec Window");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.panel = new CoffeePanel<>(this);	
		this.add(this.panel);
		this.setWindowSize(600, 600);
		
		this.gameContext = this.createGameContext();
		this.renderContext = this.createRenderContext(this.panel.getBuffer());
		
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
	
	protected abstract G createGameContext();
	protected abstract R createRenderContext(CoffeeBuffer buffer);
	
	protected abstract void initializer();
	protected abstract void config();
	protected abstract void update(G ctx);
	protected abstract void draw(R ctx);
	
	
	
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
		this.panel.setBuffer(new CoffeeBuffer(size.width, size.height));
		this.pack();
	}

	
	public G getGameContext() {
		return gameContext;
	}
	
	public R getRenderContext() {
		return renderContext;
	}

	public CoffeePanel<G, R> getPanel() {
		return panel;
	}
	
}
