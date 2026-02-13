package io.github.some_example_name.lwjgl3.iomanager;

public class IOManager {

    private Mouse mouse; //Mouse is class, mouse is object
    private Keyboard keyboard;
    private Audio audio;

    public IOManager() {
        this.mouse = new Mouse();
        this.keyboard = new Keyboard();
        this.audio = new Audio();
    }
    
    public Mouse getMouse() {
        return mouse;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public Audio getAudio() {
        return audio;
    }
    
    public void log(String msg) {
        System.out.println("[ENGINE LOG]: " + msg);
    }
}

