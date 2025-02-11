package npc;

import entity.Entity;
import main.GamePanel;

public class NPCOldman extends Entity{
	public NPCOldman(GamePanel gp) 
	{
		super(gp);
		
		name = "OldMan";
		direction = "down";
		speed = 2;
		type = 1;
		

		tempDirections.add("down");
		tempDirections.add("up");
		tempDirections.add("left");
		tempDirections.add("right");
		
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/npcs/oldman_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
		canMove = true;
		
		setDialog();
	}
	
	public void setAction() 
	{
		if(onPath) {
			int goalRow = 0, goalCol = 0;
			//dest coordinates for given path
			//goalRow = 12; goalCol=9;
			//dest coordinates for following player
			goalRow = (gp.player.worldPosY+gp.player.solidPart.y)/gp.tileSize;
			goalCol = (gp.player.worldPosX+gp.player.solidPart.x)/gp.tileSize;
			
			followGivenPath(goalRow, goalCol);
			
		}else {
			super.setAction();
		}
	}
	
	public void setDialog() 
	{
		dialogue[0] = "";
		dialogue[1] = ".";
		
	}
	
	public void speak() 
	{
		super.speak();
		onPath = true;
	}
	
	
}
