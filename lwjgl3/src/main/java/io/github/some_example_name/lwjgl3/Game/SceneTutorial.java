package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.lwjgl3.Engine.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;


public class SceneTutorial extends Scene {

    // nextPhase is stored explicitly on each constant so that Normal phases
    // end at DONE without bleeding into the Fear phases, and vice versa.
    // Using ordinal() + 1 caused NORMAL_UNHEALTHY → FEAR_HEALTHY (the bug).
    private enum TutorialPhase {

        // Normal mode chain: NORMAL_HEALTHY → NORMAL_VITAMIN → NORMAL_UNHEALTHY → DONE
        NORMAL_HEALTHY(
                "Catch the HEALTHY food!",
                "Apples, carrots and water give +1 HP and +1 Hunger.",
                OnComingFood.FoodType.HEALTHY,
                3) {
            @Override
            public TutorialPhase next() {
                return NORMAL_VITAMIN;
            }
        },
        NORMAL_VITAMIN(
                "Catch the VITAMINS!",
                "Vitamins give +1 Armor. Armor absorbs junk-food hits before HP drops.",
                OnComingFood.FoodType.VITAMIN,
                2) {
            @Override
            public TutorialPhase next() {
                return NORMAL_UNHEALTHY;
            }
        },
        NORMAL_UNHEALTHY(
                "Watch out for JUNK FOOD!",
                "Junk food drains Armor first, then HP if Armor is 0.",
                OnComingFood.FoodType.UNHEALTHY,
                2) {
            @Override
            public TutorialPhase next() {
                return DONE;
            }
        },

        // Fear & Hunger chain: FEAR_HEALTHY → FEAR_VITAMIN → FEAR_UNHEALTHY → DONE
        FEAR_HEALTHY(
                "Catch HEALTHY food — fast!",
                "In Fear & Hunger you start with only 2 HP.\nHealthy food gives +1 HP and +1 Hunger.",
                OnComingFood.FoodType.HEALTHY,
                3) {
            @Override
            public TutorialPhase next() {
                return FEAR_UNHEALTHY;
            }
        },
        FEAR_UNHEALTHY(
                "DODGE the JUNK FOOD!",
                "Junk food hits hard with low HP.\nFood also gets faster over time — stay sharp!",
                OnComingFood.FoodType.UNHEALTHY,
                2) {
            @Override
            public TutorialPhase next() {
                return DONE;
            }
        },

        // Shared terminal — next() loops to itself; SceneTutorial exits before calling
        // it
        DONE(
                "Tutorial Complete!",
                "Starting game...",
                null,
                0) {
            @Override
            public TutorialPhase next() {
                return DONE;
            }
        };

        final String title;
        final String description;
        final OnComingFood.FoodType foodType;
        final int catchGoal;

        TutorialPhase(String title, String description,
                OnComingFood.FoodType foodType, int catchGoal) {
            this.title = title;
            this.description = description;
            this.foodType = foodType;
            this.catchGoal = catchGoal;
        }

        // Each constant overrides next() explicitly — no ordinal arithmetic
        public abstract TutorialPhase next();
    }

    // Engine tools injected through ISceneManager (Dependency Inversion)
    private final EntityManager em;
    private final CollisionManager cm;
    private final MovementManager mm;
    private final IOManager io;

    // Isolated stats so tutorial catches never affect the real game state
    private final PlayerStats tutorialStats;

    // Abstract Factory instances — reused from the game layer
    private final iFoodFactory healthyFactory;
    private final iFoodFactory vitaminFactory;
    private final iFoodFactory unhealthyFactory;

    private final Player player;


    private final Stage stage;
    private Skin skin;
    private final Label titleLabel;
    private final Label descriptionLabel;
    private final Label progressLabel;

    // Phase state — all mutation goes through advancePhase() (Encapsulation)
    private TutorialPhase currentPhase;
    private int catchCount;
    private float doneTimer;
    private FoodSpawner spawner;

    private static final float DONE_DISPLAY_SECONDS = 2.5f;

