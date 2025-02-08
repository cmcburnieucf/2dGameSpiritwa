package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjHeart extends Entity{
	
	
	public GObjHeart( GamePanel gp) {
		super(gp);
		name = "Heart";
		type = 8;
		val = 2;
		
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
	}
	
	public void use(Entity entity) 
	{
		gp.ui.addMessage("Life+"+val);
		gp.player.life += val;
	}
}
