package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;

public class ResolveCollision implements CollisionResolver {
    private MovementCalculator colMove;

    public ResolveCollision(MovementCalculator colMove){
        this.colMove = colMove;
    }

    @Override
    public void collisionResolve(Entity a, Entity b) {
        // Rule 1: Droplet hits the Bucket
        if (a instanceof TextureObject && b instanceof TextureObject) {
            TextureObject objA = (TextureObject) a;
            TextureObject objB = (TextureObject) b;

            if (objA.getIsFalling() && !objB.getIsFalling()) {
                handleCatch(objA);
            } else if (objB.getIsFalling() && !objA.getIsFalling()) {
                handleCatch(objB);
            }
        }

        // Rule 2: Droplet hits the staticCircle — slide off
        if (a instanceof TextureObject && b instanceof staticCircle) {
            TextureObject droplet = (TextureObject) a;
            if (droplet.getIsFalling()) {
                handleDropletCircleSlide(droplet, (staticCircle) b);
            }
        }
        if (b instanceof TextureObject && a instanceof staticCircle) {
            TextureObject droplet = (TextureObject) b;
            if (droplet.getIsFalling()) {
                handleDropletCircleSlide(droplet, (staticCircle) a);
            }
        }

        // Rule 3: Droplet hits the Triangle — slide off
        if (a instanceof TextureObject && b instanceof Triangle) {
            TextureObject droplet = (TextureObject) a;
            if (droplet.getIsFalling()) {
                handleDropletTriangleSlide(droplet, (Triangle) b);
            }
        }
        if (b instanceof TextureObject && a instanceof Triangle) {
            TextureObject droplet = (TextureObject) b;
            if (droplet.getIsFalling()) {
                handleDropletTriangleSlide(droplet, (Triangle) a);
            }
        }

        // Rule 4: Triangle (player) hits the staticCircle obstacle
        if ((a instanceof Triangle && b instanceof staticCircle) ||
            (b instanceof Triangle && a instanceof staticCircle)) {
            Triangle tri = (a instanceof Triangle) ? (Triangle) a : (Triangle) b;
            staticCircle circle = (a instanceof staticCircle) ? (staticCircle) a : (staticCircle) b;
            handleTriangleCirclePushback(tri, circle);
        }
    }

    // ─── Droplet caught by Bucket ───
    private void handleCatch(TextureObject droplet) {
        float screenHeight = Gdx.graphics.getHeight();
        System.out.println("Score! Droplet caught.");
        float randomX = (float) Math.random() * 800;
        colMove.collisionMovement(droplet, randomX, screenHeight);
    }

    // ─── Droplet slides off Circle ───
    // Push the droplet outward from the circle's center so it glides around
    private void handleDropletCircleSlide(TextureObject droplet, staticCircle circle) {
        float dropCenterX = droplet.getX() + droplet.getTexture().getWidth() / 2f;
        float dropCenterY = droplet.getY() + droplet.getTexture().getHeight() / 2f;

        float dx = dropCenterX - circle.getX();
        float dy = dropCenterY - circle.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist == 0) {
            // Droplet is exactly on center — nudge it to the right
            dx = 1;
            dy = 0;
            dist = 1;
        }

        // Normalize the direction and apply a slide push
        float nx = dx / dist;
        float ny = dy / dist;

        float slideStrength = 3.0f; // how strongly it deflects sideways

        // Push the droplet outward along the normal away from circle center
        // Horizontal slide component is stronger so it looks like it rolls off
        float nextx =droplet.getX() + nx * slideStrength;
        float nexty =droplet.getY() + ny * slideStrength;
        colMove.collisionMovement(droplet, nextx, nexty);
        System.out.println("Droplet sliding off circle!");
    }

    // ─── Droplet slides off Triangle ───
    // Determine which side (left slope or right slope) the droplet is on, slide accordingly
    private void handleDropletTriangleSlide(TextureObject droplet, Triangle tri) {
        float dropCenterX = droplet.getX() + droplet.getTexture().getWidth() / 2f;

        float triX = tri.getX();
        float size = tri.getSize();

        float slideStrength = 3.5f;

        if (dropCenterX < triX) {
            // Droplet is on the left slope — slide left and down
            float nextx = droplet.getX() - slideStrength;
            float nexty = droplet.getY() - slideStrength * 0.5f;
            colMove.collisionMovement(droplet, nextx, nexty);
        } else {
            // Droplet is on the right slope — slide right and down
            float nextx = droplet.getX() + slideStrength;
            float nexty = droplet.getY() - slideStrength * 0.5f;
            colMove.collisionMovement(droplet, nextx, nexty);
        }

        System.out.println("Droplet sliding off triangle!");
    }

    // ─── Triangle pushed back from Circle obstacle ───
    private void handleTriangleCirclePushback(Triangle tri, staticCircle circle) {
        float dx = tri.getX() - circle.getX();
        float dist = Math.abs(dx);

        if (dist == 0) dx = 1;

        // Push the triangle away from the circle horizontally
        float pushStrength = 2.0f;
        float direction = dx > 0 ? 1 : -1;
        float nextx = tri.getX() + direction * pushStrength;
        colMove.collisionMovement(tri, nextx, tri.getY());

        System.out.println("Triangle hit an obstacle! Pushed back.");
    }
}