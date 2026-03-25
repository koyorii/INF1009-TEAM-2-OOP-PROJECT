package io.github.some_example_name.lwjgl3.Engine.collisionManager;


import io.github.some_example_name.lwjgl3.Game.Player;

import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;
import io.github.some_example_name.lwjgl3.Engine.iomanager.Audio;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementCalculator;
import io.github.some_example_name.lwjgl3.Game.OnComingFood;
import io.github.some_example_name.lwjgl3.Game.PlayerStats;

public class ResolveCollision {

    private final MovementCalculator colMove;
    private final Audio              audio;
    private final PlayerStats        playerStats; // null-safe: may be null if 3-arg constructor used

    public ResolveCollision(MovementCalculator colMove, Audio audio, PlayerStats playerStats) {
        this.colMove     = colMove;
        this.audio       = audio;
        this.playerStats = playerStats;
    }

    public void collisionResolve(Entity a, Entity b) {

        // ══ Rule: OncomingFood hits the player NPC ════════════════
        // Only active when playerStats is wired in (4-arg CollisionManager path)
        if (playerStats != null) {
            if (a instanceof OnComingFood && b instanceof Player) {
                Player player = (Player) b;
                if (!player.getIsFalling()) { handleFoodCatch((OnComingFood) a); return; }
            }
            if (b instanceof OnComingFood && a instanceof Player) {
                Player player = (Player) a;
                if (!player.getIsFalling()) { handleFoodCatch((OnComingFood) b); return; }
            }
        }
    }

    // ─── NEW: food caught by player NPC ───────────────────────────
    private void handleFoodCatch(OnComingFood food) {
        if (!food.isActive()) return;
        food.deactivate();
        playerStats.applyFood(food.getFoodType());
        switch (food.getFoodType()) {
            case HEALTHY:   audio.playSound("eat_healthy"); break;
            case VITAMIN:   audio.playSound("eat_vitamin"); break;
            case UNHEALTHY: audio.playSound("eat_junk");    break;
        }
        System.out.println("[Collision] Food caught: " + food.getFoodType()
            + " Name=" + playerStats.getName()
            + " | HP=" + playerStats.getHp()
            + " Armor=" + playerStats.getArmor()
            + " Score=" + playerStats.getScore());
    }

}
