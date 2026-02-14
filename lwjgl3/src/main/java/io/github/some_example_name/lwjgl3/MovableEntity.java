package io.github.some_example_name.lwjgl3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;

public abstract class MovableEntity extends Entity {
    protected float speed;

    public MovableEntity(){
        super();
        this.speed = 0;
    }

    public MovableEntity(float x, float y, float speed, Color color){
        super(x,y,color);
        this.speed = speed;
    }

    public float getSpeed(){
        return speed;
    }
    
    public abstract Polygon getBounds();
}
