package io.github.some_example_name.lwjgl3.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Draws the health / armor HUD overlay.
 *
 * Both modes use the same icon rows:
 *   heart.png   — active HP slot
 *   heart-bg.png — lost HP slot
 *   armor.png   — active armor slot
 *   armor-bg.png — lost/empty armor slot
 *
 * Normal mode (max 5/5) also shows score + countdown timer top-right.
 * Fearless Hunger (max 3/3) shows score only top-right.
 */
public class HudOverlay {

    private static final float ICON_SIZE  = 32f;
    private static final float ICON_GAP   = 4f;
    private static final float MARGIN_X   = 12f;
    private static final float MARGIN_TOP = 12f;
    private static final float ROW_GAP    = 6f;

    private final Texture heartFull;
    private final Texture heartEmpty;
    private final Texture armorFull;
    private final Texture armorEmpty;
    private final Texture foodFull;
    private final Texture foodEmpty;

    private final PlayerStats ps;
    private final BitmapFont  font;
    private final GlyphLayout layout;

    public HudOverlay(AssetManager am, PlayerStats ps) {
        this.heartFull  = am.get("hud/heart.png",    Texture.class);
        this.heartEmpty = am.get("hud/heart-bg.png",  Texture.class);
        this.armorFull  = am.get("hud/armor.png",    Texture.class);
        this.armorEmpty = am.get("hud/armor-bg.png",  Texture.class);
        this.foodFull   = am.get("hud/meat.png",     Texture.class);
        this.foodEmpty  = am.get("hud/meat-bg.png",  Texture.class);
        this.ps     = ps;
        this.font   = new BitmapFont();
        this.font.getData().setScale(1.5f);
        this.layout = new GlyphLayout();
    }

    /**
     * @param batch    SpriteBatch — handles begin/end internally
     * @param timeLeft seconds remaining (Normal mode); ignored for Fearless Hunger
     */
    public void render(SpriteBatch batch, float timeLeft) {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float healthY = screenH - MARGIN_TOP - ICON_SIZE;
        float foodY   = healthY - ROW_GAP - ICON_SIZE;
        float armorY  = foodY   - ROW_GAP - ICON_SIZE;

        batch.begin();

        // ── Health row ────────────────────────────────────────────
        int maxHp     = ps.getMaxHp();
        int currentHp = ps.getHp();
        for (int i = 0; i < maxHp; i++) {
            Texture icon = (i < currentHp) ? heartFull : heartEmpty;
            batch.draw(icon, MARGIN_X + i * (ICON_SIZE + ICON_GAP), healthY, ICON_SIZE, ICON_SIZE);
        }

        // ── Armor row ─────────────────────────────────────────────
        int maxArmor     = ps.getMaxArmor();
        int currentArmor = ps.getArmor();

        // ── Hunger row (Normal mode only) ─────────────────────────
        if (ps.getMode() == PlayerStats.GameMode.NORMAL) {
            int maxHunger     = ps.getMaxHunger();
            int currentHunger = ps.getHunger();
            for (int i = 0; i < maxHunger; i++) {
                Texture icon = (i < currentHunger) ? foodFull : foodEmpty;
                batch.draw(icon, MARGIN_X + i * (ICON_SIZE + ICON_GAP), foodY, ICON_SIZE, ICON_SIZE);
            }
        }

        for (int i = 0; i < maxArmor; i++) {
            Texture icon = (i < currentArmor) ? armorFull : armorEmpty;
            batch.draw(icon, MARGIN_X + i * (ICON_SIZE + ICON_GAP), armorY, ICON_SIZE, ICON_SIZE);
        }

        // ── Score (both modes, just below Pause label top-right) ─────
        String scoreText = "Score: " + ps.getScore();
        layout.setText(font, scoreText);
        font.setColor(Color.WHITE);
        float scoreY = screenH - MARGIN_TOP - 32f; // leaves room for "Press Escape" label above
        font.draw(batch, scoreText, screenW - layout.width - MARGIN_X, scoreY);

        // ── Countdown timer (Normal mode only, below score) ───────
        if (ps.getMode() == PlayerStats.GameMode.NORMAL) {
            int secs = Math.max(0, (int) Math.ceil(timeLeft));
            String timerText = String.format("Time: %d:%02d", secs / 60, secs % 60);
            layout.setText(font, timerText);
            font.setColor(secs <= 30 ? Color.RED : Color.WHITE);
            font.draw(batch, timerText,
                    screenW - layout.width - MARGIN_X,
                    scoreY - font.getLineHeight() - 8f);
            font.setColor(Color.WHITE);
        }

        batch.end();
    }

    public void dispose() {
        font.dispose();
    }
}
