package io.github.some_example_name.lwjgl3.Engine.movementManager;

import com.badlogic.gdx.Gdx;

import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;
import io.github.some_example_name.lwjgl3.Engine.iomanager.Keyboard;

public class UserMovement {

    

    // Logic for WASD Keys (used by the player)
    public void moveHorizontal(Entity e, float speed, Keyboard kb, int leftKey, int rightKey, float minX, float maxX) {
        float newX = e.getX();
        float delta = Gdx.graphics.getDeltaTime();
        // 1. Check for key presses
        if (kb.isKeyPressed(leftKey)) {
            newX -= speed * delta;
        }
        if (kb.isKeyPressed(rightKey)) {
            newX += speed * delta;
        }

        // 2. Clamp the entity so it doesn't go off screen
        if (newX < minX) newX = minX;
        if (newX > maxX) newX = maxX;

        // 3. Apply the movement
        e.setX(newX);
    }
}
