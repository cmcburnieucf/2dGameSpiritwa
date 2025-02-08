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
		//tempDirections.add("up");
		//tempDirections.add("left");
		//tempDirections.add("right");
		
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/npcs/oldman_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
		
		setDialog();
	}
	
	public void setAction() 
	{
		super.setAction();
	}
	
	public void setDialog() 
	{
		dialogue[0] = "";
		dialogue[1] = ".";
		
	}
	
	public void speak() 
	{
		super.speak();
	}
	
	
}
