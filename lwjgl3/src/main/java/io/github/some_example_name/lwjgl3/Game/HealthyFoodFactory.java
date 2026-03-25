package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class HealthyFoodFactory implements iFoodFactory {
    private Texture[] textures;

    // The factory gets its textures when it is created
    public HealthyFoodFactory(Texture[] textures) {
        this.textures = textures;
    }
    @Override
    public OnComingFood createFood(float x, float y,float speed) {

        Texture tex = textures[MathUtils.random(textures.length - 1)];
        return new OnComingFood(x, y, speed, OnComingFood.FoodType.HEALTHY, tex);
    }
}