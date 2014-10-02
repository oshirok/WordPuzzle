package com.me.GameObjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.Timer;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.GameWorld.Coke;
import com.me.Helpers.AssetLoader;

// The handler for the grid of letters. I think this should've been the GameWorld class.
public class LetterBoard {
	public Letter[][] board = new Letter[6][8]; // The grid of letters
	private Random randy = new Random(); // Random object
	private ArrayList<String> dictionary = new ArrayList<String>(); // Dictionary to choose words from
	private ArrayList<String> compatibleDictionary = new ArrayList<String>(); //Dictionary of compatible words
	private Scanner reader; // Reader object
	private ArrayList<Letter> selectedLetters = new ArrayList<Letter>(); // List of letters selected by drag
	private ArrayList<Letter[]> currentWords = new ArrayList<Letter[]>(); // List of words on board
	private ArrayList<String> completedWords = new ArrayList<String>(); // List of completed words
	private final int OFFSET = 1280 - 960; // Vertical offset (used to draw screen at bottom instead of top)
	ArrayList<Coke> animatedLetters = new ArrayList<Coke>(); // List of letter cokes (aka sprites) being moved
	private final int STEPS = 250; // steps for animation
	private int smoothCount; // Used for making animation smooth
	private ArrayList<Coke> hintCokes = new ArrayList<Coke>(); // Hint cokes (Sprites)
	private int hintTimer = 0; // Counts time until hint is displayed
	private TimeUtils timer =  new TimeUtils();
	private long startTime;
	private long endTime;
	private int time;
	public String category;
	private final int GAMEDURATION = 60000;
	
	// LetterBoard constructor of the sprite
	public LetterBoard(int x, int y, int height, int width, String sourceKey) throws FileNotFoundException {
		
		category = sourceKey;
		Map<String, String> dictionarySelect = new TreeMap<String, String>();
		dictionarySelect.put("U.S. STATES", "data/dictionary3.txt");
		dictionarySelect.put("JPN PREFECTURES", "data/dictionary2.txt");
		dictionarySelect.put("ANIMALS", "data/dictionary4.txt");
		dictionarySelect.put("UDUB", "data/dictionary5.txt");
		dictionarySelect.put("FRUITS", "data/dictionary6.txt");
		
		FileHandle file = Gdx.files.internal(dictionarySelect.get(sourceKey));
		reader = new Scanner(file.read());
		while(reader.hasNext()) {
			dictionary.add(reader.nextLine().trim());
		}
		for(int i = 0; i < 6; i++) { //fills board with random letters
			for(int j = 0; j < 8; j++) {
				board[i][j] = new Letter(randy.nextInt(26), i * 120, j * 120 + OFFSET, false);
			}
		}
		String word;
		for(int i = 0; i < 8; i++) { //attempts to add word to board 8 times
			word = chooseRandomWord();
			System.out.println("" + addWord(board, word));
		}
		updateTracker();
		startTime = timer.millis();
		endTime = timer.millis() + GAMEDURATION; //change
	}
	
	// Returns a random word from the dictionary
	private String chooseRandomWord() {
		for(String s:dictionary) {
			compatibleDictionary.add(s);
		}
		int r = randy.nextInt(compatibleDictionary.size());
		return compatibleDictionary.get(r);
	}
	
	// Updates the position of the animates tile sprites
	public void update() {
		updateAnimatedTile();
		hintTimer++;
		if(hintTimer == 300) showHint();
		time = Math.round(((endTime - timer.millis())) / 1000);
	}
	
	// Renders the sprites on the letterboard
	public void render(SpriteBatch batcher) {
		for(int i = 0; i < 6; i++) { // render all tiles on board
			for(int j = 0; j < 8; j++) {
				board[i][j].render(batcher);
			}
		} 
		
		for(int i = 0; i < selectedLetters.size(); i++) {
				batcher.setColor(200 / 255.0f, 200 / 255.0f, 255 / 255.0f, 1);
				batcher.draw(AssetLoader.letters[selectedLetters.get(i).getLetter() - 'a'], i * 40, 280, 40, 40);
		}
		
		// render moving tiles
		batcher.setColor(200 / 255.0f, 200 / 255.0f, 255 / 255.0f, 1);
		for(int i = animatedLetters.size() - 1; i >= 0; i--) {
			animatedLetters.get(i).draw(batcher);
		}
		// render the hint
		batcher.setColor(1, 1, 1, 1);
		for(Coke s: hintCokes) {
			s.draw(batcher);
		}
	}
	
