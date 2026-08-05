package org.coffeennec.graphics.particles;

import org.coffeennec.game.abstractions.GameObjectHandler;
import org.coffeennec.game.contexts.GameContext;

public class CoffeeParticles extends GameObjectHandler<Particle> {

	public void addParticles(Particle particle, int quantity) {
		for (int i = 0; i < quantity; i++) {
			this.add(new Particle(particle));
		}
	}
	
	@Override
	public void update(GameContext ctx) {
		super.update(ctx);
		this.getList().removeIf(Particle::isEnded);
	}
	
}
