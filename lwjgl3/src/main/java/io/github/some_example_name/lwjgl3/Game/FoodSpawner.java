package io.github.some_example_name.lwjgl3.Game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;

public class FoodSpawner {

    private final EntityManager entityManager;

    private float timer    = 0f;
    private float interval = 1.8f;  // seconds between spawns
    private float baseSpeed = 160f; // pixels per second downward

    // Fearless Hunger escalation
    private boolean escalate    = false;
    private float   elapsedTime = 0f;
    // Every 15 s: interval drops by 0.15 s (floor 0.4 s), speed rises by 25 px/s (cap 500)
    private static final float ESCALATE_STEP     = 15f;
    private static final float INTERVAL_STEP     = 0.15f;
    private static final float MIN_INTERVAL      = 0.4f;
    private static final float SPEED_STEP        = 25f;
    private static final float MAX_SPEED         = 500f;

    // Lane X positions — the CENTER x of each lane
    // Food draw() offsets by currentSize/2 so it stays centered in the lane
    private final float[] laneXPositions;

    //A blind list of abstract factories
    private List<iFoodFactory> factoryPool = new ArrayList<>();



    public FoodSpawner(EntityManager entityManager) {
        this.entityManager   = entityManager;


        // 4 centered lane positions across the screen width
        float screenW = Gdx.graphics.getWidth();
        laneXPositions = new float[]{
            screenW * 0.15f,
            screenW * 0.38f,
            screenW * 0.61f,
            screenW * 0.84f
        };
    }

    public void update(float delta) {
        if (escalate) {
            elapsedTime += delta;
            // Recalculate interval and speed based on how many 15-second stages have passed
            int stage = (int) (elapsedTime / ESCALATE_STEP);
            interval  = Math.max(MIN_INTERVAL, 1.8f - stage * INTERVAL_STEP);
            baseSpeed = Math.min(MAX_SPEED,    160f  + stage * SPEED_STEP);
        }

        timer += delta;
        if (timer >= interval) {
            timer = 0;
            spawnFood();
        }
    }

    // Method to add factories to the pool
    public void addFactoryToPool(iFoodFactory factory) {
        factoryPool.add(factory);
    }


    /** Call once to enable the Fearless Hunger difficulty escalation. */
    public void enableEscalation() { this.escalate = true; }


    private void spawnFood() {
        float screenH = Gdx.graphics.getHeight();

        // Spawn at the very top — this is where spawnY is recorded inside
        // OncomingFood, so the full screen height = full perspective travel
        float spawnY = screenH - 10f;
        float spawnX = laneXPositions[MathUtils.random(laneXPositions.length - 1)];
        // 1. Pick a completely random factory from the pool
        iFoodFactory selectedFactory = factoryPool.get(MathUtils.random(factoryPool.size() - 1));
        
        // 2. Ask it to build food. The Spawner doesn't know if it's healthy, junk, or vegan!
        OnComingFood newFood = selectedFactory.createFood(spawnX, spawnY, baseSpeed);

        entityManager.addEntity(newFood);
    }
}
