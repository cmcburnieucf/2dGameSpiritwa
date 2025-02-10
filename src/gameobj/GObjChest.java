package gameobj;

import entity.Entity;
import main.GamePanel;

public class GObjChest extends Entity{
	
	Entity loot;
	boolean opened = false;
	
	public GObjChest(GamePanel gp, Entity loot) {
		super(gp);
		name = "Chest";
		this.loot = loot;
		type = 11;
		collision = true;
		solidPart.x = 4;
		solidPart.y = 16;
		defX = solidPart.width = gp.tileSize;
		defY = solidPart.height = gp.tileSize/2;
		tempDirections.add("down");
		//can change filepath if needed based certain conditions
		movement = getSpriteSheet("/gobjects/"+name+"_sprite_sheet", 1, tempDirections);
		//set another frame for the opened chest
		this.tempDirections = null;
	}
	
	public void interact() {
		gp.gameState = gp.dialogState;
		if(!opened) {
			StringBuilder sb = new StringBuilder();
			sb.append("Opened Chest and Find "+loot.name+".");
			if(gp.player.inventoryList.size() >= gp.player.maxListSize) {
				sb.append("Unfortunately, you cannot carry more stuff.");
			}
			else {
				sb.append("You Obtained "+loot.name+".");
				//if object is already known
				if(inventoryMap.containsKey(loot.name)) 
				{
					inventoryMap.replace(loot.name, (inventoryMap.get(loot.name)+1));
				}
				//if you found a new object
				else 
				{
					inventoryList.add(loot);
					inventoryMap.put(loot.name, 1);
				}
				//TO DO: change frame
				opened = true;
			}
			gp.ui.currentDialog = sb.toString();
		}
		else {
			gp.ui.currentDialog = "Chest Already Opened";
		}
	}
}
