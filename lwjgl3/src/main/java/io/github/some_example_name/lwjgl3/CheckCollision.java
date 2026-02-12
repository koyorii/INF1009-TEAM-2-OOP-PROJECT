package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class CheckCollision implements CollisionChecker{
    private static final Vector2 center = new Vector2();
    private static final Vector2 v1 = new Vector2();
    private static final Vector2 v2 = new Vector2();
    @Override
    public boolean checkCollision(Entity a, Entity b) {
        // Handle Polygon vs Polygon (e.g., Triangle vs TextureObject)
        if (a instanceof MovableEntity && b instanceof MovableEntity) {
            return Intersector.overlapConvexPolygons(
                ((MovableEntity) a).getBounds(), 
                ((MovableEntity) b).getBounds()
            );
        }
        // Handle Polygon vs Circle (e.g., Triangle vs staticCircle)
        // 2. Polygon vs Circle (Triangle/Bucket vs Circle)
        if (a instanceof MovableEntity && b instanceof staticCircle) {
            return checkPolygonCircle(((MovableEntity) a).getBounds(), ((staticCircle) b).getCircleBounds());
        }
        
        // 3. Circle vs Polygon (Reverse order)
        if (a instanceof staticCircle && b instanceof MovableEntity) {
            return checkPolygonCircle(((MovableEntity) b).getBounds(), ((staticCircle) a).getCircleBounds());
        }

        return false;
    }
    private boolean checkPolygonCircle(Polygon polygon, Circle circle) {
        float[] vertices = polygon.getTransformedVertices();
        center.set(circle.x, circle.y);
        float squareRadius = circle.radius * circle.radius;

        // Step 1: Check if any edge of the polygon intersects the circle
        for (int i = 0; i < vertices.length; i += 2) {
            v1.set(vertices[i], vertices[i + 1]);
            // Connect last vertex to first vertex
            int next = (i + 2) % vertices.length;
            v2.set(vertices[next], vertices[next + 1]);

            if (Intersector.intersectSegmentCircle(v1, v2, center, squareRadius)) {
                return true;
            }
        }

        // Step 2: Check if the circle is completely inside the polygon
        return Intersector.isPointInPolygon(vertices, 0, vertices.length, circle.x, circle.y);
    }
}
