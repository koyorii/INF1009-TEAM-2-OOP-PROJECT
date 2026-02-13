package io.github.some_example_name.lwjgl3.iomanager;

public class IOManager {

    private Mouse mouse; //private variable of type Mouse called mouse
    private Keyboard keyboard; //private variable of type Keyboard called keyboard
    private Audio audio; //private variable of Audio called audio

    //constructor

    public IOManager() {
        this.mouse = new Mouse();
        this.keyboard = new Keyboard();
        this.audio = new Audio();
    }
    
    //getters

    public Mouse getMouse() {return mouse;}
    public Keyboard getKeyboard() {return keyboard;}
    public Audio getAudio() {return audio;}

    //methods
    
     public void update() {
            mouse.update(); //updates mouse position
    }

    public void log(String msg) { 
        System.out.println("[ENGINE LOG]: " + msg);
    }
    
}

