package gameobj;

import java.awt.Color;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class GObjPebble extends Projectile{

	public GObjPebble(GamePanel gp) {
		super(gp);
		name = "Pebble";
		speed = 8;
		maxLife = life = 50;
		description = "throwable pebble";
		type = 7;
		attackVal = 2;
		useCost = 10;
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
		if(user.ammo >= useCost)
			return true;
		
		return false;
	}
	
	public void reduceMana(Entity user) 
	{
		user.ammo -= useCost;
	}
	
	public Color getPColor() { return Color.black; }
	
	public int getPSize() { return 2; }
	
	public int getPSpeed() { return 1; }
	
	//how long the particle will displayed
	public int getPMaxLife() { return 15; }
}
