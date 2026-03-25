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

    private static final float GAME_DURATION = 120f; // 2 minutes for Normal mode

    private final EntityManager em;
    private final CollisionManager cm;
    private final MovementManager mm;
    private final IOManager io;
    private final PlayerStats ps;

    private final Stage stage;
    private final Player player;
    private final PlayerController playerController;
    private final FoodSpawner foodSpawner;
    private final HudOverlay hud;

    // Normal mode countdown; -1 means no timer (Fearless Hunger)
    private float timeLeft;

    public SceneGame(ISceneManager ism, EntityManager em, CollisionManager cm, MovementManager mm, IOManager io,
            PlayerStats ps) {
        super(ism);
        this.em = em;
        this.cm = cm;
        this.mm = mm;
        this.io = io;
        this.ps = ps;

        // ── Timer ─────────────────────────────────────────────────
        timeLeft = ps.hasTimeLimit() ? GAME_DURATION : -1f;

        // ── Background — pick based on difficulty ─────────────────
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = am.get("skin/craftacular-ui.json", Skin.class);
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);

        String bgKey = (ps.getMode() == PlayerStats.GameMode.FEARLESS_HUNGER)
                ? "backgrounds/fear_and_hunger_background.jpg"
                : "backgrounds/normal_mode_background.jpg";
        Texture background = am.get(bgKey, Texture.class);
        menuContainer.setBackground(new TextureRegionDrawable(new TextureRegion(background)));

        stage.addActor(menuContainer);

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // ── Player NPC ────────────────────────────────────────────
        this.player = new Player(
                am.get("player.png", Texture.class),
                screenW / 2f - 32,
                screenH * 0.08f,
                0,
                false);
        player.setDrawSize(64, 96);
        em.addEntity(player);

        // ── Food textures (local — only needed to build the spawner) ─
        Texture[] healthyTextures = {
                am.get("good_foods/apple.png", Texture.class),
                am.get("good_foods/ninjin_carrot.png", Texture.class),
                am.get("good_foods/petbottle_water_full.png", Texture.class),
        };
        Texture[] junkTextures = {
                am.get("bad_foods/can_juice.png", Texture.class),
                am.get("bad_foods/dokukinoko_benitengu_dake.png", Texture.class),
                am.get("bad_foods/rotten_apple.png", Texture.class),
        };
        Texture vitaminTexture = am.get("good_foods/Vitamin.png", Texture.class);

        playerController = new PlayerController(220f);
        foodSpawner = new FoodSpawner(em, healthyTextures, junkTextures, vitaminTexture);
        hud = new HudOverlay(am, ps);

        if (ps.getMode() == PlayerStats.GameMode.FEARLESS_HUNGER) {
            // Fearless Hunger: ramp up speed/frequency over time
            foodSpawner.enableEscalation();
        } else {
            // Normal mode: skew spawns toward bad food (20% healthy, 60% unhealthy, 20% vitamin)
            foodSpawner.enableHeavyJunk();
        }

        // ── Audio ─────────────────────────────────────────────────
        audio.playMusic("game");

        // ── Pause hint label ──────────────────────────────────────
        Label pauseLabel = new Label("Press Escape to Pause", skin, "default");
        pauseLabel.setFontScale(0.5f);
        menuContainer.add(pauseLabel)
                .expand()
                .bottom()
                .left()
                .padLeft(20)
                .padBottom(20);
    }

    @Override
    public void update(float delta) {
        playerController.handleInput(player);
        foodSpawner.update(delta);
        em.update(mm);
        cm.update();
        stage.act(delta);

        // Escape → pause
        if (io.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScene(SceneManager.State.PAUSE);
        }

        // ── Normal mode countdown ─────────────────────────────────
        if (ps.hasTimeLimit()) {
            timeLeft -= delta;
            if (timeLeft <= 0) {
                // Survived the full 2 minutes → good ending
                Gdx.app.log("Game", "Time's up! Player survived. Score: " + ps.getScore());
                ps.saveToLeaderboard();
                sceneManager.setScene(SceneManager.State.GOOD);
                return;
            }
        }

        // ── Death check ───────────────────────────────────────────
        if (ps.isDead()) {
            Gdx.app.log("Game", "Player died! Score: " + ps.getScore());
            ps.saveToLeaderboard();
            if (ps.getHp() == 0) {
                // Died from damage
                sceneManager.setScene(SceneManager.State.HEALTH);
            } else if (ps.getHunger() == 0 && ps.getScore() <= 1000) {
                // Starved AND score was too low to survive
                sceneManager.setScene(SceneManager.State.STARVED);
            } else {
                // Starved but with a decent score — still a bad health ending
                sceneManager.setScene(SceneManager.State.HEALTH);
            }
        }
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        stage.draw();
        em.draw(shape, batch);
        hud.render(batch, timeLeft);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (hud   != null) hud.dispose();
        em.clearEntities();
    }
}
