package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjWoodenShield extends Entity{

	public GObjWoodenShield(GamePanel gp) {
		super(gp);
		name = "WoodenShield";
		description = "Small Wooden Shield";
		type = 5;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;	
		defenseVal = 1;
	}

}
