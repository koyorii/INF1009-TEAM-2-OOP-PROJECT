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


public class SceneDifficulty extends Scene {
<<<<<<< Updated upstream
>>>>>>> 7e0e8bb8e6842a268786805e27d8310018f4c018:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/Game/SceneDifficulty.java
=======

>>>>>>> Stashed changes
    private Stage stage;
    private Skin  skin;

    public SceneDifficulty(ISceneManager ism) {
        super(ism);
<<<<<<< Updated upstream
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
=======

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = am.get("skin/craftacular-ui.json", Skin.class);

>>>>>>> Stashed changes
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        menuContainer.setBackground(skin.getDrawable("dirt"));
        stage.addActor(menuContainer);

        Label diffText = new Label("Select Difficulty", skin, "default");

<<<<<<< Updated upstream
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
=======
        TextButton fearButton   = new TextButton("Fear & Hunger", skin, "default");
        TextButton normalButton = new TextButton("Normal", skin, "default");

>>>>>>> Stashed changes
        fearButton.getLabel().setColor(new Color(0.5f, 0.0f, 0.0f, 1.0f));

        // Both buttons delegate to startGame(mode) so SceneManager decides
        // whether to show a tutorial based on the current username + mode.
        fearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
<<<<<<< Updated upstream
                //ism.setScene(SceneManager.State.FEAR);
=======
                ism.startGame(PlayerStats.GameMode.FEARLESS_HUNGER);
>>>>>>> Stashed changes
            }
        });

        normalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
<<<<<<< HEAD:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/actualSceneDifficulty.java
                ism.setScene(SceneManager.State.TUTORIAL);
=======
                audio.playSound("button_click");
<<<<<<< Updated upstream
                ism.setScene(SceneManager.State.GAME);
>>>>>>> 7e0e8bb8e6842a268786805e27d8310018f4c018:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/Game/SceneDifficulty.java
=======
                ism.startGame(PlayerStats.GameMode.NORMAL);
>>>>>>> Stashed changes
            }
        });

        menuContainer.add(diffText);
        menuContainer.row();
        menuContainer.add(fearButton).padTop(10);
        menuContainer.row();
        menuContainer.add(normalButton).padTop(10);
<<<<<<< Updated upstream
        menuContainer.row();
<<<<<<< HEAD:lwjgl3/src/main/java/io/github/some_example_name/lwjgl3/actualSceneDifficulty.java
=======
=======
>>>>>>> Stashed changes

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
        if (stage != null) stage.dispose();
        this.skin = null;
    }
}