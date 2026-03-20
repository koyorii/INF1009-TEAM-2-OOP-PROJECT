package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

import io.github.some_example_name.lwjgl3.Engine.entityManager.MovableEntity;
import io.github.some_example_name.lwjgl3.Engine.entityManager.iDrawableSprite;

public class playerNPC extends MovableEntity implements iDrawableSprite {

    private Texture tex;
    private boolean isFalling;
    private Polygon rectPolygon;

    // Draw size — defaults to actual texture size, but can be overridden
    // to scale the sprite down without changing the source image
    private float drawWidth;
    private float drawHeight;

    public playerNPC() {
        super();
    }

    public playerNPC(String path, float x, float y, float speed, boolean isFalling) {
        super(x, y, speed, null);
        this.tex       = new Texture(Gdx.files.internal(path));
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
    public void dispose() {
        if (tex != null) {
            tex.dispose();
            tex = null;
        }
    }
}
