package com.coffeennec.graphics.particles.effects;

import java.util.concurrent.atomic.AtomicInteger;

import com.coffeennec.graphics.FennecColor.Hex;

/*
 * 0xFF000000
 */
public class ParticleNullEffect implements ParticleEffect {

	@Override
	public Hex nextColor(Hex[] colors, AtomicInteger index) {
		return new Hex(0xFF000000);
	}

}
