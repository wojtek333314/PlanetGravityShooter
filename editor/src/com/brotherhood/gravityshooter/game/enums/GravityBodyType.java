package com.brotherhood.gravityshooter.game.enums;

/**
 * Created by Wojtek on 2016-04-10.
 */
public enum GravityBodyType {
    PLANET,
    BLACK_HOLE,
    WHITE_HOLE,
    BULLET,
    ROCKET;

    public static boolean simulateGravityBetweenTypes(GravityBodyType type1, GravityBodyType type2) {

        if (type1 == PLANET && type2 == PLANET) return true;

        if (type1 == PLANET && type2 == BULLET) return true;// planeta przyciaga pocisk
        if (type1 == BULLET && type2 == PLANET) return true;// ale pocisk nie przyciaga planety

        if (type1 == ROCKET && type2 == PLANET || type1 == PLANET && type2 == ROCKET) return true;
        return false;
    }
}
