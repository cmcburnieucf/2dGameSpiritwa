package main;

import entity.Entity;

public class CollisionChecker {
	GamePanel gp;
	
	public CollisionChecker(GamePanel gp) 
	{
		this.gp = gp;
	}
	
	public int checkEntity(Entity entity, Entity[][] targets) 
	{
		int index = -1;
		
		for(int i = 0; i < targets[gp.curMap].length; i++) 
		{
			if(targets[gp.curMap][i] != null) 
			{
				entity.solidPart.x = entity.worldPosX + entity.solidPart.x;
				entity.solidPart.y = entity.worldPosY + entity.solidPart.y;
				
				targets[gp.curMap][i].solidPart.x = 
						targets[gp.curMap][i].worldPosX + targets[gp.curMap][i].solidPart.x;
				targets[gp.curMap][i].solidPart.y = 
						targets[gp.curMap][i].worldPosY + targets[gp.curMap][i].solidPart.y;
				
				switch(entity.direction) {
				case "up":
					entity.solidPart.y -= entity.speed;
					break;
				case "down":
					entity.solidPart.y += entity.speed;
					break;
				case "left":
					entity.solidPart.x -= entity.speed;
					break;
				case "right":
					entity.solidPart.x += entity.speed;
					break;
				}
				
				if(entity.solidPart.intersects(targets[gp.curMap][i].solidPart)
				   && targets[gp.curMap][i] != entity) 
				{
					entity.collisionOn = true;
					index = i;
				}
				
				entity.solidPart.x = entity.defX;
				entity.solidPart.y = entity.defY;
				
				targets[gp.curMap][i].solidPart.x = targets[gp.curMap][i].defX;
				targets[gp.curMap][i].solidPart.y = targets[gp.curMap][i].defY;
			}
		}
		
		return index;
	}
	
	public int checkObj(Entity entity, boolean player) 
	{
		int index = -1;
		
		for(int i = 0; i < gp.gObjects[gp.curMap].length; i++) 
		{
			if(gp.gObjects[gp.curMap][i] != null) 
			{
				entity.solidPart.x = entity.worldPosX + entity.solidPart.x;
				entity.solidPart.y = entity.worldPosY + entity.solidPart.y;
				
				gp.gObjects[gp.curMap][i].solidPart.x = 
						gp.gObjects[gp.curMap][i].worldPosX + gp.gObjects[gp.curMap][i].solidPart.x;
				gp.gObjects[gp.curMap][i].solidPart.y = 
						gp.gObjects[gp.curMap][i].worldPosY + gp.gObjects[gp.curMap][i].solidPart.y;
				
				switch(entity.direction) {
				case "up":
					entity.solidPart.y -= entity.speed;
					break;
				case "down":
					entity.solidPart.y += entity.speed;
					break;
				case "left":
					entity.solidPart.x -= entity.speed;
					break;
				case "right":
					entity.solidPart.x += entity.speed;
					break;
				}
				if(entity.solidPart.intersects(gp.gObjects[gp.curMap][i].solidPart)) 
				{
					if(gp.gObjects[gp.curMap][i].collision) 
						entity.collisionOn = true;
					if(player) 
						return i;
				}
				
				entity.solidPart.x = entity.defX;
				entity.solidPart.y = entity.defY;
				
				gp.gObjects[gp.curMap][i].solidPart.x = gp.gObjects[gp.curMap][i].defX;
				gp.gObjects[gp.curMap][i].solidPart.y = gp.gObjects[gp.curMap][i].defY;
			}
		}
		
		return index;	
	}
	
	public void checkTile(Entity entity) 
	{
		//------------------------------------------------------------------------------------------
		int entityLeftX = entity.worldPosX + entity.solidPart.x;
		int entityRightX = entity.worldPosX + entity.solidPart.x + entity.solidPart.width;
		int entityTopY = entity.worldPosY + entity.solidPart.y;
		int entityBottomY = entity.worldPosY + entity.solidPart.y + entity.solidPart.height;
		
		int entityLeftCol = (entityLeftX/gp.tileSize);
		int entityRightCol = (entityRightX/gp.tileSize);
		int entityTopRow = (entityTopY/gp.tileSize);
		int entityBottomRow = (entityBottomY/gp.tileSize);
		
		int tileNum1 = 0, tileNum2 = 0;
		
		String direction = entity.direction;
		
		if(entity.knockBack) {
			
		}
		
		switch(entity.direction) 
		{
			case "up":
				entityTopRow = (entityTopY - entity.speed)/gp.tileSize;
				if(entityTopRow >= 0) {
					tileNum1 = gp.tileManager.mapTileNums[gp.curMap][entityLeftCol][entityTopRow];
					tileNum2 = gp.tileManager.mapTileNums[gp.curMap][entityRightCol][entityTopRow];
				}
				break;
			case "down":
				entityBottomRow = (entityBottomY + entity.speed)/gp.tileSize;
				if(entityBottomRow < gp.tileManager.mapTileNums[gp.curMap][entityLeftCol].length) {
					tileNum1 = gp.tileManager.mapTileNums[gp.curMap][entityLeftCol][entityBottomRow];
					tileNum2 = gp.tileManager.mapTileNums[gp.curMap][entityRightCol][entityBottomRow];
				}
				break;
			case "left":
				entityLeftCol = (entityLeftX - entity.speed)/gp.tileSize;
				if(entityLeftCol >= 0) {
					tileNum1 = gp.tileManager.mapTileNums[gp.curMap][entityLeftCol][entityTopRow];
					tileNum2 = gp.tileManager.mapTileNums[gp.curMap][entityLeftCol][entityBottomRow];
				}
				break;
			case "right":
				entityRightCol = (entityRightX + entity.speed)/gp.tileSize;
				if(entityRightCol < gp.tileManager.mapTileNums[gp.curMap].length) {
					tileNum1 = gp.tileManager.mapTileNums[gp.curMap][entityRightCol][entityTopRow];
					tileNum2 = gp.tileManager.mapTileNums[gp.curMap][entityRightCol][entityBottomRow];
				}
				break;
		}
		if(gp.tileManager.tile[tileNum1].collision || 
		   gp.tileManager.tile[tileNum2].collision) 
		{
			entity.collisionOn = true;
		}
		
	}
	
	public boolean checkPlayer(Entity entity) 
	{
		boolean contactPlayer = false;
		entity.solidPart.x = entity.worldPosX + entity.solidPart.x;
		entity.solidPart.y = entity.worldPosY + entity.solidPart.y;
				
		gp.player.solidPart.x = gp.player.worldPosX + gp.player.solidPart.x;
		gp.player.solidPart.y = gp.player.worldPosY + gp.player.solidPart.y;
				
		switch(entity.direction) {
		case "up":
			entity.solidPart.y -= entity.speed;
			break;
		case "down":
			entity.solidPart.y += entity.speed;
			break;
		case "left":
			entity.solidPart.x -= entity.speed;
			break;
		case "right":
			entity.solidPart.x += entity.speed;
			break;
		}
		
		if(entity.solidPart.intersects(gp.player.solidPart)) 
		{
			entity.collisionOn = true;
			contactPlayer = true;
		}
				
		entity.solidPart.x = entity.defX;
		entity.solidPart.y = entity.defY;
				
		gp.player.solidPart.x = gp.player.defX;
		gp.player.solidPart.y = gp.player.defY;
		
		return contactPlayer;
	}
	
}
