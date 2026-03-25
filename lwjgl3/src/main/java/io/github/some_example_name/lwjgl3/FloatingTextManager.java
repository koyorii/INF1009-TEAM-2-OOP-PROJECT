//package io.github.some_example_name.lwjgl3;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import io.github.some_example_name.lwjgl3.Game.PlayerStats;
//import io.github.some_example_name.lwjgl3.entityManager.OnComingFood;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Random;
//
//public class FloatingTextManager {
//
//
//    private static final int LOW_HP_THRESHOLD = 3;
//
//
//    private static final float POPUP_LIFETIME = 1.6f;
//
//
//    private static final float QUOTE_DISPLAY_TIME = 4.0f;
//
//    // Quote
//    private static final String[] HEALTHY_QUOTES = {
//        "Great choice! Your body thanks you.",
//        "An apple a day keeps the doctor away!",
//        "Fuel up — you're on a roll!",
//        "Eating well never felt so good.",
//        "Green is the colour of winners!",
//        "Your HP is grateful. Keep it up!",
//        "Healthy food = healthy hero!",
//    };
//
//    private static final String[] UNHEALTHY_QUOTES = {
//        "Yikes! That one stung.",
//        "Junk food is the enemy. Stay sharp!",
//        "Your stomach regrets that choice.",
//        "One step closer to game over...",
//        "That junk food had consequences.",
//        "Dodge the junk next time!",
//        "Your doctor is watching. And disappointed.",
//    };
//
//    private static final String[] LOW_HP_QUOTES = {
//        "You look hungry... try some fruit!",
//        "HP critical — dodge the junk food!",
//        "Catch the carrot. PLEASE.",
//        "One more junk and it's game over!",
//        "Veggies exist for a reason, you know.",
//        "Apple won't kill you. Junk food might.",
//        "You are what you eat. Don't be a mushroom.",
//    };
//
//
//    private final List<FloatingText> activePopups = new ArrayList<>();
//    private final BitmapFont font;
//    private final BitmapFont quoteFont;
//    private final Random rng = new Random();
//
//    private String currentQuote = "Catch the healthy food to stay strong!";
//    private float quoteTimer = 0f;
//    private float quoteAlpha = 1f;
//    private boolean fadingOut = false;
//    private String pendingQuote = null;
//
//
//    private int lastHp = Integer.MAX_VALUE;
//    private boolean wasInDangerZone = false;
//
//    public FloatingTextManager(BitmapFont font) {
//        this.font = font;
//        this.quoteFont = new BitmapFont();
//        this.quoteFont.getData().setScale(1.2f);
//    }
//
//
//    public void spawnFoodPopup(OnComingFood.FoodType foodType, float playerX, float playerY) {
//        String popupLabel;
//        Color popupColor;
//        String quote;
//
//        switch (foodType) {
//            case HEALTHY:
//                popupLabel = "+1 HP  Healthy!";
//                popupColor = new Color(0.2f, 1f, 0.2f, 1f);
//                quote = HEALTHY_QUOTES[rng.nextInt(HEALTHY_QUOTES.length)];
//                break;
//            case VITAMIN:
//                popupLabel = "+1 Armor  Vitamin!";
//                popupColor = new Color(0.2f, 0.9f, 1f, 1f);
//                quote = HEALTHY_QUOTES[rng.nextInt(HEALTHY_QUOTES.length)];
//                break;
//            case UNHEALTHY:
//                popupLabel = "-HP  Junk food!";
//                popupColor = new Color(1f, 0.25f, 0.25f, 1f);
//                quote = UNHEALTHY_QUOTES[rng.nextInt(UNHEALTHY_QUOTES.length)];
//                break;
//            default:
//                popupLabel = "Caught!";
//                popupColor = Color.WHITE;
//                quote = "";
//        }
//
//
//        float spawnX = playerX - 60f;
//        float spawnY = playerY + 120f;
//        activePopups.add(new FloatingText(popupLabel, spawnX, spawnY, POPUP_LIFETIME, popupColor));
//
//
//        setQuote(quote);
//    }
//
//
//    public void update(float delta, PlayerStats playerStats) {
//        //Low-HP quote override
//        int currentHp = playerStats.getHp();
//        boolean nowInDangerZone = currentHp <= LOW_HP_THRESHOLD;
//        boolean justEnteredDanger = nowInDangerZone && !wasInDangerZone;
//        boolean hpChangedInDanger = nowInDangerZone && (currentHp != lastHp);
//
//        if (justEnteredDanger || hpChangedInDanger) {
//            setQuote(LOW_HP_QUOTES[rng.nextInt(LOW_HP_QUOTES.length)]);
//        }
//
//        lastHp = currentHp;
//        wasInDangerZone = nowInDangerZone;
//
//
//        if (fadingOut) {
//            quoteAlpha -= delta * 3f;  // fade out
//            if (quoteAlpha <= 0f) {
//                quoteAlpha = 0f;
//                if (pendingQuote != null) {
//                    currentQuote = pendingQuote;
//                    pendingQuote = null;
//                }
//                fadingOut = false;
//                quoteTimer = 0f;
//            }
//        } else {
//            quoteAlpha = Math.min(1f, quoteAlpha + delta * 3f);  // fade in
//            quoteTimer += delta;
//            if (quoteTimer >= QUOTE_DISPLAY_TIME && pendingQuote != null) {
//                fadingOut = true;
//            }
//        }
//
//        // ── Age rising popups ───────────────────────────────────
//        Iterator<FloatingText> it = activePopups.iterator();
//        while (it.hasNext()) {
//            FloatingText ft = it.next();
//            if (!ft.update(delta)) it.remove();
//        }
//    }
//
//
//    public void drawOnly(SpriteBatch batch, PlayerStats playerStats) {
//        float screenW = Gdx.graphics.getWidth();
//        float screenH = Gdx.graphics.getHeight();
//
//
//        int hp    = playerStats.getHp();
//        int armor = playerStats.getArmor();
//        int score = playerStats.getScore();
//        int maxHp    = playerStats.getMaxHp();
//        int maxArmor = playerStats.getMaxArmor();
//
//
//        if (hp <= LOW_HP_THRESHOLD) {
//            font.setColor(1f, 0.3f, 0.3f, 1f);   // red
//        } else {
//            font.setColor(0.3f, 1f, 0.3f, 1f);   // green
//        }
//        font.draw(batch, "HP: " + hp + " / " + maxHp, screenW * 0.04f, screenH * 0.97f);
//
//        font.setColor(0.4f, 0.8f, 1f, 1f);  // cyan
//        font.draw(batch, "Armor: " + armor + " / " + maxArmor, screenW * 0.04f, screenH * 0.92f);
//
//        font.setColor(1f, 0.9f, 0.2f, 1f);  // yellow
//        font.draw(batch, "Score: " + score, screenW * 0.04f, screenH * 0.87f);
//
//        if (wasInDangerZone) {
//            quoteFont.setColor(1f, 0.85f, 0.2f, quoteAlpha);
//        } else {
//            quoteFont.setColor(1f, 1f, 1f, quoteAlpha);        // white
//        }
//
//        quoteFont.draw(batch, currentQuote,
//            screenW * 0.5f - (currentQuote.length() * 4f),
//            screenH * 0.08f);
//
//
//        font.setColor(Color.WHITE);
//        for (FloatingText ft : activePopups) {
//            ft.draw(batch, font);
//        }
//
//        // Reset font colour
//        font.setColor(Color.WHITE);
//    }
//
//    public void dispose() {
//        font.dispose();
//        quoteFont.dispose();
//    }
//
//
//    private void setQuote(String quote) {
//        if (quote.equals(currentQuote)) return;
//        pendingQuote = quote;
//        fadingOut = true;
//    }
//}
