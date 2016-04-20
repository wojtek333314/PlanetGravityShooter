package com.brotherhood.gravityshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by Wojtek on 2016-03-13.
 */
public abstract class PhysicsStage extends BaseStage {
    private static final float TIME_STEP = 1 / 500f;
    protected World world;
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private Array<Body> bodyToDestroyList = new Array<Body>();
    private HashMap<Body, TransformModel> bodyToTransformList = new HashMap<Body, TransformModel>();
    private float frameTime;
    private boolean drawDebugLines = false;
    private float accumulator = 0;

    public PhysicsStage(float gravityX, float gravityY, boolean doSleep) {
        super();
        world = new World(new Vector2(gravityX, gravityY), doSleep);
        camera = new OrthographicCamera(W, H);
        camera.position.set(W / 2, H / 2, 0);

        getBatch().setProjectionMatrix(camera.combined);
        getViewport().setCamera(camera);
    }

    public PhysicsStage() {
        this(0, 0, true);
    }


    public void setDrawDebugLines(boolean value) {
        drawDebugLines = value;
    }

    public abstract void onWorldStep();

    @Override
    public void act(float delta) {
        super.act(delta);
        frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
            onWorldStep();

            if (!world.isLocked()) {
                for (Body body : bodyToTransformList.keySet()) {
                    body.setTransform(bodyToTransformList.get(body).vector, bodyToTransformList.get(body).angle);
                }
                bodyToTransformList.clear();

                for (Body body : bodyToDestroyList)
                    if (body != null)
                        removeBodySafely(body);
                bodyToDestroyList.clear();
            }
        }
    }

    public Body createBox(float x, float y, float width, float height, BodyDef.BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = bodyType;
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        Fixture fixture = body.createFixture(shape, 1000);
        shape.dispose();
        return body;
    }

    public Body createCircle(float x, float y, float radius, BodyDef.BodyType bodyType, float density, float friction, float restitution) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = bodyType;
        Body body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        Fixture fixture = body.createFixture(shape, density);
        fixture.setFriction(friction);
        fixture.setRestitution(restitution);
        shape.dispose();
        return body;
    }

    public Body createCircle(float x, float y, float radius, float density, float friction, float restitution) {
        return createCircle(x, y, radius, BodyDef.BodyType.DynamicBody, density, friction, restitution);
    }

    public Body createBox(float x, float y, int width, int height) {
        return createBox(x, y, width, height, BodyDef.BodyType.DynamicBody);
    }

    public void setBodyToDestroy(Body bodyToRemove) {
        bodyToDestroyList.add(bodyToRemove);
    }

    public void setBodyToTransform(Body bodyToTranslate, float x,float y,float angle) {
        bodyToTransformList.put(bodyToTranslate,new TransformModel(x,y,angle));
    }

    private void removeBodySafely(Body body) {
        final Array<JointEdge> list = body.getJointList();
        while (list.size > 0) {
            world.destroyJoint(list.get(0).joint);
        }
        world.destroyBody(body);
    }

    @Override
    public void draw() {
        super.draw();

        if (drawDebugLines)
            debugRenderer.render(world, getCamera().combined);
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    @Override
    public void pause() {
        Gdx.app.exit();
        super.pause();
    }

    public World getWorld() {
        return world;
    }

    public class TransformModel {
        private Vector2 vector;
        private float angle;

        public TransformModel(Vector2 vector, float angle) {
            this.vector = vector;
            this.angle = angle;
        }

        public TransformModel(float x,float y,float angle){
            this.vector = new Vector2(x,y);
            this.angle = angle;
        }

        public Vector2 getVector() {
            return vector;
        }

        public void setVector(Vector2 vector) {
            this.vector = vector;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }
    }
}
