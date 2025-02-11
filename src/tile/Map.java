package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Map extends TileManager{

	BufferedImage worldMap[];
	public boolean miniMapOn = false;
	public Map(GamePanel gp) {
		super(gp);
		makeWorldMap();
	}
	public void makeWorldMap() {
		worldMap = new BufferedImage[gp.maxMap];
		int mapWidth, mapHeight;
		
		for(int m = 0; m < gp.maxMap; m++) {
			mapWidth = gp.tileSize*mapTileSizes[m][1]; 
			mapHeight = gp.tileSize*mapTileSizes[m][0];
			
			worldMap[m] = new BufferedImage(mapWidth, mapHeight,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) worldMap[m].createGraphics();
			
			for(int i = 0; i < mapHeight; i++) {
				for(int j = 0; j < mapWidth; j++) {
					int tileNum = mapTileNums[m][i][j];
					int x = gp.tileSize*j, y = gp.tileSize*i;
					g2.drawImage(tile[tileNum].image, x, y, null);
				}
			}
			g2.dispose();
		}
	}
	
	
	//TO DO: implement zoom in and zoom out in full screen map
	
	public void drawFullMapScreen(Graphics2D g2) {
		//screen background
		g2.setColor(Color.black);
		g2.fillRect(0,0,gp.screenWidth, gp.screenHeight);
		
		//actual map
		int width = 500, height = 500, 
				x = ((gp.screenWidth/2) - (width/2)), 
				y = ((gp.screenHeight/2) - (height/2));
		g2.drawImage(worldMap[gp.curMap], x, y, width, height, null);
		
		//draw the player
		double scale = (double) 
				gp.tileSize*gp.tileSize*mapTileSizes[gp.curMap][1]/width;
		int posX = (int)(x+gp.player.worldPosX/scale),
				posY = (int)(y+gp.player.worldPosY/scale),
				size = (int) (gp.tileSize/scale);
		
		g2.drawImage(gp.player.movement.displayStanding("down"), 
				posX, posY, size, size, null);
	}
	
	public void drawMiniMap(Graphics2D g2) {
		if(miniMapOn) {
			int width = 200, height = 200, 
				x = gp.screenWidth - width - 50, 
				y = gp.tileSize;
			
			g2.drawImage(worldMap[gp.curMap], x, y, width, height, null);
			
			//draw player
			double scale = (double) 
					gp.tileSize*gp.tileSize*mapTileSizes[gp.curMap][1]/width;
			int posX = (int)(x+gp.player.worldPosX/scale),
					posY = (int)(y+gp.player.worldPosY/scale),
					size = (int) (gp.tileSize/scale);
			
			g2.drawImage(gp.player.movement.displayStanding("down"), 
					posX, posY, size, size, null);
		}
	}
}
