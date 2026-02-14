package io.github.some_example_name.lwjgl3;

import java.util.List;

public class CollisionManager {
    private CheckCollision checker = new CheckCollision();
    private ResolveCollision resolver = new ResolveCollision();
    private getEntityList provider;
    private List<Entity> entities;
    
    public CollisionManager(getEntityList provider) {
        this.provider = provider;
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
