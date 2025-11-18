package com.coffeennec.graphics.buffers;

import java.util.ArrayList;
import java.util.List;

import com.coffeennec.graphics.CoffeeFont;
import com.coffeennec.graphics.FennecColor.Hex;
import com.coffeennec.math.Barycentric;
import com.coffeennec.math.Point2D;
import com.coffeennec.strings.FennecString;

public class CoffeeRenderer {
	private CoffeeBuffer buffer;
	private CoffeeFont font;
	

	public CoffeeRenderer(CoffeeBuffer buffer, CoffeeFont font) {
		this.buffer = buffer;
		this.font = font;
	}
	public CoffeeRenderer(CoffeeBuffer buffer) {
		this(buffer, null);
	}
	
	
	
	public void drawLine(Point2D p1, Point2D p2, Hex color) {
		int deltaX = (int) Math.abs(p2.x - p1.x);
		int deltaY = (int) Math.abs(p2.y - p1.y);
		
		int addX = p1.x < p2.x ? 1 : -1; 
		int addY = p1.y < p2.y ? 1 : -1;
		
		int err = deltaX - deltaY;
		
		int x = (int) p1.x;
		int y = (int) p1.y;
		
		while (true) {
			if (!this.buffer.boundaryCheckScreen(x,y)) break;
			
			this.buffer.set(x, y, color.getHex());
			
			int e2 = 2 * err;
			if (e2 > -deltaY) {
				err -= deltaY;
				x += addX;
			}
			
			if (e2 < deltaX) {
				err += deltaX;
				y += addY;
			}
			
		    if (Math.abs(x - (int) p2.x) <= 1 && Math.abs(y - (int) p2.y) <= 1) break;
		        
		}
		
	}
	public void drawLine(int x1, int y1, int x2, int y2, Hex color) {
		this.drawLine(new Point2D(x1, y1), new Point2D(x2, y2), color);
	}
	
	private void drawRectangle(int x, int y, int width, int height, Hex color, boolean fill) {
		for (int i = x; i < x + width; i++) {
			for (int j = y; j < y + height; j++) {
				
				if (!fill &&(
						j != y && j != y + height - 1 &&
						i != x && i != x + width - 1)) {
					continue;
				}
				
				if (this.buffer.boundaryCheckScreen(i, j)) {
					this.buffer.set(i, j, color.getHex());
				}
			}
		}
	}
	
	public void drawRect(int x, int y, int width, int height, Hex color) {
		this.drawRectangle(x, y, width, height, color, false);
	}
	public void fillRect(int x, int y, int width, int height, Hex color) {
		this.drawRectangle(x, y, width, height, color, true);
	}
	
	private void drawEllipse(int x, int y, int width, int height, Hex color, boolean fill) {
		if (width == 0 || height == 0) {
			return;
		}
		
		final double hw = width / 2;
		final double hh = height / 2;
		final double tolerance = 1 / Math.max(hw/2, hh/2);
		
		for (int i = x; i < x + width; i++) {
			for (int j = y; j < y + height; j++) {

				
				double result = (Math.pow((i - x - hw) / hw,2) + Math.pow((j - y - hh) / hh, 2));

				if (result > 1) {
					continue;
				}
				
				if (!fill && result < 1 - tolerance) {
					continue;
				}
			
				
				if (this.buffer.boundaryCheckScreen(i, j)) {
					this.buffer.set(i, j, color.getHex());
				}
			}
		}
	}
	
	public void drawEllipse(int x, int y, int width, int height, Hex color) {
		this.drawEllipse(x, y, width, height, color, false);
	}
	public void fillEllipse(int x, int y, int width, int height, Hex color) {
		this.drawEllipse(x, y, width, height, color, true);
	}
	public void drawCircle(int x, int y, int diameter, Hex color) {
		this.drawEllipse(x, y, diameter, diameter, color, false);
	}
	public void fillCircle(int x, int y, int diameter, Hex color) {
		this.drawEllipse(x, y, diameter, diameter, color, true);
	}
		
