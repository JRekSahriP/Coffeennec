package com.coffeennec.graphics.particles;

import com.coffeennec.game.abstractions.GameObjectHandler;

public class CoffeeParticles extends GameObjectHandler<Particle> {

	public void addParticles(Particle particle, int quantity) {
		for (int i = 0; i < quantity; i++) {
			add(new Particle(particle));
		}
	}
	
	@Override
	public void update() {
		super.update();
		getList().removeIf(Particle::isEnded);
	}
	
}
