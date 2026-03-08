package org.coffeennec.physics.collision;

import org.coffeennec.math.FennecMath;
import org.coffeennec.math.Point2D;
import org.coffeennec.physics.bodies.Body;
import org.coffeennec.physics.data.CollisionData;

public class CollisionResolver {

	/**
	 * The minimum absolute value of the angular impulse required to apply a
	 * change in rotational velocity to a body during collision resolution.
	 */
	public static float minAngularImpulse = 0.001f;

	
	public static CollisionData collide(Body bodyA, Body bodyB) {
		return bodyA.collideWith(bodyB);
	}
	
	
	public static void resolveCollision(Body bodyA, Body bodyB, CollisionData data) {
		Point2D normal = data.getNormal();
		
		Point2D[] contactPoints = data.getContactPoints();
		Point2D contactPoint;
		
		if (contactPoints == null || contactPoints.length == 0) {
			return;
		}
		
		if (contactPoints.length == 1) {
			contactPoint = contactPoints[0];
		} else {
			contactPoint = Point2D.findArithmeticMean(contactPoints);
		}
		
		Point2D rA = Point2D.subtract(contactPoint, bodyA.getPosition());
		Point2D rB = Point2D.subtract(contactPoint, bodyB.getPosition());
		
		Point2D velocityA = bodyA.getLinearVelocityWithRotation(rA);
		Point2D velocityB = bodyB.getLinearVelocityWithRotation(rB);
		Point2D relativeVelocity = Point2D.subtract(velocityB, velocityA);
		
		float relativeVelocityNormal = Point2D.dot(relativeVelocity, normal);
		if (relativeVelocityNormal > 0f) {
			return;
		}
			
		float rAcrossNormalA = Point2D.cross(rA, normal);
		float rAcrossNormalB = Point2D.cross(rB, normal);
		
		float denominator = 
				bodyA.getInvMass() + bodyB.getInvMass() +
				(rAcrossNormalA * rAcrossNormalA) * bodyA.getInvInertia() +
				(rAcrossNormalB * rAcrossNormalB) * bodyB.getInvInertia();
		
		if (Math.abs(denominator) < FennecMath.EPSILON) {
			return;
		}
		
		float minRestitution = Math.min(bodyA.getRestitution(), bodyB.getRestitution());
		
		float impulseMagnitude = (-(1.0f + minRestitution) * relativeVelocityNormal) / denominator;
		
		Point2D impulse = Point2D.multiply(normal, impulseMagnitude);
		
		bodyA.getLinearVelocity().subtract(Point2D.multiply(impulse, bodyA.getInvMass()));
		bodyB.getLinearVelocity().add(Point2D.multiply(impulse, bodyB.getInvMass()));
	
		float angularImpulseA = Point2D.cross(rA, impulse);
		float angularImpulseB = Point2D.cross(rB, impulse);
		
		if (Math.abs(angularImpulseA) > CollisionResolver.minAngularImpulse && bodyA.canRotate()) {
			float rotVelocity = bodyA.getRotationVelocity();
			bodyA.setRotationVelocity(rotVelocity - angularImpulseA * bodyA.getInvInertia());
		}
		
		if (Math.abs(angularImpulseB) > CollisionResolver.minAngularImpulse && bodyB.canRotate()) {
			float rotVelocity = bodyB.getRotationVelocity();
			bodyB.setRotationVelocity(rotVelocity + angularImpulseB * bodyB.getInvInertia());			
		}

		//friction
		Point2D velocityA2 = bodyA.getLinearVelocityWithRotation(rA);
		Point2D velocityB2 = bodyB.getLinearVelocityWithRotation(rB);
		Point2D relativeVelocity2 = Point2D.subtract(velocityB2, velocityA2);
		
		Point2D tangent = new Point2D(-normal.y, normal.x);
		float relativeVelocityTangent = Point2D.dot(relativeVelocity2, tangent);
		
		float rAcrossTangentA = Point2D.cross(rA, tangent);
		float rAcrossTangentB = Point2D.cross(rB, tangent);
		
		float frictionDenominator = 
				bodyA.getInvMass() + bodyB.getInvMass() +
				(rAcrossTangentA * rAcrossTangentA) * bodyA.getInvInertia() +
				(rAcrossTangentB * rAcrossTangentB) * bodyB.getInvInertia();
		
		if (Math.abs(frictionDenominator) < FennecMath.EPSILON) {
			return;
		}
		
		float frictionImpulseMagnitude = -relativeVelocityTangent / frictionDenominator;

		float minFriction = Math.min(bodyA.getFriction(), bodyB.getFriction());
		float maxFriction = minFriction * impulseMagnitude;

		frictionImpulseMagnitude = FennecMath
				.clamp(frictionImpulseMagnitude, -maxFriction, maxFriction);
		
		Point2D frictionImpulse = Point2D.multiply(tangent, frictionImpulseMagnitude);
		
		bodyA.getLinearVelocity().subtract(Point2D.multiply(frictionImpulse, bodyA.getInvMass()));
		bodyB.getLinearVelocity().add(Point2D.multiply(frictionImpulse, bodyB.getInvMass()));
			
		float angularFrictionImpulseA = Point2D.cross(rA, frictionImpulse);
		float angularFrictionImpulseB = Point2D.cross(rB, frictionImpulse);
	
		if (Math.abs(angularFrictionImpulseA) > CollisionResolver.minAngularImpulse && bodyA.canRotate()) {
			float rotVelocity = bodyA.getRotationVelocity();
			bodyA.setRotationVelocity(rotVelocity - angularFrictionImpulseA * bodyA.getInvInertia());
		}
		
		if (Math.abs(angularFrictionImpulseB) > CollisionResolver.minAngularImpulse && bodyB.canRotate()) {
			float rotVelocity = bodyB.getRotationVelocity();
			bodyB.setRotationVelocity(rotVelocity + angularFrictionImpulseB * bodyB.getInvInertia());			
		}

		
	}
	
}
