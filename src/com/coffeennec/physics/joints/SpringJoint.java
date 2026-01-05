package com.coffeennec.physics.joints;

import com.coffeennec.math.Point2D;
import com.coffeennec.physics.bodies.Body;

public class SpringJoint extends Joint {
    private float restLength;
    private float springConstant;
    private float damping;

    /**
     * @param restLength     The natural length of the spring in a state of equilibrium.
     * @param springConstant The stiffness of the spring; controls how strongly it pulls/pushes.
     * @param damping        The resistance to motion; prevents excessive oscillation.
     */
    public SpringJoint(Body bodyA, Point2D localAnchorA, Body bodyB, Point2D localAnchorB, float restLength, float springConstant, float damping) {
        super(bodyA, localAnchorA, bodyB, localAnchorB);
        this.restLength = restLength;
        this.springConstant = springConstant;
        this.damping = damping;
    }

    @Override
    public void solve(float deltaTime) {
        Point2D a1 = this.getAnchorA();
        Point2D a2 = this.getAnchorB();

        Point2D diff = Point2D.subtract(a2, a1);
        float currentDistance = diff.length();
        if (currentDistance < 0.0001f) {
			return;
		}

        Point2D normal = Point2D.normalize(diff);

        float displacement = currentDistance - this.restLength;
        float springForceMag = displacement * this.springConstant;

        Point2D relativeVelocity = Point2D.subtract(this.bodyB.getLinearVelocity(), this.bodyA.getLinearVelocity());
        float dampingForceMag = Point2D.dot(relativeVelocity, normal) * this.damping;

        float totalForce = springForceMag + dampingForceMag;
        Point2D force = Point2D.multiply(normal, totalForce);

        if (!this.bodyA.isStatic()) {
            this.bodyA.getLinearVelocity().add(Point2D.multiply(force, this.bodyA.getInvMass() * deltaTime));
        }
        if (!this.bodyB.isStatic()) {
            this.bodyB.getLinearVelocity().add(Point2D.multiply(force, -this.bodyB.getInvMass() * deltaTime));
        }        
        		
    }
}