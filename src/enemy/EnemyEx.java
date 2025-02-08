package enemy;

import java.util.Random;

import entity.Entity;
import gameobj.GObjCoin;
import gameobj.GObjHeart;
import gameobj.GObjMagic;
import main.GamePanel;

public class EnemyEx extends Entity{

	public EnemyEx(GamePanel gp) {
		super(gp);
		
		name = "Ex";
		type = 2;
		speed = 1;
		maxLife = life = 4;
		attack = 1;
		defense = 0;
		exp = 12; //how much exp you can get by defeating enemy
		
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
		movement = getSpriteSheet("/enemies/ex_sprite_sheet", 1, tempDirections);
		attackMovement = getSpriteSheet("/enemies/ex_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
		
	}
	
	public void setAction() 
	{
		super.setAction();
	}
	
	public void damageReaction() 
	{
		actionCtr = 0;
		direction = gp.player.direction;
	}
	
	public void checkDrop() 
	{
		int i = new Random().nextInt(100)+1;
		
		if(i <= 25) 
		{
			dropItem(new GObjHeart(gp));
		}
		else if(i > 25 && i <= 50) 
		{
			dropItem(new GObjMagic(gp));
		}
		else if(i > 50 && i <= 75) 
		{
			dropItem(new GObjCoin(gp));
		}
		else if(i > 75 && i <= 100) 
		{
			dropItem(null);
		}
	}
	
}
