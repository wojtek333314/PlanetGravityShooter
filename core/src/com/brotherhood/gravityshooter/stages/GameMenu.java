package com.brotherhood.gravityshooter.stages;

import com.badlogic.gdx.graphics.Color;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Przemek on 10.04.2016.
 */
public class GameMenu extends PhysicsStage {
    public static final float GRAVITY_CONSTANT = (float) (4.3f * Math.pow(10, -2));


    public GameMenu()
    {
        super(0,0,true);
        setDrawDebugLines(true);
        setBackgroundColor(new Color(0,0,0,0));
    }

    @Override
    public void onWorldStep() {

    }
}
