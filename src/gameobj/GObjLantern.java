package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjLantern extends Entity{

	public GObjLantern(GamePanel gp) {
		super(gp);
		
		name = "Lantern";
		type = 12;
		price = 30;
		lightRadius = 250;
		description = "A Lantern";
		
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
	}

}
