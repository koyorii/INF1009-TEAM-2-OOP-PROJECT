package io.github.some_example_name.lwjgl3.Engine.collisionManager;

import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;
import io.github.some_example_name.lwjgl3.Engine.entityManager.getEntityList;
import io.github.some_example_name.lwjgl3.Engine.iomanager.getInputs;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementCalculator;
import io.github.some_example_name.lwjgl3.Game.PlayerStats;

import java.util.List;

public class CollisionManager {

    private CheckCollision checker = new CheckCollision();
    private ResolveCollision resolver;
    private getEntityList provider;
    private List<Entity> entities;

    // ── 4-arg constructor (used by GameMaster with food mechanic) ─
    public CollisionManager(getEntityList provider,
                            MovementCalculator colMove,
                            getInputs audio,
                            PlayerStats playerStats) {
        this.provider = provider;
        this.resolver = new ResolveCollision(audio.getAudio(), playerStats);
    }

    // ── 3-arg constructor (fallback — no food mechanic, stats = null safe) ─
    // Kept so any other part of the codebase that constructs CollisionManager
    // the old way doesn't break
    public CollisionManager(getEntityList provider,
                            MovementCalculator colMove,
                            getInputs audio) {
        this.provider = provider;
        this.resolver = new ResolveCollision(audio.getAudio(), null);
    }


    public ResolveCollision getResolver() {
        return resolver;
    }

    // ── Call every frame ─────────────────────────────────────────
    public void update() {
        entities = provider.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity a = entities.get(i);
                Entity b = entities.get(j);
                if (checker.checkCollision(a, b)) {
                    resolver.collisionResolve(a, b);
                }
            }
        }
    }
}
