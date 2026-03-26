package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.graphics.Texture;

public class VitaminFactory implements iFoodFactory {
        private Texture textures;

    // The factory gets its textures when it is created
    public VitaminFactory(Texture textures) {
        this.textures = textures;
    }
    @Override
    public OnComingFood createFood(float x, float y,float speed) {

        return new OnComingFood(x, y, speed, OnComingFood.FoodType.VITAMIN, textures);
    }
}

