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
	public int speed, type, life, maxLife, mana, maxMana, ammo, maxAmmo;
	public int level, strength, dext, attack, defense, exp, nextLvlExp;
	public int wealth;
	public String name;
	public Entity curWeapon, curShield;
	public Projectile curShot;
	
	//Player, Seller, Trader, (and Any Other NPCs that hold items) Specific
	public Map<String, Integer> inventoryMap = new HashMap<String, Integer>();
	public ArrayList<Entity> inventoryList = new ArrayList<>();
	public final int maxListSize = 20;
	
	//for items
	public int attackVal = 0, defenseVal = 0, useCost = 0, val = 0;
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
	 * */
	
	//Displaying Characters, Enemies, NPCS, Objects, etc.
	public SpriteSheet movement;
	public SpriteSheet attackMovement;
	public SpriteSheet defenseMovement;
	
	//Life State
	public boolean alive = true, dying = false;
	boolean hpBarOn = false;
	public boolean invincible = false;
	boolean attacking = false;
	
	//All the Counters with SpriteNum
	int hpBarCtr = 0, dyingCtr = 0;
	public int invincibleCtr;
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
	
	
	public Entity(GamePanel gp) 
	{
		this.gp = gp;
	}
	
	//methods to be overriden by sub classes
	public void use(Entity entity) {};
	public void checkDrop() {}
	public void damageReaction() {};
	
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
	
	public void speak() 
	{
		gp.ui.currentDialog = dialogue[dialogIndex];
		dialogIndex++;
		
		if(dialogIndex == dialogue.length)
			dialogIndex = 0;
		
		switch(gp.player.direction) 
		{
		case "up":
			direction = "down";
			break;
		case "down":
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
	
	public void update() 
	{
		setAction();
		
		collisionOn = false;
		gp.collisionCheck.checkTile(this);
		gp.collisionCheck.checkObj(this, false);
		gp.collisionCheck.checkEntity(this, gp.npcs);
		gp.collisionCheck.checkEntity(this, gp.enemies);
		gp.collisionCheck.checkPlayer(this);
		//gp.collisionCheck.checkEntity(this, gp.iTiles);
		boolean contactPlayer = gp.collisionCheck.checkPlayer(this);
		
		if(this.type == 2 && contactPlayer) 
		{
			damagePlayer(attack);
		}
		
		//After collision check
		if(!collisionOn) 
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
			case "up-right":
				worldPosX += (speed * 0.71);
				worldPosY += (speed * 0.71);
				break;
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







































