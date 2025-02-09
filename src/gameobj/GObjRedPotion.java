package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjRedPotion extends Entity{
	
	public GObjRedPotion(GamePanel gp) {
		super(gp);
		name = "RedPotion";
		description = "Red Potion to Heal You";
		val = 5;
		type = 6;
		price = 30;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;	
	}
	
	public boolean use(Entity entity) 
	{
		gp.gameState = gp.dialogState;
		if(entity.life == entity.maxLife) {
			gp.ui.currentDialog = "Your Life is Full.";
			return false;
		}
		else {
			gp.ui.currentDialog = "You Drink the "+name+". "
				+ "Life has been recovered by "+val+" health points.";
			entity.life += val;
		}
		return true;
	}

}
