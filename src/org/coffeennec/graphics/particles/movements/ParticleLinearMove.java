package org.coffeennec.graphics.particles.movements;

import org.coffeennec.graphics.particles.Particle;
import org.coffeennec.math.Point2D;

public class ParticleLinearMove implements ParticleMovement {

	@Override
	public void move(Particle particle) {
		float radians = (float) Math.toRadians(particle.getAngle());
		
		Point2D pos = particle.getPosition();
		pos.x += Math.cos(radians) * particle.getSpeed();
		pos.y += Math.sin(radians) * particle.getSpeed();
	}

}
