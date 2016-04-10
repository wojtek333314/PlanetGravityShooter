package com.brotherhood.gravityshooter.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.brotherhood.gravityshooter.gravity.GravityBody;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Wojtek on 2016-03-13.
 */
public class MenuStage extends PhysicsStage {
    public static final float GRAVITY_CONSTANT = (float) (4.3f * Math.pow(10, -2));
    private final float bulletMass = 1.4f;
    private final float bulletRadius = 0.2f;
    private int counter = 0;


    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Array<GravityBody> bullets = new Array<GravityBody>();
    private Array<GravityBody> gravityBodies = new Array<GravityBody>();

    private float startTouchX, startTouchY;

    public MenuStage() {
        super(0, 0, true);
        setDrawDebugLines(true);



        setBackgroundColor(new Color(0, 0, 0, 0));
    }


    @Override
    public void onWorldStep() {
        gravitySimulation();
    }

    private void addBullet(float x, float y, Vector2 direction) {
        GravityBody bullet = new GravityBody(world, x, y, bulletRadius, 2);
        bullet.getBody().setLinearVelocity(direction);
        bullets.add(bullet);
    }

    private void removeAllBullets() {
        for (GravityBody bullet : bullets)
            setBodyToDestroy(bullet.getBody());
        bullets.clear();
    }

    private void gravitySimulation() {
        counter++;
        for (GravityBody gravityBody : gravityBodies)
            for (GravityBody body : bullets) {
                simulateGravity(body, gravityBody, true);
            }
    }

    private void simulateGravity(GravityBody body1, GravityBody body2, boolean enableDistanceMargin) {
        double distance = Math.hypot(body1.getBody().getPosition().x - body2.getBody().getPosition().x,
                body1.getBody().getPosition().y - body2.getBody().getPosition().y);

        double gravityForce = GRAVITY_CONSTANT * (body1.getMass() * body2.getMass()) / Math.pow(distance, 2);//sila grawitacji z wzoru fizycznego
        double angleBetweenBodies =
                (Math.atan2((body2.getBody().getPosition().y - body1.getBody().getPosition().y)
                        , (body2.getBody().getPosition().x - body1.getBody().getPosition().x)));

        double xForce = Math.cos(angleBetweenBodies) * gravityForce;
        double yForce = Math.sin(angleBetweenBodies) * gravityForce;

        if (counter >= 400) {
            counter = 0;
            System.out.println("angle: " + Math.toDegrees(angleBetweenBodies) + "| force: " + gravityForce);
        }


        body1.getBody().setLinearVelocity(body1.getBody().getLinearVelocity().x + (float) xForce, body1.getBody().getLinearVelocity().y + (float) yForce);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        shapeRenderer.setProjectionMatrix(camera.combined);
        for (GravityBody gravityBody : gravityBodies)
        {
            gravityBody.drawGravityRange(shapeRenderer);
            gravityBody.onDraw(getBatch());
        }
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 physicCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        startTouchX = physicCoords.x;
        startTouchY = physicCoords.y;
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 physicCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        addBullet(startTouchX, startTouchY, new Vector2(physicCoords.x - startTouchX, physicCoords.y - startTouchY));
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == 66)
            removeAllBullets();
        return super.keyDown(keyCode);
    }

    @Override
    public boolean scrolled(int amount) {
        if (((OrthographicCamera) getCamera()).zoom + amount * .3f > 0)
            ((OrthographicCamera) getCamera()).zoom += amount * .3f;

        return super.scrolled(amount);
    }
}
