package com.brotherhood.gravityshooter.stages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.brotherhood.gravityshooter.game.enums.ButtonType;
import com.brotherhood.gravityshooter.menu.MenuButton;
import com.brotherhood.gravityshooter.utils.LabelActor;
import com.brotherhood.gravityshooter.utils.PhysicsStage;
import com.brotherhood.gravityshooter.utils.StagePosition;
import com.brotherhood.gravityshooter.utils.enums.FontType;

/**
 * Created by Przemek on 10.04.2016.
 */
public class GameMenu extends PhysicsStage {

    Array<MenuButton> menuButtons = new Array<MenuButton>();
    private LabelActor label;


    public GameMenu() {
        super();
        setDrawDebugLines(false);
        setBackgroundColor(new Color(0, 0, 0, 0));
        createBorder();
        Image background = new Image(getTextureRegion("gfx/tlo.png"));
        background.setSize(W, H);
        addActor(background);

        MenuButton tmp = new MenuButton(this, ButtonType.START, 0, 0);
        menuButtons.add(tmp);
        float angle = (360 / (ButtonType.values().length - 1));
        for (int i = 1; i < ButtonType.values().length; i++) {
            tmp = new MenuButton(this, ButtonType.values()[i], i * angle, 1.8f);
            menuButtons.add(tmp);
        }

        resetInputProcessor();

        label = new LabelActor(getFont(FontType.ROBOTO_MEDIUM, 12), "TEST");
        label.setScale(.01f);
        addActor(label);
        label.setPosition(StagePosition.CENTER);
    }

    @Override
    public void onWorldStep() {

    }

    public void createBorder() {
        createBox(-0.01f, 0, 0.01f, H, BodyDef.BodyType.StaticBody); // left
        createBox(W + 0.02f, 0, 0.01f, H, BodyDef.BodyType.StaticBody); // right
        createBox(0, H + 0.02f, W, 0.01f, BodyDef.BodyType.StaticBody); // top
        createBox(0, -0.01f, W, 0.01f, BodyDef.BodyType.StaticBody); // bottom
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (MenuButton tmp : menuButtons) {
            tmp.onDraw(getBatch());
        }
    }
}
