package io.github.some_example_name.lwjgl3.Engine.sceneManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.assets.AssetManager;

import io.github.some_example_name.lwjgl3.Engine.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.Game.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import io.github.some_example_name.lwjgl3.Game.SceneTutorial;


public class SceneManager implements ISceneManager {

    private Scene currentScene;
    private Scene cachedGame;

    public enum State { MENU, GAME, PAUSE, DIFFICULTY, NAME, LEADERBOARD, STARVED, HEALTH, GOOD, TUTORIAL }

    private EntityManager    em;
    private CollisionManager cm;
    private MovementManager  mm;
    private IOManager        io;
    private PlayerStats      ps;
    private AssetManager     am;

    // Preferences file shared across sessions
    private static final String PREFS_NAME = "SustenancePrefs";

    // Key pattern: "seen_tutorial_<username>_<mode>"
    // This ensures every new username gets a tutorial for each mode,
    // but returning players skip it automatically.
    private String buildTutorialKey(String username, PlayerStats.GameMode mode) {
        String safeUser = (username == null || username.trim().isEmpty()) ? "Guest" : username.trim();
        String modeSuffix = (mode == PlayerStats.GameMode.FEARLESS_HUNGER) ? "fear" : "normal";
        return "seen_tutorial_" + safeUser.toLowerCase() + "_" + modeSuffix;
    }

    public SceneManager() {}

    @Override
    public void setEngineTools(EntityManager em, CollisionManager cm,
                               MovementManager mm, IOManager io,
                               PlayerStats ps, AssetManager am) {
        this.em = em;
        this.cm = cm;
        this.mm = mm;
        this.io = io;
        this.ps = ps;
        this.am = am;
    }

    @Override
    public void setScene(State state) {
        if (currentScene != null && state != State.PAUSE) {
            currentScene.dispose();
        }

        switch (state) {
            case MENU:
                if (em != null) em.clearEntities();
                if (ps != null) ps.reset();
                cachedGame = null;
                currentScene = new SceneMenu(this);
                break;

            case LEADERBOARD:
                currentScene = new SceneLeaderboard(this, ps);
                break;

            case NAME:
                currentScene = new SceneName(this, ps);
                break;

            case DIFFICULTY:
                currentScene = new SceneDifficulty(this);
                break;

            case TUTORIAL:
                // Should not be called directly — use startGame(mode) instead.
                // Fallback: show Normal tutorial if somehow reached.
                if (em != null) em.clearEntities();
                if (ps != null) ps.reset();
                currentScene = new SceneTutorial(this, em, cm, mm, io,
                    PlayerStats.GameMode.NORMAL);
                break;

            case GAME:
                if (cachedGame != null) {
                    currentScene = cachedGame;
                    cachedGame = null;
                } else {
                    if (em != null) em.clearEntities();
                    if (ps != null) ps.reset();
                    currentScene = new SceneGame(this, em, cm, mm, io, ps);
                }
                break;

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

    /**
     * Called by SceneDifficulty for both Normal and Fear & Hunger.
     *
     * Tutorial logic:
     *  - Key = "seen_tutorial_<username>_<mode>" stored in libGDX Preferences.
     *  - If the key is absent (new username OR first time picking this mode),
     *    show the tutorial and write the key so the next run skips it.
     *  - If the key exists (same username played this mode before), go straight to GAME.
     *
     * This means:
     *  - "Alice" playing Normal for the first time → tutorial shown.
     *  - "Alice" playing Normal again → tutorial skipped.
     *  - "Alice" switching to Fear & Hunger → tutorial shown (different key).
     *  - "Bob" on the same machine → tutorial shown (different username key).
     */
    @Override
    public void startGame(PlayerStats.GameMode mode) {
        ps.setMode(mode);

        String key = buildTutorialKey(ps.getName(), mode);
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);

        // Always show tutorial for guest player, they can skip if they want
        boolean isBlankName = ps.getName().equals("Blank") || ps.getName().trim().isEmpty();

        boolean hasSeen;
        if (isBlankName) {
            hasSeen = false;
            prefs.remove(key);
            prefs.flush();
        } else {
            hasSeen = prefs.getBoolean(key, false);
        }

        if (!hasSeen) {
            if (!isBlankName) {
                prefs.putBoolean(key, true);
                prefs.flush();
            }

            if (currentScene != null) currentScene.dispose();
            if (em != null) em.clearEntities();
            if (ps != null) ps.reset();

            currentScene = new SceneTutorial(this, em, cm, mm, io, mode);

        } else {
            if (em != null) em.clearEntities();
            if (ps != null) ps.reset();
            if (currentScene != null) currentScene.dispose();

            currentScene = new SceneGame(this, em, cm, mm, io, ps);
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
        System.out.println("[Scene Manager] All scene manager resources disposed.");
    }

    @Override
    public AssetManager getAssets() { return this.am; }

    @Override
    public PlayerStats getPlayerStats() { return this.ps; }

    @Override
    public io.github.some_example_name.lwjgl3.Engine.iomanager.Audio getAudio() {
        return this.io.getAudio();
    }
}
