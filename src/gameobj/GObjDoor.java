package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjDoor extends Entity{
	
	public GObjDoor(GamePanel gp) {
		super(gp);
		name = "Door";
		type = 11;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
		collision = true;
	}
	
	public void interact() {
		gp.gameState = gp.dialogState;
		gp.ui.currentDialog = "Need A Key";
	}
}
