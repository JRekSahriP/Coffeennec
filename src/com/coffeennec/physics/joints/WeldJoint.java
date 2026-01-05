package com.coffeennec.physics.joints;

import com.coffeennec.math.FennecMath;
import com.coffeennec.math.Point2D;
import com.coffeennec.physics.bodies.Body;

public class WeldJoint extends Joint {
    private float referenceAngle; 
    private float stiffness;      

    public WeldJoint(Body bodyA, Point2D localAnchorA, Body bodyB, Point2D localAnchorB, float stiffness) {
        super(bodyA, localAnchorA, bodyB, localAnchorB);
        this.stiffness = stiffness;
        
        this.referenceAngle = bodyB.getRotation() - bodyA.getRotation();
    }

    @Override
    public void solve(float deltaTime) {
        
        Point2D a1 = this.getAnchorA();
        Point2D a2 = this.getAnchorB();

        Point2D diff = Point2D.subtract(a2, a1);
        float currentDistance = diff.length();

        if (currentDistance <= FennecMath.EPSILON) {
        	return;
        }
        Point2D normal = Point2D.normalize(diff);
        float totalInvMass = this.bodyA.getInvMass() + this.bodyB.getInvMass();

        if (totalInvMass <= 0) {
        	return;
        }

        float impulseMag = (currentDistance / totalInvMass) * this.stiffness;

        if (!this.bodyA.isStatic()) {
        	Point2D correctionA = Point2D.multiply(normal, impulseMag * this.bodyA.getInvMass());
        	this.bodyA.getLinearVelocity().add(correctionA);
        	this.bodyA.move(correctionA);
        }

        if (!this.bodyB.isStatic()) {
        	Point2D correctionB = Point2D.multiply(normal, -impulseMag * this.bodyB.getInvMass());
        	this.bodyB.getLinearVelocity().add(correctionB);
        	this.bodyB.move(correctionB);
        }
        
        float currentAngleDiff = this.bodyB.getRotation() - this.bodyA.getRotation();
        float angleError = currentAngleDiff - this.referenceAngle;

        
        while (angleError > Math.PI) {
			angleError -= 2 * Math.PI;
		}
        while (angleError < -Math.PI) {
			angleError += 2 * Math.PI;
		}

        float totalInvInertia = this.bodyA.getInvInertia() + this.bodyB.getInvInertia();

        if (totalInvInertia <= 0) {
        	return;
        }
            
        float angularImpulse = (angleError / totalInvInertia) * this.stiffness;

        if (!this.bodyA.isStatic()) {
        	this.bodyA.setRotation(this.bodyA.getRotation() + angularImpulse * this.bodyA.getInvInertia());
        }
        if (!this.bodyB.isStatic()) {
        	this.bodyB.setRotation(this.bodyB.getRotation() - angularImpulse * this.bodyB.getInvInertia());
        }

    }
}