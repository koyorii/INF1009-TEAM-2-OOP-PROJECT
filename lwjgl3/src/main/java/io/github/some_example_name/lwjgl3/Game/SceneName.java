package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
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

public class SceneName extends Scene {
    private final Stage stage;
    private Skin skin;

    public SceneName(ISceneManager ism, PlayerStats ps) {
        super(ism);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        // Dirt background
        menuContainer.setFillParent(true);
        menuContainer.setBackground(skin.getDrawable("dirt"));

        // Please enter name
        Label nameLabel = new Label("Input Name", skin, "default");

        // Textfield input box
        final TextField nameInput = new TextField("", skin, "default");

        // Submit button
        TextButton startButton = new TextButton("Confirm", skin, "default");
        startButton.getLabel().setFontScale(0.8f);
        startButton.setScale(0.8f);

        // Listener to the button of submission
        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String finalName = nameInput.getText();

                if (finalName.trim().isEmpty()) {
                    ps.setName("Blank");
                } else {
                    ps.setName(finalName);
                }

                // Switch Scenes
                ism.setScene(SceneManager.State.DIFFICULTY);

            }
        });

        // Add all the stuff and assets to the screen
        menuContainer.add(nameLabel).padBottom(10);
        menuContainer.row();

        menuContainer.add(nameInput).width(300).padBottom(20);
        menuContainer.row();

        menuContainer.add(startButton).width(150);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);

    }

    @Override
    public void render(ShapeRenderer sr, SpriteBatch sb) {
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
