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

import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;


public class SceneDifficulty extends Scene {
    private Stage stage;
    private Skin skin;

    public SceneDifficulty(ISceneManager ism) {
        super(ism);

        // Initialize the Stage, direct keys to UI stage, set up containers like in HTML, apply styling, and listener for button press to go to GAME scene
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.skin = am.get("skin/craftacular-ui.json", Skin.class);
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        Label diffText = new Label("Select Difficulty", skin, "default");

        // Dirt background
        menuContainer.setFillParent(true);
        menuContainer.setBackground(skin.getDrawable("dirt"));

        // The buttons itself
        TextButton fearButton = new TextButton("Fear & Hunger", skin, "default");
        TextButton normalButton = new TextButton("Normal", skin, "default");

        // Setting colour to text of Fear button
        fearButton.getLabel().setColor(new Color(0.5f, 0.0f, 0.0f, 1.0f));

        // Button routes
        fearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                ism.getPlayerStats().setMode(PlayerStats.GameMode.FEARLESS_HUNGER);
                ism.setScene(SceneManager.State.GAME);
            }
        });

        // Button Routes
        normalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                ism.getPlayerStats().setMode(PlayerStats.GameMode.NORMAL);
                ism.setScene(SceneManager.State.GAME);
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

        // Keep menu music playing
        audio.playMusic("menu");
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
