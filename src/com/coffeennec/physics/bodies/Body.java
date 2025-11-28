package com.coffeennec.physics.bodies;

import com.coffeennec.game.abstractions.GameObject;
import com.coffeennec.math.Point2D;
import com.coffeennec.math.data.AABBData;
import com.coffeennec.physics.data.CollisionData;

public abstract class Body extends GameObject {
	protected Point2D position;

	private Point2D linearVelocity;

	protected float rotation, rotationVelocity;

	private Point2D force;

	protected float density, mass, invMass, restitution, area;

	protected boolean isStatic, canRotate;

	protected float torque, inertia, invInertia;
	protected float angularDamping; // 0 - 1 (1 = no damping)

	protected boolean transformUpdateRequired, aabbUpdateRequired;

	protected Point2D[] transformedVertices;

	protected Body(Point2D position, float density, float mass, float restitution, float area,
			boolean isStatic, boolean canRotate) {

		this.position = position;
		this.linearVelocity = Point2D.zero();
		this.rotation = 0f;
		this.rotationVelocity = 0f;
		this.force = Point2D.zero();

		this.density = density;
		this.mass = mass;
		this.restitution = restitution;
		this.area = area;
		this.isStatic = isStatic;

		this.invMass = this.isStatic ? 0f : (1f / this.mass);
		this.canRotate = canRotate;
		this.angularDamping = 1;
		this.torque = 0f;

		this.transformUpdateRequired = true;
		this.aabbUpdateRequired = true;

		this.transformedVertices = null;
	}

	public abstract CollisionData collideWith(Body other);
	
	protected abstract float calculateInertiaValue();
	protected void setInertia(float inertia) {
		this.inertia = inertia;
		this.invInertia = 1f / this.inertia; 
	}

	public abstract AABBData getAABB();
	public abstract Point2D[] getTransformedVertices();

	public void applyForce(Point2D force, Point2D point) {
		if (this.isStatic) {
			return;
		}

		this.force.add(force);

		if (this.canRotate) {
			Point2D r = Point2D.subtract(point, this.position);
			this.torque += r.x * force.y - r.y * force.x; // Torque = r * f;
		}
	}

	public void step(float deltaTime, int iterations, Point2D gravity) {
		if (this.isStatic) {
			return;
		}

		float time = deltaTime / iterations;

		this.linearVelocity.add(Point2D.multiply(gravity, time));

		if (this.canRotate) {
			float angularAcceleration = this.torque * this.invInertia;
			this.rotationVelocity += (angularAcceleration * time);
			this.rotationVelocity *= this.angularDamping;
			this.rotation += this.rotationVelocity * time;
			this.torque = 0f;
		}

		this.position.add(Point2D.multiply(this.linearVelocity, time));

		this.force = Point2D.zero();
		this.transformUpdateRequired = true;
		this.aabbUpdateRequired = true;
	}
	
	

	public Point2D getLinearVelocityWithRotation(Point2D r) {
		if (this.canRotate) {
			// v = vLinear + (omega x r)
			return Point2D.sum(this.linearVelocity, new Point2D(-this.rotationVelocity * r.y, this.rotationVelocity * r.x));
		}
		return this.linearVelocity;
	}

	public void move(Point2D amount) {
		this.position.add(amount);
		this.transformUpdateRequired = true;
		this.aabbUpdateRequired = true;
	}

	public void moveTo(Point2D position) {
		this.position = position;
		this.transformUpdateRequired = true;
		this.aabbUpdateRequired = true;
	}

	public void rotate(float amount) {
		this.rotation += amount;
		this.transformUpdateRequired = true;
		this.aabbUpdateRequired = true;
	}

	public Point2D getPosition() {
		return this.position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public Point2D getLinearVelocity() {
		return this.linearVelocity;
	}

	public void setLinearVelocity(Point2D linearVelocity) {
		this.linearVelocity = linearVelocity;
	}

	public float getRotation() {
		return this.rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getRotationVelocity() {
		return this.rotationVelocity;
	}

	public void setRotationVelocity(float rotationVelocity) {
		this.rotationVelocity = rotationVelocity;
	}

	public Point2D getForce() {
		return this.force;
	}

	public void setForce(Point2D force) {
		this.force = force;
	}

	public float getDensity() {
		return this.density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public float getMass() {
		return this.mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getInvMass() {
		return this.invMass;
	}

	public void setInvMass(float invMass) {
		this.invMass = invMass;
	}

	public float getRestitution() {
		return this.restitution;
	}

	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	public float getArea() {
		return this.area;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public boolean isStatic() {
		return this.isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean canRotate() {
		return this.canRotate;
	}

	public void setCanRotate(boolean canRotate) {
		this.canRotate = canRotate;
	}

	public float getTorque() {
		return this.torque;
	}

	public void setTorque(float torque) {
		this.torque = torque;
	}

	public void computeInertia() {
		if (this.isStatic || !this.canRotate) {
			this.inertia = 0f;
			this.invInertia = 0f;
		} else {
			this.setInertia(this.calculateInertiaValue());
		}
	}
	public float getInertia() {
		return this.inertia;
	}

	public float getInvInertia() {
		return this.invInertia;
	}

	public void setInvInertia(float invInertia) {
		this.invInertia = invInertia;
	}

	public float getAngularDamping() {
		return this.angularDamping;
	}

	public void setAngularDamping(float angularDamping) {
		this.angularDamping = angularDamping;
	}

	public boolean isTransformUpdateRequired() {
		return this.transformUpdateRequired;
	}

	public void setTransformUpdateRequired(boolean transformUpdateRequired) {
		this.transformUpdateRequired = transformUpdateRequired;
	}

	public boolean isAabbUpdateRequired() {
		return this.aabbUpdateRequired;
	}

	public void setAabbUpdateRequired(boolean aabbUpdateRequired) {
		this.aabbUpdateRequired = aabbUpdateRequired;
	}
}
