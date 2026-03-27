package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import io.github.some_example_name.lwjgl3.Engine.entityManager.MovableEntity;
import io.github.some_example_name.lwjgl3.Engine.entityManager.iDrawableSprite;

/**
 * The player-controlled NPC character.
 * Movement is handled externally by PlayerController (A/D keys),
 * so this class does NOT implement iMovable — there is no engine-driven movement here.
 */
public class Player extends MovableEntity implements iDrawableSprite {

    private Texture tex;
    private final boolean isFalling;
    private Polygon rectPolygon;

    // Draw size — defaults to actual texture size, overridden via setDrawSize()
    private float drawWidth;
    private float drawHeight;

    public Player(Texture tex, float x, float y, float speed, boolean isFalling) {
        super(x, y, speed, null);
        this.tex = tex;
        this.isFalling = isFalling;

        this.drawWidth  = tex.getWidth();
        this.drawHeight = tex.getHeight();

        buildPolygon();
    }

    /** Scales the sprite and its collision box. Call right after construction. */
    public void setDrawSize(float width, float height) {
        this.drawWidth  = width;
        this.drawHeight = height;
        buildPolygon();
    }

    private void buildPolygon() {
        float[] vertices = { 0, 0, drawWidth, 0, drawWidth, drawHeight, 0, drawHeight };
        this.rectPolygon = new Polygon(vertices);
    }

    public Texture getTexture()       { return tex; }
    public void setTexture(Texture t) { this.tex = t; }
    public boolean getIsFalling()     { return isFalling; }
    public float getDrawWidth()       { return drawWidth; }

    @Override
    public void update() {
        // Movement is driven by PlayerController — nothing to do here
    }

    @Override
    public Polygon getBounds() {
        rectPolygon.setPosition(x, y);
        return rectPolygon;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(tex, x, y, drawWidth, drawHeight);
    }

    @Override
    public void dispose() {
        if (tex != null) {
            tex.dispose();
        }
    }
}
