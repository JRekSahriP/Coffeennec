package com.coffeennec.graphics.particles.effects;

import java.util.concurrent.atomic.AtomicInteger;

import com.coffeennec.graphics.FennecColor.Hex;
import com.coffeennec.math.FennecMath;

/*
 * Random
 */
public class ParticleRandomEffect implements ParticleEffect {

	@Override
	public Hex nextColor(Hex[] colors, AtomicInteger index) {
		return colors[(int) FennecMath.random(0, colors.length - 1)];
	}

}
