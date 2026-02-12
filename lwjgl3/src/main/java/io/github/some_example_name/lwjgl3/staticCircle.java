package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class staticCircle extends nonMovableEntity{
    // Specific attribute for Circle
    private float radius;
    private Circle circleBound;
    // Default Constructor
    public staticCircle() {
        super(); // Call parent constructor
        this.radius = 0;
    }

    // Parameterized Constructor
    public staticCircle(float x, float y, float speed, float radius, Color color) {
        super(x, y, color); // Call parent constructor
        this.radius = radius;
        this.circleBound = new Circle(x,y,radius);
    }

    // Getter and Setter for radius
    public float getRadius() { return radius; }
    public void setRadius(float radius) { this.radius = radius; }

    // Draw method (Overriding)
    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(x, y, radius);
    }

    @Override
    public void update(){
        System.out.println("In circle of radius " +  this.radius + " at " + super.getX() + "," + super.getY() + " position");
    }
    
    public Circle getCircleBounds(){
        circleBound.setPosition(x, y);
        return circleBound;
    }
}

//testing for pull