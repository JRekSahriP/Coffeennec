package org.coffeennec.physics;

import java.util.ArrayList;
import java.util.List;

import org.coffeennec.game.abstractions.GameObjectHandler;
import org.coffeennec.math.Point2D;
import org.coffeennec.physics.bodies.Body;
import org.coffeennec.physics.collision.CollisionResolver;
import org.coffeennec.physics.data.CollisionData;
import org.coffeennec.physics.joints.Joint;

public class World2D extends GameObjectHandler<Body> {

	private List<Joint> joints;
	
	private Point2D gravity;
	private float time;
	private int iterations;
	
	public World2D(Point2D gravity, float time, int iterations) {
		super();
		this.gravity = gravity;
		this.time = time;
		this.iterations = iterations;
		this.joints = new ArrayList<>();
	}

	@Override
	public void update() {
		for (int iteration = 0; iteration < this.iterations; iteration++) {
			this.updateBodies();
			this.updateJoints();
		}
	}
	
	public void updateBodies() {
		List<Body> list = this.getList();
		
		for (int i = 0; i < this.getBodyCount(); i++) {
			list.get(i).step(this.time, this.iterations, this.gravity);
		}
		
		for (int i = 0; i < this.getBodyCount() - 1; i++) {
			Body bodyA = list.get(i);
			for (int j = i + 1; j < this.getBodyCount(); j++) {
				Body bodyB = list.get(j);					
				
				if (bodyA.isStatic() && bodyB.isStatic()) {
					continue;
				}
	
				if (!Body.shouldCollide(bodyA, bodyB)) {
					continue;
				}
				
				CollisionData data = CollisionResolver.collide(bodyA, bodyB);
				
				if (!data.getResult()) {
					continue;
				}
				

				if (bodyA.isStatic()) {
					
					Point2D amount = Point2D.multiply(data.getNormal(), data.getDepth());
					bodyB.move(amount);
				
				} else if (bodyB.isStatic()) {
				
					Point2D amount = Point2D.multiply(Point2D.inverse(data.getNormal()), data.getDepth());
					bodyA.move(amount);
				
				} else {
				
					Point2D amountA = Point2D.multiply(Point2D.inverse(data.getNormal()), data.getDepth());
					amountA.divide(2f);

					Point2D amountB = Point2D.multiply(data.getNormal(), data.getDepth());
					amountB.divide(2f);

					bodyA.move(amountA);
					bodyB.move(amountB);
				
				}
				
				CollisionResolver.resolveCollision(bodyA, bodyB, data);
			}	
		}
	}
		
	private void updateJoints() {
		this.joints.forEach(j -> j.solve(this.time));
		this.joints.removeIf(j -> j.isBroke());
	}
	
	public void removeBody(int index) {
		this.getList().remove(index);
	}

	public Body getBody(int index) {
		return this.getCopyList().get(index);
	}
	
	public int getBodyCount() {
		return this.getCopyList().size();
	}
	
	
	public void addJoint(Joint joint) {
		this.joints.add(joint);
		
	}
	public void addJointIfAbsent(Joint joint) {
		if (this.joints.contains(joint)) {
			return;
		}
		this.joints.add(joint);
	}
	
	public void removeJoint(Joint joint) {
		this.joints.remove(joint);
	}
	
	public Joint getJoint(int index) {
		return this.joints.get(index);
	}
	public int getJointCount() {
		return this.joints.size();
	}
	
	public float getTime() {
		return this.time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public int getIterations() {
		return this.iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
}
