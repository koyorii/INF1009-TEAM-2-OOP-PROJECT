package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;



// defines the contract for any SceneManager
public interface ISceneManager {
    void setScene(SceneManager.State state);
    void update(float delta);
    void render(ShapeRenderer shape, SpriteBatch batch);
    void dispose();
}