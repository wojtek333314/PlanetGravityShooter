package com.brotherhood.gravityshooter.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.brotherhood.gravityshooter.game.enums.ButtonType;
import com.brotherhood.gravityshooter.menu.MenuButton;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Przemek on 10.04.2016.
 */
public class GameMenu extends PhysicsStage {

    Array<MenuButton> menuButtons = new Array<MenuButton>();

    public GameMenu() {
        super(0, 0, true);
        setDrawDebugLines(false);
        setBackgroundColor(new Color(0, 0, 0, 0));
        createBorder();
        Image background = new Image(getTextureRegion("gfx/tlo.png"));
        background.setSize(W, H);
        addActor(background);

        MenuButton tmp = new MenuButton(this, ButtonType.START, 0, 0);
        menuButtons.add(tmp);
        float angle = (360 /(ButtonType.values().length - 1));
        for (int i = 1; i < ButtonType.values().length; i++) {
             tmp = new MenuButton(this, ButtonType.values()[i], i*angle, 7f);
            menuButtons.add(tmp);
        }

    }

    @Override
    public void onWorldStep() {

    }

    public void createBorder() {
        createBox(-0.01f, 0, 0.01f, H, BodyDef.BodyType.KinematicBody); // left
        createBox(W + 0.02f, 0, 0.01f, H, BodyDef.BodyType.KinematicBody); // right
        createBox(0, H + 0.02f, W, 0.01f, BodyDef.BodyType.KinematicBody); // top
        createBox(0, -0.01f, W, 0.01f, BodyDef.BodyType.KinematicBody); // bottom
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (MenuButton tmp : menuButtons) {
            tmp.onDraw(getBatch());
        }
    }
}
