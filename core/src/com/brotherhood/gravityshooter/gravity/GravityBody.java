package com.brotherhood.gravityshooter.gravity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Wojtek on 2016-03-22.
 */
public class GravityBody {
    private World worldHandle;
    private Body body;
    private Fixture fixture;
    private float mass = 1;
    private boolean hasKineticPropertiesDisabled = false;

    public GravityBody(World world, float x, float y, float radius, float mass) {
        this.worldHandle = world;
        this.mass = mass;
        createBody(x, y, radius, BodyDef.BodyType.DynamicBody);
    }

    public GravityBody(World world, float x, float y, float radius, float mass, boolean isKinematicBody) {
        this.worldHandle = world;
        this.mass = mass;
        createBody(x, y, radius, BodyDef.BodyType.KinematicBody);
    }

    private void createBody(float x, float y, float radius, BodyDef.BodyType bodyType) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = bodyType;
        body = worldHandle.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        fixture = body.createFixture(shape, mass);
        fixture.setFriction(10);
        shape.dispose();
    }

    public Body getBody() {
        return body;
    }

    public float getMass() {
        return mass;
    }

    public void drawGravityRange(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(body.getPosition().x, body.getPosition().y, getMass());
        shapeRenderer.end();
    }

    public void disableKineticProperties() {
        this.hasKineticPropertiesDisabled = true;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public boolean hasKineticPropertiesDisabled() {
        return hasKineticPropertiesDisabled;
    }
}
