package enemy;

import entity.Entity;
import main.GamePanel;

public class EnemyStrongEx extends Entity{

	public EnemyStrongEx(GamePanel gp) {
		super(gp);
		
		name = "Strong Example";
		type = 2;
		speed = 1;
		maxLife = life = 10;
		attack = 5;
		defense = 1;
		exp = 20; //how much exp you can get by defeating enemy
		
		//if needed
		defX = solidPart.x = 2;
		defY = solidPart.y = 4;
		solidPart.width = 28;
		solidPart.height = 28;
		
		attackArea.width = 48;
		attackArea.height = 48;

		tempDirections.add("down");
		tempDirections.add("up");
		tempDirections.add("left");
		tempDirections.add("right");
		
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/enemies/strongex_sprite_sheet", 1, tempDirections);
		attackMovement = getSpriteSheet("/enemies/strongex_sprite_sheet", 1, tempDirections);
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