    public SceneTutorial(ISceneManager ism,
            EntityManager em,
            CollisionManager cm,
            MovementManager mm,
            IOManager io,
            PlayerStats.GameMode mode) {
        super(ism);

        this.em = em;
        this.cm = cm;
        this.mm = mm;
        this.io = io;

        this.tutorialStats = new PlayerStats();
        this.tutorialStats.setMode(mode);

        // Build food factories (Abstract Factory pattern)
        Texture[] healthyTextures = {
                am.get("good_foods/apple.png", Texture.class),
                am.get("good_foods/ninjin_carrot.png", Texture.class),
                am.get("good_foods/petbottle_water_full.png", Texture.class)
        };
        Texture[] junkTextures = {
                am.get("bad_foods/can_juice.png", Texture.class),
                am.get("bad_foods/dokukinoko_benitengu_dake.png", Texture.class),
                am.get("bad_foods/rotten_apple.png", Texture.class)
        };
        Texture vitaminTexture = am.get("good_foods/Vitamin.png", Texture.class);

        this.healthyFactory = new HealthyFoodFactory(healthyTextures);
        this.vitaminFactory = new VitaminFactory(vitaminTexture);
        this.unhealthyFactory = new UnhealthyFoodFactory(junkTextures);

        // Player NPC
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        this.player = new Player(
                am.get("player.png", Texture.class),
                screenW / 2f - 32f,
                screenH * 0.08f,
                220f,
                false);
        player.setDrawSize(64, 96);
        em.addEntity(player);

        // UI — background differs per mode
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = am.get("skin/craftacular-ui.json", Skin.class);

        Table root = new Table();
        root.setFillParent(true);

        if (mode == PlayerStats.GameMode.FEARLESS_HUNGER) {
            Texture bg = am.get("backgrounds/fear_and_hunger_background.jpg", Texture.class);
            root.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));
        } else {
            root.setBackground(skin.getDrawable("dirt"));
        }

        stage.addActor(root);

        Label header = new Label("TUTORIAL", skin, "title");
        header.setFontScale(0.45f);

        Label modeTag = new Label(
                mode == PlayerStats.GameMode.FEARLESS_HUNGER ? "[ Fear & Hunger ]" : "[ Normal ]",
                skin, "default");
        modeTag.setFontScale(0.38f);

        titleLabel = new Label("", skin, "default");
        descriptionLabel = new Label("", skin, "default");
        progressLabel = new Label("", skin, "default");

        titleLabel.setFontScale(0.55f);
        descriptionLabel.setFontScale(0.40f);
        progressLabel.setFontScale(0.45f);

        TextButton skipBtn = new TextButton("Skip Tutorial", skin, "default");
        skipBtn.getLabel().setFontScale(0.4f);
        skipBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                sceneManager.setScene(SceneManager.State.GAME);
            }
        });

        root.top().pad(20f);
        root.add(header).colspan(2).padBottom(4f);
        root.row();
        root.add(modeTag).colspan(2).padBottom(10f);
        root.row();
        root.add(titleLabel).colspan(2).padBottom(6f);
        root.row();
        root.add(descriptionLabel).colspan(2).padBottom(8f);
        root.row();
        root.add(progressLabel).colspan(2).padBottom(20f);
        root.row();
        root.add(skipBtn).colspan(2).width(220f).height(40f);

        // Start on the first phase for this mode
        currentPhase = (mode == PlayerStats.GameMode.FEARLESS_HUNGER)
                ? TutorialPhase.FEAR_HEALTHY
                : TutorialPhase.NORMAL_HEALTHY;
        catchCount = 0;
        beginPhase();

        audio.playMusic("menu");
    }

    // Initialises labels and a fresh FoodSpawner for the current phase
    private void beginPhase() {
        catchCount = 0;
        doneTimer = 0f;

        titleLabel.setText(currentPhase.title);
        descriptionLabel.setText(currentPhase.description);
        refreshProgress();

        if (currentPhase == TutorialPhase.DONE) {
            spawner = null;
            return;
        }

        spawner = new FoodSpawner(em);
        spawner.addFactoryToPool(getFactoryForPhase(currentPhase));
    }

    // Maps each phase to its Abstract Factory via food type
    private iFoodFactory getFactoryForPhase(TutorialPhase phase) {
        if (phase.foodType == OnComingFood.FoodType.HEALTHY)
            return healthyFactory;
        if (phase.foodType == OnComingFood.FoodType.VITAMIN)
            return vitaminFactory;
        if (phase.foodType == OnComingFood.FoodType.UNHEALTHY)
            return unhealthyFactory;
        return healthyFactory;
    }

    // Clears food entities, advances to next phase using the explicit chain
    private void advancePhase() {
        em.clearEntities();
        em.addEntity(player);
        currentPhase = currentPhase.next();
        beginPhase();
    }

    private void refreshProgress() {
        if (currentPhase == TutorialPhase.DONE) {
            progressLabel.setText("");
        } else {
            progressLabel.setText("Progress: " + catchCount + " / " + currentPhase.catchGoal);
        }
    }

    // Polls entity list for deactivated food — avoids coupling to ResolveCollision
    // internals
    private void checkForCaughtFood() {
        for (io.github.some_example_name.lwjgl3.Engine.entityManager.Entity entity : em.getEntities()) {
            if (!(entity instanceof OnComingFood))
                continue;
            OnComingFood food = (OnComingFood) entity;

            if (!food.isActive() && !food.isCountedByTutorial()) {
                food.markCountedByTutorial();
                catchCount++;
                refreshProgress();

                if (catchCount >= currentPhase.catchGoal) {
                    advancePhase();
                    return;
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        stage.act(delta);

        if (io.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScene(SceneManager.State.GAME);
            return;
        }

        if (currentPhase == TutorialPhase.DONE) {
            doneTimer += delta;
            if (doneTimer >= DONE_DISPLAY_SECONDS) {
                sceneManager.setScene(SceneManager.State.GAME);
            }
            return;
        }

        spawner.update(delta);
        em.update(mm);
        cm.update();
        checkForCaughtFood();
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        stage.draw();
        em.draw(shape, batch);
    }

    @Override
    public void dispose() {
        if (stage != null)
            stage.dispose();
        this.skin = null;
        em.clearEntities();
    }
}
