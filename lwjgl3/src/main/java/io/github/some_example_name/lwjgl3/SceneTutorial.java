package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
<<<<<<< Updated upstream
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.some_example_name.lwjgl3.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.entityManager.Entity;
import io.github.some_example_name.lwjgl3.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.entityManager.OnComingFood;
import io.github.some_example_name.lwjgl3.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.sceneManager.SceneManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SceneTutorial extends Scene {

    
    private static final int   HEALTHY_GOAL   = 1;
    private static final int   VITAMIN_GOAL   = 1;
    private static final int   JUNK_GOAL      = 1;
    private static final float SPAWN_INTERVAL = 2.0f;
    private static final float FOOD_SPEED     = 140f;
    private static final float DONE_DELAY     = 3f;

    private static final String[] HEALTHY_TEXTURES = {
        "good_foods/apple.png",
        "good_foods/ninjin_carrot.png",
        "good_foods/petbottle_water_full.png",
    };

    private static final String[] JUNK_TEXTURES = {
        "bad_foods/can_juice.png",
        "bad_foods/dokukinoko_benitengu_dake.png",
        "bad_foods/rotten_apple.png",
    };

    private static final String VITAMIN_TEXTURE = "good_foods/Vitamin.png";

   
    // INTRO is the first phase — shows controls, no food drops yet
    private enum Phase { INTRO, HEALTHY, VITAMIN, JUNK, DONE }

    private Phase currentPhase  = Phase.INTRO;
    private int   phaseProgress = 0;
    private float doneTimer     = 0f;
    private float spawnTimer    = 0f;

   
    private final EntityManager    tutorialEM;
    private final CollisionManager tutorialCM;
    private final MovementManager  tutorialMM;
    private final IOManager        tutorialIO;
    private final PlayerStats      tutorialStats;
    private final PlayerController playerController;

    private TextureObject playerNPC;

    private Texture[] healthyTex;
    private Texture[] junkTex;
    private Texture   vitaminTex;

    private Stage      stage;
    private Skin       skin;
    private Label      phaseBody;
    private BitmapFont hudFont;
    private BitmapFont introFont;

    private final List<FeedbackText> feedbacks = new ArrayList<>();
    private float[] laneX;

    private static class FeedbackText {
        String text; float x, y, alpha, life; Color color;
        FeedbackText(String t, float x, float y, Color c) {
            text = t; this.x = x; this.y = y; color = c.cpy(); alpha = 1f; life = 1.4f;
        }
        boolean update(float d) {
            life -= d; y += 50f * d; alpha = Math.max(0, life / 1.4f); return life > 0;
        }
        void draw(SpriteBatch b, BitmapFont f) {
            f.setColor(color.r, color.g, color.b, alpha); f.draw(b, text, x, y);
        }
    }

   

    public SceneTutorial(ISceneManager ism) {
        super(ism);

        tutorialIO    = new IOManager();
        tutorialMM    = new MovementManager(tutorialIO);
        tutorialEM    = new EntityManager();
        tutorialStats = new PlayerStats();
        tutorialCM    = new CollisionManager(tutorialEM, tutorialMM, tutorialIO, tutorialStats);
        playerController = new PlayerController(220f);

        tutorialIO.getAudio().loadSound("catch", "catch.wav");
        tutorialIO.getAudio().loadSound("hit",   "hit.wav");

        hudFont   = new BitmapFont();
        hudFont.getData().setScale(1.3f);
        introFont = new BitmapFont();
        introFont.getData().setScale(1.5f);

        FloatingTextManager tutorialFTM = new FloatingTextManager(new BitmapFont());
        tutorialCM.getResolver().setFloatingTextManager(tutorialFTM);

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // ── Stage + background ────────────────────────────────────
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin  = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        Texture bg = new Texture(Gdx.files.internal("backgrounds/normal_mode_background.jpg"));
        Table bgTable = new Table();
        bgTable.setFillParent(true);
        bgTable.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));
        stage.addActor(bgTable);

        // ── Player NPC ────────────────────────────────────────────
        playerNPC = new TextureObject(
            "npc.png",
            screenW / 2f - 32,
            screenH * 0.08f,
            0,
            false
        );
        playerNPC.setDrawSize(64, 96);
        tutorialEM.addEntity(playerNPC);

       
        healthyTex = new Texture[HEALTHY_TEXTURES.length];
        for (int i = 0; i < HEALTHY_TEXTURES.length; i++)
            healthyTex[i] = new Texture(HEALTHY_TEXTURES[i]);

        junkTex = new Texture[JUNK_TEXTURES.length];
        for (int i = 0; i < JUNK_TEXTURES.length; i++)
            junkTex[i] = new Texture(JUNK_TEXTURES[i]);

        vitaminTex = new Texture(VITAMIN_TEXTURE);

        laneX = new float[]{
            screenW * 0.15f, screenW * 0.38f,
            screenW * 0.61f, screenW * 0.84f
        };

     
        Label tutorialHeader = new Label("TUTORIAL", skin, "title");
        tutorialHeader.setFontScale(0.5f);
        tutorialHeader.setAlignment(Align.center);

        // ── Instruction text — bottom of screen ───────────────────
        phaseBody = new Label("", skin, "default");
        phaseBody.setFontScale(0.45f);
        phaseBody.setAlignment(Align.center);

      
        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top().padTop(8);
        topTable.add(tutorialHeader).expandX().center().row();
        stage.addActor(topTable);

        // Bottom table: instructions at bottom
        Table bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom().padBottom(10);
        bottomTable.add(phaseBody).expandX().center().row();
        stage.addActor(bottomTable);

        // ── Skip button —───────────────────────
        TextButton skipBtn = new TextButton("Skip", skin, "default");
        skipBtn.getLabel().setFontScale(0.4f);
        skipBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                sceneManager.setScene(SceneManager.State.GAME);
            }
        });
        Table topBar = new Table();
        topBar.setFillParent(true);
        topBar.top().right().pad(10);
        topBar.add(skipBtn).width(70).height(28);
        stage.addActor(topBar);

        applyPhaseText();
    }

