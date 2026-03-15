package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.entityManager.FoodSpawner;
import io.github.some_example_name.lwjgl3.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.sceneManager.SceneManager;

public class GameMaster extends ApplicationAdapter {

    private SpriteBatch   batch;
    private ShapeRenderer shape;

    private ISceneManager sceneM;

    protected IOManager        IoM;
    protected MovementManager  MoveM;
    protected EntityManager    EntityM;
    protected CollisionManager collisionM;

    protected PlayerStats      playerStats;
    protected PlayerController playerController;
    protected FoodSpawner      foodSpawner;

    private Texture[] healthyTextures;
    private Texture[] junkTextures;
    private Texture   vitaminTexture;

    protected TextureObject playerNPC;  // protected so SceneGame can reference if needed

    public GameMaster() {
        this.IoM         = new IOManager();
        this.MoveM       = new MovementManager(IoM);
        this.EntityM     = new EntityManager();
        this.playerStats = new PlayerStats();
        this.collisionM  = new CollisionManager(EntityM, MoveM, IoM, playerStats);
        this.sceneM      = new SceneManager(this);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        IoM.getAudio().loadSound("catch", "catch.wav");
        IoM.getAudio().loadSound("hit",   "hit.wav");

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // ── Player NPC ────────────────────────────────────────────
        playerNPC = new TextureObject(
            "npc.png",
            screenW / 2f - 32,  // centered X
            screenH * 0.08f,    // near bottom
            0,
            false
        );

        // ── Scale the NPC sprite down to 64x96 pixels ─────────────
        // Adjust these two numbers to whatever looks right for your sprite.
        // The collision box will automatically match this size too.
        playerNPC.setDrawSize(64, 96);

        EntityM.addEntity(playerNPC);

        // ── Food textures ─────────────────────────────────────────
        healthyTextures = new Texture[]{
            new Texture("good_foods\\apple.png"),
            new Texture("good_foods\\ninjin_carrot.png"),
            new Texture("good_foods\\petbottle_water_full.png"),
        };
        junkTextures = new Texture[]{
            new Texture("bad_foods\\can_juice.png"),
            new Texture("bad_foods\\dokukinoko_benitengu_dake.png"),
            new Texture("bad_foods\\rotten_apple.png"),
        };
        vitaminTexture = new Texture("good_foods\\Vitamin.png");

        playerController = new PlayerController(220f);
        foodSpawner      = new FoodSpawner(EntityM, healthyTextures, junkTextures, vitaminTexture);

        sceneM.setScene(SceneManager.State.MENU);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Only run game logic when in GAME scene (not menu/pause)
        if (sceneM instanceof io.github.some_example_name.lwjgl3.sceneManager.SceneManager) {
            io.github.some_example_name.lwjgl3.sceneManager.SceneManager sm =
                (io.github.some_example_name.lwjgl3.sceneManager.SceneManager) sceneM;
            if (sm.getCurrentScene() instanceof SceneGame) {
                playerController.handleInput(playerNPC);
                foodSpawner.update(delta);
            }
        }

        sceneM.update(delta);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        sceneM.render(shape, batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        sceneM.dispose();
        EntityM.dispose();
        IoM.getAudio().dispose();

        if (healthyTextures != null)
            for (Texture t : healthyTextures) t.dispose();
        if (junkTextures != null)
            for (Texture t : junkTextures)    t.dispose();
        if (vitaminTexture != null)
            vitaminTexture.dispose();
    }

    public ISceneManager getSceneManager() { return sceneM; }
    public PlayerStats   getPlayerStats()  { return playerStats; }
}