package com.me.wordpuzzle;

import java.io.FileNotFoundException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.Game;
import com.me.Helpers.AssetLoader;
import com.me.Screens.GameScreen;

public class WordPuzzle extends Game {

	@Override
    public void create() {
        System.out.println("ZBGame Created!");
        AssetLoader.load();
        try {
			setScreen(new GameScreen());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }

}