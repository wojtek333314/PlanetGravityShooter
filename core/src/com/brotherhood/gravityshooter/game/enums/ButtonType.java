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
            default:
                return BodyDef.BodyType.DynamicBody;
        }
    }

    public static String definePlanetTexturePath(ButtonType buttonType) {
        switch (buttonType) {
            case START:
                return "gfx/planets/center_planet.png";
            case EXIT:
                return "gfx/planets/blue.png";
            case COIN_SHOP:
                return "gfx/planets/desert.png";
            case ITEM_SHOP:
                return "gfx/planets/desert.png";
            case RANK:
                return "gfx/planets/greenplanet.png";
            default:
                return "gfx/planets/blue.png";
        }
    }

}
