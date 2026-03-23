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

public class ScenePause extends Scene {
    private Stage stage;
    private Skin skin;

    private final IOManager io;

    // Volume duck settings
    private static final float DUCK_VOLUME    = 0.2f;  // target volume while paused
    private static final float FULL_VOLUME    = 1.0f;  // volume when back in game
    private static final float DUCK_SPEED     = 2.5f;  // how fast to fade down (units/sec)
    private static final float RESTORE_SPEED  = 3.5f;  // how fast to fade back up

    private float currentVolume = FULL_VOLUME;
    private boolean restoring   = false;  // true when fading back up before scene switch

    // Stored scene to switch to after restore fade finishes
    private SceneManager.State pendingScene = null;

    public ScenePause(ISceneManager ism, IOManager io) {
        super(ism);
        this.io = io;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.skin = am.get("skin/craftacular-ui.json", Skin.class);
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        Label pauseLabel   = new Label("PAUSED", skin, "title");
        Label unpauseLabel = new Label("Press Escape to resume game", skin, "default");

        pauseLabel.setFontScale(0.6f);
        unpauseLabel.setFontScale(0.4f);
        float buttonWidth  = 360f;
        float buttonHeight = 55f;

        TextButton returnToGameButton = new TextButton("Return to Game", skin, "default");
        TextButton returnToMenuButton = new TextButton("Return to Menu", skin, "default");
        TextButton exitGameButton     = new TextButton("Quit Game",      skin, "default");

        // Return to game — restore volume then switch
        returnToGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                startRestore(SceneManager.State.GAME);
            }
        });

        // Return to menu
        returnToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                ism.setScene(SceneManager.State.MENU);
            }
        });

        // Quit
        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                Gdx.app.exit();
            }
        });

        menuContainer.add(pauseLabel);
        menuContainer.row();
        menuContainer.add(unpauseLabel).padBottom(30);
        menuContainer.row();
        menuContainer.add(returnToGameButton).width(buttonWidth).height(buttonHeight).padBottom(20);
        menuContainer.row();
        menuContainer.add(returnToMenuButton).width(buttonWidth).height(buttonHeight).padBottom(20);
        menuContainer.row();
        menuContainer.add(exitGameButton).width(buttonWidth).height(buttonHeight).padBottom(80);

        // Start ducking music immediately on pause
        currentVolume = FULL_VOLUME;
        audio.duckMusic(currentVolume);
    }

    /** Begin fading volume back up, then switch to the given scene. */
    private void startRestore(SceneManager.State target) {
        restoring    = true;
        pendingScene = target;
    }

    @Override
    public void update(float delta) {
        stage.act(delta);

        if (restoring) {
            // Fade volume back up
            currentVolume = Math.min(FULL_VOLUME, currentVolume + RESTORE_SPEED * delta);
            audio.duckMusic(currentVolume);
            if (currentVolume >= FULL_VOLUME) {
                // Volume fully restored — switch scene now
                sceneManager.setScene(pendingScene);
            }
        } else {
            // Fade volume down toward duck target
            currentVolume = Math.max(DUCK_VOLUME, currentVolume - DUCK_SPEED * delta);
            audio.duckMusic(currentVolume);
        }

        // Escape key — restore then go back to game
        if (io.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            startRestore(SceneManager.State.GAME);
        }
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch sb) {
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
        if (stage != null) stage.dispose();
        this.skin = null;
    }
}
