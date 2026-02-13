package io.github.some_example_name.lwjgl3.iomanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Mouse {

    private Vector2 position;
    private boolean isLeftPressed;
    private boolean isRightPressed;

    public Mouse() {
        this.position = new Vector2(0, 0);
        this.isLeftPressed = false;
        this.isRightPressed = false;
    }

    public void update() {
        this.position.set(Gdx.input.getX(), Gdx.input.getY()); 
        this.isLeftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        this.isRightPressed = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
    }

    public Vector2 getMousePosition() {
        return this.position;
    }

    public boolean isButtonDown(int code) {
        return Gdx.input.isButtonPressed(code);
    }
}