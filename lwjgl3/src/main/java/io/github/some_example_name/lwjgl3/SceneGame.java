package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.some_example_name.lwjgl3.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.entityManager.FoodSpawner;
import io.github.some_example_name.lwjgl3.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.sceneManager.SceneManager;

public class SceneGame extends Scene {

    private final EntityManager em;
    private final CollisionManager cm;
    private final MovementManager mm;
    private final IOManager io;
    private final PlayerStats ps;

    private Stage stage;
    private Skin skin;
    private TextureObject playerNPC;
    private PlayerController playerController;
    private FoodSpawner foodSpawner;

    private Texture[] healthyTextures;
    private Texture[] junkTextures;
    private Texture vitaminTexture;

    public SceneGame(ISceneManager ism, EntityManager em, CollisionManager cm, MovementManager mm, IOManager io, PlayerStats ps) {
        super(ism);
        this.em = em;
        this.cm = cm;
        this.mm = mm;
        this.io = io;
        this.ps = ps;

        // Background UI
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);

        Texture normalBackground = new Texture(Gdx.files.internal("backgrounds/normal_mode_background.jpg"));
        menuContainer.setBackground(new TextureRegionDrawable(new TextureRegion(normalBackground)));

        stage.addActor(menuContainer);

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

        em.addEntity(playerNPC);

        // ── Food textures ─────────────────────────────────────────
        healthyTextures = new Texture[]{
            new Texture("good_foods/apple.png"),
            new Texture("good_foods/ninjin_carrot.png"),
            new Texture("good_foods/petbottle_water_full.png"),
        };
        junkTextures = new Texture[]{
            new Texture("bad_foods/can_juice.png"),
            new Texture("bad_foods/dokukinoko_benitengu_dake.png"),
            new Texture("bad_foods/rotten_apple.png"),
        };
        vitaminTexture = new Texture("good_foods/Vitamin.png");

        playerController = new PlayerController(220f);
        foodSpawner      = new FoodSpawner(em, healthyTextures, junkTextures, vitaminTexture);
    }

    @Override
    public void update(float delta) {
        playerController.handleInput(playerNPC);
        foodSpawner.update(delta);
        em.update(mm);
        cm.update();
        stage.act(delta);

        if (io.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScene(SceneManager.State.PAUSE);
        }

        if (ps.isDead()) {
            Gdx.app.log("Game", "Player died! Score: " + ps.getScore());
            //TODO: sceneManager.setScene(SceneManager.State.GAMEOVER);
        }
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        // Draw background FIRST using stage (has its own internal batch)
        stage.draw();

        // Then draw all game entities on top
        // batch.begin() / end() is handled inside EntityM.draw()
        em.draw(shape, batch);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin  != null) skin.dispose();

        if (vitaminTexture != null) vitaminTexture.dispose();
        for (Texture t : healthyTextures) {
            if (t != null) t.dispose();
        }
        for (Texture t : junkTextures) {
            if (t != null) t.dispose();
        }

        em.clearEntities();
    }
}
