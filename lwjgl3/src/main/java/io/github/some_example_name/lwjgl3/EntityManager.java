package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    // List to store all entities (Movable and NonMovable)
    private List<Entity> entityList;

    public EntityManager() {
        entityList = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entityList.add(entity);
    }

    // Handles logic and movement for all entities
    public void update() {
        for (Entity entity : entityList) {
            entity.update(); // Call standard update
            
            // If it's movable, call its specific movement logic
            if (entity instanceof MovableEntity) {
                ((MovableEntity) entity).movement();
            }
        }
    }

    // Handles rendering for both Shape-based and Texture-based entities
    public void draw(ShapeRenderer shape, SpriteBatch batch) {
        batch.begin();
        for (Entity e : entityList){
            if (e instanceof TextureObject){
                e.draw(batch);
            } 
        }
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity e : entityList){
            if (!(e instanceof TextureObject)){
                e.draw(shape);
            } 
        }
        shape.end();
    }

    // Returns the list for collision detection outside this class if needed
    public List<Entity> getEntities() {
        return entityList;
    }
}