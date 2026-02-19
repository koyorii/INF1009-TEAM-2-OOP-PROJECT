package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// Main gameplay scene, all fun is in here
public class SceneGame extends Scene{
    private EntityManager EntityM;
    private CollisionManager collisionM;
    private MovementManager MoveM;

    public SceneGame(GameMaster gm) {
        super(gm);

        MoveM = gm.MoveM;
        EntityM = new EntityManager();
        collisionM = new CollisionManager(EntityM,MoveM, gm.IoM.getAudio());

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

    // Checks for movement, collision, and when the escape key is paused to pause the game
    @Override
    public void update(float delta) {
        EntityM.update(MoveM);
        collisionM.update();

        if (gm.IoM.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            gm.getSceneManager().setScene(SceneManager.State.PAUSE);
        }
    }

    // Draws the shapes
    @Override
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        EntityM.draw(shape, batch);
    }

    @Override
    public void dispose() {
        EntityM.dispose();
        if (EntityM != null) {
            EntityM.dispose();
        }
    }
}
