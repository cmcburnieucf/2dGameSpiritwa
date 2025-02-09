package enemy;

import java.util.Random;

import entity.Entity;
import gameobj.GObjPebble;
import main.GamePanel;

public class EnemyShooterEx extends Entity{

	public EnemyShooterEx(GamePanel gp) {
		super(gp);
		
		name = "ExampleShooter";
		type = 2;
		speed = 1;
		maxLife = life = 4;
		attack = 2;
		defense = 0;
		exp = 12; //how much exp you can get by defeating enemy
		curShot = new GObjPebble(gp);
		
		//if needed
		defX = solidPart.x = 2;
		defY = solidPart.y = 4;
		solidPart.width = 28;
		solidPart.height = 28;

		tempDirections.add("down");
		tempDirections.add("up");
		tempDirections.add("left");
		tempDirections.add("right");
		
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/enemies/exshooter_sprite_sheet", 1, tempDirections);
		attackMovement = getSpriteSheet("/enemies/exshooter_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
		canMove = true;
	}
	
	public void setAction() 
	{
		if(onPath) {
			int goalRow = 0, goalCol = 0;
			//dest coordinates for given path
			//goalRow = 12; goalCol=9;
			//dest coordinates for following player
			goalRow = (gp.player.worldPosY+gp.player.solidPart.y)/gp.tileSize;
			goalCol = (gp.player.worldPosX+gp.player.solidPart.x)/gp.tileSize;
			
			followGivenPath(goalRow, goalCol);
			
			int i = new Random().nextInt(100) + 1;
			
			if(i > 99 && !curShot.alive && availableShotCtr == 30) 
			{
				curShot.set(worldPosX, worldPosY, direction, true, this);
				//gp.shotList.add(curShot);
				for(int j = 0; j < gp.shotList[gp.curMap].length; j++) {
					if(gp.shotList[gp.curMap][j] == null) {
						gp.shotList[gp.curMap][j] = curShot;
						break;
					}
				}
				availableShotCtr = 0;
			}
			
		}else {
			super.setAction();
		}
		
	}
	
	public void damageReaction() 
	{
		actionCtr = 0;
		//if enemy is fierce/unreasonable/angry
		onPath = true;
		//if enemy is a coward, it will move away
		//direction = gp.player.direction;
	}
	
	
	public void update() {
		super.update();
		
		int xDist = Math.abs(worldPosX - gp.player.worldPosX);
		int yDist = Math.abs(worldPosY - gp.player.worldPosY);
		int totDist = (xDist + yDist)/gp.tileSize;
		if(!onPath && totDist < 5) {
			int i = new Random().nextInt(100)+1;
			if(i > 50) {
				onPath = true;
			}
		}
		if(onPath && totDist > 20) {
			onPath = false;
		}
	}
}






























