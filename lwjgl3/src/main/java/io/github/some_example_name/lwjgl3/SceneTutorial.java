//package io.github.some_example_name.lwjgl3;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.Align;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//
//import io.github.some_example_name.lwjgl3.Engine.collisionManager.CollisionManager;
//import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;
//import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
//import io.github.some_example_name.lwjgl3.Game.OnComingFood;
//import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
//import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementManager;
//import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
//import io.github.some_example_name.lwjgl3.Engine.sceneManager.Scene;
//import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;
//import io.github.some_example_name.lwjgl3.Game.Player;
//import io.github.some_example_name.lwjgl3.Game.PlayerController;
//import io.github.some_example_name.lwjgl3.Game.PlayerStats;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//public class SceneTutorial extends Scene {
//
//
//    private static final int   HEALTHY_GOAL   = 1;
//    private static final int   VITAMIN_GOAL   = 1;
//    private static final int   JUNK_GOAL      = 1;
//    private static final float SPAWN_INTERVAL = 2.0f;
//    private static final float FOOD_SPEED     = 140f;
//    private static final float DONE_DELAY     = 3f;
//
//    private static final String[] HEALTHY_TEXTURES = {
//        "good_foods/apple.png",
//        "good_foods/ninjin_carrot.png",
//        "good_foods/petbottle_water_full.png",
//    };
//
//    private static final String[] JUNK_TEXTURES = {
//        "bad_foods/can_juice.png",
//        "bad_foods/dokukinoko_benitengu_dake.png",
//        "bad_foods/rotten_apple.png",
//    };
//
//    private static final String VITAMIN_TEXTURE = "good_foods/Vitamin.png";
//
//
//    // INTRO is the first phase — shows controls, no food drops yet
//    private enum Phase { INTRO, HEALTHY, VITAMIN, JUNK, DONE }
//
//    private Phase currentPhase  = Phase.INTRO;
//    private int   phaseProgress = 0;
//    private float doneTimer     = 0f;
//    private float spawnTimer    = 0f;
//
//
//    private final EntityManager    tutorialEM;
//    private final CollisionManager tutorialCM;
//    private final MovementManager  tutorialMM;
//    private final IOManager        tutorialIO;
//    private final PlayerStats      tutorialStats;
//    private final PlayerController playerController;
//
//    private final Player playerNPC;
//
//    private Texture[] healthyTex;
//    private Texture[] junkTex;
//    private Texture   vitaminTex;
//
//    private Stage      stage;
//    private Skin       skin;
//    private Label      phaseBody;
//    private BitmapFont hudFont;
//    private BitmapFont introFont;
//
//    private final List<FeedbackText> feedbacks = new ArrayList<>();
//    private float[] laneX;
//
//    private static class FeedbackText {
//        String text; float x, y, alpha, life; Color color;
//        FeedbackText(String t, float x, float y, Color c) {
//            text = t; this.x = x; this.y = y; color = c.cpy(); alpha = 1f; life = 1.4f;
//        }
//        boolean update(float d) {
//            life -= d; y += 50f * d; alpha = Math.max(0, life / 1.4f); return life > 0;
//        }
//        void draw(SpriteBatch b, BitmapFont f) {
//            f.setColor(color.r, color.g, color.b, alpha); f.draw(b, text, x, y);
//        }
//    }
//
//
//
//    public SceneTutorial(ISceneManager ism, PlayerStats ps) {
//        super(ism);
//
//        tutorialIO    = new IOManager();
//        tutorialMM    = new MovementManager(tutorialIO);
//        tutorialEM    = new EntityManager();
//        tutorialStats = new PlayerStats();
//        tutorialCM    = new CollisionManager(tutorialEM, tutorialMM, tutorialIO, tutorialStats);
//        playerController = new PlayerController(220f);
//
//        tutorialIO.getAudio().loadSound("catch", "catch.wav");
//        tutorialIO.getAudio().loadSound("hit",   "hit.wav");
//
//        hudFont   = new BitmapFont();
//        hudFont.getData().setScale(1.3f);
//        introFont = new BitmapFont();
//        introFont.getData().setScale(1.5f);
//
//        FloatingTextManager tutorialFTM = new FloatingTextManager(new BitmapFont());
//        tutorialCM.getResolver().setFloatingTextManager(tutorialFTM);
//
//        float screenW = Gdx.graphics.getWidth();
//        float screenH = Gdx.graphics.getHeight();
//
//        // ── Stage + background ────────────────────────────────────
//        stage = new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//        skin  = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
//
//        Texture bg = new Texture(Gdx.files.internal("backgrounds/normal_mode_background.jpg"));
//        Table bgTable = new Table();
//        bgTable.setFillParent(true);
//        bgTable.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));
//        stage.addActor(bgTable);
//
//        // ── Player NPC ────────────────────────────────────────────
//        playerNPC = new Player(
//            "npc.png",
//            screenW / 2f - 32,
//            screenH * 0.08f,
//            0,
//            false
//        );
//        playerNPC.setDrawSize(64, 96);
//        tutorialEM.addEntity(playerNPC);
//
//
//        healthyTex = new Texture[HEALTHY_TEXTURES.length];
//        for (int i = 0; i < HEALTHY_TEXTURES.length; i++)
//            healthyTex[i] = new Texture(HEALTHY_TEXTURES[i]);
//
//        junkTex = new Texture[JUNK_TEXTURES.length];
//        for (int i = 0; i < JUNK_TEXTURES.length; i++)
//            junkTex[i] = new Texture(JUNK_TEXTURES[i]);
//
//        vitaminTex = new Texture(VITAMIN_TEXTURE);
//
//        laneX = new float[]{
//            screenW * 0.15f, screenW * 0.38f,
//            screenW * 0.61f, screenW * 0.84f
//        };
//
//
//        Label tutorialHeader = new Label("TUTORIAL", skin, "title");
//        tutorialHeader.setFontScale(0.5f);
//        tutorialHeader.setAlignment(Align.center);
//
//        // ── Instruction text — bottom of screen ───────────────────
//        phaseBody = new Label("", skin, "default");
//        phaseBody.setFontScale(0.45f);
//        phaseBody.setAlignment(Align.center);
//
//
//        Table topTable = new Table();
//        topTable.setFillParent(true);
//        topTable.top().padTop(8);
//        topTable.add(tutorialHeader).expandX().center().row();
//        stage.addActor(topTable);
//
//        // Bottom table: instructions at bottom
//        Table bottomTable = new Table();
//        bottomTable.setFillParent(true);
//        bottomTable.bottom().padBottom(10);
//        bottomTable.add(phaseBody).expandX().center().row();
//        stage.addActor(bottomTable);
//
//        // ── Skip button —───────────────────────
//        TextButton skipBtn = new TextButton("Skip", skin, "default");
//        skipBtn.getLabel().setFontScale(0.4f);
//        skipBtn.addListener(new ClickListener() {
//            @Override public void clicked(InputEvent e, float x, float y) {
//                sceneManager.setScene(SceneManager.State.GAME);
//            }
//        });
//        Table topBar = new Table();
//        topBar.setFillParent(true);
//        topBar.top().right().pad(10);
//        topBar.add(skipBtn).width(70).height(28);
//        stage.addActor(topBar);
//
//        applyPhaseText();
//    }
//
//
//    @Override
//    public void update(float delta) {
//        stage.act(delta);
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            sceneManager.setScene(SceneManager.State.GAME);
//        }
//
//        // ── INTRO phase — press any key to continue ───────────────
//        if (currentPhase == Phase.INTRO) {
//            playerController.handleInput(playerNPC);
//            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
//                advanceFromIntro();
//            }
//            return;
//        }
//
//        // ── DONE phase ────────────────────────────────────────────
//        if (currentPhase == Phase.DONE) {
//            doneTimer += delta;
//            if (doneTimer >= DONE_DELAY) {
//                sceneManager.setScene(SceneManager.State.GAME);
//            }
//            return;
//        }
//
//        playerController.handleInput(playerNPC);
//
//        spawnTimer += delta;
//        if (spawnTimer >= SPAWN_INTERVAL) {
//            spawnTimer = 0f;
//            spawnFood();
//        }
//
//        tutorialEM.update(tutorialMM);
//        tutorialCM.update();
//
//        checkFoodResults();
//
//        Iterator<FeedbackText> fi = feedbacks.iterator();
//        while (fi.hasNext()) { if (!fi.next().update(delta)) fi.remove(); }
//    }
//
//    @Override
//    public void render(ShapeRenderer shape, SpriteBatch batch) {
//        stage.draw();
//        tutorialEM.draw(shape, batch);
//
//        float sw = Gdx.graphics.getWidth();
//        float sh = Gdx.graphics.getHeight();
//
//        batch.begin();
//
//        if (currentPhase == Phase.INTRO) {
//            introFont.setColor(1f, 1f, 1f, 1f);
//            String line1 = "<----  Use  A  Key";
//            String line2 = "Use  D  Key  ---->";
//            introFont.draw(batch, line1, sw * 0.5f - line1.length() * 15f, sh * 0.10f);
//            introFont.draw(batch, line2, sw * 0.5f - line2.length() * -6f, sh * 0.10f);
//
//            introFont.getData().setScale(1.1f);
//            introFont.setColor(1f, 1f, 0.3f, 1f);
//            introFont.getData().setScale(1.5f);
//        }
//
//
//        hudFont.getData().setScale(1.3f);
//        hudFont.setColor(0.3f, 1f, 0.3f, 1f);
//        hudFont.draw(batch, "HP: " + tutorialStats.getHp() + " / " + tutorialStats.getMaxHp(),
//            sw * 0.04f, sh * 0.97f);
//
//        hudFont.setColor(0.4f, 0.8f, 1f, 1f);
//        hudFont.draw(batch, "Armor: " + tutorialStats.getArmor() + " / " + tutorialStats.getMaxArmor(),
//            sw * 0.04f, sh * 0.92f);
//
//
//        if (currentPhase != Phase.INTRO && currentPhase != Phase.DONE) {
//            String prog = getProgressText();
//            hudFont.getData().setScale(1.8f);
//            hudFont.setColor(1f, 1f, 1f, 1f);
//            hudFont.draw(batch, prog, sw * 0.5f - prog.length() * 6f, sh * 0.83f);
//            hudFont.getData().setScale(1.3f);
//        }
//
//        if (currentPhase == Phase.DONE) {
//            String sub = "Starting game in " + (int)(DONE_DELAY - doneTimer + 1) + "...";
//            introFont.getData().setScale(3f);
//            introFont.setColor(1f, 1f, 1f, 1f);
//            introFont.draw(batch, sub, sw * 0.5f - sub.length() * 9.5f, sh * 0.50f);
//            introFont.getData().setScale(1.5f);
//        }
//        // Feedback popups
//        for (FeedbackText ft : feedbacks) ft.draw(batch, hudFont);
//
//        hudFont.setColor(Color.WHITE);
//        batch.end();
//    }
//
//    private void advanceFromIntro() {
//        currentPhase = Phase.HEALTHY;
//        applyPhaseText();
//    }
//
//    private void spawnFood() {
//        float spawnY = Gdx.graphics.getHeight() - 10f;
//        float spawnX = laneX[MathUtils.random(laneX.length - 1)];
//
//        OnComingFood food;
//        switch (currentPhase) {
//            case HEALTHY:
//                food = new OnComingFood(spawnX, spawnY, FOOD_SPEED,
//                    OnComingFood.FoodType.HEALTHY,
//                    healthyTex[MathUtils.random(healthyTex.length - 1)]);
//                break;
//            case VITAMIN:
//                food = new OnComingFood(spawnX, spawnY, FOOD_SPEED,
//                    OnComingFood.FoodType.VITAMIN, vitaminTex);
//                break;
//            case JUNK:
//                food = new OnComingFood(spawnX, spawnY, FOOD_SPEED,
//                    OnComingFood.FoodType.UNHEALTHY,
//                    junkTex[MathUtils.random(junkTex.length - 1)]);
//                break;
//            default:
//                return;
//        }
//
//        tutorialEM.addEntity(food);
//    }
//
//    private void checkFoodResults() {
//        for (Entity entity : new ArrayList<>(tutorialEM.getEntities())) {
//            if (!(entity instanceof OnComingFood)) continue;
//            OnComingFood food = (OnComingFood) entity;
//
//            if (!food.isActive()) {
//                recordResult(food, true);
//            } else if (food.isOffScreen()) {
//                recordResult(food, false);
//            }
//        }
//    }
//
//    private void recordResult(OnComingFood food, boolean caught) {
//        float cx = playerNPC.getX() + 32f;
//        float cy = playerNPC.getY() + 130f;
//
//        if (caught) {
//            switch (food.getFoodType()) {
//                case HEALTHY:
//                    feedbacks.add(new FeedbackText("+1 HP  Healthy!", cx - 50f, cy,
//                        new Color(0.2f, 1f, 0.2f, 1f)));
//                    if (currentPhase == Phase.HEALTHY) { phaseProgress++; checkAdvance(); }
//                    break;
//                case VITAMIN:
//                    feedbacks.add(new FeedbackText("+1 Armor  Vitamin!", cx - 60f, cy,
//                        new Color(0.2f, 0.9f, 1f, 1f)));
//                    if (currentPhase == Phase.VITAMIN) { phaseProgress++; checkAdvance(); }
//                    break;
//                case UNHEALTHY:
//                    if (currentPhase == Phase.JUNK) { phaseProgress++; checkAdvance(); }
//                    break;
//            }
//        } else {
//            if (currentPhase == Phase.JUNK && food.getFoodType() == OnComingFood.FoodType.UNHEALTHY) {
//                feedbacks.add(new FeedbackText("Dodged!", cx - 30f, cy,
//                    new Color(0.9f, 0.9f, 0.9f, 1f)));
//                phaseProgress++;
//                checkAdvance();
//            }
//        }
//    }
//
//    private void checkAdvance() {
//        int goal;
//        switch (currentPhase) {
//            case HEALTHY: goal = HEALTHY_GOAL; break;
//            case VITAMIN: goal = VITAMIN_GOAL; break;
//            case JUNK:    goal = JUNK_GOAL;    break;
//            default: return;
//        }
//        if (phaseProgress >= goal) {
//            phaseProgress = 0;
//            advancePhase();
//        }
//    }
//
//    private void advancePhase() {
//        tutorialEM.clearEntities();
//        tutorialEM.addEntity(playerNPC);
//        spawnTimer = 0f;
//
//        switch (currentPhase) {
//            case HEALTHY: currentPhase = Phase.VITAMIN; break;
//            case VITAMIN: currentPhase = Phase.JUNK;    break;
//            case JUNK:    currentPhase = Phase.DONE;    break;
//            default: break;
//        }
//
//        applyPhaseText();
//    }
//
//    private void applyPhaseText() {
//        switch (currentPhase) {
//            case INTRO:
//                phaseBody.setText("");
//                break;
//            case HEALTHY:
//                phaseBody.setText(
//                    "Apples, carrots and water give you +1 HP.");
//                break;
//            case VITAMIN:
//                phaseBody.setText(
//                    "Vitamins give you +1 Armor.\n" +
//                    "Armor shields you from junk food before your HP takes damage.");
//                break;
//            case JUNK:
//                phaseBody.setText(
//                    "Armor absorbs the hit first. No armor = lose HP!");
//                break;
//            case DONE:
//                phaseBody.setText("");
//                break;
//        }
//    }
//
//    private String getProgressText() {
//        switch (currentPhase) {
//            case HEALTHY: return "Catch healthy food: " + phaseProgress + " / " + HEALTHY_GOAL;
//            case VITAMIN: return "Catch vitamins: "     + phaseProgress + " / " + VITAMIN_GOAL;
//            case JUNK:    return "Junk food: "          + phaseProgress + " / " + JUNK_GOAL;
//            default:      return "";
//        }
//    }
//
//    @Override
//    public void dispose() {
//        if (stage      != null) stage.dispose();
//        if (skin       != null) skin.dispose();
//        if (hudFont    != null) hudFont.dispose();
//        if (introFont  != null) introFont.dispose();
//        if (vitaminTex != null) vitaminTex.dispose();
//        for (Texture t : healthyTex) if (t != null) t.dispose();
//        for (Texture t : junkTex)    if (t != null) t.dispose();
//        tutorialEM.dispose();
//        tutorialIO.getAudio().dispose();
//    }
//}
