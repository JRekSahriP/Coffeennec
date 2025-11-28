package com.coffeennec.physics;

import com.coffeennec.game.abstractions.GameObjectHandler;
import com.coffeennec.math.Point2D;
import com.coffeennec.physics.bodies.Body;
import com.coffeennec.physics.collision.CollisionResolver;
import com.coffeennec.physics.data.CollisionData;

public class World2D extends GameObjectHandler<Body> {
	private Point2D gravity;
	private float time;
	private int iterations;
	
	public World2D(Point2D gravity, float time, int iterations) {
		super();
		this.gravity = gravity;
		this.time = time;
		this.iterations = iterations;
	}

	@Override
	public void update() {
		for (int iteration = 0; iteration < this.iterations; iteration++) {
			for (int i = 0; i < this.getBodyCount(); i++) {
				this.getList().get(i).step(this.time, this.iterations, this.gravity);
			}
			
			for (int i = 0; i < this.getBodyCount() - 1; i++) {
				Body bodyA = this.getList().get(i);
				for (int j = i + 1; j < this.getBodyCount(); j++) {
					Body bodyB = this.getList().get(j);					
					
					if (bodyA.isStatic() && bodyB.isStatic()) {
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
	
	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
}
