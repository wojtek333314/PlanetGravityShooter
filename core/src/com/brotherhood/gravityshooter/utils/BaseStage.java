package com.brotherhood.gravityshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.brotherhood.gravityshooter.Launcher;
import com.brotherhood.gravityshooter.utils.enums.FontType;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by Wojciech Osak on 2015-12-06.
 */
public class BaseStage extends Stage implements Screen, InputProcessor {
    public static int W, H;
    private static HashMap<String, TextureRegion> textureRegionList = new HashMap<String, TextureRegion>();
    private static HashMap<FontKey, BitmapFont> fonts = new HashMap<FontKey, BitmapFont>();
    protected Camera camera;
    private InputMultiplexer inputMultiplexer;
    protected LabelActor FPScounterLabel;
    private DecimalFormat fpsTextFormat = new DecimalFormat("#.##");

    public BaseStage() {
        W = Gdx.graphics.getWidth();
        H = Gdx.graphics.getHeight();

        Gdx.input.setCatchBackKey(true);
        camera = new OrthographicCamera(W, H);
        camera.position.set(W / 2, H / 2, 0);
        getViewport().setCamera(camera);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
    }

    /**
     * To switch between stages, call in any BaseStage
     *
     * @param baseStage
     */
    public void redirect(final BaseStage baseStage) {
        if (baseStage == null) {
            Gdx.app.exit();
            return;
        }
        Launcher.baseStage = baseStage;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Gdx.input.setInputProcessor(baseStage.inputMultiplexer);
            }
        }).start();

    }

    /**
     * Get texture region from assets. If texture was loaded before it will be reused
     *
     * @param filePath
     * @return
     */
    public static TextureRegion getTextureRegion(String filePath) {
        System.gc();
        TextureRegion ret = textureRegionList.get(filePath);
        if (ret != null) {
            return ret;
        } else {
            Texture t = new Texture(Gdx.files.internal(filePath));
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            ret = new TextureRegion(t, 0, 0, t.getWidth(), t.getHeight());
            textureRegionList.put(filePath, ret);
        }

        return ret;
    }

    /**
     * Same as getTextureRegion() but this will cut one file to many textures
     *
     * @param pName
     * @param numberOfRows
     * @param numberOfColumns
     * @return
     */
    public static TextureRegion[] getTiledTextureRegion(String pName, int numberOfRows, int numberOfColumns) {
        Texture t = new Texture(Gdx.files.internal(pName));
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion[][] p2DTextureRegion = TextureRegion.split(t, t.getWidth() / numberOfColumns, t.getWidth() / numberOfRows);
        TextureRegion[] p1DTextureRegion = new TextureRegion[numberOfRows * numberOfColumns];

        for (int i = 0; i < numberOfRows; i++)
            System.arraycopy(p2DTextureRegion[i], 0, p1DTextureRegion, i * numberOfColumns, numberOfColumns);

        return p1DTextureRegion;
    }

    /**
     * Calculates actor ratio basing on Width dividing by Height
     *
     * @param textureRegion
     * @return
     */
    public static float actorRatioWH(Actor textureRegion) {
        return textureRegion.getWidth() / textureRegion.getHeight();
    }

    /**
     * Calculates actor ratio basing on Height dividing by Width
     *
     * @param textureRegion
     * @return
     */
    public static float actorRatioHW(Actor textureRegion) {
        return textureRegion.getHeight() / textureRegion.getWidth();
    }

    public float getX() {
        return camera.position.x;
    }

    public float getY() {
        return camera.position.y;
    }

    /**
     * FEATURE NOT READY YET
     *
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        camera.position.set(x, y, 0);
    }

    /**
     * Get font from Assets with size. If font was created before it will be reused.
     *
     * @param font
     * @param size
     * @return
     */
    public static BitmapFont getFont(FontType font, int size) {
        String fontPath = FontType.getFontPath(font);
        FontKey fontKey = new FontKey(fontPath, size);
        for (FontKey key : fonts.keySet()) {
            if (key.fontPath.equals(fontPath) && key.fontSize == size)
                return fonts.get(key);
        }
        float ratio = ((((float) Gdx.graphics.getWidth()) * size / 2500));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontType.getFontPath(font)));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = new Color(1, 1, 1, 1);
        parameter.size = (int) (size * ratio);
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
                + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
                + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toLowerCase()
                + "ãáàâçéêíõóôúüáéíñóúü¿¡ąćęłńóśźżàâæçéèêëïîôœ€ÿüûùäöüß"
                + "ãáàâçéêíõóôúüáéíñóúü¿¡ąćęłńóśźżàâæçéèêëïîôœ€ÿüûùäöüß".toUpperCase();
        BitmapFont ret = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        fonts.put(fontKey, ret);
        return ret;
    }

    /**
     * Get font from Assets with size. Always create new font
     *
     * @param font
     * @param size
     * @return
     */
    public static BitmapFont getTemporalyFont(FontType font, int size) {
        String fontPath = FontType.getFontPath(font);
        for (FontKey key : fonts.keySet()) {
            if (key.fontPath.equals(fontPath) && key.fontSize == size)
                return fonts.get(key);
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontType.getFontPath(font)));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.FIREBRICK;
        parameter.size = size * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight());
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
                + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
                + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toLowerCase()
                + "ãáàâçéêíõóôúüáéíñóúü¿¡ąćęłńóśźżàâæçéèêëïîôœ€ÿüûùäöüß"
                + "ãáàâçéêíõóôúüáéíñóúü¿¡ąćęłńóśźżàâæçéèêëïîôœ€ÿüûùäöüß".toUpperCase();
        BitmapFont ret = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        return ret;
    }

    /**
     * Get font from Assets with size. If font was created before it will be reused.
     *
     * @param font
     * @param size
     * @param color
     * @return
     */
    public static BitmapFont getFont(FontType font, int size, Color color) {
        String fontPath = FontType.getFontPath(font);
        FontKey fontKey = new FontKey(fontPath, size);
        for (FontKey key : fonts.keySet()) {
            if (key.fontPath.equals(fontPath) && key.fontSize == size)
                return fonts.get(key);
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FontType.getFontPath(font)));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = color;
        parameter.size = size * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight());
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS
                + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
                + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toLowerCase()
                + "ãáàâçéêíõóôúüáéíñóúü¿¡ąćęłńóśźżàâæçéèêëïîôœ€ÿüûùäöüß"
                + "ãáàâçéêíõóôúüáéíñóúü¿¡ąćęłńóśźżàâæçéèêëïîôœ€ÿüûùäöüß".toUpperCase();
        BitmapFont ret = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        fonts.put(fontKey, ret);
        return ret;
    }

    public void setBackgroundColor(Color backgroundColor) {
        Launcher.backgroundColor = backgroundColor;
    }

    /**
     * Faster System.out.println method().
     *
     * @param text
     */
    protected void log(Object text) {
        System.out.println(text.toString());
    }

    /**
     * to Override in every BaseStage if you want to add some logic for back button
     */
    protected void onBack() {
        Gdx.app.exit();
    }

    protected void setFPSCounterVisibility(boolean isEnabled) {
        if (isEnabled) {
            if (FPScounterLabel == null) {
                FPScounterLabel = new LabelActor(getFont(FontType.ROBOTO_REGULAR, 14, Color.WHITE), "");
                addActor(FPScounterLabel);
            }
            FPScounterLabel.setPosition(W - FPScounterLabel.getWidth(), H - FPScounterLabel.getHeight());
        } else {
            if (FPScounterLabel != null) {
                FPScounterLabel.remove();
                FPScounterLabel = null;
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        draw();
        act(delta);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (FPScounterLabel != null) {
            FPScounterLabel.setText(fpsTextFormat.format(1 / delta) + " FPS");
            FPScounterLabel.setPosition(W - FPScounterLabel.getWidth(), H - FPScounterLabel.getHeight());
        }
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

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK) {
            onBack();
            return true;
        } else
            return super.keyDown(keyCode);
    }

    /**
     * Set multiplexer, this method should be called in stage started only in Launcher.
     * Other stages have this method after redirect.
     */
    public void resetInputProcessor(){
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     * Class for font recycler
     */
    private static class FontKey {
        public String fontPath;
        public int fontSize;

        public FontKey(String fontPath, int fontSize) {
            this.fontPath = fontPath;
            this.fontSize = fontSize;
        }
    }

    protected Vector2 screenPositionToWorldPosition(float x, float y) {
        return new Vector2(camera.unproject(new Vector3(x, y, 0)).x, camera.unproject(new Vector3(x, y, 0)).y);
    }

    protected Vector2 screenPositionToWorldPosition(Vector2 vector) {
        return screenPositionToWorldPosition(vector.x, vector.y);
    }

    protected Vector2 worldPositionToScreenPosition(float x, float y) {
        return new Vector2(camera.project(new Vector3(x, y, 0)).x, camera.project(new Vector3(x, y, 0)).y);
    }

    protected Vector2 worldPositionToScreenPosition(Vector2 vector) {
        return worldPositionToScreenPosition(vector.x, vector.y);
    }


}