	public void drawPolygon(Point2D[] points, Hex color) {
		for (int i = 0; i < points.length; i++) {
			this.drawLine(points[i], points[(i+1) % points.length], color);
		}
	}
	

	public void fillPolygon(Point2D[] points, Hex color) {
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for (Point2D p : points) {
			if (p.y < minY) minY = (int) p.y;
			if (p.y > maxY) maxY = (int) p.y;
		}
		
		for (int y = minY; y <= maxY; y++) {
			List<Integer> intersections = new ArrayList<>();
			
			for (int i = 0; i < points.length; i++) {
				Point2D p1 = points[i];
				Point2D p2 = points[(i + 1) % points.length];
				
				if (p1.y > p2.y) {
					Point2D temp = p1;
					p1 = p2;
					p2 = temp;
				}
				
				if (y >= p1.y && y < p2.y) {
					double intersectX = p1.x + (y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y); 
					intersections.add((int) Math.round(intersectX));
				}
			}
			
			intersections.sort(Integer::compareTo);
			
			for (int i = 0; i < intersections.size(); i += 2) {
				if (i + 1 < intersections.size()) {
					int startX = intersections.get(i);
					int endX = intersections.get(i + 1);
					for (int x = startX; x <= endX; x++) {
						this.buffer.set(x, y, color.getHex());
					}
				}
			}
		}
	}
	
	public void fillTexturedPolygon(Point2D[] vertices, Point2D[] uvs, CoffeeBuffer texture) {
	    Point2D[] triangleVertices = new Point2D[3];
	    Point2D[] triangleUvs = new Point2D[3];
	    int numTriangles = vertices.length - 2;
	    
	    for (int i = 0; i < numTriangles; i++) {
	        triangleVertices[0] = vertices[0];
	        triangleVertices[1] = vertices[i + 1];
	        triangleVertices[2] = vertices[i + 2];
	        triangleUvs[0] = uvs[0];
	        triangleUvs[1] = uvs[i + 1];
	        triangleUvs[2] = uvs[i + 2];
	        this.fillTexturedTriangle(triangleVertices, triangleUvs, texture);
	    }
	}
	public void fillTexturedTriangle(Point2D[] vertices, Point2D[] uvs, CoffeeBuffer texture) {
	    if (vertices.length != 3 || uvs.length != 3) {
	    	return;
	    }
		
		int minX = (int) Math.min(Math.min(vertices[0].x, vertices[1].x), vertices[2].x);
	    int minY = (int) Math.min(Math.min(vertices[0].y, vertices[1].y), vertices[2].y);
	    int maxX = (int) Math.max(Math.max(vertices[0].x, vertices[1].x), vertices[2].x);
	    int maxY = (int) Math.max(Math.max(vertices[0].y, vertices[1].y), vertices[2].y);
	    Barycentric bary = new Barycentric(vertices);

	    for (int y = minY; y <= maxY; y++) {
            bary.setY(y);
	        for (int x = minX; x <= maxX; x++) {
	            bary.setX(x);
	            
	            double[] weights = bary.getWeights();
	            
	            if (!Barycentric.isValid(weights)) {
	            	continue;
	            }
	        
	            double uvX = uvs[0].x * weights[0] + uvs[1].x * weights[1] + uvs[2].x * weights[2];
                double uvY = uvs[0].y * weights[0] + uvs[1].y * weights[1] + uvs[2].y * weights[2];
                int textureX = (int) (uvX * texture.getWidth());
                int textureY = (int) (uvY * texture.getHeight());
                if (this.buffer.boundaryCheckScreen(x, y)) {
                    this.buffer.set(x, y, texture.get(textureX, textureY));
                }
            
	        }
	    }
	}
		
	
	public void drawString(String text, int x, int y, Hex color) {
		if (this.font == null) {
			FennecString.eprintf("Cannot Write {}, because font is not definied.\nFont == null\n", text);
		}
		this.buffer.blit(this.font.drawText(text, color), x, y);
	}

	
}
