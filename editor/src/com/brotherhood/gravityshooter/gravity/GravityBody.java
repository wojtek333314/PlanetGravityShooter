package com.brotherhood.gravityshooter.gravity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.brotherhood.gravityshooter.game.enums.GravityBodyType;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Wojtek on 2016-03-22.
 */
public class GravityBody {
    private final static int gravityRangeLinesCount = 36;

    protected PhysicsStage physicsStage;
    private Body body;
    private Fixture fixture;
    private GravityBodyType type = GravityBodyType.PLANET;
    private float mass = 1;
    private boolean gravityForceEnabled = true;
    private float radius;
    private float gravityRange;
    private Orbit orbit;

    public GravityBody(PhysicsStage physicsStage, float x, float y, float width, float height, float mass) {
        this.physicsStage = physicsStage;
        this.mass = mass;
        this.gravityRange = mass;
        createRectangleDynamicBody(x, y, width, height, mass);
    }

    public GravityBody(PhysicsStage physicsStage, float x, float y, float radius, float mass) {
        this(physicsStage, x, y, radius, mass, .5f, BodyDef.BodyType.DynamicBody);
    }

    public GravityBody(PhysicsStage physicsStage, float x, float y, float radius, float mass, BodyDef.BodyType bodyType) {
        this(physicsStage, x, y, radius, mass, .5f, bodyType);
    }

    public GravityBody(PhysicsStage physicsStage, float x, float y, float radius, float mass, float restitution, BodyDef.BodyType bodyType) {
        this.physicsStage = physicsStage;
        this.mass = mass;
        this.radius = radius;
        this.gravityRange = mass;
        createBody(x, y, radius, 1, restitution, bodyType);
    }

    private void createRectangleDynamicBody(float x, float y, float width, float height, float mass) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = physicsStage.getWorld().createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width,height);
        fixture = body.createFixture(shape, mass);
        fixture.setFriction(1);
        fixture.setRestitution(.5f);
        shape.dispose();
        body.setUserData(new GravityBodyUserData(this));
    }

    private void createBody(float x, float y, float radius, float friction, float restitution, BodyDef.BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = bodyType;
        body = physicsStage.getWorld().createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        fixture = body.createFixture(shape, mass);
        fixture.setFriction(friction);
        fixture.setRestitution(restitution);
        shape.dispose();
        body.setUserData(new GravityBodyUserData(this));
    }

    public Body getBody() {
        return body;
    }

    public float getMass() {
        return mass;
    }

    public float getRadius() {
        return radius;
    }

    public void drawGravityRange(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (int i = 0; i <= gravityRangeLinesCount; i++) {
            if (i % 2 == 0)
                continue;
            shapeRenderer.setColor(Color.RED);
            Vector2 startPoint = pointOnBodyEdge(i * (360 / gravityRangeLinesCount));
            Vector2 endPoint = pointOnBodyEdge((i + 1) * (360 / gravityRangeLinesCount));
            shapeRenderer.line(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }

        shapeRenderer.end();
    }

    private Vector2 pointOnBodyEdge(float alfa) {
        Vector2 result = new Vector2(0, 0);
        result.x = (float) (getBody().getPosition().x + Math.sin(Math.toRadians(alfa)) * getGravityRange());
        result.y = (float) (getBody().getPosition().y + Math.cos(Math.toRadians(alfa)) * getGravityRange());
        return result;
    }

    public void onDraw(Batch batch) {

    }

    public void onCollision(GravityBody gravityBody) {

    }

    public Fixture getFixture() {
        return fixture;
    }

    public GravityBodyType getType() {
        return type;
    }

    public boolean isGravityForceEnabled() {
        return gravityForceEnabled;
    }

    public void setGravityForceEnabled(boolean gravityForceEnabled) {
        this.gravityForceEnabled = gravityForceEnabled;
    }

    public float getGravityRange() {
        return gravityRange;
    }

    public void setGravityRange(float gravityRange) {
        this.gravityRange = gravityRange;
    }

    public void setOrbit(Orbit orbit) {
        this.orbit = orbit;
        float x = (float) (orbit.getCenter().getBody().getPosition().x + Math.sin(Math.toRadians(45)) * orbit.getHeight());
        float y = (float) (orbit.getCenter().getBody().getPosition().y + Math.cos(Math.toRadians(45)) * orbit.getHeight());
        this.getBody().setTransform(x, y, 0);
    }

    public Orbit getOrbit() {
        return orbit;
    }

    public void disableOrbiting() {
        orbit = null;
    }

    public void setType(GravityBodyType type) {
        this.type = type;
    }
}
