package com.me.GameWorld;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.GameObjects.Letter;
import com.me.GameObjects.LetterBoard;
import com.me.Helpers.AssetLoader;
import com.me.Helpers.InputHandler;

public class GameWorld {

    private LetterBoard board;
    public GameState currentState;
    private int catTimer;
    private SelectBox selectBox;
    private Stage stage;
    private Skin skin;
    private Table table = new Table();
	private InputHandler defaultHandler;
	private ArrayList<LetterBoard> tempBoards = new ArrayList<LetterBoard>();
	private ArrayList<ArrayList<Coke>> tempRows;
	private Random randy = new Random();
	private Stage gameDash;
	private Label timeLabel;
	private Label scoreLabel;
	private Label helpContentLabel;
	private Stage endMenu;
    
    public GameWorld(int midPointY) throws FileNotFoundException {
    	currentState = GameWorld.GameState.MENU;
    	stage = new Stage(720,1280,true);
    	skin = new Skin();
    	skin.addRegions(new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas")));
    	skin.load(Gdx.files.internal("skin/uiskin.json"));
    	buildMenu();
    	buildMenuBackground();
    }
    
    public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE, CAT, HELP
	}
    
    public void update(float delta) {
		// runTime += delta;

		switch (currentState) {
		case READY:
		case MENU:
			
			updateMenu(delta);
			break;

		case RUNNING:
			updateRunning(delta);
			break;
		case CAT:
			updateCat(delta);
			break;
		default:
			break;
		}
	}
    
