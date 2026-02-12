package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

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
        // P1 (-size, -size), P2 (size, -size), P3 (0, size)
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
        
        // Triangle vertices centered around (x, y)
        // P1 (bottom-left), P2 (bottom-right), P3 (top-middle)
        float x1 = x - size;      // P1 x
        float y1 = y - size;      // P1 y
        float x2 = x + size;      // P2 x
        float y2 = y - size;      // P2 y
        float x3 = x;             // P3 x
        float y3 = y + size;      // P3 y
        
        shape.triangle(x1, y1, x2, y2, x3, y3);
    }

    // Movement method (Overriding - controlled by A and D keys)
    // This demonstrates polymorphism - Triangle has different movement than Circle
    @Override
    public void movement() {
        if (Gdx.input.isKeyPressed(Keys.A)) {
            x -= speed;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            x += speed;
        }
    }
    @Override
    public void update(){
        System.out.println("In Triangle at " + super.getX() + "," + super.getY() + " position");
    }
    
    @Override
    public Polygon getBounds() {
        // Update polygon position to the entity's current x, y
        trianglePolygon.setPosition(x, y);
        return trianglePolygon;
    }
}