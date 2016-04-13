package com.brotherhood.gravityshooter.game.enums;

import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by Wojtek on 2016-04-10.
 */
public enum  PlanetType {
    BLUE,
    YELLOW,
    GREEN;

    public static float definePlanetMass(PlanetType planetType){
        switch (planetType){
            case BLUE:
                return 3;
            case YELLOW:
                return 1;
            case GREEN:
                return 2000 ;
            default:
                return 1;
        }
    }

    public static float defineRadius(PlanetType planetType){
        switch (planetType){
            case BLUE:
                return 2;
            case YELLOW:
                return 1.5f;
            case GREEN:
                return 1;
            default:
                return 1;
        }
    }

    public static BodyDef.BodyType defineBodyType(PlanetType planetType){
        switch (planetType){
            case BLUE:
                return BodyDef.BodyType.DynamicBody;
            case YELLOW:
                return BodyDef.BodyType.DynamicBody;
            case GREEN:
                return BodyDef.BodyType.DynamicBody;
            default:
                return BodyDef.BodyType.KinematicBody;
        }
    }

    public static String definePlanetTexturePath(PlanetType planetType){
        switch (planetType){
            case BLUE:
                return "gfx/planets/blue.png";
            case YELLOW:
                return "gfx/planets/yellow.png";
            case GREEN:
                return "gfx/planets/green.png";
            default:
                return null;
        }
    }
}
