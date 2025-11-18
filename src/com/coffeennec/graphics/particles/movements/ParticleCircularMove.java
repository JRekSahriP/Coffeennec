package com.coffeennec.graphics.particles.movements;

import com.coffeennec.graphics.particles.Particle;
import com.coffeennec.math.Point2D;

public class ParticleCircularMove implements ParticleMovement {

	private float radius;

	public ParticleCircularMove(float radius) {
		this.radius = radius;
	}
	
	@Override
	public void move(Particle particle) {
		float angle = (float) Math.toRadians(particle.getAngle() + (particle.getAge() * particle.getSpeed()));
		
		Point2D pos = particle.getPosition();
		pos.x = particle.getInitialPosition().x + this.radius * (float) Math.cos(angle);
		pos.y = particle.getInitialPosition().y + this.radius * (float) Math.sin(angle);
	}

}
