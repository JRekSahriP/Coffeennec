package org.coffeennec.physics.bodies;

import org.coffeennec.game.contexts.GameContext;
import org.coffeennec.game.contexts.RenderContext;
import org.coffeennec.math.Point2D;
import org.coffeennec.math.data.AABBData;
import org.coffeennec.physics.collision.FennecCollisions;
import org.coffeennec.physics.data.CollisionData;

public class CircleBody extends Body {

	private final float radius;

	public CircleBody(Point2D position, float radius, float density, float restitution, float friction, boolean isStatic, boolean canRotate) {
		super(position, density, restitution, friction, (float) (radius * radius * Math.PI), isStatic, canRotate);
		this.radius = radius;
		this.transformedVertices = new Point2D[1];
		this.computeInertia();
	}
	
	@Override
	public void update(GameContext ctx) {}

	@Override
	public void render(RenderContext ctx) {}

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
		return this.radius;
	}

	@Override
	public CollisionData collideWith(Body other) {

		if (other instanceof RectangleBody || other instanceof ConvexPolygonBody) {
			return FennecCollisions.intersectCircleAndPolygon(
					this.getPosition(), this.getRadius(),
					other.getPosition(), other.getTransformedVertices()
					);
		}	
		
		if (other instanceof CircleBody) {
			return FennecCollisions.intersectCircles(
					this.getPosition(), this.getRadius(),
					other.getPosition(), ((CircleBody)other).getRadius()
					);				
		}
		
		CollisionData data = new CollisionData();
		data.setResult(false);
		return data;
	}

}