    public void buildMenu() {
    	TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    	skin.add("default", new BitmapFont());
    	textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
    	textButtonStyle.down = skin.newDrawable("white", Color.BLUE);
    	textButtonStyle.font = skin.getFont("default");
    	textButtonStyle.font.setScale(3);
    	skin.add("default", textButtonStyle);
    	TextButton button=new TextButton("Start!", skin);
    	button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	System.out.println("HELLO");
            	try {
					start();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                // or System.exit(0);
            }
        });
    	
    	// Creates Help Button
    	TextButton helpButton = new TextButton("Help", skin);
    	helpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	System.out.println("HELLO");
					currentState = GameState.HELP; // Open Help Window
					Gdx.input.setInputProcessor(defaultHandler);
            }
        });
    	
    	SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
    	selectBoxStyle = new SelectBoxStyle();
    	selectBoxStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
    	selectBoxStyle.font = skin.getFont("default");
    	selectBoxStyle.fontColor = Color.WHITE;

    	ListStyle selectBoxListStyle = new ListStyle();
    	selectBoxListStyle.selection = skin.newDrawable("white", Color.CYAN);
    	selectBoxListStyle.font = skin.getFont("default");
    	selectBoxListStyle.fontColorUnselected = Color.WHITE;
    	selectBoxListStyle.fontColorSelected = Color.WHITE;
    	selectBoxStyle.listStyle = selectBoxListStyle;

    	ScrollPaneStyle selectBoxScrollStyle = new ScrollPaneStyle();
    	selectBoxScrollStyle.background = skin.newDrawable("white", Color.GRAY);
    	selectBoxStyle.scrollStyle = selectBoxScrollStyle;
    	
    	skin.add("default", selectBoxStyle);
    	selectBox = new SelectBox(new String[] {"U.S. STATES", "JPN PREFECTURES", "ANIMALS", "FRUITS", "UDUB"}, skin);
    	selectBox.setSize(100, 100);
    	selectBox.setPosition(350, 200);
    	
    	Label.LabelStyle labelStyle = new LabelStyle();
    	labelStyle.font = skin.getFont("default");
    	labelStyle.font.setScale(3);
    	Label label = new Label("Select Category:", labelStyle);
    	label.setAlignment(Align.center);
    	
    	Label.LabelStyle titleStyle = new LabelStyle();
    	titleStyle.font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
    	titleStyle.font.setScale(1.5f);
    	Label title = new Label("WordNoodle", titleStyle);
    	title.setAlignment(Align.center);
    	
    	table.add(title).size(450, 180).padBottom(240).row();
    	table.add(helpButton).size(450,180).padBottom(20).row();
    	table.add(label).size(450, 180).padBottom(0).row();
    	table.add(selectBox).size(450,180).padBottom(20).row();
        table.add(button).size(450,180).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }
    
    public void buildMenuBackground() throws FileNotFoundException {
    	tempRows = new ArrayList<ArrayList<Coke>>();
    	for(int i = 0; i < 12; i++) {
    		addRow(tempRows);
    	}
    }
    
    private void addRow(ArrayList<ArrayList<Coke>> tempRow) {
    	ArrayList<Coke> tempColumns = new ArrayList<Coke>();
		for(int j = 0; j < 6; j++) {
			tempColumns.add(new Coke(AssetLoader.letters[randy.nextInt(26)], j * 120, (tempRow.size() - 1) * 120, 120, 120));
		}
		tempRow.add(tempColumns);
    }
    
    public ArrayList<ArrayList<Coke>> getBGBoard() {
    	return tempRows;
    }
    
    public void updateMenu(float delta) {
    	for(ArrayList<Coke> a: tempRows) {
    		for(Coke c: a) {
    			c.translate(0, -1f);
    		}
    	}
    	if(tempRows.get(1).get(0).getY() < 0) {
    		addRow(tempRows);
    		tempRows.remove(0);
    		System.out.println("RAN");
    	}
    }

    public void updateRunning(float delta) {
    	board.update();
    	if(board.getTime() <= 0) {
    		catTimer = 120;
    		currentState = GameWorld.GameState.CAT;
    	}
    	timeLabel.setText("" + Math.max(board.getTime(), 0));
    	scoreLabel.setText("" + board.getScore());
    	String temp = "";
    	for(int i = 0; i < Math.min(board.getCurrentWords().size() - 1, 4); i++) {
    		temp += board.getCurrentWords().get(i).toUpperCase() + "\n";
    	}
    	helpContentLabel.setText(temp);
    }
    
    // Handles Game Over Event
    public void updateCat(float delta) {
		catTimer--;
		if(catTimer <= 0) {
    		currentState = GameWorld.GameState.GAMEOVER;
    		if (board.getScore() > AssetLoader.getHighScore(board.category)) {
				AssetLoader.setHighScore(board.getScore(), board.category);
				currentState = GameState.GAMEOVER;
			}
		}
		buildEndGameMenu();
    }
    
    
    public void buildEndGameMenu() {
		// TODO Auto-generated method stub
    	endMenu = new Stage(720, 1280, true);
    	Gdx.input.setInputProcessor(endMenu);
    	Table endMenuTable = new Table();
    	endMenuTable.setFillParent(true);
    	
    	Label.LabelStyle h1 = new LabelStyle();
    	h1.font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
    	h1.font.setScale(1f);
    	
    	endMenuTable.add(new Label("GAMEOVER", h1)).padBottom(75).row();
    	endMenuTable.add(new Label("Category: \n" + board.category, h1)).padBottom(75).row();
    	endMenuTable.add(new Label("Final Score: " + board.getScore(), h1)).padBottom(75).row();
    	endMenuTable.add(new Label("Highscore: " + AssetLoader.getHighScore(board.category), h1)).padBottom(75).row();
    	
    	
    	TextButton retryButton=new TextButton("Retry", skin);
    	retryButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	System.out.println("HELLO");
            	try {
					restart();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                // or System.exit(0);
            }
        });
    	
    	TextButton mmButton=new TextButton("Main\nMenu", skin);
    	mmButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	System.out.println("HELLO");
					currentState = GameWorld.GameState.MENU;
					switchInput();
            }
        });
    	
    	Table tempTable = new Table();
    	tempTable.add(retryButton).size(300, 150).padRight(20);
    	tempTable.add(mmButton).size(300, 150).padLeft(20);
    	
    	endMenuTable.add(tempTable);
    	endMenu.addActor(endMenuTable);
	}

	public LetterBoard getBoard() {
    	return board;
    }
    
    public ArrayList<LetterBoard> getTempBoards() {
    	return tempBoards;
    }
    
    // This method builds the dashboard on top of the screen and
    // Constructs the letterboard
    public void start() throws FileNotFoundException {
    	Gdx.input.setInputProcessor(defaultHandler);
		currentState = GameState.RUNNING;
        board = new LetterBoard(0, 0, 720, 720, selectBox.getSelection());
        gameDash = new Stage(720, 1280,true);
        Table parent = new Table();
        
        Table childL = new Table();
        Table childR = new Table();
        
        Label.LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        labelStyle.font.setScale(0.5f);
        
        Label.LabelStyle numberStyle = new LabelStyle();
        numberStyle.font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        numberStyle.font.setScale(1.5f);
        
        Label.LabelStyle helpTitleStyle = new LabelStyle();
        helpTitleStyle.font = new BitmapFont(Gdx.files.internal("skin/default.fnt"));
        helpTitleStyle.font.setScale(2f);
        
        Label.LabelStyle helpContentStyle = new LabelStyle();
        helpContentStyle.font = new BitmapFont(Gdx.files.internal("skin/default.fnt"));
        helpContentStyle.font.setScale(2f);
        
        Label label = new Label("Words to Find...", helpTitleStyle);
        label.setAlignment(Align.left);
        helpContentLabel = new Label("default", helpContentStyle);
        helpContentLabel.setAlignment(Align.left);
        Label scoreText = new Label("score:", skin);
        Label timeText = new Label("time:", skin);
        Label tempText = new Label("temp", skin);
        
        childL.add(label).width(360).height(80).row().align(Align.top);
        childL.add(helpContentLabel).width(360).height(200).align(Align.top);
        childL.setSize(360, 280);
        childL.top();
        parent.add(childL).width(360).top();
        
        Table scoreTable = new Table();
        Table timeTable = new Table();
        
        scoreLabel = new Label("hi", numberStyle);
        timeLabel = new Label("hi", numberStyle);
        
        scoreTable.add(new Label("SCORE:", labelStyle)).width(180).height(40).row();
        scoreTable.add(scoreLabel).width(180).height(240).row().top();
        scoreTable.top();
        timeTable.add(new Label("TIME:", labelStyle)).width(180).height(40).row();
        timeTable.add(timeLabel).width(180).height(240).row().top();
        timeTable.top();
        childR.add(scoreTable).width(180).height(280);
        childR.add(timeTable).width(180).height(280);
        childR.top().left();
        
        parent.add(childR).width(360).height(280);
        parent.top();
        parent.setFillParent(true);
        gameDash.addActor(parent);
	}
    
    // Restarts the game. No special functions.
    public void restart() throws FileNotFoundException {
    	start();
    }
    
    // Called by the render class
    public void renderWorld(SpriteBatch batcher) {
    	if(currentState == GameState.MENU) {
    		stage.act();
    		stage.draw();
    	}
    	if(currentState == GameState.RUNNING || currentState == GameState.CAT) {
    		gameDash.act();
    		gameDash.draw();
    	}
    	if(currentState == GameState.GAMEOVER) {
    		endMenu.act();
    		endMenu.draw();
    	}
    }
    
    // Switchers input processor
    public void switchInput() { // Used to use input to control menu stage
        Gdx.input.setInputProcessor(stage);
    }
    
    public void passInputHandler(InputHandler defaultHandler) {
    	this.defaultHandler = defaultHandler;
    }
}
