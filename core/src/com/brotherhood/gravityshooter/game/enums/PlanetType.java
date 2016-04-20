package com.brotherhood.gravityshooter.game.enums;

import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by Wojtek on 2016-04-10.
 */
public enum  PlanetType {
    EARTH,
    VENUS,
    MARS;

    public static float definePlanetMass(PlanetType planetType){
        switch (planetType){
            case EARTH:
                return 1;
            case VENUS:
                return 5;
            case MARS:
                return 15;
            default:
                return 1;
        }
    }

    public static float defineRadius(PlanetType planetType){
        switch (planetType){
            case EARTH:
                return .5f;
            case VENUS:
                return 1f;
            case MARS:
                return .2f;
            default:
                return 1;
        }
    }

    public static BodyDef.BodyType defineBodyType(PlanetType planetType){
        switch (planetType){
            case EARTH:
                return BodyDef.BodyType.DynamicBody;
            case VENUS:
                return BodyDef.BodyType.DynamicBody;
            case MARS:
                return BodyDef.BodyType.StaticBody;
            default:
                return BodyDef.BodyType.KinematicBody;
        }
    }

    public static String definePlanetTexturePath(PlanetType planetType){
        switch (planetType){
            case EARTH:
                return "gfx/planets/earth.png";
            case VENUS:
                return "gfx/planets/venus.png";
            case MARS:
                return "gfx/planets/mars.png";
            default:
                return null;
        }
    }
}
