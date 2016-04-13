package com.brotherhood.gravityshooter.game.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.brotherhood.gravityshooter.game.enums.GravityBodyType;
import com.brotherhood.gravityshooter.game.enums.PlanetType;
import com.brotherhood.gravityshooter.gravity.GravityBody;
import com.brotherhood.gravityshooter.utils.BaseStage;

/**
 * Created by Wojtek on 2016-04-10.
 */
public class Planet extends GravityBody {

    private Sprite sprite;
    private PlanetType planetType;

    public Planet(World world, float x, float y, PlanetType planetType) {
        super(world, x, y
                , PlanetType.defineRadius(planetType)
                , PlanetType.definePlanetMass(planetType)
                , PlanetType.defineBodyType(planetType));
        this.planetType = planetType;
        sprite = new Sprite(BaseStage.getTextureRegion(PlanetType.definePlanetTexturePath(planetType)));
        sprite.setSize(getBody().getFixtureList().get(0).getShape().getRadius() * 2
                , getBody().getFixtureList().get(0).getShape().getRadius() * 2);
    }

    @Override
    public void onDraw(Batch batch) {
        super.onDraw(batch);
        sprite.setPosition(getBody().getPosition().x - sprite.getWidth() / 2
                , getBody().getPosition().y - sprite.getHeight() / 2);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void onCollision(GravityBody gravityBody) {

        if(gravityBody.getType()== GravityBodyType.PLANET)
        {
            getBody().setLinearVelocity(0,0);
            setGravityForceEnabled(false);
        }
    }

    public PlanetType getPlanetType() {
        return planetType;
    }
}
