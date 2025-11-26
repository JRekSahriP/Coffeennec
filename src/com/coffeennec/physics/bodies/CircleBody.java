package com.coffeennec.physics.bodies;

import com.coffeennec.graphics.buffers.CoffeeBuffer;
import com.coffeennec.math.Point2D;
import com.coffeennec.math.data.AABBData;

public class CircleBody extends Body {

	private final float radius;

	protected CircleBody(Point2D position, float radius, float density, float restitution, boolean isStatic, boolean canRotate) {
		super(position, density, (float) (density * (radius * radius * Math.PI)), restitution, (float) (radius * radius * Math.PI), isStatic, canRotate);
		this.radius = radius;
		this.transformedVertices = new Point2D[1];
	}
	
	@Override
	public void update() {}

	@Override
	public void render(CoffeeBuffer b) {}

	@Override
	protected float calculateInertiaValue() {
		return (this.mass * this.radius * this.radius) * 0.5f;
	}

	@Override
	public AABBData getAABB() {
		return new AABBData(
				this.position.x - this.radius, 
				this.position.y - this.radius, 
				this.position.x + this.radius, 
				this.position.y + this.radius
			);
	}

	@Override
	public Point2D[] getTransformedVertices() {
		// Circle geometry is defined only by its center and radius, so vertex transformation is unnecessary.
		// Return the center position.
		
		if (this.transformUpdateRequired || this.transformedVertices[0] == null) {
			this.transformedVertices[0] = this.position;
			this.transformUpdateRequired = false;
		}
		
		return this.transformedVertices;
	}

	public float getRadius() {
		return radius;
	}

}
