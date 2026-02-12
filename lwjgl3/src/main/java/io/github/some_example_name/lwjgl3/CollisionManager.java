package io.github.some_example_name.lwjgl3;

import java.util.List;

public class CollisionManager {
    private CheckCollision checker = new CheckCollision();
    private ResolveCollision resolver = new ResolveCollision();

    public void update(List<Entity> entities) {
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
