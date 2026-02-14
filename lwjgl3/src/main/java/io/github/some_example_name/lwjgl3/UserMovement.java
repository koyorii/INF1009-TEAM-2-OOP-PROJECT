package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import io.github.some_example_name.lwjgl3.iomanager.Keyboard;

public class UserMovement {
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    // Logic for Arrow Keys (used by the Bucket)
    public void moveArrows(Entity e, float speed, Keyboard kb) {
        TextureObject tobj = (TextureObject) e;
        float w = tobj.getTexture().getWidth();
        float h = tobj.getTexture().getHeight();
        // Bucket movement
        if (kb.isKeyPressed(Keys.LEFT)) {
            tobj.setX(tobj.getX() - speed);
        }
        if (kb.isKeyPressed(Keys.RIGHT)) {
            tobj.setX(tobj.getX() + speed);
        }
        // Clamp bucket within screen bounds
        if (tobj.getX() < 0) tobj.setX(0);
        if (tobj.getX() + w > screenWidth) tobj.setX(screenWidth - w);
        if (tobj.getY() < 0) tobj.setY(0);;
        if (tobj.getY() + h > screenHeight) tobj.setY(screenHeight - h);
    }

    // Logic for WASD Keys (used by the Triangle)
    public void moveWASD(Entity e, float speed, Keyboard kb) {
        Triangle triObj = (Triangle) e;
        if (kb.isKeyPressed(Keys.A)) {
            triObj.setX(triObj.getX() - speed);
        }
        if (kb.isKeyPressed(Keys.D)) {
            triObj.setX(triObj.getX() + speed);
        }
        // Clamp triangle within screen bounds (accounting for size offset)
        if (triObj.getX() - triObj.getSize()< 0) triObj.setX(triObj.getSize());;
        if (triObj.getX() + triObj.getSize()> screenWidth) triObj.setX(screenWidth - triObj.getSize());
        
        if (triObj.getY() - triObj.getSize() < 0) triObj.setY(triObj.getSize());
        if (triObj.getY() + triObj.getSize() > screenHeight) triObj.setY(screenHeight - triObj.getSize());
    }
}
