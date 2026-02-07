package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.graphics.Color;

public class Entity {
    // Common attributes for all entities
    protected float x;
    protected float y;
    protected float speed;
    protected Color color;

    // Default Constructor (Overloading)
    public Entity() {
        this.x = 0;
        this.y = 0;
        this.speed = 0;
        this.color = null;
    }

    // Constructor with position only (Overloading)
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        this.speed = 0;
        this.color = null;
    }

    // Constructor with position and speed (Overloading)
    public Entity(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.color = null;
    }

    // Full Parameterized Constructor (Overloading)
    public Entity(float x, float y, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.color = color;
    }

    // Common Getters and Setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    // Common movement method (can be overridden)
    public void movement() {
        // Default implementation - can be overridden by child classes
    }

    // Common draw method (can be overridden)
    public void draw() {
        // Default implementation - can be overridden by child classes
    }
}