package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SceneManager {
    private GameMaster gm;
    private Scene currentScene;
    private Scene cachedGame;

    // All the possible states of our game
    public enum State {MENU, GAME, PAUSE};

    public SceneManager(GameMaster gm) {
        this.gm = gm;
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

    // Runs the logic for the active scene
    public void update(float delta) {
        if (currentScene != null) {
            currentScene.update(delta);
        }
    }

    // Handles the drawing, with capability to add overlay like in the pause scene
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        if (cachedGame != null) {
            cachedGame.render(shape, batch);
        }

        if (currentScene != null) {
            currentScene.render(shape, batch);
        }
    }

    // Cleans up resources when done with them
    public void dispose() {
        if (currentScene != null) {
            currentScene.dispose();
        }
    }
}
