package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjMagic extends Entity{
	
	public GObjMagic(GamePanel gp) {
		super(gp);
		name = "Magic";
		type = 8;
		val = 1;
		price = 100;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
	}
	
	public boolean use(Entity entity) 
	{
		gp.ui.addMessage("Magic+"+val);
		entity.mana += val;
		
		return true;
	}
}
