package io.github.some_example_name.lwjgl3;

public class ResolveCollision implements CollisionResolver {
    @Override
    public void collisionResolve(Entity a, Entity b) {
        // Rule 1: Droplet hits the Bucket
        if (a instanceof TextureObject && b instanceof TextureObject) {
            TextureObject objA = (TextureObject) a;
            TextureObject objB = (TextureObject) b;

            // Check which one is the falling droplet and which is the bucket
            if (objA.getIsFalling() && !objB.getIsFalling()) {
                handleCatch(objA);
            } else if (objB.getIsFalling() && !objA.getIsFalling()) {
                handleCatch(objB);
            }
        }

        // Rule 2: Triangle hits the Red Obstacle (staticCircle)
        if ((a instanceof Triangle && b instanceof staticCircle) || 
            (b instanceof Triangle && a instanceof staticCircle)) {
            System.out.println("Triangle hit an obstacle!");
            // You could stop the triangle or bounce it back here
        }
    }

    private void handleCatch(TextureObject droplet) {
        System.out.println("Score! Droplet caught.");
        // Reset droplet to the top of the screen
        droplet.setY(480); 
    }
}
