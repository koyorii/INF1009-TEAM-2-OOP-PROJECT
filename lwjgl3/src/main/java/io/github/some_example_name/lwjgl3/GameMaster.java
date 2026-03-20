package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.assets.AssetManager;

import io.github.some_example_name.lwjgl3.Game.FoodSpawner;
import io.github.some_example_name.lwjgl3.Game.PlayerController;
import io.github.some_example_name.lwjgl3.Game.PlayerStats;

public class GameMaster extends ApplicationAdapter {

    private SpriteBatch batch;
    private ShapeRenderer shape;

    private ISceneManager sceneM;

    protected IOManager IoM;
    protected MovementManager MoveM;
    protected EntityManager EntityM;
    protected CollisionManager collisionM;
    protected AssetManager assetM;

    protected PlayerStats playerStats;
    protected PlayerController playerController;
    protected FoodSpawner foodSpawner;

    private Texture[] healthyTextures;
    private Texture[] junkTextures;
    private Texture vitaminTexture;

    protected TextureObject playerNPC;  // protected so SceneGame can reference if needed

    public GameMaster() {
        this.IoM = new IOManager();
        this.MoveM = new MovementManager(IoM);
        this.EntityM = new EntityManager();
        this.playerStats = new PlayerStats();
        this.collisionM = new CollisionManager(EntityM, MoveM, IoM, playerStats);
        this.sceneM = new SceneManager();
        this.assetM = new AssetManager();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        // This loads the JSON, the Atlas, and the Texture all at once
        assetM.load("skin/craftacular-ui.json", Skin.class);

        // Background
        assetM.load("backgrounds/normal_mode_background.jpg", Texture.class);
        // To-do Fear and Hunger

        // Player
        assetM.load("player.png", Texture.class);

        // Good Food
        assetM.load("good_foods/apple.png", Texture.class);
        assetM.load("good_foods/ninjin_carrot.png", Texture.class);
        assetM.load("good_foods/petbottle_water_full.png", Texture.class);

        // Unhealthy Food
        assetM.load("bad_foods/can_juice.png", Texture.class);
        assetM.load("bad_foods/dokukinoko_benitengu_dake.png", Texture.class);
        assetM.load("bad_foods/rotten_apple.png", Texture.class);

        // Vitamin armour
        assetM.load("good_foods/Vitamin.png", Texture.class);

        // Block until finish loading
        assetM.finishLoading();

        // Inject into sceneManager via interface
        sceneM.setEngineTools(EntityM, collisionM, MoveM, IoM, playerStats, assetM);

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
        if (assetM != null) {
            assetM.dispose();
        }
        if (batch != null) {
            batch.dispose();
        };

        if (shape != null) {
            shape.dispose();
        }
        if (sceneM != null) {
            sceneM.dispose();
        }
        if (EntityM != null) {
            EntityM.dispose();
        }
        if (IoM.getAudio() != null) {
            IoM.getAudio().dispose();
        }
    }

    public ISceneManager getSceneManager() { return sceneM; }
    public PlayerStats   getPlayerStats()  { return playerStats; }
}
