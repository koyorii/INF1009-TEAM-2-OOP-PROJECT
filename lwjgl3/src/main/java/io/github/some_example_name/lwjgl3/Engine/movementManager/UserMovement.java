package io.github.some_example_name.lwjgl3.Engine.movementManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;


import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;
import io.github.some_example_name.lwjgl3.Engine.iomanager.Keyboard;

public class UserMovement {

    // Logic for Arrow Keys (used by the Bucket)
    public void moveArrows(Entity e, float speed, Keyboard kb) {
    //     float screenWidth = Gdx.graphics.getWidth();
    //     float screenHeight = Gdx.graphics.getHeight();
    //     TextureObject tobj = (TextureObject) e;
    //     float w = tobj.getTexture().getWidth();
    //     float h = tobj.getTexture().getHeight();
    //     // Bucket movement
    //     if (kb.isKeyPressed(Keys.LEFT)) {
    //         tobj.setX(tobj.getX() - speed);
    //     }
    //     if (kb.isKeyPressed(Keys.RIGHT)) {
    //         tobj.setX(tobj.getX() + speed);
    //     }
    //     // Clamp bucket within screen bounds
    //     if (tobj.getX() < 0) tobj.setX(0);
    //     if (tobj.getX() + w > screenWidth) tobj.setX(screenWidth - w);
    //     if (tobj.getY() < 0) tobj.setY(0);;
    //     if (tobj.getY() + h > screenHeight) tobj.setY(screenHeight - h);
    }

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
