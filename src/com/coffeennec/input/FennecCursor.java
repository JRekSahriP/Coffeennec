package com.coffeennec.input;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

public class FennecCursor extends MouseAdapter {

	public static final int LEFT_BUTTON = 1;
	public static final int MIDDLE_BUTTON = 2;
	public static final int RIGHT_BUTTON = 3;
	
	
    private static Point MousePosition = new Point();
	private static Point MousePositionOnScreen = new Point();
    
    private static boolean isMouseOnScreen = false;
    
    private static boolean[] mouseButtons = new boolean[10];
    private static boolean[] mouseButtonsClicked = new boolean[10];
    private static boolean[] mouseButtonsClickedBuffer = new boolean[10];
    
    
    private static int wheelRotation = 0;
    private static boolean isWheelMoved = false;

    public static void update() {
    	FennecCursor.isWheelMoved = false;
    	FennecCursor.wheelRotation = 0;
    	
    	System.arraycopy(mouseButtonsClickedBuffer, 0, mouseButtonsClicked, 0, 10);
    	Arrays.fill(FennecCursor.mouseButtonsClickedBuffer, false);
    }
    
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		FennecCursor.wheelRotation = e.getWheelRotation();
	    FennecCursor.isWheelMoved = true;	    
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		FennecCursor.MousePosition = e.getPoint();
		FennecCursor.MousePositionOnScreen = e.getLocationOnScreen();	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		FennecCursor.MousePosition = e.getPoint();
		FennecCursor.MousePositionOnScreen = e.getLocationOnScreen();
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		FennecCursor.isMouseOnScreen = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		FennecCursor.isMouseOnScreen = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (button >= FennecCursor.mouseButtons.length) {
			return;
		}
		
		FennecCursor.mouseButtons[button] = true;
		FennecCursor.mouseButtonsClickedBuffer[button] = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		
		if (button >= FennecCursor.mouseButtons.length) {
			return;
		}
		
		FennecCursor.mouseButtons[button] = false;
	}
	
	public static boolean isButtonPressed(int button) {
		return FennecCursor.mouseButtons[button];
	}
	
	public static boolean isButtonClicked(int button) {
		return FennecCursor.mouseButtonsClicked[button];
	}

	
	public static Point getMousePosition() {
		return FennecCursor.MousePosition;
	}

	public static Point getMousePositionOnScreen() {
		return FennecCursor.MousePositionOnScreen;
	}
	
	public static boolean isMouseOnScreen() {
		return FennecCursor.isMouseOnScreen;
	}
	
	public static int getWheelRotation() {
		return FennecCursor.wheelRotation;
	}
	
	public static boolean isWheelMovedUp() {
		return FennecCursor.wheelRotation < 0;
	}
	public static boolean isWheelMovedDown() {
		return FennecCursor.wheelRotation > 0;
	}
	
	public static boolean isWheelMoved() {
		return FennecCursor.isWheelMoved;
	}

	
}