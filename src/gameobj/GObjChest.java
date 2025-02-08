package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjChest extends Entity{
	
	public GObjChest(GamePanel gp) {
		super(gp);
		name = "Chest";
		type = 3;
		
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
	}
}
