package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
	GamePanel gp;
	Tile[] tile;
	int mapTileNums[][];
	
	public TileManager(GamePanel gp, String mapname) 
	{
		this.gp = gp;
		tile = new Tile[10];
		mapTileNums = new int[gp.maxScreenRow][gp.maxScreenCol];
		loadMap(mapname);
		getTileImages();
	}
	
	public void getTileImages() 
	{
		String tileNames[] = {"grass", "water", "sky", "stone_wall", "ocean", "brick"};
		try 
		{
			for(int i = 0; i < tileNames.length; i++) 
			{
				tile[i] = new Tile();
				tile[i].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+tileNames[i]+"_tile.png"));
			}
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void loadMap(String mapname) 
	{
		try 
		{
			InputStream inputStream = getClass().getResourceAsStream("/maps/"+mapname+".txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			for(int i = 0; i < gp.maxScreenRow; i++) 
			{
				String line = reader.readLine();
				String nums[] = line.split(" ");
				
				for(int j = 0; j < gp.maxScreenCol; j++) 
				{
					mapTileNums[i][j] = Integer.parseInt(nums[j]);
				}
			}
			reader.close();
			inputStream.close();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) 
	{
		for(int i = 0; i < mapTileNums.length; i++) 
		{
			for(int j = 0; j < mapTileNums[0].length; j++) 
			{
				g2.drawImage(tile[mapTileNums[i][j]].image, (j * gp.tileSize), (i * gp.tileSize), gp.tileSize, gp.tileSize, null);
				
			}
		}
	}
}























