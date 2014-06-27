package com.me.GameWorld;

import java.io.FileNotFoundException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.GameObjects.Bird;
import com.me.GameObjects.LetterBoard;

public class GameWorld {

    private LetterBoard board;

    public GameWorld(int midPointY) throws FileNotFoundException {
        board = new LetterBoard(0, 0, 720, 720);
    }

    public void update(float delta) {
    }
    
    public LetterBoard getBoard() {
    	return board;
    }
    
    public void render(SpriteBatch batcher) {
    	board.render(batcher);
    }
}
