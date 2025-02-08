package itile;

import java.awt.Rectangle;

import main.GamePanel;

public class ITileTrunk extends ITile{

	public ITileTrunk(GamePanel gp, int x, int y) {
		super(gp);
		
		name = "Trunk";
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/i_tiles/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;	
		solidPart = new Rectangle(0, 0, 0, 0);
		defX = solidPart.x;
		defY = solidPart.y;
		
		worldPosX = x;
		worldPosY = y;
	}

}
