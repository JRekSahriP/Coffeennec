package com.coffeennec.physics.joints;

import com.coffeennec.math.Point2D;
import com.coffeennec.physics.bodies.Body;

public class RevoluteJoint extends Joint {
    private float stiffness; 

    public RevoluteJoint(Body bodyA, Point2D localAnchorA, Body bodyB, Point2D localAnchorB, float stiffness) {
        super(bodyA, localAnchorA, bodyB, localAnchorB);
        this.stiffness = stiffness;
    }

    @Override
    public void solve(float deltaTime) {
    	Point2D rA = this.rotateAround(this.localAnchorA, this.bodyA.getRotation());
        Point2D rB = this.rotateAround(this.localAnchorB, this.bodyB.getRotation());

        Point2D worldAnchorA = Point2D.sum(this.bodyA.getPosition(), rA);
        Point2D worldAnchorB = Point2D.sum(this.bodyB.getPosition(), rB);

        Point2D error = Point2D.subtract(worldAnchorB, worldAnchorA);
        
        float angularTermA = (rA.x * rA.x + rA.y * rA.y) * this.bodyA.getInvInertia();
        float angularTermB = (rB.x * rB.x + rB.y * rB.y) * this.bodyB.getInvInertia();
        
        float totalInvMass = this.bodyA.getInvMass() + this.bodyB.getInvMass() + angularTermA + angularTermB;

        if (totalInvMass <= 0) {
			return;
		}
        
        Point2D impulse = Point2D.multiply(error, this.stiffness / totalInvMass);

        if (!this.bodyA.isStatic()) {            
            Point2D correctionA = Point2D.multiply(impulse, this.bodyA.getInvMass());
            this.bodyA.move(correctionA);
            this.bodyA.getLinearVelocity().add(Point2D.multiply(correctionA, 1.0f / deltaTime));

            float torqueA = (rA.x * impulse.y - rA.y * impulse.x) * this.bodyA.getInvInertia();
            this.bodyA.setRotation(this.bodyA.getRotation() + torqueA);
        }
        
        if (!this.bodyB.isStatic()) {
            Point2D correctionB = Point2D.multiply(impulse, -this.bodyB.getInvMass());
            this.bodyB.move(correctionB);
            this.bodyB.getLinearVelocity().add(Point2D.multiply(correctionB, 1.0f / deltaTime));
            
            float torqueB = (rB.x * -impulse.y - rB.y * -impulse.x) * this.bodyB.getInvInertia();
            this.bodyB.setRotation(this.bodyB.getRotation() + torqueB);
        }
    }
}