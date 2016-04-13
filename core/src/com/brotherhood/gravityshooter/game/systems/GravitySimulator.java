package com.brotherhood.gravityshooter.game.systems;

import com.badlogic.gdx.utils.Array;
import com.brotherhood.gravityshooter.game.enums.GravityBodyType;
import com.brotherhood.gravityshooter.gravity.GravityBody;

/**
 * Created by Wojtek on 2016-04-10.
 */
public class GravitySimulator {
    private Array<GravityBody> gravityBodies;
    private static int i, j;

    public GravitySimulator(Array<GravityBody> gravityBodies) {
        this.gravityBodies = gravityBodies;
    }

    public void gravitySimulation() {
        for (i = 0; i < gravityBodies.size; i++) {
            for (j = 0; j < gravityBodies.size; j++) {
                if (i != j) {
                    if (GravityBodyType.simulateGravityBetweenTypes(gravityBodies.get(i).getType(), gravityBodies.get(j).getType()))
                        simulateGravity(gravityBodies.get(i), gravityBodies.get(j));
                }
            }
        }
    }

    private void simulateGravity(GravityBody body1, GravityBody body2) {
        double distance = Math.hypot(body1.getBody().getPosition().x - body2.getBody().getPosition().x,
                body1.getBody().getPosition().y - body2.getBody().getPosition().y);

        double gravityForce = ((body1.getMass() * body2.getMass()) / Math.pow(distance, 2));//sila grawitacji z wzoru fizycznego z pominieciem stalej grawitacji
        double angleBetweenBodies =
                (Math.atan2((body2.getBody().getPosition().y - body1.getBody().getPosition().y)
                        , (body2.getBody().getPosition().x - body1.getBody().getPosition().x)));

        double xForce = Math.cos(angleBetweenBodies) * gravityForce;
        double yForce = Math.sin(angleBetweenBodies) * gravityForce;

        if (body1.isGravityForceEnabled())
            body1.getBody().applyForceToCenter(body1.getBody().getLinearVelocity().x + (float) xForce
                    , body1.getBody().getLinearVelocity().y + (float) yForce, false);
        //System.out.println(xForce+":"+yForce);
    }
}
