package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjLongSword extends Entity{

	public GObjLongSword(GamePanel gp) {
		super(gp);
		name = "LongSword";
		description = "Long Sword to use a weapon";
		type = 4;
		price = 80;
		kbPower = 4;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
		attackVal = 5;
		
		attackArea.width = 72;
		attackArea.height = 72;
	}

}
