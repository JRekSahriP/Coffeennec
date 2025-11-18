package com.coffeennec.math.data;

import com.coffeennec.math.Point2D;

public class AABBData {
	private final Point2D min;
	private final Point2D max;
	
	public AABBData(float minX, float minY, float maxX, float maxY) {
		this.min = new Point2D(minX, minY);
		this.max = new Point2D(maxX, maxY);
	}
	public AABBData(Point2D min, Point2D max) {
		this.min = min;
		this.max = max;
	}
	
	public boolean intersects(AABBData other) {
		return 	this.min.x < other.max.x &&
				this.max.x > other.min.x &&
				this.min.y < other.max.y &&
				this.max.y > other.min.y;
	}
	
	public Point2D getMin() {
		return this.min;
	}
	public Point2D getMax() {
		return this.max;
	}

}
