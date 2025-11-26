package com.coffeennec.physics;

import java.util.ArrayList;
import java.util.List;

import com.coffeennec.math.Point2D;
import com.coffeennec.math.data.RangeData;
import com.coffeennec.physics.data.AxisOverlapData;
import com.coffeennec.physics.data.CollisionData;

public class FennecCollisions {

	public static CollisionData intersectCircles(
			Point2D p1, float radius1,
			Point2D p2, float radius2) {

		CollisionData data = new CollisionData();

		float distance = Point2D.distance(p1, p2);
		float sumRadius = radius1 + radius2;

		if (distance >= sumRadius) {
			data.setResult(false);
			return data;
		}

		data.setResult(true);
		data.setNormal(Point2D.normalize(Point2D.subtract(p2, p1)));
		data.setDepth(sumRadius - distance);

		// p1 + ((p2 - p1) * radius1 / (radius1 + radius2))
		Point2D contactPoint = Point2D.sum(p1,
				Point2D.multiply(Point2D.subtract(p2, p1), 
						radius1 / (radius1 + radius2)));
		data.setContactPoint(contactPoint);
		return data;
	}

	public static CollisionData intersectCircleAndPolygon(
			Point2D circleCenter, float circleRadius,
			Point2D polygonCenter, Point2D[] polygonVertices) {

		CollisionData data = new CollisionData();
		data.setNormal(Point2D.zero());
		data.setDepth(Float.MAX_VALUE);


		Point2D axis = Point2D.zero();
		float axisDepth = 0f;

		RangeData verticesValues;
		RangeData circleValues;

		for (int i = 0; i < polygonVertices.length; i++) {
			Point2D va = polygonVertices[i];
			Point2D vb = polygonVertices[(i + 1) % polygonVertices.length];

			axis = FennecCollisions.calculateEdgeAxis(va, vb);
			
			verticesValues = FennecCollisions.projectVertices(polygonVertices, axis);
			circleValues = FennecCollisions.projectCircle(circleCenter, circleRadius, axis);

			float minA = verticesValues.getMin();
			float maxA = verticesValues.getMax();
			float minB = circleValues.getMin();
			float maxB = circleValues.getMax();

			if (minA >= maxB || minB >= maxA) {
				data.setResult(false);
				return data;
			}

			axisDepth = Math.min(maxB - minA, maxA - minB);

			if (axisDepth < data.getDepth()) {
				data.setDepth(axisDepth);
				data.setNormal(new Point2D(axis));
			}
			
		}

		int collisionPointIndex = FennecCollisions.findClosestPointOnPolygon(circleCenter, polygonVertices);
		Point2D collisionPoint = polygonVertices[collisionPointIndex];

		axis = Point2D.subtract(collisionPoint, circleCenter).normalize();

		verticesValues = FennecCollisions.projectVertices(polygonVertices, axis);
		circleValues = FennecCollisions.projectCircle(circleCenter, circleRadius, axis);

		float minA = verticesValues.getMin();
		float maxA = verticesValues.getMax();
		float minB = circleValues.getMin();
		float maxB = circleValues.getMax();

		if (minA >= maxB || minB >= maxA) {
			data.setResult(false);
			return data;
		}

		axisDepth = Math.min(maxB - minA, maxA - minB);

		if (axisDepth < data.getDepth()) {
			data.setDepth(axisDepth);
			data.setNormal(new Point2D(axis));
		}

		Point2D direction = Point2D.subtract(polygonCenter, circleCenter);
		data.setNormal(FennecCollisions.adjustNormal(direction, data.getNormal()));

		Point2D contactPoint = Point2D.sum(
				circleCenter,
				Point2D.multiply(data.getNormal(), -data.getDepth() + circleRadius)
				);
		
		data.setContactPoint(contactPoint);

		data.setResult(true);
		
		return data;
	}
	
	public static CollisionData intersectCircleAndPolygon(Point2D circleCenter, float radius, Point2D[] vertices) {
		Point2D polygonCenter = Point2D.findArithmeticMean(vertices);	
		return FennecCollisions.intersectCircleAndPolygon(circleCenter, radius, polygonCenter, vertices);
	}
	
