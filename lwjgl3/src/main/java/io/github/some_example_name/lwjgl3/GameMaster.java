package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private TextureObject bucket;
    private TextureObject[] droplets;
    private Circle circle;
    private Triangle triangle;
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        
        // Instantiate the bucket object 
        bucket = new TextureObject("bucket.png", 200, 20, 5);
        
        // Instantiate the array of 10 droplets 
        droplets = new TextureObject[10];
        for (int i = 0; i < droplets.length; i++) {
            float randomX = (float) Math.random() * 800; 
            float randomY = 400 + (i * 50); 
            droplets[i] = new TextureObject("droplet.png", randomX, randomY, 2);
        }
        
        // Instantiate Circle (center of screen, radius 30)
        circle = new Circle(400, 300, 5, 30, Color.RED);
        
        // Instantiate Triangle (right side, size 50)
        triangle = new Triangle(600, 70, 5, 50, Color.GREEN);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        
        // Draw textures
        batch.begin();
        
        // Draw the bucket 
        batch.draw(bucket.getTexture(), bucket.getX(), bucket.getY());
        
        // Draw and move all droplets 
        for (TextureObject drop : droplets) {
            batch.draw(drop.getTexture(), drop.getX(), drop.getY(), 
                       drop.getTexture().getWidth(), drop.getTexture().getHeight());
            // Use overloaded movement method for falling droplets
            drop.movement(true);
        }
        
        batch.end();
        
        // Draw shapes
        shape.begin(ShapeRenderer.ShapeType.Filled);
        
        circle.draw(shape);
        triangle.draw(shape);
        
        shape.end();
        
        // Handle movements - demonstrates polymorphism
        // Each entity has its own movement implementation
        bucket.movement();  // LEFT/RIGHT keys
        circle.movement();  // UP/DOWN keys
        triangle.movement(); // A/D keys
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
    }
}