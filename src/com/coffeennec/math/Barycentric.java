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
		 this.x2 = points[2].x;
		 this.y2 = points[2].y;

		 this.y1y2 = points[1].y - this.y2;
		 this.x2x1 = this.x2 - points[1].x;
		 this.y2y0 = this.y2 - points[0].y;
		 this.x0x2 = points[0].x - this.x2;

		 this.invArea = 1.0 / (
				 (this.y1y2) * (this.x0x2) + 
				 (this.x2x1) * (points[0].y - this.y2)
				 );
	 }

	 public void setX(int x) {
		 this.x = x - this.x2;
	 }
	 public void setY(int y) {
		 this.y = y - this.y2;
	 }

	 public double[] getWeights() {
		 double[] weights = new double[3];
		 weights[0] = (this.y1y2 * this.x + this.x2x1 * this.y) * this.invArea;
		 weights[1] = (this.y2y0 * this.x + this.x0x2 * this.y) * this.invArea;
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