	public static CollisionData intersectPolygons(
			Point2D centerA, Point2D[] verticesA,
			Point2D centerB, Point2D[] verticesB) {
		CollisionData data = new CollisionData();
		
		data.setNormal(Point2D.zero());
		data.setDepth(Float.MAX_VALUE);
		Point2D minPenetrationAxis = null;
		
		for (int i = 0; i < verticesA.length; i++) {
			Point2D p1 = verticesA[i];
			Point2D p2 = verticesA[(i + 1) % verticesA.length];
			Point2D axis = FennecCollisions.calculateEdgeAxis(p1, p2);
			
			AxisOverlapData overlap = FennecCollisions.checkAxisOverlap(verticesA, verticesB, axis);
			
			if (!overlap.isOverlapping()) {
				data.setResult(false);
				return data;
			}
			
			if (overlap.getDepth() < data.getDepth()) {
				data.setDepth(overlap.getDepth());
				minPenetrationAxis = overlap.getAxis();
			}
		}
		
		for (int i = 0; i < verticesB.length; i++) {
			Point2D p1 = verticesB[i];
			Point2D p2 = verticesB[(i + 1) % verticesB.length];
			Point2D axis = FennecCollisions.calculateEdgeAxis(p1, p2);
			
			AxisOverlapData overlap = FennecCollisions.checkAxisOverlap(verticesB, verticesB, axis);
			
			if (!overlap.isOverlapping()) {
				data.setResult(false);
				return data;
			}
			
			if (overlap.getDepth() < data.getDepth()) {
				data.setDepth(overlap.getDepth());
				minPenetrationAxis = overlap.getAxis();
			}
		}
		
		
		data.setNormal(minPenetrationAxis);
		
		Point2D direction = Point2D.subtract(centerB, centerA);
		data.setNormal(FennecCollisions.adjustNormal(direction, data.getNormal()));
		
		data.setContactPoints(FennecCollisions.findContactPoints(verticesA, verticesB, data.getNormal()));
		data.setResult(true);
		return data;
	}
	public static CollisionData intersectPolygons(Point2D[] verticesA, Point2D[] verticesB) {
		Point2D centerA = Point2D.findArithmeticMean(verticesA);
		Point2D centerB = Point2D.findArithmeticMean(verticesB);
		return FennecCollisions.intersectPolygons(centerA, verticesA, centerB, verticesB);
	}
	

	private static Point2D[] findContactPoints(Point2D[] verticesA, Point2D[] verticesB, Point2D normal) {
		List<Point2D> contacts = new ArrayList<>();

		for (Point2D v : verticesA) {
			if (FennecCollisions.isPointInsidePolygon(v, verticesB)) {
				contacts.add(v);
			}
		}

		for (Point2D v : verticesB) {
			if (FennecCollisions.isPointInsidePolygon(v, verticesA)) {
				contacts.add(v);
			}
		}

		for (int i = 0; i < verticesA.length; i++) {
			Point2D a1 = verticesA[i];
			Point2D a2 = verticesA[(i + 1) % verticesA.length];

			for (int j = 0; j < verticesB.length; j++) {
				Point2D b1 = verticesB[j];
				Point2D b2 = verticesB[(j + 1) % verticesB.length];

				Point2D intersection = FennecCollisions.getLineIntersection(a1, a2, b1, b2);
				if (intersection != null) {
					contacts.add(intersection);
				}
			}
		}

		if (contacts.isEmpty()) {
			FennecCollisions.findClosestPointsBetweenEdges(verticesA, verticesB, contacts);
		}

		return contacts.toArray(new Point2D[contacts.size()]);
	}

	private static void findClosestPointsBetweenEdges(Point2D[] polyA, Point2D[] polyB, List<Point2D> contacts) {
		float minDist = Float.MAX_VALUE;
		Point2D bestContact = null;

		for (int i = 0; i < polyA.length; i++) {
			Point2D a1 = polyA[i];
			Point2D a2 = polyA[(i + 1) % polyA.length];

			for (int j = 0; j < polyB.length; j++) {
				Point2D b1 = polyB[j];
				Point2D b2 = polyB[(j + 1) % polyB.length];

				Point2D[] closestPair = FennecCollisions.findClosestPointsBetweenSegments(a1, a2, b1, b2);
				float dist = Point2D.distance(closestPair[0], closestPair[1]);

				if (dist < minDist) {
					minDist = dist;
					bestContact = polyA[i];
				}
			}
		}

		if (bestContact != null) {
			contacts.add(bestContact);
		}
	}
	
	private static Point2D[] findClosestPointsBetweenSegments(Point2D a1, Point2D a2, Point2D b1, Point2D b2) {
		return new Point2D[]{
				FennecCollisions.closestPointOnSegment(a1, a2, b1),
				FennecCollisions.closestPointOnSegment(b1, b2, a1)
		};
	}

