package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

public class TextureObject extends Entity {
    // Specific attribute for TextureObject
    private Texture tex;

    // Default Constructor
    public TextureObject() {
        super(); // Call parent constructor
    }

    // Parameterized Constructor
    public TextureObject(String path, float x, float y, float speed) {
        super(x, y, speed); // Call parent constructor
        this.tex = new Texture(Gdx.files.internal(path));
    }

    // Getter and Setter for texture
    public Texture getTexture() { return tex; }
    public void setTexture(Texture t) { this.tex = t; }
    
    // Movement method for bucket (Overriding - controlled by LEFT and RIGHT arrow keys)
    @Override
    public void movement() {
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            x -= speed;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            x += speed;
        }
    }
    
    // Movement method for droplets (Overloading - different signature)
    // This demonstrates method overloading - same class, different method signatures
    public void movement(boolean isFalling) {
        if (isFalling) {
            y -= speed;
            
            // Reset if it hits the bottom
            if (y < 0) {
                y = 480;
            }
        }
    }
}