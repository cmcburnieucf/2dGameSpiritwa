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
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;	
	}
	
	public void use(Entity entity) 
	{
		gp.gameState = gp.dialogState;
		if(entity.life == entity.maxLife)
			gp.ui.currentDialog = "Your Life is Full.";
		else
			gp.ui.currentDialog = "You Drink the "+name+". "
				+ "Life has been recovered by "+val+" health points.";
		entity.life += val;
	}

}
