package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

//import main.GUtil;
import main.GamePanel;

/*
 * Super Class that will be used in
 * Player, Monster, and NPC classes
 * 
*/

public class Entity {
	public GamePanel gp;
	
	//Position and Direction
	public int worldPosX, worldPosY;
	public String direction = "down";
	public ArrayList<String> tempDirections = new ArrayList<>();
	
	//Player Specific Stats of which some Enemies can share
	public int defSpeed, speed, type, life, maxLife, mana, maxMana, ammo, maxAmmo;
	public int level, strength, dext, attack, defense, exp, nextLvlExp;
	public int wealth;
	public String name;
	public Entity curWeapon, curShield, curLightSrc;
	public Projectile curShot;
	
	//Player, Seller, Trader, (and Any Other NPCs that hold items) Specific
	public Map<String, Integer> inventoryMap = new HashMap<String, Integer>();
	public ArrayList<Entity> inventoryList = new ArrayList<>();
	public final int maxListSize = 20;
	
	//for items
	public int attackVal = 0, 
			defenseVal = 0, 
			useCost = 0, 
			val = 0, 
			price = 0,
			kbPower = 0,
			lightRadius = 0;
	public String description = "";
	
	/* The tutorial did this instead of just type 
	 * (with some personal added customization)
	 * public int type;
	 * public final int type_player = 0;
	 * public final int type_npc = 1;
	 * public final int type_enemy = 2;
	 * public final int type_object = 3;
	 * public final int type_weapon = 4
	 * public final int type_shield = 5;
	 * public final int type_consumable = 6;
	 * public final int type_projectile = 7;
	 * public final int type_pickUpOnly = 8;
	 * public final int type_woodCutter = 9;
	 * public final int type_rockSmasher = 10;
	 * public final int type_obstacle = 11;
	 * public final int type_lightSrc = 12;
	 * public final int type_restArea = 13;
	 * */
	
	//Displaying Characters, Enemies, NPCS, Objects, etc.
	public SpriteSheet movement;
	public SpriteSheet attackMovement;
	public SpriteSheet defenseMovement;
	public boolean canMove = false;
	
	//Life State
	public boolean alive = true, dying = false;
	boolean hpBarOn = false;
	public boolean invincible = false, knockBack = false;
	boolean attacking = false;
	public boolean onPath = false;
	
	//All the Counters with SpriteNum
	int hpBarCtr = 0, dyingCtr = 0;
	public int invincibleCtr = 0, knockBackCtr = 0;
	public int actionCtr = 0, availableShotCtr = 0;
	public int spriteCtr = 0;
	
	//Used for Dialogue State
	public String dialogue[] = new String[20];
	int dialogIndex = 0;
	
	//Used For Collision Checks
	public Rectangle solidPart = new Rectangle(0, 0, 48, 48);
	//aka solidPartDefaultX and solidPartDefaultY
	public int defX, defY;
	public boolean collisionOn = false;
	public boolean collision = false;
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public Entity attacker;
	public String knockBackDirection;
	
	public Entity(GamePanel gp) 
	{
		this.gp = gp;
	}
	
	public int getLeftX() {
		return (worldPosX+solidPart.x);
	}
	public int getRightX() {
		return (worldPosX+solidPart.x+solidPart.width);
	}
	public int getTopY() {
		return (worldPosY+solidPart.y);
	}
	public int getBottomY() {
		return (worldPosY+solidPart.y+solidPart.height);
	}
	
	public int getCol() {
		return ((worldPosX+solidPart.x)/gp.tileSize);
	}
	public int getRow() {
		return ((worldPosY+solidPart.y)/gp.tileSize);
	}
	
	//methods to be overriden by sub classes
	public boolean use(Entity entity) {return false;}
	public void checkDrop() {}
	public void damageReaction() {}
	
	public void dropItem(Entity dropped) 
	{
		for(int i = 0; i < gp.gObjects[gp.curMap].length; i++) 
		{
			if(gp.gObjects[gp.curMap][i] == null && dropped != null) 
			{
				gp.gObjects[gp.curMap][i] = dropped;
				//defeated enemy's position
				gp.gObjects[gp.curMap][i].worldPosX = worldPosX;
				gp.gObjects[gp.curMap][i].worldPosY = worldPosY;
				break;
			}
		}
	}
	
