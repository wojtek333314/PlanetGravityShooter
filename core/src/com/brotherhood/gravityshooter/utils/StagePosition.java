package com.brotherhood.gravityshooter.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Wojtek on 2016-04-20.
 */
public enum StagePosition {
    LEFT_UP,
    LEFT_DOWN,
    RIGHT_UP,
    RIGHT_DOWN,
    CENTER,
    LEFT_CENTER,
    RIGHT_CENTER,
    UP_CENTER,
    DOWN_CENTER;


    public static Vector2 getPositionOnStage(StagePosition stagePosition, Actor actor) {
        Vector2 result = new Vector2(0, 0);
        switch (stagePosition) {
            case LEFT_UP:
                result.x = 0;
                result.y = BaseStage.H;
                break;
            case LEFT_DOWN:
                result.x = 0;
                result.y = actor.getHeight();
                break;
            case RIGHT_UP:
                result.x = BaseStage.W - actor.getWidth();
                result.y = BaseStage.H;
                break;
            case RIGHT_DOWN:
                result.x = BaseStage.W - actor.getWidth();
                result.y = actor.getHeight();
                break;
            case CENTER:
                result.x = BaseStage.W / 2 - actor.getWidth() / 2;
                result.y = BaseStage.H / 2 + actor.getHeight() / 2;
                break;
            case LEFT_CENTER:
                result.x = 0;
                result.y = BaseStage.H / 2 + actor.getHeight() / 2;
                break;
            case RIGHT_CENTER:
                result.x = BaseStage.W - actor.getWidth();
                result.y = BaseStage.H / 2 + actor.getHeight() / 2;
                break;
            case UP_CENTER:
                result.x = BaseStage.W / 2 - actor.getWidth() / 2;
                result.y = BaseStage.H;
                break;
            case DOWN_CENTER:
                result.x = BaseStage.W / 2 - actor.getWidth() / 2;
                result.y = actor.getHeight();
                break;
        }System.out.println(result.x+":"+result.y);
        return result;
    }
}
