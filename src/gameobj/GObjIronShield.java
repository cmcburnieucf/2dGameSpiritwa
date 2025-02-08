package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjIronShield extends Entity{

	public GObjIronShield(GamePanel gp) {
		super(gp);
		name = "IronShield";
		description = "Big Iron Shield";
		type = 5;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
		defenseVal = 7;
	}

}
