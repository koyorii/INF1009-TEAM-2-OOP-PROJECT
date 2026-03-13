package io.github.some_example_name.lwjgl3.collisionManager;
import io.github.some_example_name.lwjgl3.entityManager.Entity;
import io.github.some_example_name.lwjgl3.entityManager.getEntityList;
import io.github.some_example_name.lwjgl3.iomanager.getInputs;
import io.github.some_example_name.lwjgl3.movementManager.MovementCalculator;

import java.util.List;

public class CollisionManager {
    private CheckCollision checker = new CheckCollision();
    private ResolveCollision resolver;
    private getEntityList provider;
    private List<Entity> entities;
    
    public CollisionManager(getEntityList provider, MovementCalculator colMove, getInputs audio) {
        this.provider = provider;
        this.resolver = new ResolveCollision(colMove, audio.getAudio());
    }

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
