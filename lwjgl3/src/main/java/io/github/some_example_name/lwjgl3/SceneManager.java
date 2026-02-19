package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// implements ISceneManager interface (abstraction)
public class SceneManager implements ISceneManager {

     // all fields are private (encapsulation)
    private final GameMaster gm;
    private Scene currentScene;
    private Scene cachedGame;

    public enum State { MENU, GAME, PAUSE }

    public SceneManager(GameMaster gm) {
        this.gm = gm;
    }

    // Function to set scenes accordingly to conditions
    @Override
    public void setScene(State state) {
        switch (state) {
            case MENU:
                if (currentScene != null) {
                    currentScene.dispose();
                }
                currentScene = new SceneMenu(gm);
                break;
             // If a cache of your progress exists, restore, else start new
            case GAME:
                if (cachedGame != null) {
                    currentScene = cachedGame;
                    cachedGame = null;
                    Gdx.input.setInputProcessor(null);
                } else {
                    
                    currentScene = new SceneGame(gm);
                }
                break;
            case PAUSE:
                cachedGame = currentScene;
                currentScene = new ScenePause(gm);
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