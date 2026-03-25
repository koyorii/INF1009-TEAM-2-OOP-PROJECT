package io.github.some_example_name.lwjgl3;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class FloatingText {

    private String text;
    private float x, y;
    private float alpha;       
    private float lifetime;      
    private float elapsed;
    private Color color;


    private static final float RISE_SPEED = 55f;

    public FloatingText(String text, float x, float y, float lifetime, Color color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.lifetime = lifetime;
        this.elapsed = 0f;
        this.alpha = 1f;
        this.color = color.cpy();
    }

   
    public boolean update(float delta) {
        elapsed += delta;
        y += RISE_SPEED * delta;

     
        float fadeStart = lifetime * 0.6f;
        if (elapsed > fadeStart) {
            alpha = 1f - ((elapsed - fadeStart) / (lifetime - fadeStart));
        }

        return elapsed < lifetime;
    }

  
    public void draw(SpriteBatch batch, BitmapFont font) {
        color.a = Math.max(0f, alpha);
        font.setColor(color);
        font.draw(batch, text, x, y);
    }

    public boolean isExpired() {
        return elapsed >= lifetime;
    }
} 
