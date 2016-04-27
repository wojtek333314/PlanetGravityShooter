package com.brotherhood.gravityshooter.game.systems;

import com.badlogic.gdx.utils.Array;
import com.brotherhood.gravityshooter.game.enums.GravityBodyType;
import com.brotherhood.gravityshooter.gravity.GravityBody;

/**
 * Created by Wojtek on 2016-04-10.
 */
public class GravitySimulator {
    public static final float GRAVITY_CONSTANT = 5;
    private Array<GravityBody> gravityBodies;
    private static int i, j;
    private static long startCalculationsTime;
    private static long calculationsTime;

    private boolean calculateInThread = true;
    private static double gravityForce;//sila grawitacji z wzoru fizycznego
    private static double angleBetweenBodies;
    private static double distance;
    private static double xForce;
    private static double yForce;


    public GravitySimulator(final Array<GravityBody> gravityBodies) {
        this.gravityBodies = gravityBodies;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (calculateInThread) {
                    try {
                        startCalculationsTime = System.currentTimeMillis();
                        gravitySimulation();
                        calculationsTime = System.currentTimeMillis() - startCalculationsTime;
                    } catch (NullPointerException e) {
                    }

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void gravitySimulation() {
        for (i = 0; i < gravityBodies.size; i++)
            for (j = 0; j < gravityBodies.size; j++)
                if (i != j)
                    if (GravityBodyType.simulateGravityBetweenTypes(gravityBodies.get(i).getType(), gravityBodies.get(j).getType()))
                        simulateGravity(gravityBodies.get(i), gravityBodies.get(j));
    }

    private void simulateGravity(GravityBody body1, GravityBody body2) {
        distance = Math.hypot(body1.getBody().getPosition().x - body2.getBody().getPosition().x,
                body1.getBody().getPosition().y - body2.getBody().getPosition().y);

      //  if (distance > body2.getMass())
       //     return;

        gravityForce = GRAVITY_CONSTANT * ((body1.getMass() * body2.getMass()) / Math.pow(distance, 2));//sila grawitacji z wzoru fizycznego
        angleBetweenBodies =
                (Math.atan2((body2.getBody().getPosition().y - body1.getBody().getPosition().y)
                        , (body2.getBody().getPosition().x - body1.getBody().getPosition().x)));

        xForce = Math.cos(angleBetweenBodies) * gravityForce;
        yForce = Math.sin(angleBetweenBodies) * gravityForce;

        if (body1.getOrbit() != null && !body1.getOrbit().isSet()) {

            float f = (float)gravityForce;
            float hh = body1.getOrbit().getHeight();
            float c = (float) Math.sqrt(f * f + hh * hh);
            float alfa = (float) Math.asin(f / c);
            float x2 = ((float) Math.sin((alfa)) * c);
            float y2 = ((float) Math.cos((alfa)) * c);

            body1.getBody().setLinearVelocity(x2, -y2);
            body1.getOrbit().setSet(true);
        }
        if (body1.isGravityForceEnabled())
            body1.getBody().applyForceToCenter((float) xForce, (float) yForce, false);
    }

    private float calculateGravityForceOnOrbit(GravityBody centerBody, float orbitHeight) {
        return (float) Math.sqrt((GRAVITY_CONSTANT * centerBody.getMass()) / orbitHeight);
    }

    private float calculateGravityForceOnOrbit(float orbitHeight, float period) {
        return (float) ((2 * Math.PI * orbitHeight) / period);
    }

    public long getCalculationsTime() {
        return calculationsTime;
    }
}
