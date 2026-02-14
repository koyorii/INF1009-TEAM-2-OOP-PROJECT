package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

public class TextureObject extends MovableEntity {
    // Specific attribute for TextureObject
    private Texture tex;
    private boolean isFalling;
    private Polygon rectPolygon;

    // Default Constructor
    public TextureObject() {
        super(); // Call parent constructor
    }

    // Parameterized Constructor
    public TextureObject(String path, float x, float y, float speed, boolean isFalling) {
        super(x, y, speed, null); // Call parent constructor
        this.tex = new Texture(Gdx.files.internal(path));
        this.isFalling = isFalling;
        float w = tex.getWidth();
        float h = tex.getHeight();

        // Rectangular vertices: [x1,y1, x2,y2, x3,y3, x4,y4]
        float[] vertices = new float[] {
            0, 0, // Bottom Left
            w, 0, // Bottom Right
            w, h, // Top Right
            0, h  // Top Left
        };
        this.rectPolygon = new Polygon(vertices);
    }

    // Getter and Setter for texture
    public Texture getTexture() { return tex; }
    public void setTexture(Texture t) { this.tex = t; }
    public boolean getIsFalling() { return isFalling; }

    // Movement method for bucket (Overriding - controlled by LEFT and RIGHT arrow keys)
    @Override
    public void movement() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float w = tex.getWidth();
        float h = tex.getHeight();

        if (isFalling) {
            y -= speed;
            // Wrap back to top if it falls below the screen
            if (y + h < 0) y = screenHeight;

            // Clamp horizontal position within screen bounds
            if (x < 0) x = 0;
            if (x + w > screenWidth) x = screenWidth - w;
        } else {
            // Bucket movement
            if (Gdx.input.isKeyPressed(Keys.LEFT)) x -= speed;
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) x += speed;

            // Clamp bucket within screen bounds
            if (x < 0) x = 0;
            if (x + w > screenWidth) x = screenWidth - w;
            if (y < 0) y = 0;
            if (y + h > screenHeight) y = screenHeight - h;
        }
    }

    // Movement method for droplets (Overloading - different signature)
    public void movement(boolean isFalling) {
        if (isFalling) {
            y -= speed;

            // Reset if it hits the bottom
            if (y < 0) {
                y = Gdx.graphics.getHeight();
            }
        }
    }

    @Override
    public void update() {
        String type = isFalling ? "Droplet" : "Bucket";
        System.out.println("In TextureObject of " + type + " at " + super.getX() + "," + super.getY() + " position");
    }

    @Override
    public Polygon getBounds() {
        rectPolygon.setPosition(x, y);
        return rectPolygon;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.tex, super.getX(), super.getY(), this.tex.getWidth(), this.tex.getHeight());
    }
}