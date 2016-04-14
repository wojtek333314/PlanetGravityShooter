package com.brotherhood.gravityshooter.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.brotherhood.gravityshooter.game.enums.ButtonType;
import com.brotherhood.gravityshooter.menu.MenuButton;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Przemek on 10.04.2016.
 */
public class GameMenu extends PhysicsStage {
    MenuButton playButton;
    MenuButton otherButton;
    MenuButton otherButton2;
    MenuButton otherButton3;
    MenuButton rightButton;
//    GravitySimulator gravitySimulator;
//    Array<GravityBody> buttons = new Array<GravityBody>();

    public GameMenu() {
        super(0, 0, true);
        setDrawDebugLines(true);
        setBackgroundColor(new Color(0, 0, 0, 0));
        createBorder();
        playButton = new MenuButton(this, W / 2, H / 2, ButtonType.START);
        otherButton = new MenuButton(this, W / 2, H / 4, ButtonType.ITEM_SHOP);
        otherButton2 = new MenuButton(this, W / 2, 15, ButtonType.COIN_SHOP);
        otherButton3 = new MenuButton(this, 12, H / 2, ButtonType.COIN_SHOP);
        rightButton = new MenuButton(this, 22, H / 2, ButtonType.ITEM_SHOP);

        /*gravitySimulator = new GravitySimulator(buttons);
        buttons.add(playButton);
        buttons.add(otherButton);
        buttons.add(otherButton2);
        buttons.add(otherButton3);*/
    }

    @Override
    public void onWorldStep() {
        //gravitySimulator.gravitySimulation();
    }

    public void createBorder() {
        Body leftBorder = createBox(-0.01f, 0, 0.01f, H, BodyDef.BodyType.StaticBody);
        Body rightBorder = createBox(W + 0.02f, 0, 0.01f, H, BodyDef.BodyType.StaticBody);
        Body topBorder = createBox(0, H + 0.01f, W, 0.01f, BodyDef.BodyType.StaticBody);
        Body bottomBorder = createBox(0, -0.01f, W, 0.01f, BodyDef.BodyType.StaticBody);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        playButton.onDraw(getBatch());
        otherButton.onDraw(getBatch());
        otherButton2.onDraw(getBatch());
        otherButton3.onDraw(getBatch());
        rightButton.onDraw(getBatch());
    }
}
