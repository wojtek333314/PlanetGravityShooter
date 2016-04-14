package com.brotherhood.gravityshooter.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.brotherhood.gravityshooter.game.enums.ButtonType;
import com.brotherhood.gravityshooter.gravity.GravityBody;
import com.brotherhood.gravityshooter.utils.BaseStage;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Przemek on 11.04.2016.
 */
public class MenuButton extends GravityBody {

    private ButtonType buttonType;
    private Image button;
    private float startX;
    private float startY;
    private float backSpeed = 0.99f;


    public MenuButton(PhysicsStage stage, float x, float y, ButtonType buttonType) {
        super(stage.getWorld(), x, y, buttonType == ButtonType.START ? 2 : 1
                , 1
                , 0.6f, ButtonType.defineBodyType(buttonType));
        this.startX = x;
        this.startY = y;
        button = new Image(BaseStage.getTextureRegion(ButtonType.definePlanetTexturePath(buttonType)));
        button.setSize((buttonType == ButtonType.START ? 2 : 1) * 2, (buttonType == ButtonType.START ? 2 : 1) * 2);
        button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                getBody().setLinearVelocity(x, y);
                super.touchDragged(event, x, y, pointer);
            }
        });

        stage.addActor(button);
        setGravityForceEnabled(false);

    }

    public void backToStartPosition() {
        float vectorX = 0, vectorY = 0;
        if (startX > getBody().getPosition().x) {
            vectorX = -backSpeed;

        } else if (startX < getBody().getPosition().x)
            vectorX = backSpeed;

        if (startY > getBody().getPosition().y) {
            vectorY = -backSpeed;

        } else if (startY < getBody().getPosition().y)
            vectorY = backSpeed;

        getBody().setLinearVelocity(-vectorX, -vectorY);


    }

    @Override
    public void onDraw(Batch batch) {
        super.onDraw(batch);
        getBody().setAngularVelocity(0);
         if(( Math.abs(getBody().getLinearVelocity().x) < 1 && Math.abs(getBody().getLinearVelocity().y) < 1))
             backToStartPosition();
        button.setPosition(getBody().getPosition().x - button.getWidth() / 2, getBody().getPosition().y - button.getHeight() / 2);
    }

    public boolean isInSafeArea() {
        if (getBody().getPosition().x > 0 + button.getWidth() / 2 + 0.01
                && getBody().getPosition().y > 0 + button.getHeight() / 2 + 0.01
                && getBody().getPosition().x < 34 - button.getWidth() / 2 - 0.01
                && getBody().getPosition().y < 20 - button.getHeight() / 2 + 0.01 )

            return true;
        else return false;
    }

}
