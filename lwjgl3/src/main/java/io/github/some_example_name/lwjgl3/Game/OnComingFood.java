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

public class OnComingFood extends MovableEntity implements iDrawableSprite,iMovable {

    public enum FoodType {
        HEALTHY,    // +1 HP
        VITAMIN,    // +1 Armor
        UNHEALTHY   // -1 Armor or -1 HP
    }

    private FoodType foodType;
    private Texture  texture;
    private boolean  active = true;

    // ── Perspective scaling ───────────────────────────────────────
    // Food starts tiny at the top (far away) and grows as it moves
    // down toward the player (close up), faking a 3D oncoming effect.

    // Minimum draw size when first spawned at the top of the screen
    private static final float MIN_SIZE = 20f;

    // Maximum draw size when the food reaches the bottom (player level)
    private static final float MAX_SIZE = 72f;

    // Current draw size — recalculated every frame based on Y position
    private float currentSize = MIN_SIZE;

    // The Y position where this food was spawned (top of screen)
    private float spawnY;

    // ── Constructor ───────────────────────────────────────────────
    public OnComingFood(float x, float y, float speed, FoodType foodType, Texture texture) {
        super(x, y, speed, Color.WHITE);
        this.foodType = foodType;
        this.texture  = texture;
        this.spawnY   = y;  // remember where it started for scale calculation
    }

    // ── Update — moves down and grows in size ─────────────────────
    @Override
    public void update() {
        if (!active) return;


        // How far has the food traveled as a 0..1 ratio?
        // 0 = just spawned at top (tiny), 1 = reached bottom (full size)
        float traveled = 1f - (y / spawnY);
        traveled = Math.max(0f, Math.min(1f, traveled)); // clamp 0..1

        // Linearly interpolate between MIN_SIZE and MAX_SIZE
        currentSize = MIN_SIZE + (MAX_SIZE - MIN_SIZE) * traveled;
    }

    // ── Draw — centered on x, scaled by currentSize ───────────────
    @Override
    public void draw(SpriteBatch batch) {
        if (!active) return;

        // Keep the food horizontally centered on its lane as it grows
        float drawX = x - currentSize / 2f;
        float drawY = y;

        batch.draw(texture, drawX, drawY, currentSize, currentSize);
    }

    // ── Collision bounds — match the current scaled size ─────────
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

    // ── Off-screen: despawn when past the bottom ──────────────────
    public boolean isOffScreen() {
        return y < -MAX_SIZE;
    }

    // ── Getters ───────────────────────────────────────────────────
    public FoodType getFoodType()  { return foodType;     }
    public Texture  getTexture()   { return texture;      }
    public boolean  isActive()     { return active;       }
    public float    getCurrentSize() { return currentSize; }

    public void deactivate() { active = false; }

    public void performMovement(float speed, boolean isAI, Keyboard kb, UserMovement userMove, AImovement aiMove){
        if (!active) return;
        float delta     = Gdx.graphics.getDeltaTime();
        // Food knows it acts as an AI, so it uses the Engine's AI tool to fall!
        aiMove.moveDown(this, speed, delta);
    }
    @Override
    public void dispose() {
        // Textures are shared — disposal handled by GameMaster, not here
    }
}