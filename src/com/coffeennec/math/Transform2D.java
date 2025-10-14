package com.coffeennec.math;

public final class Transform2D {
	public final float x, y, sin, cos;
	
	public Transform2D(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		this.cos = (float) Math.cos(angle);
		this.sin = (float) Math.sin(angle);
	}
	public Transform2D(Point2D p, float angle) {
		this(p.x, p.y, angle);
	}
	
	public static Transform2D zero() {
		return new Transform2D(0f, 0f, 0f);
	}
}
