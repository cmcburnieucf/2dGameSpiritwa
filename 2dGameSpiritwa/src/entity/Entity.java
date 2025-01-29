package entity;

import java.awt.image.BufferedImage;

/*
 * Super Class that will be used in
 * Player, Monster, and NPC classes
 * 
*/

public class Entity {
	public int posX, posY;
	public int speed;
	
	public BufferedImage up, upMove, down, downMove, left, leftMove, right, rightMove;
	public String direction;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
}
