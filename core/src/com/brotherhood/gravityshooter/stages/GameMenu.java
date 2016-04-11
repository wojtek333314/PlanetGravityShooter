package com.brotherhood.gravityshooter.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Przemek on 10.04.2016.
 */
public class GameMenu extends PhysicsStage {
    public static final float GRAVITY_CONSTANT = (float) (4.3f * Math.pow(10, -2));


    public GameMenu() {
        super(0, 0, true);
        setDrawDebugLines(true);
        setBackgroundColor(new Color(0, 0, 0, 0));
        createBorder();

    }

    @Override
    public void onWorldStep() {

    }

    public void createBorder(){
        Body leftBorder = createBox(0,0,0.01f,H, BodyDef.BodyType.StaticBody);
        Body rightBorder = createBox(W,0,1,H, BodyDef.BodyType.StaticBody);
        Body topBorder = createBox(0,H,W,1, BodyDef.BodyType.StaticBody);
        Body bottomBorder = createBox(0,0,W,1, BodyDef.BodyType.StaticBody);
    }
}
