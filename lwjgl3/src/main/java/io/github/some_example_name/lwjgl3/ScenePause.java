package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ScenePause extends Scene{
    private Stage stage; // Handles UI drawing and input events
    private Skin skin; // Container for styling through a separate JSON file (In Assets)

    public ScenePause(GameMaster gm) {
        super(gm);
        // Initialise the stage to manage UI elements
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Applies design to the paused text
        skin = new Skin(Gdx.files.internal("skin/arcade-ui.json"));
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        Label pauseLabel = new Label("PAUSED", skin, "title");

        // Positioning
        menuContainer.add(pauseLabel).padBottom(50);
        menuContainer.row();
    }

    @Override
    public void update(float delta) {
        stage.act(delta); // Enables time based actions

        // Pause the game when the player hits escape
        if (gm.IoM.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            gm.getSceneManager().setScene(SceneManager.State.GAME);
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
        if (skin != null) {
            skin.dispose();
        }
    }
}
