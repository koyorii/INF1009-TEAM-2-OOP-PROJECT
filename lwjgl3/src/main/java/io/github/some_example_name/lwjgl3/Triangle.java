package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

public class Triangle extends MovableEntity {
    // Specific attribute for Triangle
    private float size;
    private Polygon trianglePolygon;

    // Default Constructor
    public Triangle() {
        super(); // Call parent constructor
        this.size = 0;
    }

    // Parameterized Constructor
    public Triangle(float x, float y, float speed, float size, Color color) {
        super(x, y, speed, color);
        this.size = size;

        // Define vertices relative to (0,0)
        float[] vertices = new float[] {
            -size, -size, // Bottom Left
             size, -size, // Bottom Right
             0,     size  // Top Middle
        };
        this.trianglePolygon = new Polygon(vertices);
    }

    // Getter and Setter for size
    public float getSize() { return size; }
    public void setSize(float size) { this.size = size; }

    // Draw method (Overriding)
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);

        float x1 = x - size;
        float y1 = y - size;
        float x2 = x + size;
        float y2 = y - size;
        float x3 = x;
        float y3 = y + size;

        shape.triangle(x1, y1, x2, y2, x3, y3);
    }

    // Movement method (Overriding - controlled by A and D keys)
    @Override
    public void movement() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        if (Gdx.input.isKeyPressed(Keys.A)) {
            x -= speed;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            x += speed;
        }

        // Clamp triangle within screen bounds (accounting for size offset)
        if (x - size < 0) x = size;
        if (x + size > screenWidth) x = screenWidth - size;
        if (y - size < 0) y = size;
        if (y + size > screenHeight) y = screenHeight - size;
    }

    @Override
    public void update() {
        System.out.println("In Triangle at " + super.getX() + "," + super.getY() + " position");
    }

    @Override
    public Polygon getBounds() {
        trianglePolygon.setPosition(x, y);
        return trianglePolygon;
    }
}