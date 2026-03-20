package io.github.some_example_name.lwjgl3.Engine.sceneManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.assets.AssetManager;

// Blueprint for all subsequent scenes, by forcing necessary functions through abstract
public abstract class Scene {
    protected final ISceneManager sceneManager;
    protected final AssetManager am;

    public Scene(ISceneManager ism) {
        this.sceneManager = ism;
        this.am = ism.getAssets();
    }

    // All necessary functions for child to operate well
    public abstract void update(float delta);
    public abstract void render(ShapeRenderer shape, SpriteBatch spriteBatch);
    public abstract void dispose();
}
