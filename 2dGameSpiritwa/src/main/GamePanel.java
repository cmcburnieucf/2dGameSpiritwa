package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable{
	
	//Determines the Size of the Game Screen
	final int originalTileSize = 16;
	final int scale = 3;
	public final int tileSize = originalTileSize * scale;
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;
	
	//Frames Per Seconds (FPS)
	int FPS = 60;
	
	TileManager tileManager = new TileManager(this, "map0");
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	Player player = new Player(this, keyH);
	
	public GamePanel()
	{
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
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
		player.update();
	}
	
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		tileManager.draw(g2);
		player.draw(g2);
		g2.dispose();
	}
}









































