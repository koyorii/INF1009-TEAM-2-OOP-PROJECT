package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.lwjgl3.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.sceneManager.SceneManager;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

// Main gameplay scene, all fun is in here
public class SceneGame extends Scene{
    private EntityManager EntityM;
    private CollisionManager collisionM;
    private MovementManager MoveM;
    private Stage stage; // Handles UI drawing and input events
    private Skin skin; // Container for styling through a separate JSON file (In Assets)

    public SceneGame(GameMaster gm) {
        super(gm);

        // Initialize the Stage, direct keys to UI stage, set up containers like in HTML, apply styling, and listener for button press to go to GAME scene
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        // Dirt background
        menuContainer.setFillParent(true);
        menuContainer.setBackground(skin.getDrawable("dirt"));

        MoveM = gm.MoveM;
        EntityM = new EntityManager();
        collisionM = new CollisionManager(EntityM,MoveM, gm.IoM);

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
