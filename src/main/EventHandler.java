package main;

public class EventHandler {
	GamePanel gp;
	EventRect eventRects[][][];
	
	int prevEventX, prevEventY;
	boolean eventEnabled = true;
	int tempMap, tempCol, tempRow;
	
	public EventHandler(GamePanel gp) {
		
		this.gp = gp;
		eventRects = new EventRect[gp.tileManager.mapTileNums.length][][];
		
		for(int m = 0; m < eventRects.length; m++) {
			eventRects[m] = getGrid(m);
		}
		
		for(int m = 0; m < eventRects.length; m++) {
			for(int i = 0; i < eventRects[m].length; i++) {
				for(int j = 0; j < eventRects[m][i].length; j++) 
				{
					eventRects[m][i][j] = new EventRect(23, 23, 2, 2);
					eventRects[m][i][j].defX = eventRects[m][i][j].x;
					eventRects[m][i][j].defY = eventRects[m][i][j].y;
				}
			}
		}
	}
	
	public EventRect[][] getGrid(int mapIndex){
		return (new EventRect[gp.tileManager.mapTileSizes[mapIndex][0]][gp.tileManager.mapTileSizes[mapIndex][1]]);
	}
	
	public void checkEvent() {
		
		int distanceX = Math.abs(gp.player.worldPosX - prevEventX);
		int distanceY = Math.abs(gp.player.worldPosY - prevEventY);
		int distance = Math.max(distanceX, distanceY);
		
		if(distance > gp.tileSize)
			eventEnabled = true;
		
		if(eventEnabled) {
			//if(hit(0, 27, 16, "right")) pitFall(gp.dialogState);
			//else if(hit (0, 10, 20, "up")) healingEvent(gp.dialogState);
			//else if(hit(0, 10, 39, "up")) enterNewArea(1, 12, 13);
			//else if(hit(1, 12, 13, "down")) enterNewArea(0, 10, 39);
		}
	}

	public boolean hit(int map, int col, int row, String reqDirection) {
		
		boolean hit = false;
		
		if(gp.curMap == map && eventRects[map][row][col] != null) {
			
			gp.player.solidPart.x = gp.player.worldPosX + gp.player.solidPart.x;
			gp.player.solidPart.y = gp.player.worldPosY + gp.player.solidPart.y;
			
			eventRects[map][row][col].x = col*gp.tileSize + eventRects[map][row][col].x;
			eventRects[map][row][col].y = row*gp.tileSize + eventRects[map][row][col].y;
			
			if(gp.player.solidPart.intersects(eventRects[map][row][col])
				&& !eventRects[map][row][col].eventDone) {
				
				if(gp.player.direction.contentEquals(reqDirection) ||
				reqDirection.contentEquals("any")) {
					
					hit = true;
					prevEventX = gp.player.worldPosX;
					prevEventY = gp.player.worldPosY;
				}
			}
			
			gp.player.solidPart.x = gp.player.defX;
			gp.player.solidPart.y = gp.player.defY;
			
			eventRects[map][row][col].x = eventRects[map][row][col].defX;
			eventRects[map][row][col].y = eventRects[map][row][col].defY;
		}
		
		return hit;
	}
	
	//Different Types of Events Below
	
	public void pitFall(int gameState) {
		gp.gameState = gameState;
		gp.ui.currentDialog = "Pit Fall Trap Successful";
		gp.player.life -= 1;
		//eventRects[col][row].eventDone = true;
		eventEnabled = false;
	}
	
	public void healingEvent(int gameState) {
		
		if(gp.keyH.actionPressed) {
			
			gp.gameState = gameState;
			gp.player.attackCancelled = true;
			gp.ui.currentDialog = "You Recovered 1 Heart";
			
			if(gp.player.life + 2 > gp.player.maxLife)
				gp.player.life = gp.player.maxLife;
			else
				gp.player.life += 2;
			
			if(gp.player.mana + 2 > gp.player.maxMana)
				gp.player.mana = gp.player.maxMana;
			else
				gp.player.mana += 2;
			
			gp.aSetter.setEnemy();
		}
	}
	
	public void enterNewArea(int map, int col, int row) {
		
		gp.gameState = gp.transitionState;
		
		tempMap = map;
		tempCol = col;
		tempRow = row;
		
		eventEnabled = false;
	}
}
































