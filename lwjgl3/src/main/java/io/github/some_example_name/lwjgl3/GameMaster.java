package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.lwjgl3.iomanager.IOManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMaster extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shape;

    // sceneM typed as ISceneManager interface, not SceneManager directly
    // GameMaster only sees what ISceneManager exposes (abstraction)
    private ISceneManager sceneM;
    protected IOManager IoM;
    protected MovementManager MoveM;
    protected EntityManager EntityM;
    protected CollisionManager collisionM;

    @Override
    public void create() {
    	// Initialize Managers
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        IoM = new IOManager();
        MoveM = new MovementManager(IoM);

        // New Managers from your local changes
        EntityM = new EntityManager(); // Ensure this variable is declared at the class level
        collisionM = new CollisionManager(EntityM, MoveM, IoM.getAudio());

        // Load Audio Assets
        IoM.getAudio().loadSound("catch", "catch.wav");
        IoM.getAudio().loadSound("hit", "hit.wav");

        // Instantiate Game Objects
        EntityM.addEntity(new TextureObject("bucket.png", 200, 20, 2, false));
        for (int i = 0; i < 10; i++) {
            float randomX = (float) Math.random() * 800;
            float randomY = 400 + (i * 50);
            EntityM.addEntity(new TextureObject("droplet.png", randomX, randomY, 2, true));
        }
        EntityM.addEntity(new staticCircle(400, 350, 5, 30, Color.RED));
        EntityM.addEntity(new Triangle(600, 200, 5, 50, Color.GREEN));

       // SceneManager instantiated here but stored as ISceneManager
        // other classes communicate with it only through the interface
        sceneM = new SceneManager(this);
        sceneM.setScene(SceneManager.State.MENU);    }

    // getter allows other classes to access sceneM through interface only
    // encapsulation - sceneM field stays private
    public ISceneManager getSceneManager() {
        return sceneM;
}
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        sceneM.update(delta);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        sceneM.render(shape, batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        sceneM.dispose(); // Keep from HEAD
        EntityM.dispose();
        IoM.getAudio().dispose(); // Keep from your changes
    }
}
