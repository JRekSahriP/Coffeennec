package com.coffeennec.graphics.buffers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.function.Predicate;

import com.coffeennec.graphics.CoffeeFont;
import com.coffeennec.strings.FennecString;

public class CoffeeBuffer {
	private int[] buffer;
	private final Dimension size;
	
	public CoffeeBuffer(BufferedImage image) {

		if(image.getRaster().getDataBuffer() instanceof DataBufferInt) {
			this.buffer = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		} else {
			this.buffer = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), this.buffer, 0, image.getWidth());
		}
		
		this.size = new Dimension(image.getWidth(), image.getHeight());
		
	}
	public CoffeeBuffer(int width, int height) {
		this.buffer = new int[width * height];
		this.size = new Dimension(width, height);
	}
	public CoffeeBuffer(CoffeeBuffer other) {
		this.buffer = other.buffer.clone();
		this.size = new Dimension(other.size);
	}
	
	public int get(int index) {
		if (!this.boundaryCheckBuffer(index)) {
			return -1;
		}
		
		return this.buffer[index];
	}
	public int get(int x, int y) {
		return this.get(y * this.getWidth() + x);
	}
	
	public void set(int index, int value) {
		if (!this.boundaryCheckBuffer(index)) {
			return;
		}
		
		this.buffer[index] = value;
	}
	public void set(int x, int y, int value) {
		this.set(y * this.getWidth() + x, value);
	}
	
	public void fill(int value) {
		Arrays.fill(this.buffer, value);
	}
	
	public CoffeeBuffer scale(int newWidth, int newHeight) {

		if (this.getWidth() == newWidth && this.getHeight() == newHeight) {
			return this;
		}
		
		if (newWidth == 0 || newHeight == 0) {
			FennecString.eprintf("Width[{}] or Height[{}] cannot be 0", newWidth, newHeight);
			return null;
		}

	    CoffeeBuffer scaledBuffer = new CoffeeBuffer(newWidth, newHeight);
	    double scaleX = newWidth / (double)this.getWidth();
	    double scaleY = newHeight / (double)this.getHeight();
	    for (int y = 0; y < newHeight; y++) {
	        for (int x = 0; x < newWidth; x++) {
	            double srcX = x / scaleX;
	            double srcY = y / scaleY;
	            int srcXInt = (int) srcX;
	            int srcYInt = (int) srcY;
	            if (this.boundaryCheckScreen(srcXInt, srcYInt)) {
	                scaledBuffer.set(x, y, this.get(srcXInt, srcYInt));
	            }
	        }
	    }
	    return scaledBuffer;
	}

	public CoffeeBuffer scale(double scale) {
		return this.scale((int)(this.getWidth() * scale), (int)(this.getHeight() * scale));
	}
	
	public CoffeeBuffer subBuffer(int x, int y, int width, int height) {
		if(width < 0 || width > this.getWidth()) {
	        FennecString.eprintf("Error: width [{}] is invalid. It should be between 0 and the source buffer's width [{}].\n", width, this.getWidth());
	    }
	    
	    if(height < 0 || height > this.getHeight()) {
	        FennecString.eprintf("Error: height [{}] is invalid. It should be between 0 and the source buffer's height [{}].\n", height, this.getHeight());
	    }
	    
	    
	    CoffeeBuffer output = new CoffeeBuffer(width, height);
		for (int yy = 0; yy < height; yy++) {
			int py = yy + y;
			for (int xx = 0; xx < width; xx++) {
				int px = xx + x;
				
				if (!this.boundaryCheckScreen(px, py)) {
					continue;
				}
				
				int rgb = this.get(px, py);
				output.set(xx, yy, rgb);
			}
		}
		return output;
	}

	
	/**
	 * Blit (block transfer) from a buffer to another.
	 * @param other
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param ignoreIf
	 */
	public void blit(CoffeeBuffer other, int x, int y, int width, int height, Predicate<Integer> ignoreIf) {
	    if(width < 0 || width > other.getWidth()) {
	        FennecString.eprintf("Error: width [{}] is invalid. It should be between 0 and the source buffer's width [{}].\n", width, other.getWidth());
	    }
	    
	    if(height < 0 || height > other.getHeight()) {
	        FennecString.eprintf("Error: height [{}] is invalid. It should be between 0 and the source buffer's height [{}].\n", height, other.getHeight());
	    }
		
		for(int yy = 0; yy < height; yy++) {
			int py = yy + y;
			for(int xx = 0; xx < width; xx++) {
				final int argb = other.get(xx, yy);
				
				int px = xx + x;
				
				if (ignoreIf.test(argb)) {
					continue;
				}
				if (!this.boundaryCheckScreen(px, py)) {
					continue;
				}
				
				this.set(px, py, argb);
			}
			
		}
	   
	}
	
	/**
	 * Blit (block transfer) from a buffer to another, ignoring transparent pixels.
	 * @param other
	 * @param x
	 * @param y
	 */
	public void blit(CoffeeBuffer other, int x, int y) {
		this.blit(other, x, y, other.size.width, other.size.height, c -> c == 0x00000000);
	}

	
	public BufferedImage toImage() {
		BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int[] imgBuffer = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		System.arraycopy(this.buffer, 0, imgBuffer, 0, this.buffer.length);
		return img;
	}
	
	/**
	 * Returns an object of CoffeeRenderer with current buffer as parameter.
	 * @param font
	 * @return a CoffeeRenderer object
	 */
	public CoffeeRenderer getRenderer(CoffeeFont font) {
		return new CoffeeRenderer(this, font);
	}
	
	/**
	 * Returns an object of CoffeeRenderer with current buffer as parameter using the default font ("Serif", Plain, 16).
	 * @return a CoffeeRenderer object
	 */
	public CoffeeRenderer getRenderer() {
		return this.getRenderer(new CoffeeFont(new Font("Serif", Font.PLAIN, 16)));
	}
	
	public boolean boundaryCheckScreen(int x, int y) {
		return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight();
	}
	
	public boolean boundaryCheckBuffer(int idx) {
		return idx >= 0 && idx < this.getBufferSize();
	}
	public boolean boundaryCheckBuffer(int x, int y) {
		return this.boundaryCheckBuffer(y * this.getWidth() + x);
	}

	
	public int getWidth() {
		return this.size.width;
	}
	public int getHeight() {
		return this.size.height;
	}
	public int getBufferSize() {
		return this.buffer.length;
	}
	public int[] getBuffer() {
		return this.buffer;
	}
	
}
