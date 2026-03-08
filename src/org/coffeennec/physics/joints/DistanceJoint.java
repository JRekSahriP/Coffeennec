package org.coffeennec.physics.joints;

import org.coffeennec.math.FennecMath;
import org.coffeennec.math.Point2D;
import org.coffeennec.physics.bodies.Body;

public class DistanceJoint extends Joint {
    private float targetDistance;
    private float stiffness;
    private float breakForce;
    
    public DistanceJoint(Body bodyA, Point2D localAnchorA, Body bodyB, Point2D localAnchorB, float targetDistance, float stiffness) {
        super(bodyA, localAnchorA, bodyB, localAnchorB);
        this.targetDistance = targetDistance;
        this.stiffness = stiffness;
    }

    @Override
    public void solve(float deltaTime) {
        Point2D a1 = this.getAnchorA();
        Point2D a2 = this.getAnchorB();

        Point2D diff = Point2D.subtract(a2, a1);
        float currentDistance = diff.length();

        if (currentDistance < FennecMath.EPSILON) {
        	return;
        }

        float error = currentDistance - this.targetDistance;
        Point2D normal = Point2D.normalize(diff);
       
        float totalInvMass = this.bodyA.getInvMass() + this.bodyB.getInvMass();
        if (totalInvMass <= 0) {
			return;
		}

        float impulseMag = error / totalInvMass * this.stiffness;


        if (this.breakForce != 0) {
        	float currentForce = Math.abs(impulseMag) / deltaTime;
        	
        	if (currentForce > this.breakForce) {
	            this.breakJoint();
	            return;
        	}
        }
        
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
    }
    
    
    public float getTargetDistance() {
    	return this.targetDistance;
    }
    public void setTargetDistance(float targetDistance) {
    	this.targetDistance = targetDistance;
    }
}