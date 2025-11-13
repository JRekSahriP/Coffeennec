package com.coffeennec.graphics.particles.effects;

import java.util.concurrent.atomic.AtomicInteger;

import com.coffeennec.graphics.FennecColor.Hex;

public interface ParticleEffect {
	Hex nextColor(Hex[] colors, AtomicInteger index);
}
