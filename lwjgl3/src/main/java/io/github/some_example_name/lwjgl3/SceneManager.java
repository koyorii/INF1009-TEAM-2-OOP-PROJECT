package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.HashMap;
import java.util.Map;

// abstraction via interface
public class SceneManager implements ISceneManager {

    // all fields are private (encapsulation)
    private final GameMaster gm;
    private Scene currentScene;
    private Scene cachedGame;
    private final Map<String, Scene> sceneRegistry; // stores registered scenes

    public enum State { MENU, GAME, PAUSE }

    public SceneManager(GameMaster gm) {
        this.gm = gm;
        this.sceneRegistry = new HashMap<>();
    }



    // Function to set scenes accordingly to conditions
    public void setScene(State state) {
        switch (state) {
            case MENU:
                if (currentScene != null) {
                    currentScene.dispose();
                }
                currentScene = new SceneMenu(gm);
                break;


            case GAME:
                // If a cache of your progress exists, restore, else start new
                if (cachedGame != null) {
                    currentScene = cachedGame;
                    cachedGame = null; // Reset


                    Gdx.input.setInputProcessor(null);
                }
                else {
                    if (currentScene != null) {
                        currentScene.dispose();
                    }
                    currentScene = new SceneGame(gm);
                }
                break;


            case PAUSE:
                cachedGame = currentScene;
                currentScene = new ScenePause(gm);
                break;
        }
    }


    // hides dispose logic (encapsulation)
    private void disposeCurrentScene() {
        if (currentScene != null) {
            currentScene.dispose();
            currentScene = null;
        }
    }

    // implementing interface methods
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
        disposeCurrentScene();
        if (cachedGame != null) {
            cachedGame.dispose();
        }
    }

   // exposes current scene through interface for external access
    public Scene getCurrentScene() {
        return currentScene;
    }

    // check if a specific state is currently active (by enum) (overload)
    public boolean isCurrentScene(State state) {
        return currentScene != null && currentScene.getClass().getSimpleName()
               .equalsIgnoreCase("Scene" + state.name().charAt(0) 
               + state.name().substring(1).toLowerCase());
    }

    //check by class type (overload)
    public boolean isCurrentScene(Class<? extends Scene> sceneClass) {
        return currentScene != null && currentScene.getClass() == sceneClass;
    }
}