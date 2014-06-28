package com.me.GameWorld;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


// Called it Coke because there already exists a class called Sprite
public class Coke {
	private TextureRegion region;
	private float x;
	private float y;
	private float width;
	private float height;
	private float rotation;
	
	public Coke(TextureRegion region, float x, float y, float width, float height) {
		this.region = region;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = 0;
	}
	
	public Coke(TextureRegion region, float originX, float originY, float destX, float destY, float width, float height) {
		this.region = region;
		this.width = width;
		this.height = height;
		float midX = originX + (destX - originX);
		float midY = originY + (destY - originY);
		this.x = midX + width / 2;
		this.y = midY + height / 2;
		if(destX > originX && destY == originY) {
			rotation = 90;
		}
		else if(destX < originX && destY == originY) {
			rotation = 270;
		} 
		else if(destY > originY && destX == originX) {
			rotation = 0;
		} 
		else if(destY < originY && destX == originX) {
			rotation = 180;
		} else {
			rotation = (float) 0;
		}
	}
	
	public void draw(SpriteBatch batcher) {
		batcher.draw(region, x, y, x + width / 2, y + height / 2, width, height, 1, 1, rotation / 360);
	}
	
	public void translate(float dX, float dY) {
		x += dX;
		y += dY;
	}

	public TextureRegion getRegion() {
		return region;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
	public float getSize() {
		return height;
	}
	
	public float getOriginX() {
		float originX = x + width / 2;
		return originX;
	}
	
	public float getOriginY() {
		float originY = y + height / 2;
		return originY;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public void changeSize(float dS) {
		this.height += dS;
		this.width += dS;
	}
}
