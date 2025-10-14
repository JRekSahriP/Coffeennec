package com.coffeennec.math;

import com.coffeennec.strings.FennecString;

public class Point2D {
	public float x, y;
	
	public Point2D() {}
	public Point2D(java.awt.Point point) {
		this(point.x, point.y);
	}
	public Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2D(Point2D point) {
		this(point.x, point.y);
	}

	public static Point2D zero() {
		return new Point2D(0f, 0f);
	}
	
	public Point2D add(Point2D p) {
		this.x += p.x;
		this.y += p.y;
		return this;
	}
	public Point2D subtract(Point2D p) {
		this.x -= p.x;
		this.y -= p.y;
		return this;
	}
	public Point2D multiply(Point2D p) {
		this.x *= p.x;
		this.y *= p.y;
		return this;
	}
	public Point2D multiply(float s) {
		this.x *= s;
		this.y *= s;
		return this;
	}
	public Point2D divide(Point2D p) {
		this.x /= p.x;
		this.y /= p.y;
		return this;
	}
	public Point2D divide(float s) {
		this.x /= s;
		this.y /= s;
		return this;
	}
	
	public static Point2D sum(Point2D p1, Point2D p2) {
		return new Point2D(p1.x + p2.x, p1.y + p2.y);
	}
	public static Point2D subtract(Point2D p1, Point2D p2) {
		return new Point2D(p1.x - p2.x, p1.y - p2.y);
	}
	public static Point2D multiply(Point2D p1, Point2D p2) {
		return new Point2D(p1.x * p2.x, p1.y * p2.y);
	}
	public static Point2D multiply(Point2D p1, float s) {
		return new Point2D(p1.x * s, p1.y * s);
	}
	public static Point2D divide(Point2D p1, Point2D p2) {
		return new Point2D(p1.x / p2.x, p1.y / p2.y);
	}
	
	public static Point2D inverse(Point2D p) {
		return new Point2D(-p.x, -p.y);
	}

	public static Point2D transform(Point2D p, Transform2D transform) {
		return new Point2D(
				transform.cos * p.x - transform.sin * p.y + transform.x,
				transform.sin * p.x + transform.cos * p.y + transform.y
		);
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	public static Point2D normalize(Point2D p) {
		float length = p.length();
		return new Point2D(p.x / length, p.y / length);
	}
	public Point2D normalize() {
		float length = this.length();
		this.x = x / length;
		this.y = y / length;
		return this;
	}
	
	public static float dot(Point2D p1, Point2D p2) {
        return p1.x * p2.x + p1.y * p2.y;
    }

    public static float cross(Point2D p1, Point2D p2) {
        return p1.x * p2.y - p1.y * p2.x;
    }
	
	public static float distance(Point2D p1, Point2D p2) {
		float deltaX = p1.x - p2.x;
		float deltaY = p1.y - p2.y;
		return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
	
	public static Point2D findArithmeticMean(Point2D[] vertices) {
		float sumX = 0;
		float sumY = 0;
		
		for(int i = 0; i < vertices.length; i++) {
			Point2D p = vertices[i];
			sumX += p.x;
			sumY += p.y;
		}
		
		return new Point2D(sumX / vertices.length, sumY / vertices.length);
	}

	/**Retrieves the value of the specified axis.
	 * 
	 * @param axis The axis to retrieve, where:
	 * <ul>
	 * 	<li>0: X-axis</li>
	 * 	<li>1: Y-axis</li>
	 * </ul>
	 * @return The value of the specified axis, or Float.NaN if the axis is invalid
	 */
	public float getAxis(int axis) {
        switch (axis) {
            case 0: return x;
            case 1: return y;
            default: {
            	FennecString.eprintf("Invalid axis {}\n", axis);
            	return Float.NaN;
            }
        }
    }
	
	private boolean equals(Point2D other) {
		return this.x == other.x && this.y == other.y;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		
		if(other instanceof Point2D) {
			return this.equals((Point2D)other);
		} 
		return false;
	}
	
	@Override
	public String toString() {
		return FennecString.format("{x: {}, y: {}}\n", x, y);
	}
	
}
