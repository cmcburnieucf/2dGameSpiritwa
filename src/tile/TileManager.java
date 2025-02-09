package tile;

import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.GUtil;

public class TileManager {
	GamePanel gp;
	public Tile[] tile;
	// 1st [] and 2nd [] stores actual map, and
	// 3rd [] is an array that stores all the maps
	public int mapTileNums[][][];
	//mapTileSizes[1]{9, 9}
	public int mapTileSizes[][];
	
	public TileManager(GamePanel gp) 
	{
		this.gp = gp;
		String[] temp;
		
		temp = getNames("/tiles/tileInfo");
		tile = new Tile[temp.length];
		getTileImages(temp);
		
		temp = getNames("/maps/mapInfo");
		//testmap 8 8
		
		mapTileNums = new int[temp.length][][];
		mapTileSizes = new int[temp.length][];
		
		String[] temp2;
		for(int i = 0; i < temp.length; i++) {
			temp2 = temp[i].split(" "); //splitting the info obtained from mapInfo.txt
			
			mapTileSizes[i] = getMapSize(temp2[1], temp2[2]);
			mapTileNums[i] = loadMap(temp2[0], i, mapTileSizes[i][0], mapTileSizes[i][1]);
		}
		
		temp = null;
	}
	
	public int[] getMapSize(String rowSize, String colSize){
		int[] res = new int[2];
		res[0] = Integer.parseInt(rowSize);
		res[1] = Integer.parseInt(colSize);
		return res;
	}
	
	public int[][] loadMap(String filename, int mapIndex, int maxRow, int maxCol) 
	{
		InputStream inputStream = null;
		BufferedReader reader = null;
		String line;
		String[] nums;
		int[][] res = new int[maxRow][maxCol];;
		
		try {
			
			inputStream = getClass().getResourceAsStream("/maps/"+filename+".txt");
			reader = new BufferedReader(new InputStreamReader(inputStream));
			
			//get the type of image the tile will be 
			//and build the grid map
			for(int i = 0; i < res.length; i++) {
				line = reader.readLine();
				nums = line.split(" ");
				for(int j = 0; j < res[0].length; j++) {
					//if the map column exist in the file you are reading from
					res[i][j] = Integer.parseInt(nums[j]);
				}
			}
			
			reader.close();
			inputStream.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public void getTileImages(String[] tileNames) 
	{
		String[] tempArr = null;
		
		try 
		{	
			for(int i = 0; i < tileNames.length; i++) 
			{
				tempArr = tileNames[i].split(" ");
				tile[i] = new Tile();
				tile[i].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+tempArr[0]+"_tile.png"));
				tile[i].collision = Boolean.parseBoolean(tempArr[1]);
				
				tile[i].image = GUtil.scaleImage(tile[i].image, gp.tileSize, gp.tileSize);
			}
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) 
	{
		for(int i = 0; i < mapTileNums[gp.curMap].length; i++) 
		{
			for(int j = 0; j < mapTileNums[gp.curMap][i].length; j++) 
			{
				int worldX = (int)Math.round(j * gp.tileSize);
				int worldY = (int)Math.round(i * gp.tileSize);
				
				int screenX = (int)Math.round(worldX - gp.player.worldPosX + gp.player.screenPosX);
				int screenY = (int)Math.round(worldY - gp.player.worldPosY + gp.player.screenPosY);
				
				if(((worldX + gp.tileSize) > (gp.player.worldPosX - gp.player.screenPosX)) && 
					((worldX - gp.tileSize) < gp.player.worldPosX + gp.player.screenPosX) &&
					((worldY + gp.tileSize) > (gp.player.worldPosY - gp.player.screenPosY)) && 
					((worldY - gp.tileSize) < gp.player.worldPosY + gp.player.screenPosY))
				{
					g2.drawImage(tile[(int) mapTileNums[gp.curMap][i][j]].image, screenX, screenY, null);
				}
				
				
			}
		}
	}
	
	public String[] getNames(String filepath) 
	{
		ArrayList<String> res = new ArrayList<>();
		InputStream inputStream = null;
		BufferedReader reader = null;
		String line = null;
		
		try 
		{
			inputStream = getClass().getResourceAsStream(filepath+".txt");
			reader = new BufferedReader(new InputStreamReader(inputStream));
			
			while((line = reader.readLine()) != null) 
			{
				res.add(line);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		line = null;
		reader = null;
		inputStream = null;
		return res.toArray(new String[res.size()]);
	}
	
}























