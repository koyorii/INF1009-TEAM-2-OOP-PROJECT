package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;

public class AImovement {
    public void move(Entity e, float speed){
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        if (e instanceof TextureObject) {
            ///setting the variables used
            TextureObject tobj = (TextureObject) e;
            float w = tobj.getTexture().getWidth();
            float h = tobj.getTexture().getHeight();
            //the dropping movement
            tobj.setY(tobj.getY()-speed);
            //resetting the heights
            if (tobj.getY() + h < 0)  tobj.setY(screenHeight);
            //limiting the droplet from going over screen
            if (tobj.getX() < 0)  tobj.setX(0);
            if (tobj.getX() + w > screenWidth) tobj.setX(screenWidth - w);
        }
    }
}
