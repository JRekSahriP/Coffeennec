package com.coffeennec.graphics.particles.movements;

import com.coffeennec.graphics.particles.Particle;
import com.coffeennec.math.Point2D;

public class ParticleWaveMove implements ParticleMovement {

	private float waveAmplitude;
	private float waveFrequency;
	
	public ParticleWaveMove(float waveAmplitude, float waveFrequency) {
		this.waveAmplitude = waveAmplitude;
		this.waveFrequency = waveFrequency;
	}

	@Override
	public void move(Particle particle) {
		float radians = (float) Math.toRadians(particle.getAngle());
		
		float deltaX = (float) (Math.cos(radians) * particle.getSpeed());
		float deltaY = (float) (Math.sin(radians) * particle.getSpeed());
		
		float perpendicularRadius = (float) (radians + Math.PI / 2.0);
		float oscillation = (float) (waveAmplitude * Math.sin(particle.getAge() * waveFrequency));
		
		Point2D pos = particle.getPosition();
		pos.x += deltaX + Math.cos(perpendicularRadius) * oscillation;
		pos.y += deltaY + Math.sin(perpendicularRadius) * oscillation;
	}

}
