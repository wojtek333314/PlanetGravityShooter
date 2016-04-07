package com.brotherhood.gravityshooter;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.brotherhood.gravityshooter.stages.MenuStage;

/**
 * Created by Wojciech Osak on 2015-12-06.
 */
public class Launcher extends Game implements Screen {
    public static com.brotherhood.gravityshooter.utils.BaseStage baseStage;
    public static Color backgroundColor = new Color(1, 0, 0, 1);

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        baseStage.draw();
        baseStage.act(delta);
    }

    @Override
    public void create() {
        baseStage = new MenuStage();
        setScreen(this);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
