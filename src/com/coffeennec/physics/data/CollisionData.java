package com.coffeennec.physics.data;

import com.coffeennec.math.Point2D;

public class CollisionData {
	private boolean result;
	private Point2D normal;
	private float depth;
	private Point2D[] contactPoints;
	
	
	
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public Point2D getNormal() {
		return normal;
	}
	public void setNormal(Point2D normal) {
		this.normal = normal;
	}
	public float getDepth() {
		return depth;
	}
	public void setDepth(float depth) {
		this.depth = depth;
	}
	public Point2D[] getContactPoints() {
		return contactPoints;
	}
	public void setContactPoints(Point2D[] contactPoints) {
		this.contactPoints = contactPoints;
	}
	public void setContactPoint(Point2D contactPoint) {
		this.contactPoints = new Point2D[] {contactPoint};
	}
	
	
}
