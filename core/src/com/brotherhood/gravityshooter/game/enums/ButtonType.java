package com.brotherhood.gravityshooter.game.enums;

import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by Przemek on 12.04.2016.
 */
public enum ButtonType {
    START,
    EXIT,
    COIN_SHOP,
    ITEM_SHOP,
    OTHER_BUTTON,
    RANK;

    public static BodyDef.BodyType defineBodyType(ButtonType buttonType) {
        switch (buttonType) {
            case START:
                return BodyDef.BodyType.StaticBody;
            case EXIT:
                return BodyDef.BodyType.DynamicBody;
            case COIN_SHOP:
                return BodyDef.BodyType.DynamicBody;
            case ITEM_SHOP:
                return BodyDef.BodyType.DynamicBody;
            case RANK:
                return BodyDef.BodyType.DynamicBody;

            default:
                return BodyDef.BodyType.DynamicBody;
        }
    }

    public static String definePlanetTexturePath(ButtonType buttonType) {
        switch (buttonType) {
            case START:
                return "gfx/planets/green.png";
            case EXIT:
                return "gfx/planets/blue.png";
            case COIN_SHOP:
                return "gfx/planets/blue.png";
            case ITEM_SHOP:
                return "gfx/planets/yellow.png";
            case RANK:
                return "gfx/planets/blue.png";
            default:
                return "gfx/planets/blue.png";
        }
    }
}
