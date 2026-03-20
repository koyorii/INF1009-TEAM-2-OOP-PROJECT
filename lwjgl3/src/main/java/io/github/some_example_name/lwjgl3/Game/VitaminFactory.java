package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.lwjgl3.Engine.entityManager.iFoodFactory;

public class VitaminFactory implements iFoodFactory {
    @Override
    public OnComingFood createFood(float x, float y,float speed, Texture texture) {
        OnComingFood food = new OnComingFood(x, y, speed, OnComingFood.FoodType.VITAMIN, texture);
        return food;
    }
}

