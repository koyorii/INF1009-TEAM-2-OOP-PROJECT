package io.github.some_example_name.lwjgl3.Engine.iomanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class Audio {

    private final Map<String, Sound> soundEffects;
    private final Map<String, Music> musicTracks;
    private Music currentMusic;

    public Audio() {
        this.soundEffects = new HashMap<>();
        this.musicTracks  = new HashMap<>();
    }

    // ── Sound Effects (short, one-shot) ──────────────────────────

    public void loadSound(String key, String filePath) {
        try {
            com.badlogic.gdx.files.FileHandle file = Gdx.files.internal(filePath);
            if (!file.exists() || file.length() == 0) {
                System.out.println("[AUDIO] Skipping SFX (missing/empty): " + filePath);
                return;
            }
            Sound sound = Gdx.audio.newSound(file);
            soundEffects.put(key, sound);
            System.out.println("[AUDIO] Loaded SFX: " + key + " from " + filePath);
        } catch (Exception e) {
            System.out.println("[AUDIO] Failed to load SFX '" + key + "': " + e.getMessage());
        }
    }

    public void playSound(String key) {
        playSound(key, 1.0f);
    }

    public void playSound(String key, float volume) {
        Sound sound = soundEffects.get(key);
        if (sound != null) {
            sound.play(volume);
        } else {
            System.out.println("[AUDIO] SFX not found: " + key);
        }
    }

    // ── Background Music (looping) ───────────────────────────────

    public void loadMusic(String key, String filePath) {
        try {
            com.badlogic.gdx.files.FileHandle file = Gdx.files.internal(filePath);
            if (!file.exists() || file.length() == 0) {
                System.out.println("[AUDIO] Skipping BGM (missing/empty): " + filePath);
                return;
            }
            Music music = Gdx.audio.newMusic(file);
            musicTracks.put(key, music);
            System.out.println("[AUDIO] Loaded BGM: " + key + " from " + filePath);
        } catch (Exception e) {
            System.out.println("[AUDIO] Failed to load BGM '" + key + "': " + e.getMessage());
        }
    }

    /**
     * Play a named music track. Stops whatever is currently playing first.
     * @param key    key used in loadMusic()
     * @param loop   true = loop continuously (BGM), false = play once
     * @param volume 0.0 – 1.0
     */
    public void playMusic(String key, boolean loop, float volume) {
        stopMusic();
        Music music = musicTracks.get(key);
        if (music != null) {
            music.setLooping(loop);
            music.setVolume(volume);
            music.play();
            currentMusic = music;
            System.out.println("[AUDIO] Playing BGM: " + key);
        } else {
            System.out.println("[AUDIO] BGM not found: " + key);
        }
    }

    /** Convenience overload — loops at full volume. */
    public void playMusic(String key) {
        playMusic(key, true, 1.0f);
    }

    public void stopMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
        currentMusic = null;
    }

    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }

    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    /** Lower volume to duckVolume (e.g. 0.2f) for pause effect. */
    public void duckMusic(float duckVolume) {
        if (currentMusic != null) {
            currentMusic.setVolume(duckVolume);
        }
    }

    /** Restore volume back to full (1.0f). */
    public void unduckMusic() {
        if (currentMusic != null) {
            currentMusic.setVolume(1.0f);
        }
    }

    public boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    // ── Cleanup ──────────────────────────────────────────────────

    public void dispose() {
        for (Sound s : soundEffects.values()) s.dispose();
        soundEffects.clear();

        for (Music m : musicTracks.values()) m.dispose();
        musicTracks.clear();

        currentMusic = null;
        System.out.println("[AUDIO] All audio resources disposed.");
    }
}