package io.github.some_example_name.lwjgl3.iomanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound; // Change Music to Sound
import java.util.HashMap;
import java.util.Map;

public class Audio {

    private Map<String, Sound> soundEffects; // Using Sound instead of Music

    public Audio() {
        this.soundEffects = new HashMap<>();
    }

    public void loadSound(String key, String filePath) {
        // Use newSound for effects
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
        soundEffects.put(key, sound);
        System.out.println("[AUDIO] Loaded sound: " + key + " from " + filePath);
    }

    public void playSound(String key) {
        Sound sound = soundEffects.get(key);
        if (sound != null) {
            // Sound.play() automatically handles rewinding and overlapping
            sound.play(1.0f); 
            System.out.println("Playing Sound Now");
        } else {
            System.out.println("[AUDIO] Sound not found: " + key);
        }
    }

    public void playSound(String key, float volume) {
        Sound sound = soundEffects.get(key);
        if (sound != null) {
            sound.play(volume);
        }
    }

    public void dispose() {
        for (Sound s : soundEffects.values()) {
            s.dispose();
        }
        soundEffects.clear();
        System.out.println("[AUDIO] All audio resources disposed.");
    }
}