	public int getXDist(Entity target) {
		return Math.abs(worldPosX - target.worldPosX);
	}
	public int getYDist(Entity target) {
		return Math.abs(worldPosY - target.worldPosY);
	}
	public int getTotDist(Entity target) {
		return (getXDist(target)+getYDist(target))/gp.tileSize;
	}
	public int getGoalCol(Entity target) {
		return (target.worldPosX+target.solidPart.x)/gp.tileSize;
	}
	public int getGoalRow(Entity target) {
		return (target.worldPosY+target.solidPart.y)/gp.tileSize;
	}
	
	public void setAction() 
	{
		actionCtr++;
		
		if(actionCtr == 120) 
		{
			Random rand = new Random();
			int i = rand.nextInt(100)+1;
			
			if(i <= 25) 
			{
				direction = "up";
			}
			else if(i > 25 && i <= 50) 
			{
				direction = "down";
			}
			else if(i > 50 && i <= 75) 
			{
				direction = "left";
			}
			else if(i > 75 && i <= 100) 
			{
				direction = "right";
			}
			actionCtr = 0;
		}
	}
	
	public void followGivenPath(int goalRow, int goalCol){
		int startRow = (worldPosY+solidPart.y)/gp.tileSize, 
				startCol = (worldPosX+solidPart.x)/gp.tileSize;
		gp.pathH.setNodes(startCol, startRow, goalCol, goalRow);
		if(gp.pathH.followPath()) {
			//next WorldPosX and next WorldPosY
			int nextX = gp.pathH.path.get(0).col*gp.tileSize, 
					nextY = gp.pathH.path.get(0).row*gp.tileSize;
			
			int leftX = worldPosX+solidPart.x, 
				rightX = worldPosX+solidPart.x+solidPart.width, 
				topY = worldPosY+solidPart.y, 
				bottomY = worldPosY+solidPart.y+solidPart.height;
			
			if((topY > nextY) && (leftX >= nextX) && 
				(rightX < nextX+gp.tileSize)) {
				direction = "up";
			}
			else if((topY < nextY) && (leftX >= nextX) && 
					(rightX < nextX+gp.tileSize)) {
					direction = "down";
			}
			else if((topY >= nextY) && (bottomY < nextY+gp.tileSize) && 
					(leftX > nextX)) {
					direction = "left";
			}
			else if((topY >= nextY) && (bottomY < nextY+gp.tileSize) && 
					(leftX < nextX)) {
				direction = "right";
			}
			else if((topY > nextY) && (leftX >= nextX)) {
				direction = "up";
				checkCollision();
				if(collisionOn) {
					direction = "left";
				}
			}
			else if((topY > nextY) && (leftX < nextX)) {
				direction = "up";
				checkCollision();
				if(collisionOn) {
					direction = "right";
				}
			}
			else if((topY < nextY) && (leftX >= nextX)) {
				direction = "down";
				checkCollision();
				if(collisionOn) {
					direction = "left";
				}
			}
			else if((topY < nextY) && (leftX < nextX)) {
				direction = "down";
				checkCollision();
				if(collisionOn) {
					direction = "right";
				}
			}
			
			//for given path
			//nextX = gp.pathH.path.get(0).col;
			//nextY = gp.pathH.path.get(0).row;
			
			//if goal is reached, stop the entity
			//if(nextX == goalCol && nextY == goalRow) {
				//onPath = false;
			//}
			
		}
	}
	
	public void speak() 
	{
		gp.ui.currentDialog = dialogue[dialogIndex];
		dialogIndex++;
		
		if(dialogIndex == dialogue.length)
			dialogIndex = 0;
		
		switch(gp.player.direction) 
		{
		case "up":
		case "upright":
		case "upleft":
			direction = "down";
			break;
		case "down":
		case "downright":
		case "downleft":
			direction = "up";
			break;
		case "left":
			direction = "right";
			break;
		case "right":
			direction = "left";
			break;
		}
	}
	
	public void interact() {}
	
