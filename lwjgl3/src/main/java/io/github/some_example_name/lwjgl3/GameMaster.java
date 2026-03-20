package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.Engine.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;
import io.github.some_example_name.lwjgl3.Game.FoodSpawner;
import io.github.some_example_name.lwjgl3.Game.PlayerController;
import io.github.some_example_name.lwjgl3.Game.PlayerStats;

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
        this.sceneM      = new SceneManager();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        // Inject managers into SceneManager so it can pass them to SceneGame
        ((SceneManager)sceneM).setEngineTools(EntityM, collisionM, MoveM, IoM, playerStats);

        IoM.getAudio().loadSound("catch", "catch.wav");
        IoM.getAudio().loadSound("hit",   "hit.wav");

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
        batch.dispose();
        shape.dispose();
        sceneM.dispose();
        EntityM.dispose();
        IoM.getAudio().dispose();
    }

    public ISceneManager getSceneManager() { return sceneM; }
    public PlayerStats   getPlayerStats()  { return playerStats; }
}
