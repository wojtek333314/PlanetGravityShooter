package com.brotherhood.gravityshooter.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.brotherhood.gravityshooter.game.enums.ButtonType;
import com.brotherhood.gravityshooter.gravity.GravityBody;
import com.brotherhood.gravityshooter.stages.GameStage;
import com.brotherhood.gravityshooter.utils.BaseStage;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

import static com.brotherhood.gravityshooter.utils.PhysicsStage.H;
import static com.brotherhood.gravityshooter.utils.PhysicsStage.W;

/**
 * Created by Przemek on 11.04.2016.
 */
public class MenuButton extends GravityBody {

    private ButtonType buttonType;
    private Image button;
    private float velocity = 10;
    private float angle;
    private float distanceFromCenter;



    public MenuButton(final PhysicsStage stage, ButtonType buttonType, float angle, float distanceFromCenter) {
        super(stage.getWorld()
                , calculatePositionByAngle(angle,distanceFromCenter).x
                , calculatePositionByAngle(angle,distanceFromCenter).y
                , buttonType == ButtonType.START ? 3 : 2
                , 1
                , 0.6f
                , ButtonType.defineBodyType(buttonType));
        button = new Image(BaseStage.getTextureRegion(ButtonType.definePlanetTexturePath(buttonType)));
        button.setSize((buttonType == ButtonType.START ? 3 : 2) * 2, (buttonType == ButtonType.START ? 3 : 2) * 2);
        this.angle = angle;
        this.distanceFromCenter = distanceFromCenter;
        button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.redirect(new GameStage());
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                getBody().setLinearVelocity(x, y);
                super.touchDragged(event, x, y, pointer);
            }
        });

        stage.addActor(button);

    }

     private static Vector2 calculatePositionByAngle(float angle,float distanceFromCenter){
        float centerX = (W/2 + (float) (distanceFromCenter * Math.sin(Math.toRadians(angle))));
        float centerY = (H/2 + (float) (Math.cos(Math.toRadians(angle)) * distanceFromCenter));
         System.out.println(centerX + ":" + centerY);
        return new Vector2(centerX,centerY);
    }

  /*  public void backToStartPosition() {
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


    }*/

    @Override
    public void onDraw(Batch batch) {
        super.onDraw(batch);
        getBody().setAngularVelocity(0);
        angle += 0.1f;
        getBody().setTransform(calculatePositionByAngle(angle,distanceFromCenter),0);
       // if(( Math.abs(getBody().getLinearVelocity().x) < 3 && Math.abs(getBody().getLinearVelocity().y) < 3))
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
