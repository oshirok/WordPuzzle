package com.me.GameObjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.Helpers.AssetLoader;

// The handler for the grid of letters. I think this should've been the GameWorld class.
public class LetterBoard {
	public Letter[][] board = new Letter[8][8];
	private Random randy = new Random();
	private ArrayList<String> dictionary = new ArrayList<String>();
	private ArrayList<String> compatibleDictionary = new ArrayList<String>();
	private Scanner reader;
	private ArrayList<Letter> selectedLetters = new ArrayList<Letter>();
	private ArrayList<Letter[]> currentWords = new ArrayList<Letter[]>();
	private ArrayList<String> completedWords = new ArrayList<String>();
	private final int OFFSET = 1280 - 720;
	
	public LetterBoard(int x, int y, int height, int width) throws FileNotFoundException {
		FileHandle file = Gdx.files.internal("data/dictionary3.txt");
		reader = new Scanner(file.read());
		while(reader.hasNext()) {
			dictionary.add(reader.nextLine().trim());
		}
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j] = new Letter(randy.nextInt(26), i * 90, j * 90 + OFFSET, false);
			}
		}
		String word;
		for(int i = 0; i < 8; i++) {
			word = chooseRandomWord();
			System.out.println("" + addWord(board, word));
		}
		updateTracker();
	}
	
	private String chooseRandomWord() {
		for(String s:dictionary) {
			compatibleDictionary.add(s);
		}
		int r = randy.nextInt(compatibleDictionary.size());
		return compatibleDictionary.get(r);
	}
	
	public void render(SpriteBatch batcher) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j].render(batcher);
			}
		}
	}
	
	// resets all unused letters
	public void sweep() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j].isEmpty()) place((char) randy.nextInt(26), i, j, false);;
			}
		}
	}
	
	// add a word to the board
	public boolean addWord(Letter[][] layer, String s) {
		int direction = randy.nextInt(4);
		boolean found = false;
		int x1 = 0;
		int y1 = 0;
		int x = 0;
		int y = 0;
		while(!found) {
			x1 = randy.nextInt(8);
			y1 = randy.nextInt(8);
			if(layer[x1][y1].isEmpty()) {
				x = x1;
				y = y1;
				found = true;
			}
		}
		/*for(int i = 0; i < 8; i++) {
			board[i][0] = new Letter(s.toLowerCase().charAt(i) - 'a' , i * 90, 0 * 90, true);
		}*/
		Letter[] tracker = new Letter[s.length()];
		boolean result = addWord(layer, x, y, s.trim(), direction, tracker);
		if(result) {
			currentWords.add(tracker);
		}
		return result;
	}
	
	private boolean addWord(Letter[][] layer, int x, int y, String s, int direction, Letter[] tracker) {
		if(s.isEmpty())
			return true;
		place((char) (s.toLowerCase().charAt(0) - 'a'), x, y, true);
		tracker[tracker.length - s.length()] = board[x][y];
		switch(direction % 4) {
		case 0: //NORTH
			if((y - 1 > -1) && layer[x][y - 1].isEmpty() && board[x][y - 1].isEmpty()) { //NORTH
				if(addWord(layer, x, y - 1, s.substring(1), 0, tracker)) return true;
			} else if ((x + 1 < 8) && layer[x + 1][y].isEmpty() && board[x + 1][y].isEmpty()) { //EAST
				if(addWord(layer, x + 1, y, s.substring(1), 1, tracker)) return true;
			} else if((x - 1 > -1) && layer[x - 1][y].isEmpty() && board[x - 1][y].isEmpty()) { //WEST
				if(addWord(layer, x - 1, y, s.substring(1), 3, tracker)) return true;
			} else {
				place((char) randy.nextInt(26), x, y, false);
				return false;
			}

		case 1: //EAST
			if ((x + 1 < 8) && layer[x + 1][y].isEmpty() && board[x + 1][y].isEmpty()) { //EAST
				if(addWord(layer, x + 1, y, s.substring(1), 1, tracker)) return true;
			} else if((y - 1 > -1) && layer[x][y - 1].isEmpty() && board[x][y - 1].isEmpty()) { //NORTH
				if(addWord(layer, x, y - 1, s.substring(1), 0, tracker)) return true;
			} else if((y + 1 < 8) && layer[x][y + 1].isEmpty() && board[x][y + 1].isEmpty()) { //SOUTH
				if(addWord(layer, x, y + 1, s.substring(1), 2, tracker)) return true;
			} else {
				place((char) randy.nextInt(26), x, y, false);
				return false;
			}
		case 2: //SOUTH
			if((y + 1 < 8) && layer[x][y + 1].isEmpty() && board[x][y + 1].isEmpty()) { //SOUTH
				if(addWord(layer, x, y + 1, s.substring(1), 2, tracker)) return true;
			} else if((x - 1 > -1) && layer[x - 1][y].isEmpty() && board[x - 1][y].isEmpty()) { //WEST
				if(addWord(layer, x - 1, y, s.substring(1), 3, tracker)) return true;
			} else if ((x + 1 < 8) && layer[x + 1][y].isEmpty() && board[x + 1][y].isEmpty()) { //EAST
				if(addWord(layer, x + 1, y, s.substring(1), 1, tracker)) return true;
			} else {
				place((char) randy.nextInt(26), x, y, false);
				return false;
			}
		case 3: //WEST
			if((x - 1 > -1) && layer[x - 1][y].isEmpty() && board[x - 1][y].isEmpty()) { //WEST
				if(addWord(layer, x - 1, y, s.substring(1), 3, tracker)) return true;
			} else if((y - 1 > -1) && layer[x][y - 1].isEmpty() && board[x][y - 1].isEmpty()) { //NORTH
				if(addWord(layer, x, y - 1, s.substring(1), 0, tracker)) return true;
			} else if((y + 1 < 8) && layer[x][y + 1].isEmpty() && board[x][y + 1].isEmpty()) { //SOUTH
				if(addWord(layer, x, y + 1, s.substring(1), 2, tracker)) return true;
			} else {
				place((char) randy.nextInt(26), x, y, false);
				return false;
			}
		}
		place((char) randy.nextInt(26), x, y, false);
		return false;
	}
	
	public void place(char c, int x, int y, boolean used) {
		board[x][y].setChar(c);
		board[x][y].setUsed(used);
	}
	
	public void onDrag(int screenX, int screenY) {
		Letter returnedLetter = board[screenX/90][(screenY - OFFSET)/90].selected();
		if(returnedLetter != null) selectedLetters.add(returnedLetter);
	}
	
	public void onTouchUp() {
		String tempString = "";
		for(int i = 0; i < selectedLetters.size(); i++) {
			tempString += selectedLetters.get(i).getLetter();
			selectedLetters.get(i).setSelected(false);
		}
		System.out.println(tempString);
		if(contains(tempString)) {
			System.out.println("FOUND!!!!");
			for(int i = 0; i < selectedLetters.size(); i++) {
				selectedLetters.get(i).setUsed(false);
			}
			completedWords.add(tempString);
			sweep();
			updateTracker();
			System.out.println("" + addWord(board, chooseRandomWord()));
			System.out.println("" + addWord(board, chooseRandomWord()));
		}
		
		// Print Tracker for debugging purposes
		for(Letter[] l:currentWords) {
			for(Letter le: l) {
				System.out.print(le.getLetter());
			}
			System.out.print(" | ");
		}
		System.out.println();
		// Clear the selectedLetters
		selectedLetters.clear();
	}
	
	private void updateTracker() {
		// Update Tracker
		for(int i = currentWords.size() - 1; i >= 0; i--) {
			currentWords.get(i)[0].setFirst(true);
			String tempWord = "";
			for(int j = 0; j < currentWords.get(i).length; j++) {
				tempWord += currentWords.get(i)[j].getLetter();
			}
			if(!contains(tempWord)) {
				for(int k = 0; k < currentWords.get(i).length; k++) {
					currentWords.get(i)[k].setUsed(false);
					currentWords.get(i)[k].setFirst(false);
				}
				currentWords.remove(i);
			}
		}
	}
	
	private boolean contains(String tempString) {
		for(String word:compatibleDictionary) {
			if(word.equalsIgnoreCase(tempString)) return true;
		}
		return false;
	}
	
	public void renderWordList(SpriteBatch batcher) {
		int i = 0;
		for(String word:completedWords) {
			for(int j = 0; j < word.length(); j++) {
				batcher.draw(AssetLoader.letters[word.charAt(j) - 'a'], j * 40, 240 + (completedWords.size() - 1) * 40 - i * 40, 40, 40);
			}
			i++;
		}
	}
}