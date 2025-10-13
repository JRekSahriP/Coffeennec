package com.coffeennec.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class FennecKeys extends KeyAdapter {
	private static boolean[] pressedKeys = new boolean[65535];
	private static boolean[] typedKeys = new boolean[65535];
	
	public static void update() {
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
	

	public static boolean isPressed(char key) {
		return isPressed(KeyEvent.getExtendedKeyCodeForChar(key));
	}
	public static boolean isPressed(int key) {
		return pressedKeys[key];
	}
	
	public static boolean isTyped(char key) {
		return isTyped(KeyEvent.getExtendedKeyCodeForChar(key));
	}
	public static boolean isTyped(int key) {
		return typedKeys[key];
	}
	
}
