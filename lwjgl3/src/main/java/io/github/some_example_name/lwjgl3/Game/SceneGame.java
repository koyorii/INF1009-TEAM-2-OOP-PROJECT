package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.some_example_name.lwjgl3.Engine.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;

public class SceneGame extends Scene {

    private final EntityManager em;
    private final CollisionManager cm;
    private final MovementManager mm;
    private final IOManager io;
    private final PlayerStats ps;

    private Stage stage;
    private Skin skin;
    private Player player;
    private PlayerController    playerController;
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

        this.skin = am.get("skin/craftacular-ui.json", Skin.class);
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);

        Texture normalBackground = am.get("backgrounds/normal_mode_background.jpg", Texture.class);
        menuContainer.setBackground(new TextureRegionDrawable(new TextureRegion(normalBackground)));

        stage.addActor(menuContainer);

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        io.getAudio();

        // ── Player NPC ────────────────────────────────────────────
        this.player = new Player(
            am.get("player.png", Texture.class),
            screenW / 2f - 32,
            screenH * 0.08f,
            0,
            false
        );

        // ── Scale the NPC sprite down to 64x96 pixels ─────────────
        // Adjust these two numbers to whatever looks right for your sprite.
        // The collision box will automatically match this size too.
        player.setDrawSize(64, 96);

        em.addEntity(player);

        // ── Food textures ─────────────────────────────────────────
        healthyTextures = new Texture[]{
            am.get("good_foods/apple.png", Texture.class),
            am.get("good_foods/ninjin_carrot.png", Texture.class),
            am.get("good_foods/petbottle_water_full.png", Texture.class),
        };

        junkTextures = new Texture[]{
            am.get("bad_foods/can_juice.png", Texture.class),
            am.get("bad_foods/dokukinoko_benitengu_dake.png", Texture.class),
            am.get("bad_foods/rotten_apple.png", Texture.class),
        };

        vitaminTexture = am.get("good_foods/Vitamin.png", Texture.class);

        playerController = new PlayerController(220f);
        foodSpawner      = new FoodSpawner(em, healthyTextures, junkTextures, vitaminTexture);

        // Label for instructions
        Label pauseLabel = new Label("Press Escape to Pause", skin,"default");
        pauseLabel.setFontScale(0.5f);

        // Adds label to tell player how to pause
        menuContainer.add(pauseLabel)
            .expand()      // Pushes the cell to take up all available space
            .bottom()      // Align bottom
            .left()        // Align left
            .padLeft(20)   // Padding so look a bit nicer
            .padBottom(20);
    }

    @Override
    public void update(float delta) {
        playerController.handleInput(player);
        foodSpawner.update(delta);
        em.update(mm);
        cm.update();

        stage.act(delta);

        // Escape key pauses the game
        if (io.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScene(SceneManager.State.PAUSE);
        }

        // If player dies, log their score, and send to respective scenes.
        if (ps.isDead()) {
            Gdx.app.log("Game", "Player died! Score: " + ps.getScore());
            ps.saveToLeaderboard();
            // If health drops to 0 jump to health end, cuz actually dying is worse than starvation
            if (ps.getHp() == 0 && ps.getScore() <= 7999) {
                //
            }
            // Starved end, if health is above 0 but hunger is 0
            else if (ps.getHunger() == 0 && ps.getScore() <= 7999) {
                sceneManager.setScene(SceneManager.State.STARVED);
            }
            // Survived
            else {
                //sceneManager.setScene(SceneManager.State.SURVIVED);
            }
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
        if (stage != null) {
            stage.dispose();
        }

        // Just in case
        this.skin = null;
        this.vitaminTexture = null;
        this.healthyTextures = null;
        this.junkTextures = null;

        em.clearEntities();
    }
}
