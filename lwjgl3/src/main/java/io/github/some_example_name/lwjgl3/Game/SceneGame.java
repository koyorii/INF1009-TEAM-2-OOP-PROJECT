package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    private boolean isGameOver = false;

    private final EntityManager em;
    private final CollisionManager cm;
    private final MovementManager mm;
    private final IOManager io;
    private final PlayerStats ps;

    private final Stage stage;
    private Skin skin;
    private final Player player;
    private final FoodSpawner foodSpawner;
    private final HudOverlay hud;

    // Normal mode countdown; -1 means no timer (Fearless Hunger)
    private float timeLeft;

    // private final FloatingTextManager floatingTextManager;
    private final BitmapFont gameFont;

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

        this.skin = am.get("skin/craftacular-ui.json", Skin.class);
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
                220f,
                false);

        // ── Scale the NPC sprite down to 64x96 pixels ─────────────
        // Adjust these two numbers to whatever looks right for your sprite.
        // The collision box will automatically match this size too.
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

        foodSpawner = new FoodSpawner(em);
        hud = new HudOverlay(am, ps);

        iFoodFactory healthyFactory = new HealthyFoodFactory(healthyTextures);
        iFoodFactory unhealthFoodFactory = new UnhealthyFoodFactory(junkTextures);
        iFoodFactory vitaminFactory = new VitaminFactory(vitaminTexture);

        if (ps.getMode() == PlayerStats.GameMode.FEARLESS_HUNGER) {

            // Fill the Spawner's pool to create your percentages (10 items total)
            // 50% chance (5 out of 10)
            for (int i = 0; i < 5; i++)
                foodSpawner.addFactoryToPool(healthyFactory);

            // 30% chance (3 out of 10)
            for (int i = 0; i < 3; i++)
                foodSpawner.addFactoryToPool(unhealthFoodFactory);

            // 20% chance (2 out of 10)
            for (int i = 0; i < 2; i++)
                foodSpawner.addFactoryToPool(vitaminFactory);

            // Fearless Hunger: ramp up speed/frequency over time
            foodSpawner.enableEscalation();
        } else {
            // Normal mode: skew spawns toward bad food (20% healthy, 60% unhealthy, 20%
            // vitamin)
            // Fill the Spawner's pool to create your percentages (10 items total)
            // 50% chance (5 out of 10)
            for (int i = 0; i < 2; i++)
                foodSpawner.addFactoryToPool(healthyFactory);

            // 30% chance (3 out of 10)
            for (int i = 0; i < 6; i++)
                foodSpawner.addFactoryToPool(unhealthFoodFactory);

            // 20% chance (2 out of 10)
            for (int i = 0; i < 2; i++)
                foodSpawner.addFactoryToPool(vitaminFactory);
        }

        // ── Audio ─────────────────────────────────────────────────
        // Play different BGM depending on difficulty
        if (ps.getMode() == PlayerStats.GameMode.FEARLESS_HUNGER) {
            audio.playMusic("game_hard");
        } else {
            audio.playMusic("game");
        }

        gameFont = new BitmapFont();
        gameFont.getData().setScale(1.5f);
        // floatingTextManager = new FloatingTextManager(gameFont);

        // cm.getResolver().setFloatingTextManager(floatingTextManager);

        // Label for instructions
        Label pauseLabel = new Label("Press Escape to Pause", skin, "default");
        pauseLabel.setFontScale(0.5f);

        // Adds label to tell player how to pause
        menuContainer.add(pauseLabel)
                .expand() // Pushes the cell to take up all available space
                .top() // Alignment
                .right()
                .padRight(20) // Padding so look a bit nicer
                .padTop(20);
    }

    @Override
    public void update(float delta) {

        foodSpawner.update(delta);
        em.update(mm);
        cm.update();
        stage.act(delta);
        // floatingTextManager.update(delta, ps);

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
        if (ps.isDead() && !isGameOver) {
            isGameOver = true;
            Gdx.app.log("Game", "Player died! Score: " + ps.getScore());
            ps.saveToLeaderboard();
            if (ps.getHp() == 0) {
                // Died from damage
                sceneManager.setScene(SceneManager.State.HEALTH);
            } else if (ps.getHunger() == 0 && ps.getScore() <= 1700) {
                // Starved AND score was too low to survive
                sceneManager.setScene(SceneManager.State.STARVED);
            } else {
                // Starved but with a decent score — still a bad health ending
                sceneManager.setScene(SceneManager.State.GOOD);
            }
        }
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        stage.draw();
        em.draw(shape, batch);
        hud.render(batch, timeLeft);

        // Draw floating popups

    }

    @Override
    public void dispose() {
        if (stage != null)
            stage.dispose();
        if (hud != null)
            hud.dispose();

        // Just in case
        this.skin = null;

        em.clearEntities();
    }
}
