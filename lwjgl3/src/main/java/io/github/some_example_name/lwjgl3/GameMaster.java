package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.iomanager.IOManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    protected SceneManager sceneM;
    protected IOManager IoM;
    protected MovementManager MoveM;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        IoM = new IOManager();
        MoveM = new MovementManager(IoM);

        sceneM = new SceneManager(this);
        sceneM.setScene(SceneManager.State.MENU); // Start the game at MENU scene
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        sceneM.update(delta);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        sceneM.render(shape, batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        sceneM.dispose();
    }
}
