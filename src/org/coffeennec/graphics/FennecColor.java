package org.coffeennec.graphics;

public class FennecColor {

	public static Hex getRandomHex(boolean hasAlpha) {
	    int alpha = hasAlpha ? (int)(Math.random() * 256) : 255;
	    int red = (int) (Math.random() * 256);
	    int green = (int) (Math.random() * 256);
	    int blue = (int) (Math.random() * 256);
	    return new Hex(alpha, red, green, blue);
	}
	
	public static Hex blendColors(Hex color1, Hex color2, float percentage) {		
		int a1 = color1.getA();
		int a2 = color2.getA();
		
		int r1 = color1.getR();
		int r2 = color2.getR();
		
		int g1 = color1.getG();
		int g2 = color2.getG();
		
		int b1 = color1.getB();
		int b2 = color2.getB();
		
		float amount = percentage / 100.0f;
		float inversePercentage = 1 - amount;
		
		return new Hex(
						(int) Math.abs((inversePercentage * a1) + (amount * a2)),
						(int) Math.abs((inversePercentage * r1) + (amount * r2)),
						(int) Math.abs((inversePercentage * g1) + (amount * g2)),
						(int) Math.abs((inversePercentage * b1) + (amount * b2))
				);
	}
	
	public static Hex blendColors(int color1, int color2, int percentage) {
		return blendColors(new Hex(color1), new Hex(color2), percentage);
	}
	

	public static Hex[] getTransitionColors(Hex color1, Hex color2) {
		Hex[] colors = new Hex[100];

		for(int i = 0; i < 100; i++) {
			colors[i] = blendColors(color1, color2, i);
		}

		return colors;
	}
	public static Hex[] getTransitionColors(int color1, int color2) {
		return getTransitionColors(new Hex(color1), new Hex(color2));
	}


	public static int additive(int dst, int src) {
		int dstA = (dst >> 24) & 0xFF;
		int dstR = (dst >> 16) & 0xFF;
		int dstG = (dst >> 8) & 0xFF;
		int dstB = dst & 0xFF;

		int srcA = (src >> 24) & 0xFF;
		int srcR = (src >> 16) & 0xFF;
		int srcG = (src >> 8) & 0xFF;
		int srcB = src & 0xFF;

		int r = Math.min(255, dstR + srcR);
		int g = Math.min(255, dstG + srcG);
		int b = Math.min(255, dstB + srcB);
		int a = Math.min(255, dstA + srcA);

		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static int over(int dst, int src) {
		int srcA = (src >> 24) & 0xFF;
		if (srcA == 0) return dst;
		if (srcA == 255) return src;

		int dstA = (dst >> 24) & 0xFF;
		int dstR = (dst >> 16) & 0xFF;
		int dstG = (dst >> 8) & 0xFF;
		int dstB = dst & 0xFF;

		int srcR = (src >> 16) & 0xFF;
		int srcG = (src >> 8) & 0xFF;
		int srcB = src & 0xFF;

		int invSrcA = 255 - srcA;
		int outA = srcA + ((dstA * invSrcA) >> 8);

		if (outA == 0) return 0;

		int outR = (srcR * srcA + (dstR * dstA * invSrcA >> 8)) / outA;
		int outG = (srcG * srcA + (dstG * dstA * invSrcA >> 8)) / outA;
		int outB = (srcB * srcA + (dstB * dstA * invSrcA >> 8)) / outA;

		return (outA << 24) | (outR << 16) | (outG << 8) | outB;
	}

	
	public static class Hex {
		private int value;
		
		public Hex() {}
		public Hex(int r, int g, int b) {
			this(255, r, g, b);
		}
		public Hex(int a, int r, int g, int b) {
			this.set(a, r, g, b);
		}
		public Hex(int hex) {
			this.value = hex;
		}
		
		
		public int getA() {
			return (this.value >> 24) & 0xFF;
		}
		public int getR() {
			return (this.value >> 16) & 0xFF;
		}
		public int getG() {
			return (this.value >> 8) & 0xFF;
		}
		public int getB() {
			return (this.value) & 0xFF;
		}
		
		public void setA(int alpha) {
			this.value = (this.value & 0x00FFFFFF) | (alpha << 24);
		}
		public void setR(int red) {
			this.value = (this.value & 0xFF00FFFF) | (red << 16);
		}
		public void setG(int green) {
			this.value = (this.value & 0xFFFF00FF) | (green << 8);
		}
		public void setB(int blue) {
			this.value = (this.value & 0xFFFFFF00) | (blue);
		}
		public void set(int a, int r, int g, int b) {
		    this.setA(a);
		    this.setR(r);
		    this.setG(g);
		    this.setB(b);
		}
		

		public int getHex() {
			return this.value;
		}
		
		public void setHex(int hex) {
			this.value = hex;
		}
		
		public Hex blend(Hex other, float percentage) {
			return FennecColor.blendColors(this, other, percentage);
		}

		public Hex additive(Hex destination) {
			return new Hex(FennecColor.additive(destination.value, this.value));
		}

		public Hex over(Hex destination) {
			return new Hex(FennecColor.over(destination.value, this.value));
		}
		

	}
}
