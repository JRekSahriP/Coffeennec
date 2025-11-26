package com.coffeennec.physics.data;

import com.coffeennec.math.Point2D;

public class AxisOverlapData {
	private boolean isOverlapping;
	private float depth;
	private Point2D axis;
	
	public AxisOverlapData() {}
	public AxisOverlapData(boolean isOverlapping, float depth, Point2D axis) {
		this.isOverlapping = isOverlapping;
		this.depth = depth;
		this.axis = axis;
	}
	
	public boolean isOverlapping() {
		return isOverlapping;
	}
	public void setOverlapping(boolean isOverlapping) {
		this.isOverlapping = isOverlapping;
	}
	public float getDepth() {
		return depth;
	}
	public void setDepth(float depth) {
		this.depth = depth;
	}
	public Point2D getAxis() {
		return axis;
	}
	public void setAxis(Point2D axis) {
		this.axis = axis;
	}	
}
