package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.iomanager.IOManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private EntityManager EntityM;
    private CollisionManager collisionM;
    private MovementManager MoveM;
    private IOManager IoM;
    @Override
    public void create() {
        IoM = new IOManager();
        EntityM = new EntityManager();
        collisionM = new CollisionManager(EntityM);
        MoveM = new MovementManager(IoM);
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        
        // Instantiate the bucket object 
        EntityM.addEntity(new TextureObject("bucket.png", 200, 20, 2,false));
        // Instantiate the array of 10 droplets 
        for (int i = 0; i < 10; i++){
            float randomX = (float) Math.random() * 800;
            float randomY = 400 + (i * 50); 
            EntityM.addEntity(new TextureObject("droplet.png", randomX, randomY, 2, true));
        }
        
        // Instantiate Circle (center of screen, radius 30)
        EntityM.addEntity(new staticCircle(400, 350, 5, 30, Color.RED));
        
        // Instantiate Triangle (right side, size 50)
        EntityM.addEntity(new Triangle(600, 200, 5, 50, Color.GREEN));
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        EntityM.update(MoveM);
        collisionM.update();
        EntityM.draw(shape, batch); 
        
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
    }
}