package com.coffeennec.graphics.particles.effects;

import java.util.concurrent.atomic.AtomicInteger;

import com.coffeennec.graphics.FennecColor.Hex;

/**
 * 0 -> 1 -> 2 -> ... -> END -> ... -> 2 -> 1 -> 0
 */
public class ParticleBounceEffect implements ParticleEffect {

	@Override
	public Hex nextColor(Hex[] colors, AtomicInteger index) {
		int bounceIndex = index.get() % (2 * colors.length - 2);
		if (bounceIndex >= colors.length) {
            bounceIndex = 2 * colors.length - 2 - bounceIndex;
        }
		
		Hex color = colors[bounceIndex];
		
		//index = ++index % (colors.length * 2)
		index.set(index.addAndGet(1) % (colors.length * 2));
		return color;
	}

}
