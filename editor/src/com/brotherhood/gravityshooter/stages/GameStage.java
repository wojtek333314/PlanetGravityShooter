package com.brotherhood.gravityshooter.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
import com.brotherhood.gravityshooter.game.objects.Rocket;
import com.brotherhood.gravityshooter.game.systems.GravitySimulator;
import com.brotherhood.gravityshooter.gravity.GravityBody;
import com.brotherhood.gravityshooter.gravity.GravityBodyUserData;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Wojtek on 2016-04-10.
 */
public class GameStage extends PhysicsStage implements ContactListener, ContactFilter {
    public static Array<GravityBody> gravityBodies = new Array<GravityBody>();
    private GravitySimulator gravitySimulator;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private Vector2 touchPosition = new Vector2(0, 0);
    private PlanetType currentPlanetTypeToAdd = PlanetType.EARTH;
    private int currentPlanetTypeIndex = 0;
    private boolean moveCamera = false;
    private Rocket rocket;

    public GameStage() {
        super();
        setBackgroundColor(new Color(0, 0, 0, 0));
    //    setDrawDebugLines(true);
        shapeRenderer.setProjectionMatrix(camera.combined);

        world.setContactListener(this);
        world.setContactFilter(this);

        gravitySimulator = new GravitySimulator(gravityBodies);

        Planet sun = new Planet(this, 10, 10, PlanetType.MARS);
        gravityBodies.add(sun);

        rocket = new Rocket(this, 10, 0);
        gravityBodies.add(rocket);
    }

    @Override
    public void onWorldStep() {

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        shapeRenderer.setProjectionMatrix(camera.combined);

        for (GravityBody gravityBody : gravityBodies) {
            gravityBody.onDraw(getBatch());
            gravityBody.drawGravityRange(shapeRenderer);
        }

        rocket.onDraw(getBatch());

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
        if (compareCollision(fixtureA, fixtureB, GravityBodyType.PLANET, GravityBodyType.PLANET))
            return true;

        if (compareCollision(fixtureA, fixtureB, GravityBodyType.PLANET, GravityBodyType.BULLET))
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

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 touchUpVector = screenPositionToWorldPosition(screenX, screenY);
        Vector2 forceVector = new Vector2(touchUpVector.x - touchPosition.x, touchUpVector.y - touchPosition.y);
        moveCamera = false;
        if (button == 0) {

            Planet planet = new Planet(this, touchPosition.x, touchPosition.y, currentPlanetTypeToAdd);
            planet.getBody().setLinearVelocity(forceVector);
            gravityBodies.add(planet);

            for (GravityBody gravityBody : gravityBodies)
                gravityBody.getBody().applyForceToCenter(.0000001f, .0000001f, true);
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0)//left
            touchPosition = screenPositionToWorldPosition(screenX, screenY);
        else
            moveCamera = true;
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 touchUpVector = screenPositionToWorldPosition(screenX, screenY);
        Vector2 forceVector = new Vector2(touchUpVector.x - touchPosition.x, touchUpVector.y - touchPosition.y);

        if (moveCamera) {
            camera.position.x += forceVector.x / -300;
            camera.position.y += forceVector.y / -300;
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean scrolled(int amount) {
        if (((OrthographicCamera) getCamera()).zoom + amount * .3f > 0)
            ((OrthographicCamera) getCamera()).zoom += amount * .3f;

        return super.scrolled(amount);
    }

    @Override
    public boolean keyDown(int keyCode) {
        switch (keyCode) {
            case Input.Keys.ENTER:

                System.out.println("frames:" + (1 / Gdx.graphics.getDeltaTime())
                        + "  |   bodies:" + gravityBodies.size
                        + "  |   calculationsTime[ms]:" + gravitySimulator.getCalculationsTime());
                break;
            case Input.Keys.P:
                for (GravityBody gravityBody : gravityBodies) {
                    setBodyToDestroy(gravityBody.getBody());
                }
                gravityBodies.clear();
                break;
            case Input.Keys.D:
                currentPlanetTypeIndex++;
                if (currentPlanetTypeIndex >= PlanetType.values().length)
                    currentPlanetTypeIndex = 0;
                currentPlanetTypeToAdd = PlanetType.values()[currentPlanetTypeIndex];
                break;
            case Input.Keys.A:
                currentPlanetTypeIndex--;
                if (currentPlanetTypeIndex < 0)
                    currentPlanetTypeIndex = PlanetType.values().length - 1;
                currentPlanetTypeToAdd = PlanetType.values()[currentPlanetTypeIndex];
                break;
        }
        return super.keyDown(keyCode);
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == 'r')
            for (GravityBody gravityBody : gravityBodies)
                gravityBody.getBody().setLinearVelocity(0, 0);
        return super.keyTyped(character);
    }

    @Override
    public void setBodyToDestroy(Body bodyToRemove) {
        super.setBodyToDestroy(bodyToRemove);
        for (int i = 0; i < gravityBodies.size; i++)
            if (gravityBodies.get(i).getBody().equals(bodyToRemove))
                gravityBodies.removeIndex(i);
    }
}

