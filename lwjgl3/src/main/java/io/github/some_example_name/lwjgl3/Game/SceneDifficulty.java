package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
<<<<<<< HEAD:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/actualSceneDifficulty.java
import io.github.some_example_name.lwjgl3.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.sceneManager.SceneManager;

public class actualSceneDifficulty extends Scene {

=======

import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;


public class SceneDifficulty extends Scene {
>>>>>>> 7e0e8bb8e6842a268786805e27d8310018f4c018:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/Game/SceneDifficulty.java
    private Stage stage;
    private Skin skin;

    public SceneDifficulty(ISceneManager ism) {
        super(ism);
<<<<<<< HEAD:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/actualSceneDifficulty.java
        // Initialise the Stage, direct keys to UI stage, set up
        // containers like in HTML, apply styling, and listener for button press
        // to go to GAME scene
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

=======

        // Initialize the Stage, direct keys to UI stage, set up containers like in HTML, apply styling, and listener for button press to go to GAME scene
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.skin = am.get("skin/craftacular-ui.json", Skin.class);
>>>>>>> 7e0e8bb8e6842a268786805e27d8310018f4c018:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/Game/SceneDifficulty.java
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        Label diffText = new Label("Select Difficulty", skin, "default");

        // Dirt background
        menuContainer.setFillParent(true);
        menuContainer.setBackground(skin.getDrawable("dirt"));

<<<<<<< HEAD:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/actualSceneDifficulty.java
        TextButton fearButton   = new TextButton("Fear & Hunger", skin, "default");
=======
        // The buttons itself
        TextButton fearButton = new TextButton("Fear & Hunger", skin, "default");
>>>>>>> 7e0e8bb8e6842a268786805e27d8310018f4c018:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/Game/SceneDifficulty.java
        TextButton normalButton = new TextButton("Normal", skin, "default");

        // Setting colour to text of Fear button
        fearButton.getLabel().setColor(new Color(0.5f, 0.0f, 0.0f, 1.0f));

        // Button routes
        fearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                //ism.setScene(SceneManager.State.FEAR);
            }
        });

        // Normal mode now routes to TUTORIAL first, then TUTORIAL goes to GAME
        normalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
<<<<<<< HEAD:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/actualSceneDifficulty.java
                ism.setScene(SceneManager.State.TUTORIAL);
=======
                audio.playSound("button_click");
                ism.setScene(SceneManager.State.GAME);
>>>>>>> 7e0e8bb8e6842a268786805e27d8310018f4c018:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/Game/SceneDifficulty.java
            }
        });

        // Instruction Text
        menuContainer.add(diffText);
        menuContainer.row();
        // Game buttons
        menuContainer.add(fearButton).padTop(10);
        menuContainer.row();
        menuContainer.add(normalButton).padTop(10);
        menuContainer.row();
<<<<<<< HEAD:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/actualSceneDifficulty.java
=======

        // Keep menu music playing
        audio.playMusic("menu");
>>>>>>> 7e0e8bb8e6842a268786805e27d8310018f4c018:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/Game/SceneDifficulty.java
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch spriteBatch) {
        stage.draw();
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }

        this.skin = null;
    }
}