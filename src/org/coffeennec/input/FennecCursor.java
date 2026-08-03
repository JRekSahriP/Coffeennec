package org.coffeennec.input;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

public final class FennecCursor extends MouseAdapter {
	private static final FennecCursor instance = new FennecCursor();

	public static final int LEFT_BUTTON = 1;
	public static final int MIDDLE_BUTTON = 2;
	public static final int RIGHT_BUTTON = 3;
	
	
    private Point mousePosition = new Point();
	private Point mousePositionOnScreen = new Point();
    
    private boolean isMouseOnScreen = false;
    
    private boolean[] mouseButtons = new boolean[10];
    private boolean[] mouseButtonsClicked = new boolean[10];
    private boolean[] mouseButtonsClickedBuffer = new boolean[10];
    
    
    private int wheelRotation = 0;
    private boolean isWheelMoved = false;
    
    private FennecCursor() {}
    
    public static FennecCursor getInstance() {
    	return instance;
    }

    public void update() {
    	isWheelMoved = false;
    	wheelRotation = 0;
    	
    	System.arraycopy(mouseButtonsClickedBuffer, 0, mouseButtonsClicked, 0, 10);
    	Arrays.fill(mouseButtonsClickedBuffer, false);
    }
    
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		wheelRotation = e.getWheelRotation();
	    isWheelMoved = true;	    
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mousePosition = e.getPoint();
		mousePositionOnScreen = e.getLocationOnScreen();	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosition = e.getPoint();
		mousePositionOnScreen = e.getLocationOnScreen();
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
		int button = e.getButton();
		
		if (button >= mouseButtons.length) {
			return;
		}
		
		mouseButtons[button] = true;
		mouseButtonsClickedBuffer[button] = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		
		if (button >= mouseButtons.length) {
			return;
		}
		
		mouseButtons[button] = false;
	}
	
	public boolean isButtonPressed(int button) {
		return mouseButtons[button];
	}
	
	public boolean isButtonClicked(int button) {
		return mouseButtonsClicked[button];
	}

	
	public Point getMousePosition() {
		return mousePosition;
	}

	public Point getMousePositionOnScreen() {
		return mousePositionOnScreen;
	}
	
	public boolean isMouseOnScreen() {
		return isMouseOnScreen;
	}
	
	public int getWheelRotation() {
		return wheelRotation;
	}
	
	public boolean isWheelMovedUp() {
		return wheelRotation < 0;
	}
	public boolean isWheelMovedDown() {
		return wheelRotation > 0;
	}
	
	public boolean isWheelMoved() {
		return isWheelMoved;
	}

	
}