package io.github.some_example_name.lwjgl3;

import io.github.some_example_name.lwjgl3.entityManager.OnComingFood.FoodType;

public class PlayerStats {

    private static final int MAX_HP    = 10;
    private static final int MAX_ARMOR = 10;

    private int hp;
    private int armor;
    private int score;

    public PlayerStats() {
        this.hp    = MAX_HP;
        this.armor = 0;
        this.score = 0;
    }

    // ─── Called by ResolveCollision when food is caught ──────────
    public void applyFood(FoodType type) {
        switch (type) {

            case HEALTHY:
                // +1 HP, capped at MAX_HP
                hp = Math.min(MAX_HP, hp + 1);
                score += 150;
                System.out.println("[Stats] Healthy food caught! HP=" + hp + " Score=" + score);
                break;

            case VITAMIN:
                // +1 Armor, capped at MAX_ARMOR
                armor = Math.min(MAX_ARMOR, armor + 1);
                score += 100;
                System.out.println("[Stats] Vitamin caught! Armor=" + armor + " Score=" + score);
                break;

            case UNHEALTHY:
                // Armor absorbs the hit first; if no armor, lose HP
                if (armor > 0) {
                    armor--;
                    System.out.println("[Stats] Unhealthy food hit! Armor blocked. Armor=" + armor);
                } else {
                    hp = Math.max(0, hp - 1);
                    System.out.println("[Stats] Unhealthy food hit! HP=" + hp);
                }
                score = Math.max(0, score - 50);
                break;
        }
    }

    public boolean isDead()    { return hp <= 0; }

    // ─── Getters ─────────────────────────────────────────────────
    public int getHp()       { return hp;       }
    public int getArmor()    { return armor;    }
    public int getScore()    { return score;    }
    public int getMaxHp()    { return MAX_HP;   }
    public int getMaxArmor() { return MAX_ARMOR;}
}