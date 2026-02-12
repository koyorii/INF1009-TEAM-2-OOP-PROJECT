package io.github.some_example_name.lwjgl3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public abstract class Entity{
    protected float x;
    protected float y;
    protected Color color;


    //Overloading constructors
    public Entity() {
        this.x = 0;
        this.y = 0;
        this.color = Color.WHITE;
    }

    public Entity(float x, float y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    //Getters and Setters
    public float getX() {return x;}
    public void setX(float x) {this.x = x;}
    public float getY() {return y;}
    public void setY(float y) {this.y = y;}
    public Color getColor() {return color;}
    public void setColor(Color color) {this.color = color;}

    public abstract void update();

    
    public void draw(ShapeRenderer shape) {

    }
    public void draw(SpriteBatch batch) {

    }
    
}

