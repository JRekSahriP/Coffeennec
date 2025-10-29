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
			buffer = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		} else {
			buffer = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), buffer, 0, image.getWidth());
		}
		
		size = new Dimension(image.getWidth(), image.getHeight());
		
	}
	public CoffeeBuffer(int width, int height) {
		buffer = new int[width * height];
		size = new Dimension(width, height);
	}
	
	
	public int get(int index) {
		if (!boundaryCheckBuffer(index)) {
			return -1;
		}
		
		return buffer[index];
	}
	public int get(int x, int y) {
		return get(y * getWidth() + x);
	}
	
	public void set(int index, int value) {
		if (!boundaryCheckBuffer(index)) {
			return;
		}
		
		buffer[index] = value;
	}
	public void set(int x, int y, int value) {
		set(y * getWidth() + x, value);
	}
	
	public void fill(int value) {
		Arrays.fill(buffer, value);
	}
	
	public CoffeeBuffer scale(int newWidth, int newHeight) {

		if (getWidth() == newWidth && getHeight() == newHeight) {
			return this;
		}
		
		if (newWidth == 0 || newHeight == 0) {
			FennecString.eprintf("Width[{}] or Height[{}] cannot be 0", newWidth, newHeight);
			return null;
		}

	    CoffeeBuffer scaledBuffer = new CoffeeBuffer(newWidth, newHeight);
	    double scaleX = newWidth / (double)getWidth();
	    double scaleY = newHeight / (double)getHeight();
	    for (int y = 0; y < newHeight; y++) {
	        for (int x = 0; x < newWidth; x++) {
	            double srcX = x / scaleX;
	            double srcY = y / scaleY;
	            int srcXInt = (int) srcX;
	            int srcYInt = (int) srcY;
	            if (boundaryCheckScreen(srcXInt, srcYInt)) {
	                scaledBuffer.set(x, y, get(srcXInt, srcYInt));
	            }
	        }
	    }
	    return scaledBuffer;
	}

	public CoffeeBuffer scale(double scale) {
		return scale((int)(getWidth() * scale), (int)(getHeight() * scale));
	}
	
	public CoffeeBuffer subBuffer(int x, int y, int width, int height) {
		if(width < 0 || width > getWidth()) {
	        FennecString.eprintf("Error: width [{}] is invalid. It should be between 0 and the source buffer's width [{}].\n", width, getWidth());
	    }
	    
	    if(height < 0 || height > getHeight()) {
	        FennecString.eprintf("Error: height [{}] is invalid. It should be between 0 and the source buffer's height [{}].\n", height, getHeight());
	    }
	    
	    
	    CoffeeBuffer output = new CoffeeBuffer(width, height);
		for (int yy = 0; yy < height; yy++) {
			int py = yy + y;
			for (int xx = 0; xx < width; xx++) {
				int px = xx + x;
				
				if (!boundaryCheckScreen(px, py)) {
					continue;
				}
				
				int rgb = get(px, py);
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
				if (!boundaryCheckScreen(px, py)) {
					continue;
				}
				
				set(px, py, argb);
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
		blit(other, x, y, other.size.width, other.size.height, c -> c == 0x00000000);
	}

	
	public BufferedImage toImage() {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		int[] imgBuffer = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		System.arraycopy(buffer, 0, imgBuffer, 0, buffer.length);
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
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
	
	public boolean boundaryCheckBuffer(int idx) {
		return idx >= 0 && idx < getBufferSize();
	}
	public boolean boundaryCheckBuffer(int x, int y) {
		return boundaryCheckBuffer(y * getWidth() + x);
	}

	
	public int getWidth() {
		return size.width;
	}
	public int getHeight() {
		return size.height;
	}
	public int getBufferSize() {
		return buffer.length;
	}
	public int[] getBuffer() {
		return buffer;
	}
	
}
