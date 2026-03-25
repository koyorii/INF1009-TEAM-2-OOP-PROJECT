package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import io.github.some_example_name.lwjgl3.Engine.entityManager.MovableEntity;
import io.github.some_example_name.lwjgl3.Engine.entityManager.iDrawableSprite;
import io.github.some_example_name.lwjgl3.Engine.iomanager.Keyboard;
import io.github.some_example_name.lwjgl3.Engine.movementManager.AImovement;
import io.github.some_example_name.lwjgl3.Engine.movementManager.UserMovement;
import io.github.some_example_name.lwjgl3.Engine.movementManager.iMovable;

public class Player extends MovableEntity implements iDrawableSprite,iMovable {

    private Texture tex;
    private boolean isFalling;
    private Polygon rectPolygon;

    // Draw size — defaults to actual texture size, but can be overridden
    // to scale the sprite down without changing the source image
    private float drawWidth;
    private float drawHeight;

    public Player() {
        super();
    }

    public Player(Texture tex, float x, float y, float speed, boolean isFalling) {
        super(x, y, speed, null);
        this.tex = tex;
        this.isFalling = isFalling;

        // Default draw size = actual texture size
        this.drawWidth  = tex.getWidth();
        this.drawHeight = tex.getHeight();

        buildPolygon();
    }

    // ── Call this right after construction to shrink the sprite ───
    // e.g. playerNPC.setDrawSize(64, 96);
    public void setDrawSize(float width, float height) {
        this.drawWidth  = width;
        this.drawHeight = height;
        buildPolygon(); // rebuild collision box to match new size
    }

    private void buildPolygon() {
        float[] vertices = new float[]{ 0,0, drawWidth,0, drawWidth,drawHeight, 0,drawHeight };
        this.rectPolygon = new Polygon(vertices);
    }

    // ── Getters ───────────────────────────────────────────────────
    public Texture getTexture()       { return tex;        }
    public void setTexture(Texture t) { this.tex = t;      }
    public boolean getIsFalling()     { return isFalling;  }
    public float getDrawWidth()       { return drawWidth;  }
    public float getDrawHeight()      { return drawHeight; }

    @Override
    public void update() {
        // intentionally empty — debug print removed
    }

    @Override
    public Polygon getBounds() {
        rectPolygon.setPosition(x, y);
        return rectPolygon;
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draw at drawWidth x drawHeight instead of raw texture size
        batch.draw(tex, x, y, drawWidth, drawHeight);
    }

    @Override
    public void performMovement(float speed, boolean isAI, Keyboard kb, UserMovement userMove, AImovement aiMove){
        float minX = 0;
        float maxX = Gdx.graphics.getWidth() - this.drawWidth;
        userMove.moveHorizontal(this, this.getSpeed(), kb, com.badlogic.gdx.Input.Keys.A, com.badlogic.gdx.Input.Keys.D, minX, maxX);
    }

    @Override
    public void dispose() {
        if (tex != null) {
            tex.dispose();
        }
    }
}
