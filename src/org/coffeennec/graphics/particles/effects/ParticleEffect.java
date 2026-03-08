package org.coffeennec.graphics.particles.effects;

import java.util.concurrent.atomic.AtomicInteger;

import org.coffeennec.graphics.FennecColor.Hex;

public interface ParticleEffect {
	Hex nextColor(Hex[] colors, AtomicInteger index);
}
