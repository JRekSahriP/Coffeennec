package com.coffeennec.math;

public class Barycentric {
	 private final double invArea;

	 private final double y1y2;
	 private final double x2x1;
	 private final double y2y0;
	 private final double x0x2;

	 private final double x2;
	 private final double y2;

	 private double x;
	 private double y;

	 public Barycentric(Point2D[] points) {
		 x2 = points[2].x;
		 y2 = points[2].y;

		 y1y2 = points[1].y - y2;
		 x2x1 = x2 - points[1].x;
		 y2y0 = y2 - points[0].y;
		 x0x2 = points[0].x - x2;

		 invArea = 1.0 / (
				 (y1y2) * (x0x2) + 
				 (x2x1) * (points[0].y - y2)
				 );
	 }

	 public void setX(int x) {
		 this.x = x - x2;
	 }
	 public void setY(int y) {
		 this.y = y - y2;
	 }

	 public double[] getWeights() {
		 double[] weights = new double[3];
		 weights[0] = (y1y2 * x + x2x1 * y) * invArea;
		 weights[1] = (y2y0 * x + x0x2 * y) * invArea;
		 weights[2] = 1 - weights[0] - weights[1];
		 return weights;
	 }


	 public static boolean isValid(double[] weights) {
		 for (double weight : weights) {
			 if (weight < 0) return false;
		 }
		 return true;
	 }
}
