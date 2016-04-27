package com.brotherhood.gravityshooter.game.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.brotherhood.gravityshooter.game.enums.GravityBodyType;
import com.brotherhood.gravityshooter.gravity.GravityBody;
import com.brotherhood.gravityshooter.utils.BaseStage;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Wojtek on 2016-04-17.
 */
public class Rocket extends GravityBody {
    private static final float width = 0.55f;
    private static final float height = 1.5f;
    private Sprite sprite;

    private RocketEngine leftEngine;
    private RocketEngine rightEngine;

    public Rocket(PhysicsStage stage, float x, float y) {
        super(stage, x, y, width, height, 2);

        leftEngine = new RocketEngine(stage, RocketEngine.EngineType.LEFT);
        rightEngine = new RocketEngine(stage, RocketEngine.EngineType.RIGHT);

        WeldJointDef leftJoint = new WeldJointDef();
        leftJoint.collideConnected = true;
        leftJoint.bodyA = getBody();
        leftJoint.bodyB = leftEngine.getBody();
        leftJoint.localAnchorA.set(0, -height * .9f);
        leftJoint.localAnchorB.set(-width, 0);


        WeldJointDef rightJoint = new WeldJointDef();
        rightJoint.collideConnected = true;
        rightJoint.bodyA = getBody();
        rightJoint.bodyB = rightEngine.getBody();
        rightJoint.localAnchorA.set(0, -height * .9f);
        rightJoint.localAnchorB.set(width, 0);

        stage.getWorld().createJoint(leftJoint);
        stage.getWorld().createJoint(rightJoint);

        sprite = new Sprite(BaseStage.getTextureRegion("gfx/rockets/basic/rocket.png"));
        sprite.setSize(width * 2, height * 2);
        sprite.setOrigin(width, height);

        getBody().setLinearVelocity(2,1);
        getBody().setTransform(getBody().getPosition().x, getBody().getPosition().y, 0);

        setType(GravityBodyType.PLANET);
    }

    @Override
    public void drawGravityRange(ShapeRenderer shapeRenderer) {
    }

    @Override
    public void onDraw(Batch batch) {
        super.onDraw(batch);
        sprite.setPosition(getBody().getPosition().x - sprite.getWidth() / 2, getBody().getPosition().y - sprite.getHeight() / 2);
        sprite.setRotation((float) Math.toDegrees(getBody().getAngle()));
        batch.begin();
        sprite.draw(batch);
        batch.end();

        rightEngine.onDraw(batch);
        leftEngine.onDraw(batch);
    }

    private static class RocketEngine extends GravityBody {
        private static final float width = .25f;
        private static final float height = .75f;
        private Sprite sprite;

        public RocketEngine(PhysicsStage physicsStage, EngineType type) {
            super(physicsStage, 0, 0, width, height, 1);

            switch (type) {
                case LEFT:
                    sprite = new Sprite(BaseStage.getTextureRegion("gfx/rockets/basic/right_engine.png"));
                    break;
                case RIGHT:
                    sprite = new Sprite(BaseStage.getTextureRegion("gfx/rockets/basic/left_engine.png"));
                    break;
            }
            sprite.setSize(width * 2, height * 2);
            sprite.setOrigin(width, height);
        }

        @Override
        public void onDraw(Batch batch) {
            super.onDraw(batch);
            sprite.setPosition(getBody().getPosition().x - sprite.getWidth() / 2, getBody().getPosition().y - sprite.getHeight() / 2);
            sprite.setRotation((float) Math.toDegrees(getBody().getAngle()));
            batch.begin();
            sprite.draw(batch);
            batch.end();
        }

        enum EngineType {
            LEFT,
            RIGHT
        }
    }
}
