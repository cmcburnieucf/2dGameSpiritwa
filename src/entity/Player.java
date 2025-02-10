package entity;

//import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gameobj.GObjMagicBall;
import gameobj.GObjKey;
import gameobj.GObjSmallSword;
import gameobj.GObjWoodenShield;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	
	KeyHandler keyH;
	public final int screenPosX, screenPosY;
	public int standCounter = 0;
	public boolean attackCancelled = false, 
					isStanding = false, 
					lightSrcUpdated = false;
	
	
	public Player(GamePanel gp, KeyHandler keyH) 
	{
		super(gp);
		this.keyH = keyH;
		
		type = 0;
		
		this.screenPosX = gp.screenWidth/2 - (gp.tileSize/2);
		this.screenPosY = gp.screenHeight/2 - (gp.tileSize/2);
		
		//setting solid Part to check for collision:
		// positionX = 8
		// positionY = 16
		// solid part width = 32
		// solid part height = 32
		solidPart = new Rectangle(8, 16, 32, 32);
		defX = solidPart.x;
		defY = solidPart.y;
		
		setDefaultValues();
		
		tempDirections.add("down");
		tempDirections.add("up");
		tempDirections.add("left");
		tempDirections.add("right");
		
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/player/player_sprite_sheet", 4, tempDirections);
		attackMovement = getSpriteSheet("/player/player_sprite_sheet", 4, tempDirections);
		//defenseMovement = getSpriteSheet("/player/player_defending_sprite_sheet", 4, tempDirections);
		this.tempDirections = null;
	}
	
	public void setDefaultValues()
	{
		//by setting worldPosX and worldPosY
		//to these values, you spawn the player
		//in the center of a 50 x 50 world map
		//change the numbers as needed
		worldPosX = (gp.tileSize * ((gp.tileManager.mapTileSizes[gp.curMap][1]/2)-1)) - (gp.tileSize/2);
		worldPosY = (gp.tileSize * ((gp.tileManager.mapTileSizes[gp.curMap][0]/2)-1)) - (gp.tileSize/2);
		direction = "down";
		life = maxLife = 6;
		mana = maxMana = 4;
		ammo = maxAmmo = 10;
		defSpeed = speed = 3;
		level = 1;
		strength = 1;
		dext = 1;
		exp = 0;
		nextLvlExp = 5;
		wealth = 0;
		curWeapon = new GObjSmallSword(gp);
		curShield = new GObjWoodenShield(gp);
		curShot = new GObjMagicBall(gp);
		attack = getAttack();
		defense = getDefense();
		canMove = true;
		
		setUpInventory();
	}
	
	public void setUpInventory() 
	{
		inventoryList.clear();
		inventoryMap.clear();
		
		inventoryList.add(curWeapon);
		inventoryMap.put(curWeapon.name, 1);
		inventoryList.add(curShield);
		inventoryMap.put(curShield.name, 1);
		inventoryList.add(new GObjKey(gp));
		inventoryMap.put("Key", 0);
		
		inventoryMap.put("RedPotion", 0);
	}
	
	public void setDefPos() 
	{
		worldPosX = (gp.tileSize * ((gp.tileManager.mapTileSizes[gp.curMap][1]/2)-1)) - (gp.tileSize/2);
		worldPosY = (gp.tileSize * ((gp.tileManager.mapTileSizes[gp.curMap][0]/2)-1)) - (gp.tileSize/2);
		direction = "down";
	}
	
	public void restoreManaAndLife() 
	{
		life = maxLife = 6;
		mana = maxMana = 4;
		invincible = false;
	}
	
	public int getAttack() 
	{
		int res;
		
		attackArea = curWeapon.attackArea;
		res = strength * curWeapon.attackVal;
		
		return res;
	}
	
	public int getDefense() 
	{
		int res = dext * curShield.defenseVal;
		return res;
	}
	
	public void pickUpObjs(int index) 
	{
		if(index != -1) 
		{
			attackCancelled = true;
			
			if(gp.gObjects[gp.curMap][index].type == 8) 
			{
				gp.gObjects[gp.curMap][index].use(this);
				gp.gObjects[gp.curMap][index] = null;
			}
			else if(gp.gObjects[gp.curMap][index].type == 11) {
				if(gp.keyH.actionPressed) {
					gp.gObjects[gp.curMap][index].interact();
				}
			}
			else 
			{
				String text;
			
				if(inventoryList.size() < maxListSize) 
				{
					//if object is already known
					if(inventoryMap.containsKey(gp.gObjects[gp.curMap][index].name)) 
					{
						inventoryMap.replace(
								gp.gObjects[gp.curMap][index].name, 
								(inventoryMap.get(gp.gObjects[gp.curMap][index].name)+1)
						);
					}
					//if you found a new object
					else 
					{
						inventoryList.add(gp.gObjects[gp.curMap][index]);
						inventoryMap.put(gp.gObjects[gp.curMap][index].name, 1);
					}
					
					text = "You Obtained "+gp.gObjects[gp.curMap][index].name+".";
					gp.gObjects[gp.curMap][index] = null;
				}
				else 
				{
					text = "Your Inventory is Full.";
				}
				
				gp.ui.addMessage(text);
			}
		}
	}
	
	public void interactWithNPC(int index) 
	{
		if(gp.keyH.actionPressed) 
		{
			if(index != -1) 
			{
				attackCancelled = true;
				gp.gameState = gp.dialogState;
				gp.npcs[gp.curMap][index].speak();
			}
			
		}
	}
	
	private void contactEnemies(int index) {
		if(index != -1) 
		{
			if(invincible == false && !gp.enemies[gp.curMap][index].dying) 
			{
				int damage = gp.enemies[gp.curMap][index].attack - defense;
				if(damage < 0)
					damage = 0;
				
				life -= damage;
				invincible = true;
			}
		}
	}
	
	public void damageEnemy(Entity attacker, int index, int attack, int kbPow) 
	{
		if(index != -1) 
		{
			if(!gp.enemies[gp.curMap][index].invincible) 
			{
				if(kbPow > 0) {
					setKnockBack(gp.enemies[gp.curMap][index], attacker, kbPow);
				}
				
				int damage = attack - gp.enemies[gp.curMap][index].defense;
				
				if(damage < 0)
					damage = 0;
				
				gp.enemies[gp.curMap][index].life -= damage;
				gp.enemies[gp.curMap][index].invincible = true;
				gp.enemies[gp.curMap][index].damageReaction();
				
				if(gp.enemies[gp.curMap][index].life <= 0) 
				{
					gp.enemies[gp.curMap][index].dying = true;
					exp += gp.enemies[gp.curMap][index].exp;
					checkLevelUp();
				}
			}
		}
	}
	
	public void damageShot(int index) {
		if(index != -1) {
			Entity shot = gp.shotList[gp.curMap][index];
			shot.alive = false;
			makeParticle(shot, shot);
		}
	}
	
	/*public void damageITiles(int index) 
	{
		if(index != -1 && gp.iTiles[gp.curMap][index].removable &&
				gp.iTiles[gp.curMap][index].isRightItem(this) && 
				!gp.iTiles[gp.curMap][index].invincible) 
		{
			gp.iTiles[gp.curMap][index].life--;
			gp.iTiles[gp.curMap][index].invincible = true;
			
			makeParticle(gp.iTiles[gp.curMap][index], gp.iTiles[gp.curMap][index]);
			
			if(gp.iTiles[gp.curMap][index].life <= 0)
				gp.iTiles[gp.curMap][index] = gp.iTiles[gp.curMap][index].getRemovedForm();
		}
	}*/

	public void checkLevelUp() 
	{	
		if(exp >= nextLvlExp) 
		{
			level++;
			nextLvlExp *= 3;
			maxLife += 2;
			strength++;
			dext++;
			attack = getAttack();
			defense = getDefense();
			
			gp.gameState = gp.dialogState;
			gp.ui.currentDialog = "Level Up By 1";
		}
	}
	
	public void selectItem() 
	{
		int index = gp.ui.getItemIndex(gp.ui.playerSlotCol, gp.ui.playerSlotRow, gp.ui.maxPlayerSlotRow);
		if(index < inventoryList.size()) 
		{
			Entity selected = inventoryList.get(index);
			switch(selected.type) 
			{
			//if a weapon
			case 4:
				curWeapon = selected;
				attack = getAttack();
				//getEntityAttackImages("player", "player", curWeapon.name, 16, 32);
				break;
			//if a shield
			case 5:
				curShield = selected;
				defense = getDefense();
				break;
			//if a consumable
			case 6:
				if(inventoryMap.get(selected.name) > 0)
					if(selected.use(this)) {
						inventoryMap.replace(selected.name,(inventoryMap.get(selected.name)-1));
					}
				else 
				{
					gp.gameState = gp.dialogState;
					gp.ui.currentDialog = "You Have No "+selected.name;
				}
				break;
			case 12:
				if(curLightSrc == selected) {
					curLightSrc = null;
				}
				else {
					curLightSrc = selected;
				}
				lightSrcUpdated = true;
				break;
			}
		}
	}

	public void handleAttack()
	{
		spriteCtr++;
		attackMovement.changeFrame = true;
		
		if(spriteCtr > 5 && spriteCtr <= 25) 
		{
			int curWorldX = worldPosX, curWorldY = worldPosY;
			int solidAreaWidth = solidPart.width, solidAreaHeight = solidPart.height;
			
			//adjust player's position for the attackArea
			switch(direction) 
			{
			case "up":
				worldPosY -= attackArea.height;
				break;
			case "down":
				worldPosY += attackArea.height;
				break;
			case "left":
				worldPosX -= attackArea.width;
				break;
			case "right":
				worldPosX += attackArea.width;
				break;
			}
			solidPart.width = attackArea.width;
			solidPart.height = attackArea.height;
			
			damageEnemy(this, gp.collisionCheck.checkEntity(this, gp.enemies), attack, curWeapon.kbPower);
			int shotIndex = gp.collisionCheck.checkEntity(this, gp.shotList);
			damageShot(shotIndex);
			
			//damageITiles(gp.collisionCheck.checkEntity(this, gp.iTiles));
			
			worldPosX = curWorldX;
			worldPosY = curWorldY;
			
			solidPart.width = solidAreaWidth;
			solidPart.height = solidAreaHeight;
			
		}
		else if(spriteCtr > 25) 
		{
			spriteCtr = 0;
			attacking = false;
		}
	}
	
	public void update() 
	{
		if(attacking) 
		{
			handleAttack();
		}
		//this stops the player from doing animation while not moving around
		else if(keyH.downPressed || keyH.upPressed || keyH.rightPressed || keyH.leftPressed
		|| keyH.actionPressed) 
		{	
			//movement.changeFrame = true;
			
			if(keyH.downPressed)
				direction = "down";
			else if(keyH.upPressed)
				direction = "up";
			else if(keyH.rightPressed)
				direction = "right";
			else if(keyH.leftPressed)
				direction = "left";
			else if(keyH.upPressed && keyH.rightPressed)
				direction = "upright";
			
			collisionOn = false;
			gp.collisionCheck.checkTile(this);
			
			pickUpObjs(gp.collisionCheck.checkObj(this, true));
			interactWithNPC(gp.collisionCheck.checkEntity(this, gp.npcs));
			contactEnemies(gp.collisionCheck.checkEntity(this, gp.enemies));
			
			//gp.collisionCheck.checkEntity(this, gp.iTiles);
			
			gp.eventH.checkEvent();
			
			//After collision check
			if(!collisionOn && !keyH.actionPressed) 
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
			
			if(keyH.actionPressed && !attackCancelled) 
			{
				attacking = true;
				spriteCtr = 0;
			}
			
			attackCancelled = false;
			gp.keyH.actionPressed = false;
			
			spriteCtr++;
			if(spriteCtr > 20) 
			{
				movement.changeFrame = true;
				spriteCtr = 0;
			}
		}
		else 
		{
			isStanding = true;
			/*standCounter++;
			if(standCounter == 20) 
			{
				isStanding = true;
				standCounter = 0;
			}*/
		}
		
		if(gp.keyH.shotPressed && !curShot.alive 
			&& availableShotCtr == 30 && curShot.haveMana(this)) 
		{
			curShot.set(worldPosX, worldPosY, direction,
					true, this);
			
			curShot.reduceMana(this);
			
			//gp.shotList.add(curShot);
			for(int i = 0; i < gp.shotList[gp.curMap].length; i++) {
				if(gp.shotList[gp.curMap][i] == null) {
					gp.shotList[gp.curMap][i] = curShot;
					break;
				}
			}
			
			availableShotCtr = 0;
		}
		
		if(invincible) 
		{
			invincibleCtr++;
			if(invincibleCtr > 60) 
			{
				invincible = false;
				invincibleCtr = 0;
			}
		}
		
		if(availableShotCtr < 60) 
		{
			availableShotCtr++;
		}
		
		if(life > maxLife)
			life = maxLife;
		
		if(mana > maxMana)
			mana = maxMana;
		
		if(life <= 0) {
			gp.gameState = gp.gameOverState;
			gp.ui.commandNum = -1;
		}
	}
	
	public void draw(Graphics2D g2) 
	{
		BufferedImage image = null;
		int tempSX = screenPosX, tempSY = screenPosY;
		
		if(attacking) {
			if(direction == "left") tempSX = screenPosX - gp.tileSize;
			else if(direction == "up") tempSY = screenPosY - gp.tileSize;
			
			//change to next frame
			if(attackMovement.changeFrame) {
				image = attackMovement.nextFrame(direction);
				attackMovement.changeFrame = false;
			}
		}
		else if(isStanding) {
			image = movement.displayStanding(direction);
			isStanding = false;
		}
		else {
			
			if(movement.changeFrame) {
				image = movement.nextFrame(direction);
				movement.changeFrame = false;
			}
			else {
				image = movement.curImage;
			}
		}
		
		if(invincible)
			changeAlpha(g2, 0.3f);
		
		g2.drawImage(image, tempSX, tempSY, null);
		
		changeAlpha(g2, 1.0f);
	}
}


































