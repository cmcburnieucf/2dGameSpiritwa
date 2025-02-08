package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjCoin extends Entity{
	
	public GObjCoin(GamePanel gp) {
		super(gp);
		name = "Coin";
		val = 1;
		description = "Coin that can be used as Money";
		type = 8;
		
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
	}
	
	public void use(Entity entity) 
	{
		gp.ui.addMessage("Coin+"+val);
		gp.player.wealth += val;
	}

}
