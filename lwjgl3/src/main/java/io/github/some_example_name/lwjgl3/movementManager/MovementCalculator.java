package io.github.some_example_name.lwjgl3.movementManager;

import io.github.some_example_name.lwjgl3.entityManager.Entity;

public interface MovementCalculator {
    public void calculateMovement(Entity e, boolean isAI, float speed);
    public void collisionMovement(Entity e, float x, float y);
}
