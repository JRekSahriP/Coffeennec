package org.coffeennec.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public final class FennecKeys extends KeyAdapter {
	private static final FennecKeys instance = new FennecKeys();

	private boolean[] pressedKeys = new boolean[65535];
	private boolean[] typedKeys = new boolean[65535];
	
	private FennecKeys() {}
	
	public static FennecKeys getInstance() {
		return instance;
	}
	
	public void update() {
		Arrays.fill(typedKeys, false);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		pressedKeys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys[e.getKeyCode()] = false;
		typedKeys[e.getKeyCode()] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
//		e.getKeyCode() // returning 0...
		typedKeys[KeyEvent.getExtendedKeyCodeForChar(e.getKeyChar())] = true;
	}
	

	public boolean isPressed(char key) {
		return isPressed(KeyEvent.getExtendedKeyCodeForChar(key));
	}
	public boolean isPressed(int key) {
		return pressedKeys[key];
	}
	
	public boolean isTyped(char key) {
		return isTyped(KeyEvent.getExtendedKeyCodeForChar(key));
	}
	public boolean isTyped(int key) {
		return typedKeys[key];
	}
	
}
