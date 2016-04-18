package com.brotherhood.gravityshooter.game.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.brotherhood.gravityshooter.game.enums.PlanetType;
import com.brotherhood.gravityshooter.gravity.GravityBody;
import com.brotherhood.gravityshooter.utils.BaseStage;
import com.brotherhood.gravityshooter.utils.PhysicsStage;

/**
 * Created by Wojtek on 2016-04-10.
 */
public class Planet extends GravityBody {

    private Sprite sprite;
    private PlanetType planetType;
    private Sprite range;

    public Planet(PhysicsStage physicsStage, float x, float y, PlanetType planetType) {
        super(physicsStage , x, y
                , PlanetType.defineRadius(planetType)
                , PlanetType.definePlanetMass(planetType)
                , 0//restitution
                , PlanetType.defineBodyType(planetType));
        this.planetType = planetType;
        sprite = new Sprite(BaseStage.getTextureRegion(PlanetType.definePlanetTexturePath(planetType)));
        sprite.setSize(getBody().getFixtureList().get(0).getShape().getRadius() * 2
                , getBody().getFixtureList().get(0).getShape().getRadius() * 2);

        range = new Sprite(BaseStage.getTextureRegion("gfx/planets/range.png"));
        range.setSize(getGravityRange() * 2, getGravityRange() * 2);
    }

    @Override
    public void onDraw(Batch batch) {
        super.onDraw(batch);
        sprite.setPosition(getBody().getPosition().x - sprite.getWidth() / 2
                , getBody().getPosition().y - sprite.getHeight() / 2);
        range.setPosition(sprite.getX() + sprite.getWidth() / 2 - range.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2 - range.getHeight() / 2);
        batch.begin();
        range.draw(batch);
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void onCollision(GravityBody gravityBody) {
      //  if(((Planet) gravityBody).getPlanetType() == PlanetType.EARTH)
     //       physicsStage.setBodyToDestroy(gravityBody.getBody());
    }

    public PlanetType getPlanetType() {
        return planetType;
    }
}
