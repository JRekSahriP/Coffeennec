package com.coffeennec.graphics;

public class FennecColor {

	public static Hex getRandomHex(boolean hasAlpha) {
	    int alpha = hasAlpha ? (int)(Math.random() * 256) : 255;
	    int red = (int)(Math.random() * 256);
	    int green = (int)(Math.random() * 256);
	    int blue = (int)(Math.random() * 256);
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

	
	public static class Hex {
		private int value;
		
		public Hex() {}
		public Hex(int r, int g, int b) {
			this(255, r, g, b);
		}
		public Hex(int a, int r, int g, int b) {
			set(a, r, g, b);
		}
		public Hex(int hex) {
			value = hex;
		}
		
		
		public int getA() {
			return (value >> 24) & 0xFF;
		}
		public int getR() {
			return (value >> 16) & 0xFF;
		}
		public int getG() {
			return (value >> 8) & 0xFF;
		}
		public int getB() {
			return (value) & 0xFF;
		}
		
		public void setA(int alpha) {
			value = (value & 0x00FFFFFF) | (alpha << 24);
		}
		public void setR(int red) {
			value = (value & 0xFF00FFFF) | (red << 16);
		}
		public void setG(int green) {
			value = (value & 0xFFFF00FF) | (green << 8);
		}
		public void setB(int blue) {
			value = (value & 0xFFFFFF00) | (blue);
		}
		public void set(int a, int r, int g, int b) {
		    setA(a);
		    setR(r);
		    setG(g);
		    setB(b);
		}
		

		public int getHex() {
			return value;
		}
		
		public void setHex(int hex) {
			value = hex;
		}
		

	}
}
