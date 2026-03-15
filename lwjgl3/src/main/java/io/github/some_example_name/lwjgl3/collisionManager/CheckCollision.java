package io.github.some_example_name.lwjgl3.collisionManager;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.lwjgl3.entityManager.Entity;
import io.github.some_example_name.lwjgl3.entityManager.MovableEntity;
import io.github.some_example_name.lwjgl3.entityManager.OnComingFood;
import io.github.some_example_name.lwjgl3.staticCircle;

public class CheckCollision {

    private static final Vector2 center = new Vector2();
    private static final Vector2 v1     = new Vector2();
    private static final Vector2 v2     = new Vector2();

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

        // ── Polygon vs Circle (MovableEntity vs staticCircle) ────
        if (a instanceof MovableEntity && b instanceof staticCircle) {
            return checkPolygonCircle(
                ((MovableEntity) a).getBounds(),
                ((staticCircle) b).getCircleBounds()
            );
        }

        // ── Circle vs Polygon (reverse order) ────────────────────
        if (a instanceof staticCircle && b instanceof MovableEntity) {
            return checkPolygonCircle(
                ((MovableEntity) b).getBounds(),
                ((staticCircle) a).getCircleBounds()
            );
        }

        return false;
    }

    // ─── Helper: polygon edge intersection with circle ────────────
    private boolean checkPolygonCircle(Polygon polygon, Circle circle) {
        float[] vertices     = polygon.getTransformedVertices();
        center.set(circle.x, circle.y);
        float squareRadius   = circle.radius * circle.radius;

        for (int i = 0; i < vertices.length; i += 2) {
            v1.set(vertices[i], vertices[i + 1]);
            int next = (i + 2) % vertices.length;
            v2.set(vertices[next], vertices[next + 1]);

            if (Intersector.intersectSegmentCircle(v1, v2, center, squareRadius)) {
                return true;
            }
        }

        return Intersector.isPointInPolygon(vertices, 0, vertices.length, circle.x, circle.y);
    }
}