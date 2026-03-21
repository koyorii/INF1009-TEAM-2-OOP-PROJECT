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

public class SceneLeaderboard extends Scene {
    private final Stage stage;
    private Skin skin;

    public SceneLeaderboard(ISceneManager ism, PlayerStats ps) {
        super(ism);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = am.get("skin/craftacular-ui.json", Skin.class);

        Table menuContainer = new Table();
        menuContainer.setFillParent(true);
        stage.addActor(menuContainer);

        // Dirt background
        menuContainer.setFillParent(true);
        menuContainer.setBackground(skin.getDrawable("dirt"));

        // Leaderboard test at the top
        Label leaderboardText = new Label("Leaderboard", skin, "title");
        leaderboardText.setFontScale(0.6f);

        // Container fot the list items
        Table scrollTable = new Table();

        // Populate the list with top x user data
        for (String entry : ps.getLeaderboardList()) {
            Label label = new Label(entry, skin);
            scrollTable.add(label).pad(10).left();
            scrollTable.row(); // Move to next line
        }

        // Put scroll table inside new scrollable with design
        ScrollPane leaderboardScrollPane = new ScrollPane(scrollTable, skin);
        leaderboardScrollPane.setFadeScrollBars(false); // Keeps bars visible
        ScrollPane.ScrollPaneStyle leaderboardStyle = new ScrollPane.ScrollPaneStyle(leaderboardScrollPane.getStyle());
        leaderboardStyle.vScroll = null;
        leaderboardScrollPane.setStyle(leaderboardStyle);

        // Add to UI
        //Leaderboard Text
        menuContainer.add(leaderboardText).padTop(20);
        menuContainer.row();

        menuContainer.add(leaderboardScrollPane).expand().fill().pad(20);
        menuContainer.row();

        // Back button
        TextButton backBtn = new TextButton("Back to Menu", skin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audio.playSound("button_click");
                ism.setScene(SceneManager.State.MENU);
            }
        });
        menuContainer.add(backBtn).padBottom(20).width(300);

        // Keep menu music playing
        audio.playMusic("menu");
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
        stage.dispose();
    }
}
