package npc;

import entity.Entity;
import gameobj.GObjIronShield;
import gameobj.GObjLongSword;
import gameobj.GObjRedPotion;
import gameobj.GObjSmallSword;
import gameobj.GObjWoodenShield;
import main.GamePanel;

public class NPCSeller extends Entity{

	public NPCSeller(GamePanel gp) {
		super(gp);
		name = "Seller";
		direction = "down";
		speed = 2;
		type = 1;
		
		tempDirections.add("down");
		//tempDirections.add("up");
		//tempDirections.add("left");
		//tempDirections.add("right");
		
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/npcs/seller_sprite_sheet", 1, tempDirections);
		this.tempDirections = null;
		
		setDialog();
		setUpInventory();
		
		
	}
	
	public void setAction() 
	{
		
	}
	
	public void setDialog() 
	{
		dialogue[0] = "You Want To Buy?";
		dialogue[1] = "I Might Have Some Things For You.";
		
	}
	
	public void speak() 
	{
		super.speak();
		gp.gameState = gp.sellState;
		gp.ui.seller = this;
	}
	
	public void setUpInventory() 
	{
		
		inventoryList.add(new GObjRedPotion(gp));
		inventoryMap.put("RedPotion", 10);
		inventoryList.add(new GObjLongSword(gp));
		inventoryMap.put("LongSword", 1);
		inventoryList.add(new GObjSmallSword(gp));
		inventoryMap.put("SmallSword", 2);
		inventoryList.add(new GObjWoodenShield(gp));
		inventoryMap.put("WoodenShield", 5);
		inventoryList.add(new GObjIronShield(gp));
		inventoryMap.put("IronShield", 1);
	}
}




















































