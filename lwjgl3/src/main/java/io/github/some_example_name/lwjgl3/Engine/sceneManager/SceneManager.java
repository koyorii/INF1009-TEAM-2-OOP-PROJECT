package io.github.some_example_name.lwjgl3.Engine.sceneManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.assets.AssetManager;

import io.github.some_example_name.lwjgl3.*;
import io.github.some_example_name.lwjgl3.Engine.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.Game.*;

// implements ISceneManager interface (abstraction)
public class SceneManager implements ISceneManager {

    // all fields are private (encapsulation)
    private Scene currentScene;
    private Scene cachedGame;

    public enum State {MENU, GAME, PAUSE, DIFFICULTY, NAME}

    private EntityManager em;
    private CollisionManager cm;
    private MovementManager mm;
    private IOManager io;
    private PlayerStats ps;
    private AssetManager am;

    public SceneManager() {
    }

    public void setEngineTools(EntityManager em, CollisionManager cm, MovementManager mm, IOManager io, PlayerStats ps,  AssetManager am) {
        this.em = em;
        this.cm = cm;
        this.mm = mm;
        this.io = io;
        this.ps = ps;
        this.am = am;
    }

    // Function to set scenes accordingly to conditions
    @Override
    public void setScene(State state) {
        switch (state) {
            case MENU:
                if (currentScene != null) {
                    currentScene.dispose();
                    cachedGame = null;
                    // Reset entities and scores
                    em.clearEntities();
                    ps.reset();
                }

                currentScene = new SceneMenu(this);
                break;

            // If a cache of your progress exists, restore, else start new
            case GAME:
                if (cachedGame != null) {
                    currentScene = cachedGame;
                    cachedGame = null;
                } else {

                    currentScene = new SceneGame(this, em, cm, mm, io, ps);
                }
                break;

            case PAUSE:
                cachedGame = currentScene;
                currentScene = new ScenePause(this, io);
                break;

            case DIFFICULTY:
                currentScene = new SceneDifficulty(this);
                break;
            case NAME:
                currentScene = new SceneName(this, ps);
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

    // This would allow top level clear of progression to properly clear resources
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

    @Override
    public AssetManager getAssets() {
        return this.am;
    }
}
