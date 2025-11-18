package com.coffeennec.graphics;

import java.awt.Dimension;
import java.util.Arrays;

import com.coffeennec.game.time.Cooldown;
import com.coffeennec.graphics.buffers.CoffeeBuffer;

public class SpriteSheet {
	private CoffeeBuffer spriteSheet;
	private CoffeeBuffer lastFrame;
	private Dimension defaultFrameSize;
	
	private Cooldown cd;
	
	private int[] maxFrames;
	private int frame;
	private int yPos;
	
	public SpriteSheet(CoffeeBuffer spriteSheet, int frameWidth, int frameHeight, long frameDelay) {
		this.spriteSheet = new CoffeeBuffer(spriteSheet);
		this.defaultFrameSize = new Dimension(frameWidth, frameHeight);
		
		int maxFramesX = spriteSheet.getWidth() / frameWidth;
		int maxFramesY = spriteSheet.getHeight() / frameHeight;
		this.maxFrames = new int[maxFramesY];
		Arrays.fill(this.maxFrames, maxFramesX);
		
		this.cd = new Cooldown(frameDelay);
		
		this.frame = 0;
		this.yPos = 0;
	}
	
	public CoffeeBuffer nextSprite() {
		if (this.cd.isInCooldown()) {
			return new CoffeeBuffer(this.lastFrame);
		}
		
		final Dimension s = this.defaultFrameSize;
		CoffeeBuffer sprite = new CoffeeBuffer(s.width, s.height);
		int x = this.frame % this.maxFrames[this.yPos] * s.width;
		int y = this.yPos * s.height;
		
		sprite.blit(this.spriteSheet.subBuffer(x, y, sprite.getWidth(), sprite.getHeight()), 0, 0);
		this.lastFrame = sprite;
		
		this.frame++;
		this.cd.use();
		return sprite;
	}
	
	/**
	 * Sets the current row of the sprite sheet.
	 * If the new row is different from the current row,
	 * the frame counter is reset to 0 and the cooldown is reset.
	 * @param row - The new row index
	 */
	public void setRow(int row) {
		if (this.yPos != row) {
			this.frame = 0;
			this.cd.reset();
		}
		this.yPos = row;
	}
	
	/**
	 * Sets the current Y position of the sprite sheet.
	 * @param yPos - The new row index
	 */
	public void setYPos(int yPos) {
		this.yPos = yPos;
	}
	
	public void setMaxFrames(int[] maxFrames) {
		this.maxFrames = maxFrames;
	}
	
	public void setMaxVerticalFrames(int maxVerticalFrames) {
		int maxFramesX = this.spriteSheet.getWidth() / this.defaultFrameSize.width;
		this.maxFrames = new int[maxVerticalFrames];
		Arrays.fill(this.maxFrames, maxFramesX);
	}
	public void setMaxHorizontalFrames(int row, int maxHorizontalFrames) {
		this.maxFrames[row] = maxHorizontalFrames;
	}

	public void setDelay(int delay) {
		this.cd.setDuration(delay);
	}
	
	public long getDelay() {
		return this.cd.getDuration();
	}
	
	public int getCurrentRow() {
		return this.yPos;
	}
	
}
