package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.ISceneManager;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.Engine.sceneManager.SceneManager;

public class SceneGoodEnding extends Scene {
    private final Stage stage;
    private final Skin skin;

    public SceneGoodEnding(ISceneManager ism, PlayerStats ps) {
        super(ism);
        // Initialize the Stage, direct keys to UI stage, set up containers like in HTML, apply styling, and listener for button press to go to GAME scene
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.skin = am.get("skin/craftacular-ui.json", Skin.class);
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        // Setting the background to back at home
        Texture endBackground = am.get("backgrounds/home_background.jpg", Texture.class);
        menuContainer.setBackground(new TextureRegionDrawable(new TextureRegion(endBackground)));

        // Player outcome image
        Texture playerPicTexture = am.get("good_ending_npc.png", Texture.class);
        Image playerPicImage = new Image(playerPicTexture);
        playerPicImage.setScaling(Scaling.stretch);

        // Show final score for user reference
        Label scoreLabel = new Label("Final Score: " + ps.getScore(), skin, "default");

        // Route buttons
        TextButton menuButton = new TextButton("Return to Menu", skin, "default");
        TextButton exitButton = new TextButton("Quit Game", skin, "default");
        menuButton.getLabel().setFontScale(0.8f);
        exitButton.getLabel().setFontScale(0.8f);

        // Button to return to menu
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                ism.setScene(SceneManager.State.MENU);
            }
        });

        // Button to quit game
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                Gdx.app.exit();
            }
        });

        // Play good ending music
        audio.playMusic("ending_good");
        menuContainer.add(scoreLabel).colspan(2).padTop(25);
        menuContainer.row();

        // Boy Eating
        menuContainer.add(playerPicImage).colspan(2).size(400, 300).expandY().center();
        menuContainer.row();

        // The buttons
        menuContainer.add(menuButton).left().pad(30).width(230).height(60);
        menuContainer.add(exitButton).right().pad(30).width(230).height(60);
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
    }
}
