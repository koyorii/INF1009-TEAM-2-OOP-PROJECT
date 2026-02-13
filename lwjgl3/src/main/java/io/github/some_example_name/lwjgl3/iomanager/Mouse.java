package io.github.some_example_name.lwjgl3.iomanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Mouse {

    private final Vector2 position;
    private boolean isLeftPressed;
    private boolean isRightPressed;

    public Mouse() {
        this.position = new Vector2(0, 0);
        this.isLeftPressed = false;
        this.isRightPressed = false;
    }

    public void update() {

        this.position.set(
                Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()  //reads the current mouse position, converts it to  game’s coordinate system and updates position vector
        );

        this.isLeftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        this.isRightPressed = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
    }

    //allows other classes to read the value safely, but not modify it

    public Vector2 getMousePosition() {return new Vector2(position);}

    public boolean isLeftPressed() {return isLeftPressed;}

    public boolean isRightPressed() {return isRightPressed;}
    
}