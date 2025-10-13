package com.coffeennec.input;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class FennecCursor extends MouseAdapter {

    private static Point MousePosition = new Point();
	private static Point MousePositionOnScreen = new Point();
    
    private static boolean isMousePressed = false;
    private static boolean isMouseOnScreen = false;
    
    private static int mouseButtonPressed = -1; // -1 == nothing

    
    private static int wheelRotation = 0;
    private static boolean isWheelMoved = false;

    public static void update() {
    	isWheelMoved = false;
    	wheelRotation = 0;
    }
    
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		wheelRotation = e.getWheelRotation();
	    isWheelMoved = true;	    
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		MousePosition = e.getPoint();
		MousePositionOnScreen = e.getLocationOnScreen();	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		MousePosition = e.getPoint();
		MousePositionOnScreen = e.getLocationOnScreen();
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		isMouseOnScreen = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		isMouseOnScreen = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		isMousePressed = true;
		mouseButtonPressed = e.getButton();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		isMousePressed = false;
		mouseButtonPressed = -1;
	}
	
    public static Point getMousePosition() {
		return MousePosition;
	}

	public static Point getMousePositionOnScreen() {
		return MousePositionOnScreen;
	}
	
	public static boolean isMouseOnScreen() {
		return isMouseOnScreen;
	}

	
	
	public static boolean isMousePressed() {
		return isMousePressed;
	}

	public static int getMouseButtonPressed() {
		return mouseButtonPressed;
	}
	
	public static boolean isLeftButtonPressed() {
		return mouseButtonPressed == 1;
	}
	
	public static boolean isMiddleButtonPressed() {
		return mouseButtonPressed == 2;
	}
	
	public static boolean isRightButtonPressed() {
		return mouseButtonPressed == 3;
	}
	

	public static int getWheelRotation() {
		return wheelRotation;
	}
	
	public static boolean isWheelMovedUp() {
		return wheelRotation < 0;
	}
	public static boolean isWheelMovedDown() {
		return wheelRotation > 0;
	}
	
	public static boolean isWheelMoved() {
		return isWheelMoved;
	}

	
}