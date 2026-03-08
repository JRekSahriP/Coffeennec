package org.coffeennec.graphics.particles.effects;

import java.util.concurrent.atomic.AtomicInteger;

import org.coffeennec.graphics.FennecColor.Hex;
import org.coffeennec.math.FennecMath;

/*
 * Random
 */
public class ParticleRandomEffect implements ParticleEffect {

	@Override
	public Hex nextColor(Hex[] colors, AtomicInteger index) {
		return colors[(int) FennecMath.random(0, colors.length - 1)];
	}

}
