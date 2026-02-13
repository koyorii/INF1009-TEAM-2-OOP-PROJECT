package io.github.some_example_name.lwjgl3.iomanager;

import com.badlogic.gdx.Gdx;

public class Keyboard {

    public Keyboard() { //Constructor
    }

    public boolean isKeyPressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode); //keycode for the key ID you want to check.
    }

    public boolean isKeyJustPressed(int keycode) {
        return Gdx.input.isKeyJustPressed(keycode);
    }

}