package io.github.some_example_name.lwjgl3.sceneManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.some_example_name.lwjgl3.*;
import io.github.some_example_name.lwjgl3.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.movementManager.MovementManager;

// implements ISceneManager interface (abstraction)
public class SceneManager implements ISceneManager {

    // all fields are private (encapsulation)
    private Scene currentScene;
    private Scene cachedGame;

    public enum State {MENU, GAME, PAUSE}

    private EntityManager em;
    private CollisionManager cm;
    private MovementManager mm;
    private IOManager io;
    private PlayerStats ps;

    public SceneManager() {
    }

    public void setEngineTools(EntityManager em, CollisionManager cm, MovementManager mm, IOManager io, PlayerStats ps) {
        this.em = em;
        this.cm = cm;
        this.mm = mm;
        this.io = io;
        this.ps = ps;
    }

    // Function to set scenes accordingly to conditions
    @Override
    public void setScene(State state) {
        switch (state) {
            case MENU:
                if (currentScene != null) {
                    currentScene.dispose();
                }
                currentScene = new SceneMenu(this);
                break;
            // If a cache of your progress exists, restore, else start new
            case GAME:
                if (cachedGame != null) {
                    currentScene = cachedGame;
                    cachedGame = null;
                    Gdx.input.setInputProcessor(null);
                } else {

                    currentScene = new SceneGame(this, em, cm, mm, io, ps);
                }
                break;
            case PAUSE:
                cachedGame = currentScene;
                currentScene = new ScenePause(this, io);
                break;
        }
    }


    @Override
    public void update(float delta) {
        if (currentScene != null) {
            currentScene.update(delta);
        }
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        // Render cached game first (underneath pause overlay)
        if (cachedGame != null) {
            cachedGame.render(shape, batch);
        }
        if (currentScene != null) {
            currentScene.render(shape, batch);
        }
    }

    // This would allow top level clear of progression so as to properly clear resources
    @Override
    public void dispose() {
        if (currentScene != null) {
            currentScene.dispose();
            currentScene = null;
        }
        if (cachedGame != null) {
            cachedGame.dispose();
            cachedGame = null;
        }
    }

    // exposes current scene through interface for external access
    public Scene getCurrentScene() {
        return currentScene;
    }
}
