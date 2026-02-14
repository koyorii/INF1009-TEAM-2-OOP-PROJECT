package io.github.some_example_name.lwjgl3;

import io.github.some_example_name.lwjgl3.iomanager.getInputs;
import io.github.some_example_name.lwjgl3.iomanager.Keyboard;

public class MovementManager implements MovementCalculator{
    private AImovement AImove = new AImovement();
    private UserMovement Usermove = new UserMovement();
    private getInputs inputs;

    public MovementManager (getInputs inputs){
        this.inputs = inputs;
    }
    public void calculateMovement(Entity e, boolean isAI, float speed){
        if(isAI){
            AImove.move(e,speed);
        } else {
            Keyboard kb = inputs.getKeyboard();
            if (e instanceof Triangle){
                Usermove.moveWASD(e, speed,kb);
            } else if (e instanceof TextureObject) {
                // Only move if it's the bucket (not a falling droplet)
                if (!((TextureObject) e).getIsFalling()) {
                    Usermove.moveArrows(e, speed, kb);
                }
            }
        }   
    }
}
