package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjSmallSword extends Entity{

	public GObjSmallSword(GamePanel gp) {
		super(gp);
		name = "SmallSword";
		description = "Small Sword to use a weapon";
		type = 4;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;	
		attackVal = 1;
		
		attackArea.width = 36;
		attackArea.height = 36;
	}

}
