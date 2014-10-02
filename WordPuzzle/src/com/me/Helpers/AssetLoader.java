package com.me.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

    public static Texture textures2, stopCatTexture, helpScreen;

    public static Texture lettersTexture;
    public static TextureRegion[] letters = new TextureRegion[26];

    public static TextureRegion arrow, stopCat, help;

    public static BitmapFont font;
    private static Preferences prefs;
    public static void load() {
        
        textures2 = new Texture(Gdx.files.internal("data/textures2.png"));
        textures2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        stopCatTexture = new Texture(Gdx.files.internal("data/stop.png"));
        stopCatTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        helpScreen = new Texture(Gdx.files.internal("data/help.png"));
        helpScreen.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        arrow = new TextureRegion(textures2, 0, 0, 16, 16);
        arrow.flip(false, true);
        
        lettersTexture = new Texture(Gdx.files.internal("data/letters.png"));
        lettersTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        stopCat = new TextureRegion(stopCatTexture, 0, 0, 1024, 1024);
        stopCat.flip(false, true);
        
        help = new TextureRegion(helpScreen, 0, 0, 720, 1280);
        help.flip(false,  true);
        
        // Load in the letters yay!
        for(int i = 0; i < 4; i++) {
        	for(int j = 0; j < 8; j++) {
        		if(i * 8 + j < 26) {
        			letters[i * 8 + j] = new TextureRegion(lettersTexture, 16 * j, 16 * i, 16, 16);
        			letters[i * 8 + j].flip(false, true);
        		}
        	}
        }
        
        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.setScale(2.5f, -2.5f);
		
		// Create and retrieve settings from preferences file
		prefs = Gdx.app.getPreferences("WordPuzzle");

		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}

    }
    
    public static void setHighScore(int val, String dictionarySelect) {
		prefs.putInteger("highScore-" + dictionarySelect, val);
		prefs.flush();
	}

	public static int getHighScore(String dictionarySelect) {
		return prefs.getInteger("highScore-" + dictionarySelect);
	}
    
    public static void dispose() {
        // Must dispose textures
        textures2.dispose();
    }

}