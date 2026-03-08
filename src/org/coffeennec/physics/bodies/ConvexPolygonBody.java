package org.coffeennec.physics.bodies;

import org.coffeennec.graphics.buffers.CoffeeRenderer;
import org.coffeennec.math.Point2D;
import org.coffeennec.math.Transform2D;
import org.coffeennec.math.data.AABBData;
import org.coffeennec.physics.collision.FennecCollisions;
import org.coffeennec.physics.data.CollisionData;

public class ConvexPolygonBody extends Body {

	private Point2D[] initialVertices;
	
	public ConvexPolygonBody(Point2D position, Point2D[] vertices, float density, float restitution, float friction, boolean isStatic, boolean canRotate, boolean centerVertices) {
		super(position, density, restitution, friction, calculateArea(vertices), isStatic, canRotate);
		this.initialVertices = vertices.clone();
		
		if (centerVertices) {
			centerVertices();
		}
		
		this.transformedVertices = new Point2D[this.initialVertices.length];
		
		this.computeInertia();
	}
	

	@Override
	public void update() {}

	@Override
	public void render(CoffeeRenderer r) {}

	@Override
	public CollisionData collideWith(Body other) {
		if (other instanceof RectangleBody || other instanceof ConvexPolygonBody) {
			return FennecCollisions.intersectPolygons(
					this.getPosition(), this.getTransformedVertices(),
					other.getPosition(), other.getTransformedVertices()
					);
		}
		
		if (other instanceof CircleBody) {
			CollisionData data = FennecCollisions.intersectCircleAndPolygon(
					other.getPosition(), ((CircleBody)other).getRadius(),
					this.getPosition(), this.getTransformedVertices()
					);
			data.setNormal(Point2D.inverse(data.getNormal()));
			return data;
		}
		
		CollisionData data = new CollisionData();
		data.setResult(false);
		return data;
	}

	@Override
	protected float calculateInertiaValue() {
		float numerator = 0.0f;
		float denominator = 0.0f;
		int n = this.initialVertices.length;
		
		for (int i = 0; i < n; i++) {
			Point2D p1 = this.initialVertices[i];
			Point2D p2 = this.initialVertices[(i + 1) % n];
			
			float cross = Point2D.cross(p1, p2);
			numerator += cross * (Point2D.dot(p1, p1) + Point2D.dot(p1, p2) + Point2D.dot(p2, p2));
			denominator += cross;
		}
		
		float inertiaFactor = (1.0f/6.0f) * (numerator / denominator);
		return this.mass * inertiaFactor;
	}

	@Override
	public AABBData getAABB() {
		Point2D[] vertices = this.getTransformedVertices();
		
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;

		for (int i = 0; i < vertices.length; i++) {
			minX = Math.min(minX, vertices[i].x);
			maxX = Math.max(maxX, vertices[i].x);
			minY = Math.min(minY, vertices[i].y);
			maxY = Math.max(maxY, vertices[i].y);
		}
		
		return new AABBData(minX, minY, maxX, maxY);
	}

	@Override
	public Point2D[] getTransformedVertices() {
		if (!this.transformUpdateRequired) {
			return this.transformedVertices;
		}

		if (this.isStatic || !this.canRotate) {
	        for (int i = 0; i < this.initialVertices.length; i++) {
	            this.transformedVertices[i] = Point2D.sum(this.initialVertices[i], this.position);
	        }

	        if (this.isStatic) {
	            this.transformUpdateRequired = false; 
	        }

	        return this.transformedVertices;
	    }
		

		Transform2D transform = new Transform2D(this.position, this.rotation);
		for (int i = 0; i < this.initialVertices.length; i++) {
			Point2D p = this.initialVertices[i];
			this.transformedVertices[i] = Point2D.transform(p, transform);
		}

		this.transformUpdateRequired = false;
		return this.transformedVertices;
	}

	private void centerVertices() {
		Point2D center = Point2D.findArithmeticMean(this.initialVertices);
		
		for (Point2D p : this.initialVertices) {
			p.subtract(center);
		}
	}

	private static float calculateArea(Point2D[] vertices) {		
		float sum1 = 0.0f;
		float sum2 = 0.0f;
		int n = vertices.length;
		
		for (int i = 0; i < n; i++) {
			Point2D p1 = vertices[i];
			Point2D p2 = vertices[(i + 1) % n];
			
			sum1 += p1.x * p2.y;
			sum2 += p1.y * p2.x;
		}
		
		return Math.abs(sum1 - sum2) * 0.5f;
	}
	
}
