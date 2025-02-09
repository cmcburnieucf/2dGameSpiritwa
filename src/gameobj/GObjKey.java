package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjKey extends Entity{
	
	public GObjKey(GamePanel gp) 
	{
		super(gp);
		name = "Key";
		description = "Key to Open Locked Doors";
		type = 6;
		price = 75;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
	}
	
	public boolean use(Entity entity) {
		gp.gameState = gp.dialogState;
		int objIndex = getDetected(entity, gp.gObjects, "Door");
		if(objIndex != -1) {
			gp.ui.currentDialog = "Used The "+name+" For The Door";
			gp.gObjects[gp.curMap][objIndex] = null;
		}
		else {
			gp.ui.currentDialog = "Can't Use Key! Nothing There";
			return false;
		}
		
		return true;
	}
}
