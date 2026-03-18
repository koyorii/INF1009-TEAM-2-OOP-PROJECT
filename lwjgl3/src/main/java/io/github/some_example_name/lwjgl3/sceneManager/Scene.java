package io.github.some_example_name.lwjgl3.sceneManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.some_example_name.lwjgl3.GameMaster;

// Blueprint for all subsequent scenes, by forcing necessary functions through abstract
public abstract class Scene {
    protected final ISceneManager sceneManager;

    public Scene(ISceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    // All necessary functions for child to operate well
    public abstract void update(float delta);
    public abstract void render(ShapeRenderer shape, SpriteBatch spriteBatch);
    public abstract void dispose();
}
