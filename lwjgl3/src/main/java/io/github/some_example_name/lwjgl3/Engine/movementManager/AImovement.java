package io.github.some_example_name.lwjgl3.Engine.movementManager;




import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;

public class AImovement {
    public void moveDown(Entity e, float speed, float delta){
        e.setY(e.getY() - (speed * delta));
    }
}
