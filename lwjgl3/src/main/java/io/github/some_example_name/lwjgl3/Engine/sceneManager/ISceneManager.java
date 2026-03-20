package io.github.some_example_name.lwjgl3.Engine.sceneManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.assets.AssetManager;
import io.github.some_example_name.lwjgl3.Engine.collisionManager.CollisionManager;
import io.github.some_example_name.lwjgl3.Engine.entityManager.EntityManager;
import io.github.some_example_name.lwjgl3.Engine.iomanager.IOManager;
import io.github.some_example_name.lwjgl3.Engine.movementManager.MovementManager;
import io.github.some_example_name.lwjgl3.Game.PlayerStats;


// defines the contract for any SceneManager
public interface ISceneManager {
    void setScene(SceneManager.State state);
    void update(float delta);
    void render(ShapeRenderer shape, SpriteBatch batch);
    void setEngineTools(EntityManager em, CollisionManager cm, MovementManager mm, IOManager io, PlayerStats ps, AssetManager am);
    AssetManager getAssets(); // All scenes will call this to borrow textures
    void dispose();
}
