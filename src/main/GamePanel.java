package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.Color;

import javax.swing.JPanel;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import itile.ITile;
import tile.TileManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable{
	
	//Determines the Size of the Game Screen
	final int originalTileSize = 16, scale = 3;
	public final int tileSize = originalTileSize * scale;
	public final int maxScreenCol = 16, maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;
	public boolean fullScreenOn = false;
	
	//Settings for the world map
	//max map settings for the biggest map that can be made (aka worst case scenario)
	public final int maxMap = 1;
	public int curMap = 0;
	
	//Frames Per Seconds (FPS)
	int FPS = 60;
	
	public TileManager tileManager = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public EventHandler eventH = new EventHandler(this);
	public PathFinder pathH = new PathFinder(this);
	ConfigHandler configH = new ConfigHandler(this);
	Thread gameThread;
	
	public CollisionChecker collisionCheck = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	
	public Player player = new Player(this, keyH);
	
	//an array to display the number of objects simutaneously.
	//does not restrict number of objects that can be created.
	public Entity gObjects[][] = new Entity[maxMap][10];
	public Entity npcs[][] = new Entity[maxMap][10];
	public Entity enemies[][] = new Entity[maxMap][100];
	//public ITile iTiles[][] = new ITile[maxMap][10];
	public Entity[][] shotList = new Entity[maxMap][20];
	public ArrayList<Entity> particles = new ArrayList<>();
	ArrayList<Entity> entityList = new ArrayList<>();
	
	//Game States
	public int gameState;
	public final int mainMenuState = 0,
					 playState = 1, 
					 pauseState = 2, 
					 dialogState = 3,
					 statusState = 4,
					 optionsState = 5,
					 gameOverState = 6,
					 transitionState = 7,
					 sellState = 8;
	
	public GamePanel()
	{
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame(boolean restart) {
		aSetter.setObj(); //TO DO: make sprite sheets
		aSetter.setNPC();//TO DO: make sprite sheets
		aSetter.setEnemy();//TO DO: make sprite sheets
		//aSetter.setITiles();//TO DO: make sprite sheets
		if(!restart)
			gameState = mainMenuState;
	}
	
	public void retryGame() 
	{
		player.setDefPos();
		player.restoreManaAndLife();
		aSetter.setNPC();//TO DO: make sprite sheets
		aSetter.setEnemy();//TO DO: make sprite sheets
	}
	
	public void restartGame() 
	{	
		player.setDefaultValues();
		setupGame(true);	
	}
	
	public void startGameThread() 
	{
		gameThread = new Thread(this);
		gameThread.start();
	}

	//Game Loop Started by startGameThread()
	@Override
	public void run() {
		double drawInterval = 1_000_000_000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while(gameThread != null) 
		{
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			
			if(delta >= 1) 
			{
				//Update
				update();
				//Render
				repaint();
				
				delta--;
			}
		}
	}
	
	public void update() 
	{
		if(gameState == playState) {
			
			player.update();
			
			for(int i = 0; i < npcs[curMap].length; i++) 
			{
				if(npcs[curMap][i] != null)
					npcs[curMap][i].update();
			}
			
			for(int i = 0; i < enemies[curMap].length; i++) 
			{
				if(enemies[curMap][i] != null) 
				{
					if(enemies[curMap][i].alive && !enemies[curMap][i].dying)
						enemies[curMap][i].update();
					
					if(!enemies[curMap][i].alive) {
						enemies[curMap][i].checkDrop();
						enemies[curMap][i] = null;
					}
				}
			}
			
			for(int i = 0; i < shotList[curMap].length; i++) {
				if(shotList[curMap][i] != null) 
				{
					if(shotList[curMap][i].alive)
						shotList[curMap][i].update();
					if(!shotList[curMap][i].alive)
						shotList[curMap][i] = null;
				}
			}
			
			for(int i = 0; i < particles.size(); i++) 
			{
				if(particles.get(i) != null) 
				{
					if(particles.get(i).alive)
						particles.get(i).update();
					
					if(!particles.get(i).alive)
						particles.remove(i);
				}
			}
			/*
			for(int i = 0; i < iTiles[curMap].length; i++) 
			{
				if(iTiles[curMap][i] != null)
					iTiles[curMap][i].update();
			}*/
			
		}
		else if(gameState == pauseState) {
			//nothing.
		}
	}
	
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		if(gameState == mainMenuState)
			ui.draw(g2);
			
		else if(gameState == playState) 
		{
			tileManager.draw(g2);
			
			/*for(int i = 0; i < iTiles[curMap].length; i++) 
			{
				if(iTiles[curMap][i] != null)
					iTiles[curMap][i].draw(g2);
			}*/
			
			//adding to entity list
			entityList.clear();
			
			entityList.add(player);
			
			for(int i = 0; i < shotList[curMap].length; i++) {
				if(shotList[curMap][i] != null)
					entityList.add(shotList[curMap][i]);
			}
			
			for(int i = 0; i < npcs[curMap].length; i++) 
			{
				if(npcs[curMap][i] != null)
					entityList.add(npcs[curMap][i]);
			}
			
			for(int i = 0; i < gObjects[curMap].length; i++) 
			{
				if(gObjects[curMap][i] != null)
					entityList.add(gObjects[curMap][i]);
			}
			
			for(int i = 0; i < enemies[curMap].length; i++) 
			{
				if(enemies[curMap][i] != null)
					entityList.add(enemies[curMap][i]);
			}
			
			for(int i = 0; i < particles.size(); i++) 
			{
				if(particles.get(i) != null)
					entityList.add(particles.get(i));
			}
			
			
			//Sort EntityList
			Collections.sort(
				entityList, new Comparator<Entity>() {
					@Override
					public int compare(Entity e1, Entity e2) {
						int res = Integer.compare(e1.worldPosY, e2.worldPosY);
						return res;
					}
				}
			);
			
			for(int i = 0; i < entityList.size(); i++) {
				if(entityList.get(i) != null)
					entityList.get(i).draw(g2);
			}
			
			ui.draw(g2);
		}
		
		g2.dispose();
	}
}









































