package entity;

//import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	
	GamePanel gp;
	KeyHandler keyH;
	
	public Player(GamePanel gp, KeyHandler keyH) 
	{
		this.gp = gp;
		this.keyH = keyH;
		setDefaultValues();
		getPlayerImages();
	}
	
	public void setDefaultValues()
	{
		posX = 10;
		posY = 10;
		speed = 4;
		direction = "down";
	}
	
	public void getPlayerImages() 
	{
		try 
		{
			up = ImageIO.read(getClass().getResourceAsStream("/player/player_back.png"));
			upMove = ImageIO.read(getClass().getResourceAsStream("/player/player_back_moving.png"));
			
			down = ImageIO.read(getClass().getResourceAsStream("/player/player_front.png"));
			downMove = ImageIO.read(getClass().getResourceAsStream("/player/player_front_moving.png"));
			
			right = ImageIO.read(getClass().getResourceAsStream("/player/player_right.png"));
			rightMove = ImageIO.read(getClass().getResourceAsStream("/player/player_right_moving.png"));
			
			left = ImageIO.read(getClass().getResourceAsStream("/player/player_left.png"));
			leftMove = ImageIO.read(getClass().getResourceAsStream("/player/player_left_moving.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void update() 
	{
		//this stops the player from doing animation while not moving around
		if(keyH.downPressed || keyH.upPressed || keyH.rightPressed || keyH.leftPressed)
			spriteCounter++;
		
		if(keyH.downPressed) 
		{
			direction = "down";
			posY += speed;
		}
		else if(keyH.upPressed) 
		{
			direction = "up";
			posY -= speed;
		}
		else if(keyH.rightPressed) 
		{
			direction = "right";
			posX += speed;
		}
		else if(keyH.leftPressed) 
		{
			direction = "left";
			posX -= speed;
		}
		else if(keyH.upPressed && keyH.rightPressed) 
		{
			direction = "up-right";
			posX += (speed * 0.71);
			posY += (speed * 0.71);
		}
		
		
		if(spriteCounter > 20) 
		{
			if(spriteNum == 1)
				spriteNum = 2;
			else if(spriteNum == 2)
				spriteNum = 1;
			spriteCounter = 0;
		}
		
	}
	
	public void draw(Graphics2D g2) 
	{
		BufferedImage image = null;
		
		switch(direction) 
		{
		case "up":
			if(spriteNum == 1)
				image = up;
			if(spriteNum == 2)
				image = upMove;
			break;
		case "down":
			if(spriteNum == 1)
				image = down;
			if(spriteNum == 2)
				image = downMove;
			break;
		case "right":
			if(spriteNum == 1)
				image = right;
			if(spriteNum == 2)
				image = rightMove;
			break;
		case "left":
			if(spriteNum == 1)
				image = left;
			if(spriteNum == 2)
				image = leftMove;
			break;
		}
		g2.drawImage(image, posX, posY, gp.tileSize, gp.tileSize, null);
	}
}


































