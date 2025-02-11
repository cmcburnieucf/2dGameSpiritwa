package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjTent extends Entity{

	boolean used = false;
	
	public GObjTent(GamePanel gp) {
		super(gp);
		name = "Tent";
		type = 6;
		description = "A tent to sleep in";
		price = 100;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
	}
	public boolean use(Entity entity) {
		if(!used) {
			gp.gameState = gp.sleepState;
			gp.player.life += 2;
			gp.player.mana += 1;
			used = true;
		}
		return false;
		
	}
}
