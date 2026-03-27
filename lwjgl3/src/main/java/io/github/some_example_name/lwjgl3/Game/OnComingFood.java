package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import io.github.some_example_name.lwjgl3.Engine.entityManager.MovableEntity;
import io.github.some_example_name.lwjgl3.Engine.entityManager.iDrawableSprite;

<<<<<<< Updated upstream
public class OnComingFood extends MovableEntity implements iDrawableSprite {
=======
public class OnComingFood extends MovableEntity implements iDrawableSprite, iMovable {
>>>>>>> Stashed changes

    public enum FoodType {
        HEALTHY,
        VITAMIN,
        UNHEALTHY
    }

<<<<<<< Updated upstream
    private FoodType foodType;
    private Texture  texture;
    private boolean  active = true;
=======
    private final FoodType foodType;
    private final Texture  texture;
    private boolean active = true;
>>>>>>> Stashed changes

    // new: scene tutorial
    private boolean countedByTutorial = false;

    private static final float MIN_SIZE = 20f;
    private static final float MAX_SIZE = 72f;
    private float currentSize = MIN_SIZE;
<<<<<<< Updated upstream

    // The Y position where this food was spawned (top of screen)
    private float spawnY;
=======
    private final float spawnY;
>>>>>>> Stashed changes

    public OnComingFood(float x, float y, float speed, FoodType foodType, Texture texture) {
        super(x, y, speed, Color.WHITE);
        this.foodType = foodType;
        this.texture  = texture;
        this.spawnY   = y;
    }

    @Override
    public void update() {
        if (!active) return;
<<<<<<< Updated upstream

        float delta     = Gdx.graphics.getDeltaTime();
        float screenH   = Gdx.graphics.getHeight();

        // Move downward
        y -= speed * delta;

        // How far has the food traveled as a 0..1 ratio?
        // 0 = just spawned at top (tiny), 1 = reached bottom (full size)
=======
>>>>>>> Stashed changes
        float traveled = 1f - (y / spawnY);
        traveled = Math.max(0f, Math.min(1f, traveled));
        currentSize = MIN_SIZE + (MAX_SIZE - MIN_SIZE) * traveled;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!active) return;
        float drawX = x - currentSize / 2f;
        batch.draw(texture, drawX, y, currentSize, currentSize);
    }

    @Override
    public Polygon getBounds() {
        float drawX = x - currentSize / 2f;
        Polygon p = new Polygon(new float[]{
            0, 0,
            currentSize, 0,
            currentSize, currentSize,
            0, currentSize
        });
        p.setPosition(drawX, y);
        return p;
    }

<<<<<<< Updated upstream
    // ── Off-screen: despawn when past the bottom ──────────────────
    public boolean isOffScreen() {
        return y < -MAX_SIZE;
    }

    // ── Getters ───────────────────────────────────────────────────
    public FoodType getFoodType()  { return foodType;     }
    public Texture  getTexture()   { return texture;      }
    public boolean  isActive()     { return active;       }
    public float    getCurrentSize() { return currentSize; }
=======
    public FoodType getFoodType()  { return foodType; }
    public Texture  getTexture()   { return texture;  }
    public boolean  isActive()     { return active;   }
    public void     deactivate()   { active = false;  }
>>>>>>> Stashed changes

    // new: scene tutorial
    public boolean isCountedByTutorial()  { return countedByTutorial; }
    public void    markCountedByTutorial() { this.countedByTutorial = true; }

<<<<<<< Updated upstream
    @Override
    public void dispose() {
        // Textures are shared — disposal handled by GameMaster, not here
    }
=======
    @Override
    public void performMovement(float speed, boolean isAI, Keyboard kb,
                                UserMovement userMove, AImovement aiMove) {
        if (!active) return;
        float delta = Gdx.graphics.getDeltaTime();
        aiMove.moveDown(this, speed, delta);
    }

    @Override
    public void dispose() {}
>>>>>>> Stashed changes
}