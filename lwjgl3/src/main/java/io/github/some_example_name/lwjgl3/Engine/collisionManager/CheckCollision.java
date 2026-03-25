package io.github.some_example_name.lwjgl3.Engine.collisionManager;

import com.badlogic.gdx.math.Intersector;


import io.github.some_example_name.lwjgl3.Engine.entityManager.Entity;
import io.github.some_example_name.lwjgl3.Engine.entityManager.MovableEntity;
import io.github.some_example_name.lwjgl3.Game.OnComingFood;

public class CheckCollision {

    public boolean checkCollision(Entity a, Entity b) {

        // ── Skip inactive food immediately ────────────────────────
        if (a instanceof OnComingFood && !((OnComingFood) a).isActive()) return false;
        if (b instanceof OnComingFood && !((OnComingFood) b).isActive()) return false;

        // ── Polygon vs Polygon (covers OnComingFood vs TextureObject,
        //    Triangle vs TextureObject, and any two MovableEntities) ─
        if (a instanceof MovableEntity && b instanceof MovableEntity) {
            return Intersector.overlapConvexPolygons(
                ((MovableEntity) a).getBounds(),
                ((MovableEntity) b).getBounds()
            );
        }

        return false;
    }
}
