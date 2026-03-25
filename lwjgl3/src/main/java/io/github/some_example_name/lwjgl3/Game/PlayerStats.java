package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

import io.github.some_example_name.lwjgl3.Game.OnComingFood.FoodType;

public class PlayerStats {

    public enum GameMode { NORMAL, FEARLESS_HUNGER }

    private static final int MAX_HUNGER_NORMAL = 5;
    private static final int MAX_HUNGER_FEARLESS = 3;

    // Per-mode values (set by setMode before each game)
    private int maxHp;      // growth cap  (Integer.MAX_VALUE = unlimited)
    private int maxArmor;   // growth cap
    private int maxHunger;  // hunger cap
    private int startHp;    // HP the player starts with on reset
    private GameMode mode;

    private int hp;
    private int hunger;
    private int armor;
    private int score;

    private String name;

    public PlayerStats() {
        this.mode     = GameMode.NORMAL;
        this.maxHp     = 5;
        this.maxArmor  = 5;
        this.maxHunger = MAX_HUNGER_NORMAL;
        this.startHp   = 5;
        this.hp        = startHp;
        this.hunger    = maxHunger;
        this.armor     = 0;
        this.score     = 0;
        this.name      = null;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
        if (mode == GameMode.NORMAL) {
            this.maxHp     = 5;
            this.maxArmor  = 5;
            this.maxHunger = MAX_HUNGER_NORMAL;
            this.startHp   = 5;   // start full
            this.hunger    = maxHunger;
        } else { // FEARLESS_HUNGER — capped at 3 HP / 3 armor
            this.maxHp     = 3;
            this.maxArmor  = 3;
            this.maxHunger = MAX_HUNGER_FEARLESS;
            this.startHp   = 3;
            this.hunger    = maxHunger;
        }
    }

    /** Normal mode has a 2-minute countdown; Fearless Hunger is endless. */
    public boolean hasTimeLimit() {
        return mode == GameMode.NORMAL;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ─── Called by ResolveCollision when food is caught ──────────
    public void applyFood(FoodType type) {
        switch (type) {

            case HEALTHY:
                // +1 HP, capped at maxHp
                hp = Math.min(maxHp, hp + 1);
                hunger = Math.min(maxHunger, hunger + 1);
                score += 150;
                System.out.println("[Stats] Healthy food caught! HP=" + hp + " Hunger=" + hunger + " Score=" + score);
                break;

            case VITAMIN:
                // +1 Armor, capped at maxArmor
                armor = Math.min(maxArmor, armor + 1);
                score += 200;
                System.out.println("[Stats] Vitamin caught! Armor=" + armor + " Score=" + score);
                break;

            case UNHEALTHY:
                // Armor absorbs the hit first; if no armor, lose HP
                if (armor > 0) {
                    armor--;
                    hunger = Math.min(maxHunger, hunger - 1);
                    System.out.println("[Stats] Unhealthy food hit! Armor blocked. Armor=" + armor + " Hunger=" + hunger
                            + " Score=" + score);
                } else {
                    hp = Math.max(0, hp - 1);
                    hunger = Math.min(maxHunger, hunger - 1);
                    System.out
                            .println("[Stats] Unhealthy food hit! HP=" + hp + " Hunger=" + hunger + " Score=" + score);
                }
                score = Math.max(0, score - 50);
                break;
        }
    }

    // Save to local file within your OS, not sure about Linux, but definitely for
    // Windows
    public void saveToLeaderboard() {
        // Get local file
        Preferences prefs = Gdx.app.getPreferences("MyGameLeaderboard");

        // Insert into top 20
        for (int i = 0; i < 20; i++) {
            int savedScore = prefs.getInteger("score" + i, 0);

            if (this.score > savedScore) {
                // Shift lower scores down
                for (int j = 9; j > i; j--) {
                    prefs.putInteger("score" + j, prefs.getInteger("score" + (j - 1), 0));
                    prefs.putString("name" + j, prefs.getString("name" + (j - 1), "---"));
                }
                // Insert new high score
                prefs.putInteger("score" + i, this.score);
                prefs.putString("name" + i, (this.name == null) ? "Guest" : this.name);
                prefs.flush(); // Write to disk!!!!
                break;
            }
        }
    }

    // For leaderboard to get 20 past highets scores
    public Array<String> getLeaderboardList() {
        Preferences prefs = Gdx.app.getPreferences("MyGameLeaderboard");
        Array<String> lines = new Array<>();
        for (int i = 0; i < 20; i++) {
            String n = prefs.getString("name" + i, "---");
            int s = prefs.getInteger("score" + i, 0);
            lines.add((i + 1) + ". " + n + " : " + s);
        }
        return lines;
    }

    // Check if player die
    public boolean isDead() {
        return hp <= 0 || hunger <= 0;
    }

    // Reset upon new game
    public void reset() {
        this.hp     = startHp;
        this.hunger = MAX_HUNGER_NORMAL;
        this.armor  = 0;
        this.score  = 0;
        System.out.println("[Stats] Game Reset: HP=" + hp + " Mode=" + mode);
    }

    // ─── Getters ─────────────────────────────────────────────────
    public int getHp() { return hp; }
    public int getHunger() { return hunger; }
    public int getArmor() { return armor; }
    public int getScore() { return score; }
    public String getName() { return name; }
    public int getMaxHp() { return maxHp; }
    public int getMaxArmor() { return maxArmor; }
    public int getMaxHunger() { return maxHunger; }
    public GameMode getMode() { return mode; }
}
