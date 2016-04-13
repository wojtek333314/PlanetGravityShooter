package com.brotherhood.gravityshooter.gravity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.brotherhood.gravityshooter.game.enums.GravityBodyType;

/**
 * Created by Wojtek on 2016-03-22.
 */
public class GravityBody {
    private World worldHandle;
    private Body body;
    private Fixture fixture;
    private GravityBodyType type = GravityBodyType.PLANET;
    private float mass = 1;
    private boolean gravityForceEnabled = true;
    private float radius;

    public GravityBody(World world, float x, float y, float radius, float mass) {
        this.worldHandle = world;
        this.mass = mass;
        this.radius = radius;
        createBody(x, y, radius, BodyDef.BodyType.DynamicBody);
    }

    public GravityBody(World world, float x, float y, float radius, float mass, BodyDef.BodyType bodyType) {
        this.worldHandle = world;
        this.mass = mass;
        this.radius = radius;
        createBody(x, y, radius, bodyType);
    }

    private void createBody(float x, float y, float radius, BodyDef.BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = bodyType;
        body = worldHandle.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        fixture = body.createFixture(shape, mass);
        fixture.setFriction(mass);
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
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(body.getPosition().x, body.getPosition().y, getRadius(), 20);
        shapeRenderer.end();
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
}
