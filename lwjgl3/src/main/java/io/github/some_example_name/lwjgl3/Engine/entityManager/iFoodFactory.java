package io.github.some_example_name.lwjgl3.Engine.entityManager;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.lwjgl3.Game.OnComingFood;

public interface iFoodFactory {
    OnComingFood createFood(float x, float y, float speed, Texture texture);
}