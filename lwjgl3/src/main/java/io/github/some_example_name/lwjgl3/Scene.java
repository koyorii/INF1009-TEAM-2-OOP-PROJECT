package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// Blueprint for all subsequent scenes, by forcing necessary functions through abstract
public abstract class Scene {
    // Reference to GameMaster
    protected GameMaster gm;

    public Scene(GameMaster gm) {
        this.gm = gm;
    }

    // All necessary functions for child to operate well
    public abstract void update(float delta);
    public abstract void render(ShapeRenderer shape, SpriteBatch spriteBatch);
    public abstract void dispose();
}
