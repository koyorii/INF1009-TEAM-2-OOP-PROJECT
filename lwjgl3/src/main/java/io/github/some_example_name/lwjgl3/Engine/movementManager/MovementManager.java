package io.github.some_example_name.lwjgl3.Engine.movementManager;


import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;
import io.github.some_example_name.lwjgl3.Engine.iomanager.Keyboard;
import io.github.some_example_name.lwjgl3.Engine.iomanager.getInputs;

public class MovementManager implements MovementCalculator{
    private AImovement AImove = new AImovement();
    private UserMovement Usermove = new UserMovement();
    private getInputs inputs;

    public MovementManager (getInputs inputs){
        this.inputs = inputs;
    }
    public void calculateMovement(Entity e, boolean isAI, float speed){
        // if(isAI){
        //     AImove.move(e,speed);
        // } else {
        //     Keyboard kb = inputs.getKeyboard();
        //     if (e instanceof Triangle){
        //     } else if (e instanceof TextureObject) {
        //         // Only move if it's the bucket (not a falling droplet)
        //         if (!((TextureObject) e).getIsFalling()) {
        //             Usermove.moveArrows(e, speed, kb);
        //         }
        //     }
        // }   
        if (e instanceof iMovable) {
            Keyboard kb = inputs.getKeyboard();
            
            // 2. Tell the entity to move itself using the engine's tools!
            ((iMovable) e).performMovement(speed, isAI, kb, Usermove, AImove);
        }
    }
    public void collisionMovement(Entity e, float x, float y){
        e.setX(x);
        e.setY(y);
    }
}
