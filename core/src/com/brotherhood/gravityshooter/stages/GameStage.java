package com.brotherhood.gravityshooter.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.brotherhood.gravityshooter.game.enums.GravityBodyType;
import com.brotherhood.gravityshooter.game.enums.PlanetType;
import com.brotherhood.gravityshooter.game.objects.Planet;
import com.brotherhood.gravityshooter.game.systems.GravitySimulator;
import com.brotherhood.gravityshooter.gravity.GravityBody;
import com.brotherhood.gravityshooter.gravity.GravityBodyUserData;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Wojtek on 2016-04-10.
 */
public class GameStage extends PhysicsStage implements ContactListener, ContactFilter {
    private Array<GravityBody> gravityBodies = new Array<GravityBody>();
    private GravitySimulator gravitySimulator;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public GameStage() {
        super();
        setBackgroundColor(new Color(0, 0, 0, 0));
        shapeRenderer.setProjectionMatrix(camera.combined);

        world.setContactListener(this);
        world.setContactFilter(this);
        //setDrawDebugLines(true);

        gravitySimulator = new GravitySimulator(gravityBodies);

        Planet planet = new Planet(world, W / 2, H / 2, PlanetType.GREEN);

        gravityBodies.add(planet);

        Planet planet2 = new Planet(world, W / 4, H / 3, PlanetType.BLUE);
        gravityBodies.add(planet2);

    //    planet2.getBody().setLinearVelocity(1, .2f);
     //   planet.getBody().setLinearVelocity(-1, -.2f);
    }

    @Override
    public void onWorldStep() {
        gravitySimulator.gravitySimulation();
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
    public void beginContact(Contact contact) {
        ((GravityBodyUserData) contact.getFixtureA().getBody().getUserData()).getGravityBody()
                .onCollision(((GravityBodyUserData) contact.getFixtureB().getBody().getUserData()).getGravityBody());
        ((GravityBodyUserData) contact.getFixtureB().getBody().getUserData()).getGravityBody()
                .onCollision(((GravityBodyUserData) contact.getFixtureA().getBody().getUserData()).getGravityBody());
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if(compareCollision(fixtureA,fixtureB,GravityBodyType.PLANET,GravityBodyType.PLANET))
            return true;

        if(compareCollision(fixtureA,fixtureB,GravityBodyType.PLANET,GravityBodyType.BULLET))
            return true;

        return false;
    }

    private boolean compareCollision(Fixture fixA, Fixture fixB, GravityBodyType typeA, GravityBodyType typeB) {
        return ((((GravityBodyUserData) fixA.getBody().getUserData()).getGravityBody().getType() == typeA
                && ((GravityBodyUserData) fixB.getBody().getUserData()).getGravityBody().getType() == typeB)
                ||
                (((GravityBodyUserData) fixA.getBody().getUserData()).getGravityBody().getType() == typeB
                        && ((GravityBodyUserData) fixB.getBody().getUserData()).getGravityBody().getType() == typeA));
    }

}
