package io.github.some_example_name.lwjgl3.entityManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.some_example_name.lwjgl3.TextureObject;
import io.github.some_example_name.lwjgl3.movementManager.MovementCalculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityManager implements getEntityList {

    private List<Entity> entityList;

    public EntityManager() {
        entityList = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entityList.add(entity);
    }

    public void update(MovementCalculator moveM) {

        // Step 1: Remove OncomingFood that passed off-screen (missed)
        // or was caught (deactivated by ResolveCollision)
        Iterator<Entity> iter = entityList.iterator();
        while (iter.hasNext()) {
            Entity e = iter.next();
            if (e instanceof OnComingFood) {
                OnComingFood food = (OnComingFood) e;
                if (food.isOffScreen() || !food.isActive()) {
                    iter.remove();
                }
            }
        }

        // Step 2: Update all remaining entities
        for (Entity entity : entityList) {
            entity.update();

            // OncomingFood moves itself in its own update() — skip MovementCalculator for it
            // TextureObject player NPC is moved by PlayerController (A/D keys) — also skip
            // MovementCalculator is only needed for other MovableEntities (Triangle etc.)
            if (entity instanceof MovableEntity
                    && !(entity instanceof OnComingFood)
                    && !(entity instanceof TextureObject)) {
                MovableEntity moveEntity = (MovableEntity) entity;
                moveM.calculateMovement(moveEntity, false, moveEntity.getSpeed());
            }
        }
    }

    public void draw(ShapeRenderer shape, SpriteBatch batch) {
        // Draw all SpriteBatch-based entities in one begin/end block
        batch.begin();
        for (Entity e : entityList) {
            if (e instanceof TextureObject || e instanceof OnComingFood) {
                e.draw(batch);
            }
        }
        batch.end();

        // Draw all ShapeRenderer-based entities
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entityList) {
            if (!(e instanceof TextureObject) && !(e instanceof OnComingFood)) {
                e.draw(shape);
            }
        }
        shape.end();
    }

    @Override
    public List<Entity> getEntities() {
        return entityList;
    }

    public void clearEntities() {
        entityList.clear();
    }

    public void dispose() {
        for (Entity e : entityList) {
            e.dispose();
        }
        entityList.clear();
    }
}
