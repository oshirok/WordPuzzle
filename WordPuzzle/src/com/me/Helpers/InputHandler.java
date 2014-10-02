package com.me.Helpers;

import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.me.GameWorld.GameWorld;

public class InputHandler implements InputProcessor {
    private GameWorld myWorld;
    private float scaleFactorX, scaleFactorY;
    // Ask for a reference to the Bird when InputHandler is created.
    public InputHandler(GameWorld world, float scaleFactorX,
			float scaleFactorY) {
        // myBird now represents the gameWorld's bird.
        myWorld = world;
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	if(myWorld.currentState == GameWorld.GameState.MENU) {
    		myWorld.switchInput();
			//myWorld.start();
    	} else if(myWorld.currentState == GameWorld.GameState.GAMEOVER) {
    	} else if(myWorld.currentState == GameWorld.GameState.HELP) {
    		myWorld.currentState = GameWorld.GameState.MENU;
    		myWorld.switchInput();
    	}
        return true; // Return true to say we handled the touch.
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	screenX = scaleX(screenX);
    	screenY = scaleY(screenY);
    	// if touchUp in board area
    	if(myWorld.currentState == GameWorld.GameState.RUNNING) myWorld.getBoard().onTouchUp();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
    	screenX = scaleX(screenX);
    	screenY = scaleY(screenY);
    	if(screenY > 320 && screenY < 1280 && myWorld.currentState == GameWorld.GameState.RUNNING) myWorld.getBoard().onDrag(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY);
	}

}