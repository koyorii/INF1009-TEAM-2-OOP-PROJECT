package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Circle extends Entity {
    // Specific attribute for Circle
    private float radius;

    // Default Constructor
    public Circle() {
        super(); // Call parent constructor
        this.radius = 0;
    }

    // Parameterized Constructor
    public Circle(float x, float y, float speed, float radius, Color color) {
        super(x, y, speed, color); // Call parent constructor
        this.radius = radius;
    }

    // Getter and Setter for radius
    public float getRadius() { return radius; }
    public void setRadius(float radius) { this.radius = radius; }

    // Draw method (Overriding)
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, radius);
    }

    // Movement method (Overriding - controlled by UP and DOWN arrow keys)
    @Override
    public void movement() {
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            y += speed;
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            y -= speed;
        }
    }
}

//testing for pull