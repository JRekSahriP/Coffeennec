package org.coffeennec.graphics.particles.effects;

import java.util.concurrent.atomic.AtomicInteger;

import org.coffeennec.graphics.FennecColor.Hex;

/**
 * 0 -> 1 -> 2 -> ... -> END
 */
public class ParticleSequenceEffect implements ParticleEffect {

	@Override
	public Hex nextColor(Hex[] colors, AtomicInteger index) {
		Hex color = colors[index.get()];
		
		//index = ++index % (colors.length)
		index.set(index.addAndGet(1) % colors.length);
		return color;
	}
	
}
