package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import io.github.some_example_name.lwjgl3.Engine.entityManager.MovableEntity;
import io.github.some_example_name.lwjgl3.Engine.entityManager.iDrawableSprite;
import io.github.some_example_name.lwjgl3.Engine.iomanager.Keyboard;
import io.github.some_example_name.lwjgl3.Engine.movementManager.AImovement;
import io.github.some_example_name.lwjgl3.Engine.movementManager.UserMovement;
import io.github.some_example_name.lwjgl3.Engine.movementManager.iMovable;

public class OnComingFood extends MovableEntity implements iDrawableSprite, iMovable {

    public enum FoodType {
        HEALTHY,
        VITAMIN,
        UNHEALTHY
    }

    private final FoodType foodType;
    private final Texture  texture;
    private boolean active = true;

    // new: scene tutorial
    private boolean countedByTutorial = false;

    private static final float MIN_SIZE = 20f;
    private static final float MAX_SIZE = 72f;
    private float currentSize = MIN_SIZE;
    private final float spawnY;

    public OnComingFood(float x, float y, float speed, FoodType foodType, Texture texture) {
        super(x, y, speed, Color.WHITE);
        this.foodType = foodType;
        this.texture  = texture;
        this.spawnY   = y;
    }

    @Override
    public void update() {
        if (!active) return;
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

    public FoodType getFoodType()  { return foodType; }
    public Texture  getTexture()   { return texture;  }
    public boolean  isActive()     { return active;   }
    public void     deactivate()   { active = false;  }

    // new: scene tutorial
    public boolean isCountedByTutorial()  { return countedByTutorial; }
    public void    markCountedByTutorial() { this.countedByTutorial = true; }

    @Override
    public void performMovement(float speed, boolean isAI, Keyboard kb,
                                UserMovement userMove, AImovement aiMove) {
        if (!active) return;
        float delta = Gdx.graphics.getDeltaTime();
        aiMove.moveDown(this, speed, delta);
    }

    @Override
    public void dispose() {}
}