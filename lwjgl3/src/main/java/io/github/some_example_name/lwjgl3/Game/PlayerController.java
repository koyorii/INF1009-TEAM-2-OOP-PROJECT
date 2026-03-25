// package io.github.some_example_name.lwjgl3.Game;

// import com.badlogic.gdx.Gdx;
// import com.badlogic.gdx.Input;
// import com.badlogic.gdx.math.MathUtils;

// public class PlayerController {

//     private float speed;

//     public PlayerController(float speed) {
//         this.speed = speed;
//     }

//     public void handleInput(Player player) {
//         float delta = Gdx.graphics.getDeltaTime();
//         float newX  = player.getX();

//         // Support both A/D and arrow keys for horizontal movement
//         if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
//             newX -= speed * delta;
//         if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//             newX += speed * delta;

//         // Clamp using drawWidth so player never goes off either edge
//         float maxX = Gdx.graphics.getWidth() - player.getDrawWidth();
//         player.setX(MathUtils.clamp(newX, 0, maxX));
//     }

//     public void setSpeed(float speed) { this.speed = speed; }
//     public float getSpeed()           { return speed; }
// }
