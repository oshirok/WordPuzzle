package com.me.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.Helpers.AssetLoader;

// Letter is each letter that appears on the screen. There are 64.
public class Letter {
	private TextureRegion texture = AssetLoader.bg;
	private int c;
	private int x;
	private int y;
	private boolean used;
	private boolean selected;
	private boolean first;
	
	// Constructor for letter
	public Letter(int c, int x, int y, boolean used) {
		texture = AssetLoader.letters[c];
		this.x = x;
		this.y = y;
		this.c = c;
		System.out.println(texture);
		this.used = used;
		this.first = false;
	}
	
	// When called by the GameRenderer, renders the letter
	public void render(SpriteBatch batcher) {
		if(selected) {
			batcher.setColor(200 / 255.0f, 200 / 255.0f, 255 / 255.0f, 1);
			batcher.draw(texture, x, y, 120, 120);
			batcher.setColor(1, 1, 1, 1);
		} else if(first) {
			batcher.setColor(200 / 255.0f, 200 / 255.0f, 200 / 255.0f, 1);
			batcher.draw(texture, x, y, 120, 120);
			batcher.setColor(1, 1, 1, 1);
		}
		else batcher.draw(texture, x, y, 120, 120);
		// remove comment for debugging
		// if(used) batcher.draw(AssetLoader.bird, x, y, 30, 30);
	}
	
	// Returns true if not used
	public boolean isEmpty() {
		return !used;
	}

	// Returns itself if the letter is selected
	public Letter selected() {
		if(!selected) {
			selected = true;
			Gdx.input.vibrate(5);
			return this;
		}
		return null;
	}
	
	// Returns the char the letter represents
	public char getLetter() {
		return (char) (c + 'a');
	}
	
	// Gets the coordinates. Used for testing.
	public String getCoordinates() {
		return "[" + x + "," + y + "]";
	}
	
	// Sets selected
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	// Sets used
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public void setChar(int c) {
		this.c = c;
		if(c > 25) c = 25;
		texture = AssetLoader.letters[c];
	}
	
	public void setFirst(boolean first) {
		this.first = first;
	}
	
	public TextureRegion getSprite() {
		return AssetLoader.letters[c];
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getOriginX() {
		return x + 120 / 2;
	}
	
	public int getOriginY() {
		return y + 120 / 2;
	}
}
