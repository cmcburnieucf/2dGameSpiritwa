package gameobj;

import java.awt.Color;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class GObjMagicBall extends Projectile{

	public GObjMagicBall(GamePanel gp) {
		super(gp);
		name = "MagicBall";
		speed = 7;
		maxLife = life = 80;
		description = "Some Magic Ball to throw";
		type = 7;
		attackVal = 2;
		useCost = 1;
		kbPower = 5;
		alive = false;
		tempDirections.add("down");
		tempDirections.add("up");
		tempDirections.add("left");
		tempDirections.add("right");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;	
	}
	
	public boolean haveMana(Entity user) 
	{
		if(user.mana >= useCost)
			return true;
		
		return false;
	}
	
	public void reduceMana(Entity user) 
	{
		user.mana -= useCost;
	}
	
	public Color getPColor() { return Color.orange; }
	
	public int getPSize() { return 10; }
	
	public int getPSpeed() { return 2; }
	
	//how long the particle will displayed
	public int getPMaxLife() { return 10; }
}