	public int getDetected(Entity user, Entity[][] targets, String targetname) {
		int index = -1;
		
		int nextWX = user.getLeftX();
		int nextWY = user.getTopY();
		
		switch(user.direction) {
		case "up":
			nextWY = user.getTopY()-1;
			break;
		case "down":
			nextWY = user.getBottomY()+1;
			break;
		case "left":
			nextWX = user.getLeftX()-1;
			break;
		case "right":
			nextWX = user.getRightX()+1;
			break;
		}
		int col = (nextWX/gp.tileSize), row = (nextWY/gp.tileSize);
		
		for(int i = 0; i < targets[gp.curMap].length; i++) {
			if(targets[gp.curMap][i] != null &&
				targets[gp.curMap][i].getCol() == col &&
				targets[gp.curMap][i].getRow() == row &&
				targets[gp.curMap][i].name.contentEquals(targetname)) {
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	public boolean checkCollision() {
		collisionOn = false;
		gp.collisionCheck.checkTile(this);
		gp.collisionCheck.checkObj(this, false);
		gp.collisionCheck.checkEntity(this, gp.npcs);
		gp.collisionCheck.checkEntity(this, gp.enemies);
		gp.collisionCheck.checkPlayer(this);
		//gp.collisionCheck.checkEntity(this, gp.iTiles);
		boolean contactPlayer = gp.collisionCheck.checkPlayer(this);
		return contactPlayer;
	}
	
	public void update() 
	{
		if(knockBack) {
			checkCollision();
			if(collisionOn) {
				knockBackCtr = 0;
				knockBack = false;
				speed = defSpeed;
			}
			else if(!collisionOn) {
				switch(gp.player.direction) {
				case "down":
					worldPosY += speed;
					break;
				case "up":
					worldPosY -= speed;
					break;
				case "right": 
					worldPosX += speed;
					break;
				case "left":
					worldPosX -= speed;
					break;
				case "upright":
					worldPosX += (speed * 0.71);
					worldPosY -= (speed * 0.71);
					break;
				case "upleft":
					worldPosX -= (speed * 0.71);
					worldPosY -= (speed * 0.71);
					break;
				case "downright":
					worldPosX += (speed * 0.71);
					worldPosY += (speed * 0.71);
					break;
				case "downleft":
					worldPosX -= (speed * 0.71);
					worldPosY += (speed * 0.71);
					break;
				}
			}
			knockBackCtr++;
			if(knockBackCtr >= 10) {
				knockBack = false;
				knockBackCtr = 0;
				speed = defSpeed;
			}
		}
		else {
			setAction();
			//checkCollision();
			
			if(this.type == 2 && checkCollision()) 
			{
				damagePlayer(attack);
			}
			
			//After collision check
			if(!collisionOn && canMove) 
			{
				switch(direction) 
				{
				case "down":
					worldPosY += speed;
					break;
				case "up":
					worldPosY -= speed;
					break;
				case "right": 
					worldPosX += speed;
					break;
				case "left":
					worldPosX -= speed;
					break;
				case "upright":
					worldPosX += (speed * 0.71);
					worldPosY -= (speed * 0.71);
					break;
				case "upleft":
					worldPosX -= (speed * 0.71);
					worldPosY -= (speed * 0.71);
					break;
				case "downright":
					worldPosX += (speed * 0.71);
					worldPosY += (speed * 0.71);
					break;
				case "downleft":
					worldPosX -= (speed * 0.71);
					worldPosY += (speed * 0.71);
					break;
				}
			}
		}
		
		spriteCtr++;
		movement.changeFrame = true;
		if(spriteCtr > 20) 
		{
			spriteCtr = 0;
		}
		
		if(invincible) 
		{
			invincibleCtr++;
			if(invincibleCtr > 30) 
			{
				invincible = false;
				invincibleCtr = 0;
			}
		}
		
		if(availableShotCtr < 60) 
		{
			availableShotCtr++;
		}
	}
	
	public void checkShot(int shotInterval, int rate) {
		//shoot projectile if "Fate" allows it
		int i = new Random().nextInt(rate) + 1;
		
		if(i == 0 && !curShot.alive && 
			availableShotCtr == (shotInterval)) 
		{
			curShot.set(worldPosX, worldPosY, direction, true, this);
			for(int j = 0; j < gp.shotList[gp.curMap].length; j++) {
				if(gp.shotList[gp.curMap][j] == null) {
					gp.shotList[gp.curMap][j] = curShot;
					break;
				}
			}
			availableShotCtr = 0;
		}
	}
	
	public void checkPursuit(Entity target, int dist, int rate, boolean quit) {
		int i = new Random().nextInt(rate)+1;
		if(i == 0) {
			//stop chasing/following
			if(quit && getTotDist(target) > dist) {
				onPath = false;
			}
			//start chasing/following
			else if(!quit && getTotDist(target) < dist) {
				onPath = true;
			}
		}
		
		
	}
	
	public void damagePlayer(int attack) 
	{
		if(!gp.player.invincible) {
			int damage = attack - gp.player.defense;
			if(damage < 0)
				damage = 0;
			
			gp.player.life -= damage;
			gp.player.invincible = true;
		}
	}
	
	public void setKnockBack(Entity target, Entity attacker, int kbPow) {
		this.attacker = attacker;
		target.knockBackDirection = attacker.direction;
		target.speed += kbPow;
		target.knockBack = true;
	}
	
	public SpriteSheet getSpriteSheet(String filepath, int numFrames, ArrayList<String> directions) 
	{
		//if player
		//file path would be: /player/player_sprite_sheet.png
		SpriteSheet newSheet = null;
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(filepath+".png"));
			//if we get the image, make the new SpriteSheet
			newSheet = new SpriteSheet(image, numFrames, directions);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return newSheet;
	}
	
	public void draw(Graphics2D g2) 
	{
		int screenX = worldPosX - gp.player.worldPosX + gp.player.screenPosX;
		int screenY = worldPosY - gp.player.worldPosY + gp.player.screenPosY;
		
		if(((worldPosX + gp.tileSize) > (gp.player.worldPosX - gp.player.screenPosX)) && 
		   ((worldPosX - gp.tileSize) < gp.player.worldPosX + gp.player.screenPosX) &&
		   ((worldPosY + gp.tileSize) > (gp.player.worldPosY - gp.player.screenPosY)) && 
		   ((worldPosY - gp.tileSize) < gp.player.worldPosY + gp.player.screenPosY))
		{
			BufferedImage image = null;
			
			if(movement.changeFrame) {
				image = movement.nextFrame(direction);
				movement.changeFrame = false;
			}
			else {
				image = movement.displayStanding(direction);
			}
			if(type == 2 && hpBarOn) 
			{
				//the length of 1 hp
				double scale = (double) gp.tileSize / maxLife;
				//cur length of hp bar
				double hpBar = scale * life;
				
				g2.setColor(Color.black);
				g2.fillRect(screenX-1, screenY-16, gp.tileSize, 12);
				
				
				g2.setColor(Color.pink);
				g2.fillRect(screenX, screenY-15, (int)hpBar, 10);
				
				hpBarCtr++;
				
				if(hpBarCtr > 600) 
				{
					hpBarCtr = 0;
					hpBarOn = false;
				}
			}
			
			if(invincible) 
			{
				hpBarOn = true;
				hpBarCtr = 0;
				if(type == 2)
					changeAlpha(g2, 0.5f);
			}
			
			if(dying) 
			{
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, screenX, screenY, null);
			
			changeAlpha(g2, 1.0f);
			
		}
	}

	private void dyingAnimation(Graphics2D g2) {
		dyingCtr++;
		int i = 5;
		if((dyingCtr <= i) || (dyingCtr > (i*2) && dyingCtr <= (i*3)) ||
			(dyingCtr > (i*4) && dyingCtr <= (i*5)) || (dyingCtr > (i*6) && dyingCtr <= (i*7)))
			changeAlpha(g2, 0.0f);
		else if((dyingCtr > (i) && dyingCtr <= (i*2)) || (dyingCtr > (i*3) && dyingCtr <= (i*4)) ||
				(dyingCtr > (i*5) && dyingCtr <= (i*6)) || (dyingCtr > (i*7) && dyingCtr <= (i*8))) 
			changeAlpha(g2, 1.0f);
		else if(dyingCtr >(i*8)) 
			alive = false;
		
	}
	
	public void changeAlpha(Graphics2D g2, float alpha) 
	{
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}
	
	
	public Color getPColor() {return null;}
	
	public int getPSize() {return 0;}
	
	public int getPSpeed() {return 0;}
	
	//how long the particle will displayed
	public int getPMaxLife() {return 0;}
	
	
	public void makeParticle(Entity gen, Entity target) 
	{
		gp.particles.add(new Particle(gp, target, gen.getPColor(),
				gen.getPSize(), gen.getPSpeed(), gen.getPMaxLife(),
				2, 1));
		gp.particles.add(new Particle(gp, target, gen.getPColor(),
				gen.getPSize(), gen.getPSpeed(), gen.getPMaxLife(),
				2, -1));
		gp.particles.add(new Particle(gp, target, gen.getPColor(),
				gen.getPSize(), gen.getPSpeed(), gen.getPMaxLife(),
				-2, 1));
		gp.particles.add(new Particle(gp, target, gen.getPColor(),
				gen.getPSize(), gen.getPSpeed(), gen.getPMaxLife(),
				-2, -1));
	}
	
}







































