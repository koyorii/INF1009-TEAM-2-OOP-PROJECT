package io.github.some_example_name.lwjgl3.iomanager;

public class Audio {

    private String path; //atrributes
    private boolean isPlaying; //attributes

    public Audio() { //constructor
        this.path = "";
        this.isPlaying = false;
    }

    public void playAudio(String path) {
        this.path = path;
        System.out.println("Playing audio from: " + path);
        this.isPlaying = true;

    }

    public void stopAudio() {
        System.out.println("Stopping audio from: " + path);
        this.isPlaying = false;
    }

    public boolean isPlaying() {return isPlaying;}

    public String getPath() {return path;}
}