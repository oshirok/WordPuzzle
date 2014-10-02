package com.me.GameWorld; 

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


// Called it Coke because there already exists a class called Sprite
public class Coke {
	private TextureRegion region;
	private float x;
	private float y;
	private float width;
	private float height;
	private float rotation;
	private float alpha;
	
	
	// Construtor for Coke (Sprite) object
	public Coke(TextureRegion region, float x, float y, float width, float height) {
		this.region = region;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = 0;
		this.alpha = 1;
	}
	
	// Constructor for Coke (Sprite) object for hint arrows
	public Coke(TextureRegion region, float originX, float originY, float destX, float destY, float width, float height) {
		this.region = region;
		this.width = width;
		this.height = height;
		float midX = (originX + destX) / 2;
		float midY = (originY + destY) / 2;
		Vector2 v2 = new Vector2(originX, destX);
		this.x = midX - width / 2;
		this.y = midY - height / 2;
		rotation = (float) Math.atan((destY - originY) / (destX - originX));
		if(destX < originX) rotation = rotation + (float) Math.PI;
		this.alpha = 1;
	}
	
	// draws the sprite
	public void draw(SpriteBatch batcher) {
		batcher.setColor(1, 1, 1, alpha);
		batcher.draw(region, x, y, width / 2, height / 2, width, height, 1, 1, (float) (rotation / (2*Math.PI) * 360));
		batcher.setColor(1, 1, 1, 1);
	}
	
	// translates the sprite
	public void translate(float dX, float dY) {
		x += dX;
		y += dY;
	}

	// returns the Sprite textureRegion
	public TextureRegion getRegion() {
		return region;
	}

	// Returns the x position of the sprite
	public float getX() {
		return x;
	}

	// returns the y position of the sprite
	public float getY() {
		return y;
	}
	
	// returns the width of the sprite
	public float getWidth() {
		return width;
	}

	// returns the height of the sprite
	public float getHeight() {
		return height;
	}
	
	// returns the size of the sprite
	public float getSize() {
		return height;
	}
	
	// returns the origin x-position of the sprite
	public float getOriginX() {
		float originX = x + width / 2;
		return originX;
	}
	
	// returns the origin y-position of the sprite
	public float getOriginY() {
		float originY = y + height / 2;
		return originY;
	}

	// returns the region of the sprite
	public void setRegion(TextureRegion region) {
		this.region = region;
	}

	// sets the x-position of the sprite
	public void setX(float x) {
		this.x = x;
	}

	// sets the y-position of the sprite
	public void setY(float y) {
		this.y = y;
	}

	// sets the width of the sprite
	public void setWidth(float width) {
		this.width = width;
	}

	// sets the height of the sprite
	public void setHeight(float height) {
		this.height = height;
	}
	
	// changes the size of the sprite by delta
	public void changeSize(float dS) {
		this.height += dS;
		this.width += dS;
	}
	
	public void setAlpha(float dS) {
		this.alpha += dS;
		if(this.alpha >= 1) this.alpha = 1;
		if(this.alpha <= 0) this.alpha = 0;
	}
	
	public float getAlpha(float dS) {
		return this.alpha;
	}
}
