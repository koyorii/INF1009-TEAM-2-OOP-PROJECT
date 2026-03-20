package io.github.some_example_name.lwjgl3.Engine.collisionManager;

import com.badlogic.gdx.Gdx;

import io.github.some_example_name.lwjgl3.Triangle;
import io.github.some_example_name.lwjgl3.staticCircle;
import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;
import io.github.some_example_name.lwjgl3.Engine.iomanager.Audio;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementCalculator;
import io.github.some_example_name.lwjgl3.Game.OnComingFood;
import io.github.some_example_name.lwjgl3.Game.PlayerStats;
import io.github.some_example_name.lwjgl3.Game.playerNPC;

public class ResolveCollision {

    private MovementCalculator colMove;
    private Audio              audio;
    private PlayerStats        playerStats; // null-safe: may be null if 3-arg constructor used

    public ResolveCollision(MovementCalculator colMove, Audio audio, PlayerStats playerStats) {
        this.colMove     = colMove;
        this.audio       = audio;
        this.playerStats = playerStats;
    }

    public void collisionResolve(Entity a, Entity b) {

        // ══ Rule: OncomingFood hits the player NPC ════════════════
        // Only active when playerStats is wired in (4-arg CollisionManager path)
        if (playerStats != null) {
            if (a instanceof OnComingFood && b instanceof playerNPC) {
                playerNPC player = (playerNPC) b;
                if (!player.getIsFalling()) { handleFoodCatch((OnComingFood) a); return; }
            }
            if (b instanceof OnComingFood && a instanceof playerNPC) {
                playerNPC player = (playerNPC) a;
                if (!player.getIsFalling()) { handleFoodCatch((OnComingFood) b); return; }
            }
        }

        // ══ Original Rule 1: Droplet hits the Bucket ══════════════
        if (a instanceof playerNPC && b instanceof playerNPC) {
            playerNPC objA = (playerNPC) a;
            playerNPC objB = (playerNPC) b;
            if (objA.getIsFalling() && !objB.getIsFalling())      handleCatch(objA);
            else if (objB.getIsFalling() && !objA.getIsFalling()) handleCatch(objB);
        }

        // ══ Original Rule 2: Droplet slides off staticCircle ══════
        if (a instanceof playerNPC && b instanceof staticCircle) {
            playerNPC droplet = (playerNPC) a;
            if (droplet.getIsFalling()) handleDropletCircleSlide(droplet, (staticCircle) b);
        }
        if (b instanceof playerNPC && a instanceof staticCircle) {
            playerNPC droplet = (playerNPC) b;
            if (droplet.getIsFalling()) handleDropletCircleSlide(droplet, (staticCircle) a);
        }

        // ══ Original Rule 3: Droplet slides off Triangle ══════════
        if (a instanceof playerNPC && b instanceof Triangle) {
            playerNPC droplet = (playerNPC) a;
            if (droplet.getIsFalling()) handleDropletTriangleSlide(droplet, (Triangle) b);
        }
        if (b instanceof playerNPC && a instanceof Triangle) {
            playerNPC droplet = (playerNPC) b;
            if (droplet.getIsFalling()) handleDropletTriangleSlide(droplet, (Triangle) a);
        }

        // ══ Original Rule 4: Triangle hits staticCircle obstacle ══
        if ((a instanceof Triangle && b instanceof staticCircle) ||
            (b instanceof Triangle && a instanceof staticCircle)) {
            Triangle     tri    = (a instanceof Triangle)     ? (Triangle)     a : (Triangle)     b;
            staticCircle circle = (a instanceof staticCircle) ? (staticCircle) a : (staticCircle) b;
            handleTriangleCirclePushback(tri, circle);
        }
    }

    // ─── NEW: food caught by player NPC ───────────────────────────
    private void handleFoodCatch(OnComingFood food) {
        if (!food.isActive()) return;
        food.deactivate();
        playerStats.applyFood(food.getFoodType());
        audio.playSound(food.getFoodType() == OnComingFood.FoodType.UNHEALTHY ? "hit" : "catch");
        System.out.println("[Collision] Food caught: " + food.getFoodType()
            + " | HP=" + playerStats.getHp()
            + " Armor=" + playerStats.getArmor()
            + " Score=" + playerStats.getScore());
    }

    // ─── Original handlers (unchanged) ───────────────────────────
    private void handleCatch(playerNPC droplet) {
        System.out.println("Score! Droplet caught.");
        audio.playSound("catch");
        float randomX = (float) Math.random() * 800;
        colMove.collisionMovement(droplet, randomX, Gdx.graphics.getHeight());
    }

    private void handleDropletCircleSlide(playerNPC droplet, staticCircle circle) {
        float dx   = (droplet.getX() + droplet.getTexture().getWidth()  / 2f) - circle.getX();
        float dy   = (droplet.getY() + droplet.getTexture().getHeight() / 2f) - circle.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) { dx = 1; dy = 0; dist = 1; }
        float slideStrength = 3.0f;
        colMove.collisionMovement(droplet,
            droplet.getX() + (dx / dist) * slideStrength,
            droplet.getY() + (dy / dist) * slideStrength);
        System.out.println("Droplet sliding off circle!");
    }

    private void handleDropletTriangleSlide(playerNPC droplet, Triangle tri) {
        float slideStrength = 3.5f;
        boolean onLeft = (droplet.getX() + droplet.getTexture().getWidth() / 2f) < tri.getX();
        colMove.collisionMovement(droplet,
            droplet.getX() + (onLeft ? -slideStrength : slideStrength),
            droplet.getY() - slideStrength * 0.5f);
        System.out.println("Droplet sliding off triangle!");
        audio.playSound("hit");
    }

    private void handleTriangleCirclePushback(Triangle tri, staticCircle circle) {
        float dx        = tri.getX() - circle.getX();
        float direction = dx >= 0 ? 1 : -1;
        colMove.collisionMovement(tri, tri.getX() + direction * 2.0f, tri.getY());
        System.out.println("Triangle hit an obstacle! Pushed back.");
        audio.playSound("hit");
    }
}