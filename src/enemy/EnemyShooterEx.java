package enemy;

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
			//time to stop following/chasing
			checkPursuit(gp.player, 15, 100, true);
			
			//dest coordinates for given path
			//goalRow = 12; goalCol=9;
			//dest coordinates for following player
			followGivenPath(getGoalRow(gp.player), getGoalCol(gp.player));
			checkShot(30, 200);
		}
		else {
			//time to start chasing/following
			checkPursuit(gp.player, 15, 100, false);
			//tutorial changed this to "getRandomDirection()".
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
	
}






























