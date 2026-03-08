package org.coffeennec.physics.joints;

import org.coffeennec.math.Point2D;
import org.coffeennec.physics.bodies.Body;

public class RopeJoint extends Joint {
    private float maxLength;
    private float stiffness;
    private float breakForce;
    
    public RopeJoint(Body bodyA, Point2D localAnchorA, Body bodyB, Point2D localAnchorB, float maxLength, float stiffness, float breakForce) {
        super(bodyA, localAnchorA, bodyB, localAnchorB);
        this.maxLength = maxLength;
        this.stiffness = stiffness;
        this.breakForce = breakForce;
    }

    @Override
    public void solve(float deltaTime) {
        Point2D a1 = this.getAnchorA();
        Point2D a2 = this.getAnchorB();

        Point2D diff = Point2D.subtract(a2, a1);
        float currentDistance = diff.length();

        if (currentDistance <= this.maxLength) {
            return; 
        }

        float error = currentDistance - this.maxLength;
        Point2D normal = Point2D.normalize(diff);
        
        float totalInvMass = this.bodyA.getInvMass() + this.bodyB.getInvMass();
        if (totalInvMass <= 0) {
			return;
		}

        float impulseMag = (error / totalInvMass) * this.stiffness;


        if (this.breakForce != 0) {
        	float currentForce = Math.abs(impulseMag) / deltaTime;
            
        	if (currentForce > this.breakForce) {
	        	this.breakJoint();
	            return;
        	}
        }
        
        if (!this.bodyA.isStatic()) {
            Point2D velA = Point2D.multiply(normal, impulseMag * this.bodyA.getInvMass());
            this.bodyA.getLinearVelocity().add(velA);
            this.bodyA.move(velA);
        }

        if (!this.bodyB.isStatic()) {
            Point2D velB = Point2D.multiply(normal, -impulseMag * this.bodyB.getInvMass());
            this.bodyB.getLinearVelocity().add(velB);
            this.bodyB.move(velB);
        }
    }
}