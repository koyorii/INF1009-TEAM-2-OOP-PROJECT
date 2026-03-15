package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.some_example_name.lwjgl3.sceneManager.Scene;
import io.github.some_example_name.lwjgl3.sceneManager.SceneManager;

public class SceneGame extends Scene {

    private Stage stage;
    private Skin  skin;

    public SceneGame(GameMaster gm) {
        super(gm);

        // Background UI
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        menuContainer.setBackground(skin.getDrawable("dirt"));
        stage.addActor(menuContainer);

        // All entities (playerNPC + food) live in gm.EntityM
        // FoodSpawner adds OncomingFood every frame via gm.foodSpawner.update()
        // No separate EntityManager needed here
    }

    @Override
    public void update(float delta) {
        gm.EntityM.update(gm.MoveM);
        gm.collisionM.update();
        stage.act(delta);

        if (gm.IoM.getKeyboard().isKeyJustPressed(Input.Keys.ESCAPE)) {
            gm.getSceneManager().setScene(SceneManager.State.PAUSE);
        }

        if (gm.playerStats.isDead()) {
            Gdx.app.log("Game", "Player died! Score: " + gm.playerStats.getScore());
            // TODO: gm.getSceneManager().setScene(SceneManager.State.GAMEOVER);
        }
    }

    @Override
    public void render(ShapeRenderer shape, SpriteBatch batch) {
        // Draw background FIRST using stage (has its own internal batch)
        stage.draw();

        // Then draw all game entities on top
        // batch.begin() / end() is handled inside EntityM.draw()
        gm.EntityM.draw(shape, batch);
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin  != null) skin.dispose();
        // Do NOT dispose gm.EntityM here — GameMaster.dispose() owns it
    }
}