package itile;

import java.awt.Color;

import entity.Entity;
import main.GamePanel;

public class ITileDryTree extends ITile{

	public ITileDryTree(GamePanel gp) {
		super(gp);
		
		name = "drytree";
		life = 3;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/i_tiles/"+name+"_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;	
		removable = true;
	}
	
	public ITile getRemovedForm() 
	{
		return new ITileTrunk(gp, worldPosX/gp.tileSize, worldPosY/gp.tileSize);
	}
	
	public boolean isRightItem(Entity entity) 
	{
		if(entity.curWeapon.type == 9) 
		{
			return true;
		}
		return false;
	}
	
	public Color getPColor() { return Color.black; }
	
	public int getPSize() { return 6; }
	
	public int getPSpeed() { return 1; }
	
	//how long the particle will displayed
	public int getPMaxLife() { return 20; }
	
}



































