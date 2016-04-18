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

import java.util.Random;

import static com.brotherhood.gravityshooter.utils.PhysicsStage.H;
import static com.brotherhood.gravityshooter.utils.PhysicsStage.W;

/**
 * Created by Przemek on 11.04.2016.
 */
public class MenuButton extends GravityBody {
    private Random random = new Random();
    private ButtonType buttonType;
    private Image button;
    private float startX;
    private float startY;
    private State state;
    private PhysicsStage stage;
    private boolean isKeyPressedDown = false;
    private boolean breathThreadWorking = true;
    private float breathAmplitude = .2f;
    private float maxSpeedBeforeResetPosition = 2f;
    private float radius;



    public MenuButton(final PhysicsStage stage, ButtonType buttonType, float angle, float distanceFromCenter) {
        super(stage.getWorld()
                , calculatePositionByAngle(angle,distanceFromCenter).x
                , calculatePositionByAngle(angle,distanceFromCenter).y
                , buttonType == ButtonType.START ? 3 : 2
                , 1
                , 0.6f
                , ButtonType.defineBodyType(buttonType));
        this.radius = buttonType == ButtonType.START ? 3 : 2;
        this.stage = stage;
        this.startX = calculatePositionByAngle(angle, distanceFromCenter).x;
        this.startY = calculatePositionByAngle(angle, distanceFromCenter).y;
        button = new Image(BaseStage.getTextureRegion(ButtonType.definePlanetTexturePath(buttonType)));
        button.setSize((buttonType == ButtonType.START ? 3 : 2) * 2, (buttonType == ButtonType.START ? 3 : 2) * 2);
       
        button.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (state == State.IDLE) {
                    stage.redirect(new GameStage());
                    breathThreadWorking = false;
                }
                isKeyPressedDown = false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isKeyPressedDown = true;
                System.out.println(getBody().getLinearVelocity().x + ":" + getBody().getLinearVelocity().y + state + "/" + isKeyPressedDown);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (state == State.APPEARING || state == State.DISAPPIRING)
                    return;
                getBody().setLinearVelocity(x, y);
                changeState(State.SIMULATING);
            }
        });
        stage.addActor(button);
        changeState(State.IDLE);

        getBody().setLinearVelocity(-breathAmplitude + random.nextFloat() * (breathAmplitude - (-breathAmplitude)),
                -breathAmplitude + random.nextFloat() * (breathAmplitude - (-breathAmplitude)));
        Thread breathThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (breathThreadWorking) {
                    if (state == State.IDLE) {
                        getBody().setLinearVelocity(randomBreathVector());
                    }

                    try {
                        Thread.sleep(1250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        breathThread.start();
    }

    private Vector2 randomBreathVector() {
        Vector2 result = new Vector2(-breathAmplitude + random.nextFloat() * (breathAmplitude - (-breathAmplitude)),
                -breathAmplitude + random.nextFloat() * (breathAmplitude - (-breathAmplitude)));
        Vector2 positionInWorld = new Vector2(getBody().getPosition().x + result.x, getBody().getPosition().y + result.y);

        while (!isVectorInStartCircle(positionInWorld)) {
            result = new Vector2(-breathAmplitude + random.nextFloat() * (breathAmplitude - (-breathAmplitude)),
                    -breathAmplitude + random.nextFloat() * (breathAmplitude - (-breathAmplitude)));
            positionInWorld = new Vector2(getBody().getPosition().x + result.x, getBody().getPosition().y + result.y);
        }

        return result;
    }

    private boolean isVectorInStartCircle(Vector2 vector) {
        float distance = (float) Math.sqrt(Math.pow(startX - vector.x, 2) + Math.pow(startY - vector.y, 2));
        return distance < radius;
    }

    private static Vector2 calculatePositionByAngle(float angle, float distanceFromCenter) {
        float centerX = (W / 2 + (float) (distanceFromCenter * Math.sin(Math.toRadians(angle))));
        float centerY = (H / 2 + (float) (Math.cos(Math.toRadians(angle)) * distanceFromCenter));
        return new Vector2(centerX, centerY);
    }


    public void backToStartPosition() {
        if (Math.abs(getBody().getLinearVelocity().x) < maxSpeedBeforeResetPosition
                && Math.abs(getBody().getLinearVelocity().y) < maxSpeedBeforeResetPosition
                && Math.abs(getBody().getLinearVelocity().x) != 0
                && Math.abs(getBody().getLinearVelocity().y) != 0) {
            changeState(State.DISAPPIRING);
        }
    }

    public void changeTransparencyDown() {
        if (button.getColor().a <= 0) {
            button.getColor().a = 0;
            getBody().setLinearVelocity(0, 0);
            stage.setBodyToTransform(getBody(), startX, startY, 0);
            changeState(State.APPEARING);
        } else
            button.getColor().a -= (1 / 30f);
    }

    public void changeTransparencyUp() {
        button.getColor().a += (1 / 30f);
        if (button.getColor().a >= 1) {
            button.getColor().a = 1;
            getBody().setLinearVelocity(0, 0);
            stage.setBodyToTransform(getBody(), startX, startY, 0);
            changeState(State.IDLE);
        }
    }

    @Override
    public void onDraw(Batch batch) {
        super.onDraw(batch);
        getBody().setAngularVelocity(0);

        if ((Math.abs(getBody().getLinearVelocity().x) > breathAmplitude //0.2f to max wartosc wysuniecia dla oddychania
                || Math.abs(getBody().getLinearVelocity().y) > breathAmplitude)
                && (state == State.IDLE || state == State.SIMULATING)) {
            changeState(State.SIMULATING);
            if (state == State.IDLE || (state == State.SIMULATING && isKeyPressedDown))
                isKeyPressedDown = false;
        }

        if (state == State.SIMULATING && !isKeyPressedDown)
            backToStartPosition();

        switch (state) {
            case DISAPPIRING:
                changeTransparencyDown();
                break;
            case APPEARING:
                changeTransparencyUp();
                break;
        }
        button.setPosition(getBody().getPosition().x - button.getWidth() / 2
                , getBody().getPosition().y - button.getHeight() / 2);

    }

    private void changeState(State state) {
        if (this.state == state)
            return;
        this.state = state;
    }

    enum State {
        IDLE,
        DISAPPIRING,
        APPEARING,
        SIMULATING
    }


}
