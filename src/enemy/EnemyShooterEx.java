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
		attack = 1;
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
	}
	
	public void setAction() 
	{
		super.setAction();
		int i = new Random().nextInt(100) + 1;
		
		if(i > 99 && !curShot.alive && availableShotCtr == 30) 
		{
			curShot.set(worldPosX, worldPosY, direction, true, this);
			gp.shotList.add(curShot);
			availableShotCtr = 0;
		}
	}
	
	public void damageReaction() 
	{
		actionCtr = 0;
		direction = gp.player.direction;
	}

}
