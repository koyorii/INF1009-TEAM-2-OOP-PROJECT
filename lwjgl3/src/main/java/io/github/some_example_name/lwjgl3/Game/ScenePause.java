package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;

public class ScenePause extends Scene{
    private Stage stage; // Handles UI drawing and input events
    private Skin skin; // Container for styling through a separate JSON file (In Assets)

    private final IOManager io;

    public ScenePause(ISceneManager ism, IOManager io) {
        super(ism);
        this.io = io;

        // Initialize the stage to manage UI elements
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Applies design to the paused text by borrowing fom assetmanager
        this.skin = am.get("skin/craftacular-ui.json", Skin.class);
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        // Pause text
        Label pauseLabel = new Label("PAUSED", skin, "title");
        // Unpause instructions
        Label unpauseLabel = new Label("Press Escape to resume game", skin, "default");

        // Scaling
        pauseLabel.setFontScale(0.6f);
        unpauseLabel.setFontScale(0.4f);
        float buttonWidth = 360f;
        float buttonHeight = 55f;

        // Setting up button designs
        TextButton returnToGameButton = new TextButton("Return to Game", skin, "default");
        TextButton returnToMenuButton = new TextButton("Return to Menu", skin, "default");
        TextButton exitGameButton = new TextButton("Quit Game", skin, "default");

        // On click, return to game, just in case players dk how to read :D
        returnToGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ism.setScene(SceneManager.State.GAME);
            }
        });

        // On click, return to menu
        returnToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ism.setScene(SceneManager.State.MENU);
            }
        });

        // On click, exit game
        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Pause text add to container
        menuContainer.add(pauseLabel);
        menuContainer.row();

        // Unpause instructions add to container
        menuContainer.add(unpauseLabel).padBottom(30);
        menuContainer.row();

        // Add return to game button to container
        menuContainer.add(returnToGameButton).width(buttonWidth).height(buttonHeight).padBottom(20);
        menuContainer.row();

        // Add return to menu button to container
        menuContainer.add(returnToMenuButton).width(buttonWidth).height(buttonHeight).padBottom(20);
        menuContainer.row();

        // Add exit button to container
        menuContainer.add(exitGameButton).width(buttonWidth).height(buttonHeight).padBottom(80);
    }

    @Override
    public void update(float delta) {
        stage.act(delta); // Enables time based actions

        // Pause the game when the player hits escape
        if (io.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.setScene(SceneManager.State.GAME);
        }
    }

    // Draws a semi transparent overlay with the PAUSED text
    @Override
    public void render(ShapeRenderer shape, SpriteBatch sb) {
        // Color the screen darker when paused
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.4f);
        shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shape.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

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