	private static Point2D closestPointOnSegment(Point2D segStart, Point2D segEnd, Point2D point) {
		Point2D segment = Point2D.subtract(segEnd, segStart);
		float segLengthSq = (float) Math.pow(segment.length(), 2);
		
		if (segLengthSq == 0) {
			return segStart;
		}

		float t = Math.max(0, Math.min(1, 
				Point2D.dot(Point2D.subtract(point, segStart), segment) / segLengthSq));

		return Point2D.sum(segStart, Point2D.multiply(segment, t));
	}


	private static RangeData projectCircle(Point2D center, float radius, Point2D axis) {
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;

		Point2D direction = Point2D.normalize(axis);
		Point2D directionAndRadius = Point2D.multiply(direction, radius);

		Point2D p1 = Point2D.sum(center, directionAndRadius); 
		Point2D p2 = Point2D.subtract(center, directionAndRadius);

		min = Point2D.dot(p1, axis);
		max = Point2D.dot(p2, axis);

		if (min > max) {
			float temp = min;
			min = max;
			max = temp;
		}

		return new RangeData(min, max);
	}

	private static RangeData projectVertices(Point2D[] vertices, Point2D axis) {
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;

		for (int i = 0; i < vertices.length; i++) {
			Point2D p = vertices[i];
			float project = Point2D.dot(p, axis);

			min = Math.min(min, project);
			max = Math.max(max, project);
		}

		return new RangeData(min, max);
	}

	private static int findClosestPointOnPolygon(Point2D point, Point2D[] vertices) {
		int index = -1;

		float minDistance = Float.MAX_VALUE;

		for (int i = 0; i < vertices.length; i++) {
			Point2D p = vertices[i];
			float distance = Point2D.distance(p, point);

			if (distance < minDistance) {
				minDistance = distance;
				index = i;
			}
		}

		return index;
	}

	private static Point2D adjustNormal(Point2D direction, Point2D normal) {
		if (Point2D.dot(direction, normal) < 0f) {
			return Point2D.inverse(normal);
		}
		return normal;
	}

	private static Point2D calculateEdgeAxis(Point2D p1, Point2D p2) {
		Point2D edge = Point2D.subtract(p2, p1);
		return new Point2D(-edge.y, edge.x).normalize();
	}
	
	private static AxisOverlapData checkAxisOverlap(Point2D[] verticesA, Point2D[] verticesB, Point2D axis) {
	    RangeData valuesA = FennecCollisions.projectVertices(verticesA, axis);
	    RangeData valuesB = FennecCollisions.projectVertices(verticesB, axis);

	    float minA = valuesA.getMin();
	    float maxA = valuesA.getMax();
	    float minB = valuesB.getMin();
	    float maxB = valuesB.getMax();

	    if (minA >= maxB || minB >= maxA) {
	        return new AxisOverlapData(false, 0, null);
	    }

	    float axisDepth = Math.min(maxB - minA, maxA - minB);
	    
	    return new AxisOverlapData(true, axisDepth, new Point2D(axis));
	}

	private static boolean isPointInsidePolygon(Point2D point, Point2D[] polygon) {
	    boolean inside = false;
	    
	    for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
	    	
	    	boolean isCrossingHorizontalLine = (polygon[i].y > point.y) != (polygon[j].y > point.y);

	    	float intersectionX = (polygon[j].x - polygon[i].x) * (point.y - polygon[i].y) /
	    	                        (polygon[j].y - polygon[i].y) + polygon[i].x;

	    	boolean isPointToTheLeft = point.x < intersectionX;
	    	
	        if (isCrossingHorizontalLine && isPointToTheLeft) {
	            inside = !inside;
	        }
	    }
	    
	    return inside;
	}

	private static Point2D getLineIntersection(Point2D a1, Point2D a2, Point2D b1, Point2D b2) {
	    float det = (a2.x - a1.x) * (b2.y - b1.y) - (a2.y - a1.y) * (b2.x - b1.x);
	    
	    if (det == 0) {
			return null;
		}
	    
	    float t = ((b1.x - a1.x) * (b2.y - b1.y) - (b1.y - a1.y) * (b2.x - b1.x)) / det;
	    float u = -((a2.x - a1.x) * (b1.y - a1.y) - (a2.y - a1.y) * (b1.x - a1.x)) / det;
	    
	    if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
	        return new Point2D(
	            a1.x + t * (a2.x - a1.x),
	            a1.y + t * (a2.y - a1.y)
	        );
	    }
	    
	    return null;
	}

}
