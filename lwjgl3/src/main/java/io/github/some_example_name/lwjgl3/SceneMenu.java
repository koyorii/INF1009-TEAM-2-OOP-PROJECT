package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.some_example_name.lwjgl3.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.sceneManager.SceneManager;

// Main menu screen
public class SceneMenu extends Scene{
    private Stage stage; // Handles UI drawing and input events
    private Skin skin; // Container for styling through a separate JSON file (In Assets)

    public SceneMenu(GameMaster gm) {
        super(gm);

        // Initialise the Stage, direct keys to UI stage, set up containers like in HTML, apply styling, and listener for button press to go to GAME scene
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        // Dirt background
        menuContainer.setFillParent(true);
        menuContainer.setBackground(skin.getDrawable("dirt"));

        // Text above the buttons
        Label gameName = new Label("Sustenance", skin, "title");
        Label authorName = new Label("a game by Lab P10 Team 2", skin, "default");
        gameName.setFontScale(0.6f);
        authorName.setFontScale(0.6f);

        // All the different buttons in main menu
        TextButton playButton = new TextButton("New Game", skin, "default");
        TextButton leadButton = new TextButton("Leaderboard", skin, "default");
        TextButton exitButton = new TextButton("Exit", skin, "default");

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gm.getSceneManager().setScene(SceneManager.State.GAME);
            }
        });

        leadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gm.getSceneManager().setScene(SceneManager.State.Leaderboard);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Title Card
        menuContainer.add(gameName);
        menuContainer.row();

        // Author Intro
        menuContainer.add(authorName).left().padBottom(60);
        menuContainer.row();

        // Play button
        menuContainer.add(playButton).width(255).height(45).padBottom(20);
        menuContainer.row();

        // Leaderboard button
        menuContainer.add(leadButton).width(255).height(45).padBottom(20);
        menuContainer.row();

        // Exit button, don't need call dispose cuz Libgdx will do it for us
        menuContainer.add(exitButton).width(255).height(45);
    }

    // Update input logic
    @Override
    public void update(float delta) {
        stage.act(delta); // Allows for time based actions
    }

    // Draws all actors in the stage
    @Override
    public void render(ShapeRenderer shape, SpriteBatch sb) {
        stage.draw();
    }

    // Resource release
    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
    }
}
