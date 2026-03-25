package io.github.some_example_name.lwjgl3.Engine.movementManager;

import io.github.some_example_name.lwjgl3.Engine.iomanager.Keyboard;

public interface iMovable {
    // Pass the tools the entity might need to move itself
    public void performMovement(float speed, boolean isAI, Keyboard kb, UserMovement userMove, AImovement aiMove);
}
