package com.coffeennec.physics.bodies;

import com.coffeennec.graphics.buffers.CoffeeBuffer;
import com.coffeennec.math.Point2D;
import com.coffeennec.math.Transform2D;
import com.coffeennec.math.data.AABBData;

public class RectangleBody extends Body {

	private final float width;
	private final float height;
	private Point2D[] initialVertices;

	protected RectangleBody(Point2D position, float width, float height, float density, float restitution, boolean isStatic, boolean canRotate) {
		super(position, density, density * (width * height), restitution, (width * height), isStatic, canRotate);
		this.width = width;
		this.height = height;
		this.initialVertices = new Point2D[] {
				new Point2D(-width / 2f, -height / 2f),
				new Point2D( width / 2f, -height / 2f),
				new Point2D( width / 2f,  height / 2f),
				new Point2D(-width / 2f,  height / 2f)	
		};
	}

	@Override
	public void update() {}

	@Override
	public void render(CoffeeBuffer b) {}

	@Override
	protected float calculateInertiaValue() {
		return (1f / 12f) * this.mass * (this.width * this.width + this.height * this.height);
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
		if (this.isStatic || !this.canRotate ) {
			return this.initialVertices.clone();
		}

		if (!this.transformUpdateRequired && this.transformedVertices != null) {
			return this.transformedVertices;
		}

		Transform2D transform = new Transform2D(this.position, this.rotation);
		for (int i = 0; i < 4; i++) {
			Point2D p = this.initialVertices[i];
			this.transformedVertices[i] = Point2D.transform(p, transform);
		}

		this.transformUpdateRequired = false;
		return this.transformedVertices;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Point2D[] getInitialVertices() {
		return initialVertices.clone();
	}
	
}
