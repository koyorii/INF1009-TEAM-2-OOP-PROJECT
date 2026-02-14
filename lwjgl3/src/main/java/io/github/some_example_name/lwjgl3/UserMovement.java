package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Input.Keys;

import io.github.some_example_name.lwjgl3.iomanager.Keyboard;

public class UserMovement {
    // Logic for Arrow Keys (used by the Bucket)
    public void moveArrows(Entity e, float speed, Keyboard kb) {
        if (kb.isKeyPressed(Keys.LEFT)) {
            e.setX(e.getX() - speed);
        }
        if (kb.isKeyPressed(Keys.RIGHT)) {
            e.setX(e.getX() + speed);
        }
    }

    // Logic for WASD Keys (used by the Triangle)
    public void moveWASD(Entity e, float speed, Keyboard kb) {
        if (kb.isKeyPressed(Keys.A)) {
            e.setX(e.getX() - speed);
        }
        if (kb.isKeyPressed(Keys.D)) {
            e.setX(e.getX() + speed);
        }
    }
}
