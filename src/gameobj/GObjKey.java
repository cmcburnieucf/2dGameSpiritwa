package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjKey extends Entity{
	
	public GObjKey(GamePanel gp) 
	{
		super(gp);
		name = "Key";
		description = "Key to Open Locked Doors";
		type = 3;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
	}
}
