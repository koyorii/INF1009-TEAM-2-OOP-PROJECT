package io.github.some_example_name.lwjgl3.Engine.sceneManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.assets.AssetManager;

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

    public enum State {MENU, GAME, PAUSE, DIFFICULTY, NAME, LEADERBOARD, STARVED, HEALTH, GOOD, TUTORIAL}

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

        if (currentScene != null && state != State.PAUSE) {
            // Only pause don't dispose of scene
            currentScene.dispose();
        }

        switch (state) {
            // First scene of the game
            case MENU:
                if (em != null) {
                    em.clearEntities();
                }
                if (ps != null) {
                    ps.reset();
                }

                cachedGame = null;
                currentScene = new SceneMenu(this);
                break;

            case LEADERBOARD:
                currentScene = new SceneLeaderboard(this, ps);
                break;

            // Scene to pick name default to Blank if empty
            case NAME:
                currentScene = new SceneName(this, ps);
                break;

            // Select difficulty scene
            case DIFFICULTY:
                currentScene = new SceneDifficulty(this);
                break;

            // If a cache of your progress exists, restore, else start new
            case GAME:
                if (cachedGame != null) {
                    currentScene = cachedGame;
                    cachedGame = null;
                } else {
                    if (em != null) {
                        em.clearEntities();
                    }
                    if (ps != null) {
                        ps.reset();
                    }
                    currentScene = new SceneGame(this, em, cm, mm, io, ps);
                }
                break;

//            case TUTORIAL:
//                if (currentScene != null) {
//                    currentScene.dispose();
//                }
//                currentScene = new SceneTutorial(this, ps);
//                break;

                // Pause scene
            case PAUSE:
                cachedGame = currentScene;
                currentScene = new ScenePause(this, io);
                break;

            case STARVED:
                currentScene = new SceneStarveEnding(this, ps);
                break;

            case HEALTH:
                currentScene = new SceneHealthEnding(this, ps);
                break;

            case GOOD:
                currentScene = new SceneGoodEnding(this, ps);
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
        System.out.println("[Scene Manager] All scene manager resources disposed.");
    }

    @Override
    public AssetManager getAssets() {
        return this.am;
    }

    @Override
    public PlayerStats getPlayerStats() {
        return this.ps;
    }

    @Override
    public io.github.some_example_name.lwjgl3.Engine.iomanager.Audio getAudio() {
        return this.io.getAudio();
    }
}
