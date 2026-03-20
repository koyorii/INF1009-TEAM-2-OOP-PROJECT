package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.Engine.entityManager.iFoodFactory;
import io.github.some_example_name.lwjgl3.Game.OnComingFood.FoodType;

public class FoodSpawner {

    private EntityManager entityManager;

    private float timer    = 0f;
    private float interval = 1.8f;  // seconds between spawns
    private float baseSpeed = 160f; // pixels per second downward

    // Lane X positions — the CENTER x of each lane
    // Food draw() offsets by currentSize/2 so it stays centered in the lane
    private float[] laneXPositions;

    private Texture[] healthyTextures;
    private Texture[] junkTextures;
    private Texture   vitaminTexture;
    private iFoodFactory healthyFactory = new HealthyFoodFactory();
    private iFoodFactory unhealthFoodFactory = new UnhealthyFoodFactory();
    private iFoodFactory vitaminFactory = new VitaminFactory();
    
    public FoodSpawner(EntityManager entityManager,
                       Texture[] healthyTextures,
                       Texture[] junkTextures,
                       Texture vitaminTexture) {
        this.entityManager   = entityManager;
        this.healthyTextures = healthyTextures;
        this.junkTextures    = junkTextures;
        this.vitaminTexture  = vitaminTexture;

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
        timer += delta;
        if (timer >= interval) {
            timer = 0;
            spawnFood();
        }
    }

    private void spawnFood() {
        float screenH = Gdx.graphics.getHeight();

        // Spawn at the very top — this is where spawnY is recorded inside
        // OncomingFood, so the full screen height = full perspective travel
        float spawnY = screenH - 10f;
        float spawnX = laneXPositions[MathUtils.random(laneXPositions.length - 1)];

        FoodType type    = randomFoodType();
        Texture  texture = textureFor(type);

        OnComingFood newFood;
        if (type == FoodType.HEALTHY) {
            newFood = healthyFactory.createFood(spawnX, spawnY, baseSpeed, texture);
        } else if (type == FoodType.UNHEALTHY) {
            newFood = unhealthFoodFactory.createFood(spawnX, spawnY, baseSpeed, texture);
        } else {
            newFood = vitaminFactory.createFood(spawnX, spawnY, baseSpeed, texture);
        }
        entityManager.addEntity(newFood);
    }

    // 50% healthy, 30% unhealthy, 20% vitamin
    private FoodType randomFoodType() {
        int r = MathUtils.random(9);
        if (r < 5) return FoodType.HEALTHY;
        if (r < 8) return FoodType.UNHEALTHY;
        return FoodType.VITAMIN;
    }

    private Texture textureFor(FoodType type) {
        switch (type) {
            case HEALTHY:   return healthyTextures[MathUtils.random(healthyTextures.length - 1)];
            case UNHEALTHY: return junkTextures[MathUtils.random(junkTextures.length - 1)];
            case VITAMIN:   return vitaminTexture;
            default:        return healthyTextures[0];
        }
    }

    public void setInterval(float interval) { this.interval = interval; }
    public void setBaseSpeed(float speed)   { this.baseSpeed = speed;   }
}