package com.me.GameWorld;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.GameObjects.Letter;
import com.me.GameObjects.LetterBoard;
import com.me.GameWorld.GameWorld.GameState;
import com.me.Helpers.AssetLoader;

public class GameRenderer {

    private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    private SpriteBatch batcher;

    private int midPointY;
    private int gameHeight;

    public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
        myWorld = world;

        // The word "this" refers to this instance.
        // We are setting the instance variables' values to be that of the
        // parameters passed in from GameScreen.
        this.gameHeight = gameHeight;
        this.midPointY = midPointY;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 720, 1280);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public void render(float runTime) {

        // We will move these outside of the loop for performance later.
        // Bird bird = myWorld.getBird();

        // Fill the entire screen with black, to prevent potential flickering.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // Begin ShapeRenderer
        // shapeRenderer.begin(ShapeType.Filled);
        
        /*// Draw Background color
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, 136, midPointY + 66);

        // Draw Grass
        shapeRenderer.setColor(111 / 255.0f, 186 / 255.0f, 45 / 255.0f, 1);
        shapeRenderer.rect(0, midPointY + 66, 136, 11);

        // Draw Dirt
        shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1);
        shapeRenderer.rect(0, midPointY + 77, 136, 52);
        
        // End ShapeRenderer
        shapeRenderer.end();

        // Begin SpriteBatch
        batcher.begin();
        myWorld.render(batcher);
        // Disable transparency 
        // This is good for performance when drawing images that do not require
        // transparency.
        batcher.disableBlending();
        batcher.draw(AssetLoader.bg, 0, midPointY + 23, 136, 43);

        // The bird needs transparency, so we enable that again.
        batcher.enableBlending();
        
        // Draw bird at its coordinates. Retrieve the Animation object from AssetLoader
        // Pass in the runTime variable to get the current frame.
        batcher.draw(AssetLoader.birdAnimation.getKeyFrame(runTime),
                bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight());
        
        // End SpriteBatch*/
        if(myWorld.currentState == GameWorld.GameState.RUNNING)
        renderBoard(runTime);
        switch(myWorld.currentState) {
        	case RUNNING:
        		renderBoard(runTime);
        		break;
        	case MENU:
        		renderMenu(runTime);
        		break;
        	case GAMEOVER:
        		renderGameOver(runTime);
        		break;
        	case CAT:
        		renderStopCat(runTime);
        		break;
        	case HELP:
        		renderHelp(runTime);
        	default:
        		break;
        }
        myWorld.renderWorld(batcher);
    }
    
    private void renderHelp(float runTime) {
		batcher.begin();
		batcher.draw(AssetLoader.help, 0, 0, 720, 1280);
		batcher.end();
	}

	public OrthographicCamera getCam() {
    	return cam;
    }

    public void renderBoard(float runTime) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(55 / 255.0f, 55 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, 720, 280);
        shapeRenderer.end();
        batcher.begin();
        myWorld.getBoard().renderWordList(batcher);
        myWorld.getBoard().render(batcher);
        AssetLoader.font.setScale(2.5f, -2.5f);
    	AssetLoader.font.setColor(1f, 1f, 1f, 1f);
        batcher.end();
    }
    
    private void renderMenu(float runTime) {
    	batcher.begin();
    	for(ArrayList<Coke> a: myWorld.getBGBoard()){
    		for(Coke c: a) {
    			c.draw(batcher);
    		}
    	}
        batcher.end();
    	Gdx.gl.glEnable(GL10.GL_BLEND);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 0.8f);
        shapeRenderer.rect(0, 0, 720, 1280);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);
    }
    
    private void renderGameOver(float runTime) {
    	renderBoard(runTime);
    	Gdx.gl.glEnable(GL10.GL_BLEND); //NEED TO ENABLE FOR TRANSPARENCY
    	shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.75f);
        shapeRenderer.rect(0, 0, 720, 1280);
        shapeRenderer.end();
    	batcher.begin();
    	Gdx.gl.glDisable(GL10.GL_BLEND);
    	/*AssetLoader.font.setScale(1f, -1f);
    	AssetLoader.font.setColor(1f, 1f, 1f, 1f);
    	// AssetLoader.font.draw(batcher, "GAMEOVER", 340 - 4 * 10 * 8, 40);
    	AssetLoader.font.draw(batcher, "Category: ", 320 - 4 * 5.0f * 10, 120);
    	AssetLoader.font.draw(batcher, myWorld.getBoard().category, 320 - 4 * 5.0f * 10, 190);
    	AssetLoader.font.draw(batcher, "FINAL SCORE: " + myWorld.getBoard().getScore(), 320 - 4 * 5.0f * 10, 340);
    	AssetLoader.font.setScale(1f, -1f);
    	AssetLoader.font.draw(batcher, "Highscore: " + AssetLoader.getHighScore(myWorld.getBoard().category), 320 - 4 * 5.0f * 10, 450);
    	AssetLoader.font.setColor(1f, 1f, 1f, (float) Math.abs(Math.sin(runTime)));
        AssetLoader.font.draw(batcher, "Play Again?", 360 - 4 * 10 * 5.0f, 920);*/
        batcher.end();
    }
    
    private void renderStopCat(float runTime) {
    	renderBoard(runTime);
    	batcher.begin();
    	batcher.draw(AssetLoader.stopCat, 0, 320, 720, 720);
    	batcher.end();
    }
}
