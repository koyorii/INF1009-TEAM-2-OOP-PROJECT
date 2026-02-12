package io.github.some_example_name.lwjgl3;
import com.badlogic.gdx.graphics.Color;

public abstract class nonMovableEntity extends Entity{
    public nonMovableEntity(){
        super();
    }

    public nonMovableEntity(float x, float y, Color color){
        super(x,y,color);
    }
}
