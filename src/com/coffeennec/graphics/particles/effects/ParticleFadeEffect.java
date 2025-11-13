package com.coffeennec.graphics.particles.effects;

import java.util.concurrent.atomic.AtomicInteger;

import com.coffeennec.graphics.FennecColor;
import com.coffeennec.graphics.FennecColor.Hex;

/**
 * 0 -> .. -> 1 -> .. -> 2 -> ... -> END | Interpolates between colors smoothly
 */
public class ParticleFadeEffect implements ParticleEffect {

	@Override
	public Hex nextColor(Hex[] colors, AtomicInteger index) {
		int tempIndex = index.get();
		
		//index = ++index % (colors.length * 2)
		index.set(index.addAndGet(1) % (colors.length * 2));
				
		if (tempIndex >= colors.length * 2 - 1) {
			tempIndex = tempIndex % colors.length * 2 - 1;
		}
		
		if (tempIndex % 2 == 0) {
			return colors[tempIndex / 2];
		}
	
		int fadeIndex = tempIndex / 2;
		int nextIndex = (fadeIndex + 1) % colors.length;
	
		return FennecColor.blendColors(colors[fadeIndex], colors[nextIndex], 50);		
	}
	
}
