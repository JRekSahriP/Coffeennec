package com.coffeennec.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.coffeennec.graphics.FennecColor.Hex;
import com.coffeennec.graphics.buffers.CoffeeBuffer;

public class CoffeeFont {
	private Font font;
	
	public CoffeeFont(String fontName, int style, int size) {
		this(new Font(fontName, style, size));	
	}
	public CoffeeFont(Font font) {
		this.font = font;	
	}
	
	public CoffeeBuffer drawText(String text, Hex color) {
		
		BufferedImage temp = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = temp.createGraphics();
		
		FontRenderContext frc =	g2d.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(text, frc);
		
        int width = (int) Math.ceil(bounds.getWidth());
        int height = (int) Math.ceil(bounds.getHeight());

        BufferedImage fontImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dFont = fontImage.createGraphics();

        g2dFont.setFont(font);
        g2dFont.setColor(new Color(color.getHex()));
        g2dFont.drawString(text, 0, (int) -bounds.getY());
        g2dFont.dispose();
     
        return new CoffeeBuffer(fontImage);
	}
	
}
