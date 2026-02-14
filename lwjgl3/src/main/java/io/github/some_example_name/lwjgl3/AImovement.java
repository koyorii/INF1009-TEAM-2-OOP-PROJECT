package io.github.some_example_name.lwjgl3;

public class AImovement {
    public void move(Entity e, float speed){
        if (e instanceof TextureObject) {
            e.setY(e.getY()-speed);
            if(e.getY() < 0 ){
                e.setY(480);
            }
        }
    }
}