=======
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
import io.github.some_example_name.lwjgl3.Game.OnComingFood;
import io.github.some_example_name.lwjgl3.Game.iFoodFactory;
import io.github.some_example_name.lwjgl3.Game.FoodSpawner;
import io.github.some_example_name.lwjgl3.Game.Player;
import io.github.some_example_name.lwjgl3.Game.PlayerStats;
import io.github.some_example_name.lwjgl3.Game.HealthyFoodFactory;
import io.github.some_example_name.lwjgl3.Game.VitaminFactory;
import io.github.some_example_name.lwjgl3.Game.UnhealthyFoodFactory;


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
            3
        ) {
            @Override public TutorialPhase next() { return NORMAL_VITAMIN; }
        },
        NORMAL_VITAMIN(
            "Catch the VITAMINS!",
            "Vitamins give +1 Armor. Armor absorbs junk-food hits before HP drops.",
            OnComingFood.FoodType.VITAMIN,
            2
        ) {
            @Override public TutorialPhase next() { return NORMAL_UNHEALTHY; }
        },
        NORMAL_UNHEALTHY(
            "Watch out for JUNK FOOD!",
            "Junk food drains Armor first, then HP if Armor is 0.",
            OnComingFood.FoodType.UNHEALTHY,
            2
        ) {
            @Override public TutorialPhase next() { return DONE; }
        },

        // Fear & Hunger chain: FEAR_HEALTHY → FEAR_VITAMIN → FEAR_UNHEALTHY → DONE
        FEAR_HEALTHY(
            "Catch HEALTHY food — fast!",
            "In Fear & Hunger you start with only 2 HP.\nHealthy food gives +1 HP and +1 Hunger.",
            OnComingFood.FoodType.HEALTHY,
            3
        ) {
            @Override public TutorialPhase next() { return FEAR_VITAMIN; }
        },
        FEAR_VITAMIN(
            "Grab VITAMINS for Armor!",
            "You only have 2 Armor slots.\nVitamins are your only shield — catch every one you can.",
            OnComingFood.FoodType.VITAMIN,
            2
        ) {
            @Override public TutorialPhase next() { return FEAR_UNHEALTHY; }
        },
        FEAR_UNHEALTHY(
            "DODGE the JUNK FOOD!",
            "Junk food hits hard with low HP.\nFood also gets faster over time — stay sharp!",
            OnComingFood.FoodType.UNHEALTHY,
            2
        ) {
            @Override public TutorialPhase next() { return DONE; }
        },

        // Shared terminal — next() loops to itself; SceneTutorial exits before calling it
        DONE(
            "Tutorial Complete!",
            "Starting game...",
            null,
            0
        ) {
            @Override public TutorialPhase next() { return DONE; }
        };

        final String title;
        final String description;
        final OnComingFood.FoodType foodType;
        final int catchGoal;

        TutorialPhase(String title, String description,
                      OnComingFood.FoodType foodType, int catchGoal) {
            this.title       = title;
            this.description = description;
            this.foodType    = foodType;
            this.catchGoal   = catchGoal;
        }

        // Each constant overrides next() explicitly — no ordinal arithmetic
        public abstract TutorialPhase next();
    }

    // Engine tools injected through ISceneManager (Dependency Inversion)
    private final EntityManager    em;
    private final CollisionManager cm;
    private final MovementManager  mm;
    private final IOManager        io;

    // The mode determines which phase chain to start on
    private final PlayerStats.GameMode mode;

    // Isolated stats so tutorial catches never affect the real game state
    private final PlayerStats tutorialStats;

    // Abstract Factory instances — reused from the game layer
    private final iFoodFactory healthyFactory;
    private final iFoodFactory vitaminFactory;
    private final iFoodFactory unhealthyFactory;

    private final Player player;

    // UI
    private final Stage stage;
    private       Skin  skin;
    private final Label titleLabel;
    private final Label descriptionLabel;
    private final Label progressLabel;

    // Phase state — all mutation goes through advancePhase() (Encapsulation)
    private TutorialPhase currentPhase;
    private int           catchCount;
    private float         doneTimer;
    private FoodSpawner   spawner;

    private static final float DONE_DISPLAY_SECONDS = 2.5f;

    public SceneTutorial(ISceneManager ism,
                         EntityManager em,
                         CollisionManager cm,
                         MovementManager mm,
                         IOManager io,
                         PlayerStats.GameMode mode) {
        super(ism);

        this.em   = em;
        this.cm   = cm;
        this.mm   = mm;
        this.io   = io;
        this.mode = mode;

        this.tutorialStats = new PlayerStats();
        this.tutorialStats.setMode(mode);

        // Build food factories (Abstract Factory pattern)
        Texture[] healthyTextures = {
            am.get("good_foods/apple.png",               Texture.class),
            am.get("good_foods/ninjin_carrot.png",        Texture.class),
            am.get("good_foods/petbottle_water_full.png", Texture.class)
        };
        Texture[] junkTextures = {
            am.get("bad_foods/can_juice.png",                 Texture.class),
            am.get("bad_foods/dokukinoko_benitengu_dake.png", Texture.class),
            am.get("bad_foods/rotten_apple.png",              Texture.class)
        };
        Texture vitaminTexture = am.get("good_foods/Vitamin.png", Texture.class);

        this.healthyFactory   = new HealthyFoodFactory(healthyTextures);
        this.vitaminFactory   = new VitaminFactory(vitaminTexture);
        this.unhealthyFactory = new UnhealthyFoodFactory(junkTextures);

        // Player NPC
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        this.player = new Player(
            am.get("player.png", Texture.class),
            screenW / 2f - 32f,
            screenH * 0.08f,
            220f,
            false
        );
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
            skin, "default"
        );
        modeTag.setFontScale(0.38f);

        titleLabel       = new Label("", skin, "default");
        descriptionLabel = new Label("", skin, "default");
        progressLabel    = new Label("", skin, "default");

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
        doneTimer  = 0f;

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
        if (phase.foodType == OnComingFood.FoodType.HEALTHY)   return healthyFactory;
        if (phase.foodType == OnComingFood.FoodType.VITAMIN)   return vitaminFactory;
        if (phase.foodType == OnComingFood.FoodType.UNHEALTHY) return unhealthyFactory;
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

    // Polls entity list for deactivated food — avoids coupling to ResolveCollision internals
    private void checkForCaughtFood() {
        for (io.github.some_example_name.lwjgl3.Engine.entityManager.Entity entity
                : em.getEntities()) {
            if (!(entity instanceof OnComingFood)) continue;
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
>>>>>>> Stashed changes

    @Override
    public void update(float delta) {
        stage.act(delta);

<<<<<<< Updated upstream
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScene(SceneManager.State.GAME);
        }

        // ── INTRO phase — press any key to continue ───────────────
        if (currentPhase == Phase.INTRO) {
            playerController.handleInput(playerNPC);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                advanceFromIntro();
            }
            return;
        }

        // ── DONE phase ────────────────────────────────────────────
        if (currentPhase == Phase.DONE) {
            doneTimer += delta;
            if (doneTimer >= DONE_DELAY) {
=======
        if (io.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScene(SceneManager.State.GAME);
            return;
        }

        if (currentPhase == TutorialPhase.DONE) {
            doneTimer += delta;
            if (doneTimer >= DONE_DISPLAY_SECONDS) {
>>>>>>> Stashed changes
                sceneManager.setScene(SceneManager.State.GAME);
            }
            return;
        }

<<<<<<< Updated upstream
        playerController.handleInput(playerNPC);

        spawnTimer += delta;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0f;
            spawnFood();
        }

        tutorialEM.update(tutorialMM);
        tutorialCM.update();

        checkFoodResults();

        Iterator<FeedbackText> fi = feedbacks.iterator();
        while (fi.hasNext()) { if (!fi.next().update(delta)) fi.remove(); }
=======
        spawner.update(delta);
        em.update(mm);
        cm.update();
        checkForCaughtFood();
>>>>>>> Stashed changes
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        stage.draw();
<<<<<<< Updated upstream
        tutorialEM.draw(shape, batch);

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        batch.begin();

        if (currentPhase == Phase.INTRO) {
            introFont.setColor(1f, 1f, 1f, 1f);
            String line1 = "<----  Use  A  Key";
            String line2 = "Use  D  Key  ---->";
            introFont.draw(batch, line1, sw * 0.5f - line1.length() * 15f, sh * 0.10f);
            introFont.draw(batch, line2, sw * 0.5f - line2.length() * -6f, sh * 0.10f);
           
            introFont.getData().setScale(1.1f);
            introFont.setColor(1f, 1f, 0.3f, 1f);
            introFont.getData().setScale(1.5f);
        }

      
        hudFont.getData().setScale(1.3f);
        hudFont.setColor(0.3f, 1f, 0.3f, 1f);
        hudFont.draw(batch, "HP: " + tutorialStats.getHp() + " / " + tutorialStats.getMaxHp(),
            sw * 0.04f, sh * 0.97f);

        hudFont.setColor(0.4f, 0.8f, 1f, 1f);
        hudFont.draw(batch, "Armor: " + tutorialStats.getArmor() + " / " + tutorialStats.getMaxArmor(),
            sw * 0.04f, sh * 0.92f);

 
        if (currentPhase != Phase.INTRO && currentPhase != Phase.DONE) {
            String prog = getProgressText();
            hudFont.getData().setScale(1.8f);
            hudFont.setColor(1f, 1f, 1f, 1f);
            hudFont.draw(batch, prog, sw * 0.5f - prog.length() * 6f, sh * 0.83f);
            hudFont.getData().setScale(1.3f);
        }

        if (currentPhase == Phase.DONE) {
            String sub = "Starting game in " + (int)(DONE_DELAY - doneTimer + 1) + "...";
            introFont.getData().setScale(3f);
            introFont.setColor(1f, 1f, 1f, 1f);
            introFont.draw(batch, sub, sw * 0.5f - sub.length() * 9.5f, sh * 0.50f);
            introFont.getData().setScale(1.5f);
        }
        // Feedback popups
        for (FeedbackText ft : feedbacks) ft.draw(batch, hudFont);

        hudFont.setColor(Color.WHITE);
        batch.end();
    }

    private void advanceFromIntro() {
        currentPhase = Phase.HEALTHY;
        applyPhaseText();
    }

    private void spawnFood() {
        float spawnY = Gdx.graphics.getHeight() - 10f;
        float spawnX = laneX[MathUtils.random(laneX.length - 1)];

        OnComingFood food;
        switch (currentPhase) {
            case HEALTHY:
                food = new OnComingFood(spawnX, spawnY, FOOD_SPEED,
                    OnComingFood.FoodType.HEALTHY,
                    healthyTex[MathUtils.random(healthyTex.length - 1)]);
                break;
            case VITAMIN:
                food = new OnComingFood(spawnX, spawnY, FOOD_SPEED,
                    OnComingFood.FoodType.VITAMIN, vitaminTex);
                break;
            case JUNK:
                food = new OnComingFood(spawnX, spawnY, FOOD_SPEED,
                    OnComingFood.FoodType.UNHEALTHY,
                    junkTex[MathUtils.random(junkTex.length - 1)]);
                break;
            default:
                return;
        }

        tutorialEM.addEntity(food);
    }

    private void checkFoodResults() {
        for (Entity entity : new ArrayList<>(tutorialEM.getEntities())) {
            if (!(entity instanceof OnComingFood)) continue;
            OnComingFood food = (OnComingFood) entity;

            if (!food.isActive()) {
                recordResult(food, true);
            } else if (food.isOffScreen()) {
                recordResult(food, false);
            }
        }
    }

    private void recordResult(OnComingFood food, boolean caught) {
        float cx = playerNPC.getX() + 32f;
        float cy = playerNPC.getY() + 130f;

        if (caught) {
            switch (food.getFoodType()) {
                case HEALTHY:
                    feedbacks.add(new FeedbackText("+1 HP  Healthy!", cx - 50f, cy,
                        new Color(0.2f, 1f, 0.2f, 1f)));
                    if (currentPhase == Phase.HEALTHY) { phaseProgress++; checkAdvance(); }
                    break;
                case VITAMIN:
                    feedbacks.add(new FeedbackText("+1 Armor  Vitamin!", cx - 60f, cy,
                        new Color(0.2f, 0.9f, 1f, 1f)));
                    if (currentPhase == Phase.VITAMIN) { phaseProgress++; checkAdvance(); }
                    break;
                case UNHEALTHY:
                    if (currentPhase == Phase.JUNK) { phaseProgress++; checkAdvance(); }
                    break;
            }
        } else {
            if (currentPhase == Phase.JUNK && food.getFoodType() == OnComingFood.FoodType.UNHEALTHY) {
                feedbacks.add(new FeedbackText("Dodged!", cx - 30f, cy,
                    new Color(0.9f, 0.9f, 0.9f, 1f)));
                phaseProgress++;
                checkAdvance();
            }
        }
    }

    private void checkAdvance() {
        int goal;
        switch (currentPhase) {
            case HEALTHY: goal = HEALTHY_GOAL; break;
            case VITAMIN: goal = VITAMIN_GOAL; break;
            case JUNK:    goal = JUNK_GOAL;    break;
            default: return;
        }
        if (phaseProgress >= goal) {
            phaseProgress = 0;
            advancePhase();
        }
    }

    private void advancePhase() {
        tutorialEM.clearEntities();
        tutorialEM.addEntity(playerNPC);
        spawnTimer = 0f;

        switch (currentPhase) {
            case HEALTHY: currentPhase = Phase.VITAMIN; break;
            case VITAMIN: currentPhase = Phase.JUNK;    break;
            case JUNK:    currentPhase = Phase.DONE;    break;
            default: break;
        }

        applyPhaseText();
    }

    private void applyPhaseText() {
        switch (currentPhase) {
            case INTRO:
                phaseBody.setText("");
                break;
            case HEALTHY:
                phaseBody.setText(
                    "Apples, carrots and water give you +1 HP.");
                break;
            case VITAMIN:
                phaseBody.setText(
                    "Vitamins give you +1 Armor.\n" +
                    "Armor shields you from junk food before your HP takes damage.");
                break;
            case JUNK:
                phaseBody.setText(
                    "Armor absorbs the hit first. No armor = lose HP!");
                break;
            case DONE:
                phaseBody.setText("");
                break;
        }
    }

    private String getProgressText() {
        switch (currentPhase) {
            case HEALTHY: return "Catch healthy food: " + phaseProgress + " / " + HEALTHY_GOAL;
            case VITAMIN: return "Catch vitamins: "     + phaseProgress + " / " + VITAMIN_GOAL;
            case JUNK:    return "Junk food: "          + phaseProgress + " / " + JUNK_GOAL;
            default:      return "";
        }
=======
        em.draw(shape, batch);
>>>>>>> Stashed changes
    }

    @Override
    public void dispose() {
<<<<<<< Updated upstream
        if (stage      != null) stage.dispose();
        if (skin       != null) skin.dispose();
        if (hudFont    != null) hudFont.dispose();
        if (introFont  != null) introFont.dispose();
        if (vitaminTex != null) vitaminTex.dispose();
        for (Texture t : healthyTex) if (t != null) t.dispose();
        for (Texture t : junkTex)    if (t != null) t.dispose();
        tutorialEM.dispose();
        tutorialIO.getAudio().dispose();
=======
        if (stage != null) stage.dispose();
        this.skin = null;
        em.clearEntities();
>>>>>>> Stashed changes
    }
}