	// resets all unused letters
	public void sweep() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j].isEmpty()) place((char) randy.nextInt(26), i, j, false);
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
			x1 = randy.nextInt(6);
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
	
	// partner recursive backtracking method to addWord
	private boolean addWord(Letter[][] layer, int x, int y, String s, int direction, Letter[] tracker) {
		if(s.isEmpty())
			return true;
		place((char) (s.toLowerCase().charAt(0) - 'a'), x, y, true);
		tracker[tracker.length - s.length()] = board[x][y];
		switch(direction % 4) {
		case 0: //NORTH
			if((y - 1 > -1) && layer[x][y - 1].isEmpty() && board[x][y - 1].isEmpty()) { //NORTH (If north is empty)
				if(addWord(layer, x, y - 1, s.substring(1), 0, tracker)) return true; // (try placing north)
			} else if ((x + 1 < 6) && layer[x + 1][y].isEmpty() && board[x + 1][y].isEmpty()) { //EAST
				if(addWord(layer, x + 1, y, s.substring(1), 1, tracker)) return true;
			} else if((x - 1 > -1) && layer[x - 1][y].isEmpty() && board[x - 1][y].isEmpty()) { //WEST
				if(addWord(layer, x - 1, y, s.substring(1), 3, tracker)) return true;
			} else {
				break;
			}

		case 1: //EAST
			if ((x + 1 < 6) && layer[x + 1][y].isEmpty() && board[x + 1][y].isEmpty()) { //EAST
				if(addWord(layer, x + 1, y, s.substring(1), 1, tracker)) return true;
			} else if((y - 1 > -1) && layer[x][y - 1].isEmpty() && board[x][y - 1].isEmpty()) { //NORTH
				if(addWord(layer, x, y - 1, s.substring(1), 0, tracker)) return true;
			} else if((y + 1 < 8) && layer[x][y + 1].isEmpty() && board[x][y + 1].isEmpty()) { //SOUTH
				if(addWord(layer, x, y + 1, s.substring(1), 2, tracker)) return true;
			} else {
				break;
			}
		case 2: //SOUTH
			if((y + 1 < 8) && layer[x][y + 1].isEmpty() && board[x][y + 1].isEmpty()) { //SOUTH
				if(addWord(layer, x, y + 1, s.substring(1), 2, tracker)) return true;
			} else if((x - 1 > -1) && layer[x - 1][y].isEmpty() && board[x - 1][y].isEmpty()) { //WEST
				if(addWord(layer, x - 1, y, s.substring(1), 3, tracker)) return true;
			} else if ((x + 1 < 6) && layer[x + 1][y].isEmpty() && board[x + 1][y].isEmpty()) { //EAST
				if(addWord(layer, x + 1, y, s.substring(1), 1, tracker)) return true;
			} else {
				break;
			}
		case 3: //WEST
			if((x - 1 > -1) && layer[x - 1][y].isEmpty() && board[x - 1][y].isEmpty()) { //WEST
				if(addWord(layer, x - 1, y, s.substring(1), 3, tracker)) return true;
			} else if((y - 1 > -1) && layer[x][y - 1].isEmpty() && board[x][y - 1].isEmpty()) { //NORTH
				if(addWord(layer, x, y - 1, s.substring(1), 0, tracker)) return true;
			} else if((y + 1 < 8) && layer[x][y + 1].isEmpty() && board[x][y + 1].isEmpty()) { //SOUTH
				if(addWord(layer, x, y + 1, s.substring(1), 2, tracker)) return true;
			} else {
				break;
			}
		}
		place((char) randy.nextInt(26), x, y, false);
		return false;
	}
	
	// Places a letter onto the LetterBoard
	public void place(char c, int x, int y, boolean used) {
		board[x][y].setChar(c);
		board[x][y].setUsed(used);
	}
	
	// Selects letter that is touched
	public void onDrag(int screenX, int screenY) {
		Letter returnedLetter = board[screenX/120][(screenY - OFFSET)/120].selected();
		if(returnedLetter != null) {
			selectedLetters.add(returnedLetter);
		}
	}
	
	// Checks if selected word is contained within dictionary, if so it raises the score
	public void onTouchUp() {
		String tempString = "";
		for(int i = 0; i < selectedLetters.size(); i++) {
			tempString += selectedLetters.get(i).getLetter();
			selectedLetters.get(i).setSelected(false);
		}
		System.out.println(tempString);
		// if String is found...
		if(contains(tempString)) {
			System.out.println("FOUND!!!!");
			for(int i = 0; i < selectedLetters.size(); i++) {
				selectedLetters.get(i).setUsed(false);
			}
			completedWords.add(0, tempString);
			makeAnimatedTile(selectedLetters);
			sweep();
			updateTracker();
			System.out.println("" + addWord(board, chooseRandomWord()));
			System.out.println("" + addWord(board, chooseRandomWord()));
			hintCokes.clear(); hintTimer = 0;
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
	
	// Checks to make sure that all the current words in the currentWord list
	// are indeed on the board.
	private void updateTracker() {
		// Update Tracker
		for(int i = currentWords.size() - 1; i >= 0; i--) {
			currentWords.get(i)[0].setFirst(true); //Labels first letter to be grey
			String tempWord = "";
			for(int j = 0; j < currentWords.get(i).length; j++) {
				tempWord += currentWords.get(i)[j].getLetter();
			}
			if(!contains(tempWord)) { // If word in currentWord is not complete, remove it
				for(int k = 0; k < currentWords.get(i).length; k++) {
					currentWords.get(i)[k].setUsed(false);
					currentWords.get(i)[k].setFirst(false);
				}
				currentWords.remove(i);
			}
		}
	}
	
	// Checks if the string is contained within the dictionary
	private boolean contains(String tempString) {
		for(String word:compatibleDictionary) {
			if(word.equalsIgnoreCase(tempString)) return true;
		}
		return false;
	}
	
	// Renders the list of completed Words
	public void renderWordList(SpriteBatch batcher) {
		int i = 0;
		for(String word:completedWords) {
			for(int j = 0; j < word.length(); j++) {
				//if(i >= completedWords.get(0).length()) {
					batcher.draw(AssetLoader.letters[word.charAt(j) - 'a'], i * 40 + selectedLetters.size() * 40, 280, 40, 40);
				//}
				i++;
			}
		}
	}
	
	// Shows the hint
	public void showHint() {
		int index = randy.nextInt(currentWords.size());
		for(int i = 0; i < currentWords.get(index).length - 1; i++) {
			hintCokes.add(new Coke(AssetLoader.arrow, currentWords.get(index)[i].getOriginX(), currentWords.get(index)[i].getOriginY(), currentWords.get(index)[i + 1].getOriginX(), currentWords.get(index)[i + 1].getOriginY(), 40, 40));
		}
	}
	
	// Constructs a tile that is to be animated
	public void makeAnimatedTile(ArrayList<Letter> selectedLetters) {
		animatedLetters.clear();
		for(Letter l: selectedLetters) {
			animatedLetters.add(new Coke(AssetLoader.letters[(l.getLetter() - 'a')], l.getX(), l.getY(), 120, 120));
		}
		smoothCount = 0;
	}
	
	// Updates the position of the animated Tile
	public void updateAnimatedTile() {
		for(int i = 0; i < animatedLetters.size(); i++) {
			animatedLetters.get(i).changeSize(4f);
			animatedLetters.get(i).translate(-2f, -2f);
			animatedLetters.get(i).setAlpha(-0.04f);
			//if(animatedLetters.get(i).getY() > 240) animatedLetters.get(i).translate((i * 40 - animatedLetters.get(i).getX()) / (STEPS - smoothCount), (280 - animatedLetters.get(i).getY()) / (STEPS - smoothCount));
			//if(animatedLetters.get(i).getWidth() > 40) animatedLetters.get(i).changeSize((40 - animatedLetters.get(i).getSize()) / (STEPS - smoothCount));
		}
		if(smoothCount < STEPS - 5) smoothCount+=5;
	}
	
	// Gets the score by counting the letters in the CompletedWords list
	public int getScore() {
		int sum = 0;
		for(String w:completedWords) {
			sum += w.length();
		}
		return sum;
	}
	
	public int getTime() {
		return time;
	}
	
	// To be implemented...
	public void stopGame() {
		
	}

	// To be implemented...
	public void resetGame() {
		
	}
	
	public ArrayList<String> getCurrentWords() {
		ArrayList<String> result = new ArrayList<String>();
		for(Letter[] letters: currentWords) {
			String temp = "";
			for(Letter l: letters) {
				temp += l.getLetter();
			}
			result.add(temp);
		}
		return result;
	}
	
}