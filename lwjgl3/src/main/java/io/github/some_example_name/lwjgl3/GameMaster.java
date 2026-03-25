package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.Engine.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;
import io.github.some_example_name.lwjgl3.Game.PlayerStats;

public class GameMaster extends ApplicationAdapter {

    private SpriteBatch batch;
    private ShapeRenderer shape;

    private ISceneManager sceneM;

    private IOManager ioM;
    private MovementManager moveM;
    private EntityManager entityM;
    private CollisionManager collisionM;
    private AssetManager assetM;
    private PlayerStats playerStats;

    public GameMaster() {
        this.ioM       = new IOManager();
        this.moveM     = new MovementManager(ioM);
        this.entityM   = new EntityManager();
        this.playerStats = new PlayerStats();
        this.collisionM  = new CollisionManager(entityM, moveM, ioM, playerStats);
        this.sceneM    = new SceneManager();
        this.assetM    = new AssetManager();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        // This loads the JSON, the Atlas, and the Texture all at once
        assetM.load("skin/craftacular-ui.json", Skin.class);

        // Backgrounds
        assetM.load("backgrounds/normal_mode_background.jpg", Texture.class);
        assetM.load("backgrounds/fear_and_hunger_background.jpg", Texture.class);
        assetM.load("backgrounds/home_background.jpg", Texture.class);
        assetM.load("backgrounds/paintblack.jpg", Texture.class);

        // End images
        assetM.load("bad_starvation_ending.png", Texture.class);
        assetM.load("bad_health_ending.png", Texture.class);
        assetM.load("good_ending_npc.png", Texture.class);

        // Player
        assetM.load("player.png", Texture.class);

        // Good food
        assetM.load("good_foods/apple.png", Texture.class);
        assetM.load("good_foods/ninjin_carrot.png", Texture.class);
        assetM.load("good_foods/petbottle_water_full.png", Texture.class);

        // Unhealthy food
        assetM.load("bad_foods/can_juice.png", Texture.class);
        assetM.load("bad_foods/dokukinoko_benitengu_dake.png", Texture.class);
        assetM.load("bad_foods/rotten_apple.png", Texture.class);

        // Vitamin / armour
        assetM.load("good_foods/Vitamin.png", Texture.class);

        // HUD icons
        assetM.load("hud/heart.png",    Texture.class);
        assetM.load("hud/heart-bg.png", Texture.class);
        assetM.load("hud/armor.png",    Texture.class);
        assetM.load("hud/armor-bg.png", Texture.class);

        // Block until all assets finish loading
        assetM.finishLoading();

        // ── Audio ─────────────────────────────────────────────────
        ioM.getAudio().loadMusic("menu",        "audio/bgm_menu.mp3");
        ioM.getAudio().loadMusic("game",        "audio/bgm_game.mp3");
        ioM.getAudio().loadMusic("ending_good", "audio/bgm_ending_good.mp3");
        ioM.getAudio().loadMusic("ending_bad",  "audio/bgm_ending_bad.mp3");

        ioM.getAudio().loadSound("eat_healthy",  "audio/sfx_eat_healthy.mp3");
        ioM.getAudio().loadSound("eat_junk",     "audio/sfx_eat_junk.mp3");
        ioM.getAudio().loadSound("eat_vitamin",  "audio/sfx_eat_vitamin.mp3");
        ioM.getAudio().loadSound("button_click", "audio/sfx_button_click.mp3");
        ioM.getAudio().loadSound("game_over",    "audio/sfx_game_over.mp3");

        // Inject engine tools into SceneManager via interface
        sceneM.setEngineTools(entityM, collisionM, moveM, ioM, playerStats, assetM);

        sceneM.setScene(SceneManager.State.MENU);
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
        if (assetM   != null) assetM.dispose();
        if (batch    != null) batch.dispose();
        if (shape    != null) shape.dispose();
        if (sceneM   != null) sceneM.dispose();
        if (entityM  != null) entityM.dispose();
        if (ioM != null && ioM.getAudio() != null) ioM.getAudio().dispose();
    }

    public ISceneManager getSceneManager() { return sceneM; }
    public PlayerStats   getPlayerStats()  { return playerStats; }
}
