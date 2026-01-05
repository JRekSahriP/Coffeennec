package com.coffeennec.physics.joints;

import com.coffeennec.math.Point2D;
import com.coffeennec.physics.bodies.Body;

public abstract class Joint {
	protected final Body bodyA;
	protected final Point2D localAnchorA;
	
	protected final Body bodyB;
	protected final Point2D localAnchorB;

	private boolean isBroke;
	
	public Joint(Body bodyA, Point2D localAnchorA, Body bodyB, Point2D localAnchorB) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
		this.localAnchorA = localAnchorA;
		this.localAnchorB = localAnchorB;
		this.isBroke = false;		
	}
	
	public abstract void solve(float deltaTime);
	
	public Point2D getAnchorA() {
		return Point2D.sum(
				this.bodyA.getPosition(),
				this.rotateAround(this.localAnchorA, this.bodyA.getRotation())
				);
	}
	public Point2D getAnchorB() {
		return Point2D.sum(
				this.bodyB.getPosition(),
				this.rotateAround(this.localAnchorB, this.bodyB.getRotation())
				);
	}
	
	
	protected Point2D rotateAround(Point2D point, float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		return new Point2D(
				point.x * cos - point.y * sin,
				point.x * sin + point.y * cos
				);
	}

	public void breakJoint() {
		this.isBroke = true;
	}
	public boolean isBroke() {
		return this.isBroke;
	}
	
	public Body getBodyA() {
		return this.bodyA;
	}
	public Body getBodyB() {
		return this.bodyB;
	}
	
}
