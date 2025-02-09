package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import enemy.EnemyEx;
import enemy.EnemyShooterEx;
import entity.Entity;
import gameobj.GObjChest;
import gameobj.GObjCoin;
import gameobj.GObjDoor;
import gameobj.GObjHeart;
import gameobj.GObjIronShield;
import gameobj.GObjKey;
import gameobj.GObjLongSword;
import gameobj.GObjMagic;
import gameobj.GObjMagicBall;
import gameobj.GObjPebble;
import gameobj.GObjRedPotion;
import gameobj.GObjSmallSword;
import gameobj.GObjWoodenShield;
import itile.ITile;
//import gameobj.GObjKey;
import npc.NPCOldman;
import npc.NPCSeller;

public class AssetSetter {
	GamePanel gp;
	int mapNum = 0;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObj() {
		gp.gObjects[mapNum] = obtainEntities("/gobjects/objectInfo.txt");
	}
	
	public void setNPC() {
		gp.npcs[mapNum] = obtainEntities("/npcs/npcInfo.txt");
	}
	
	public void setEnemy() {
		gp.enemies[mapNum] = obtainEntities("/enemies/enemyInfo.txt");
	}
	
	public void setITiles() {
		//gp.iTiles[mapNum] = obtainITiles("i_tiles/i_tileInfo.txt");
	}
	
	
	public Entity[] obtainEntities(String filepath) {
		ArrayList<Entity> res = new ArrayList<>();
		InputStream inputStream = null;
		BufferedReader reader = null;
		String line = null;
		Entity temp;
		String[] tempArr = null;
		try 
		{
			inputStream = getClass().getResourceAsStream(filepath);
			if (inputStream == null) {
			    throw new RuntimeException("File not found: " + filepath);
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));
			int i = 0;
			while((line = reader.readLine()) != null) 
			{
				tempArr = line.split(" ");
				temp = obtainEntityHelper(tempArr[0]);
				if(temp == null) {
					throw new RuntimeException("Entity Type Not Found: " + temp);
				}
				res.add(temp);
				res.get(i).worldPosX = Integer.parseInt(tempArr[1])*gp.tileSize;
				res.get(i).worldPosY = Integer.parseInt(tempArr[2])*gp.tileSize;
				i++;
			}
			
			reader.close();
			inputStream.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return res.toArray(new Entity[res.size()]);
	}
	
	public ITile[] obtainITiles(String filepath) {
		ArrayList<ITile> res = new ArrayList<>();
		InputStream inputStream = null;
		BufferedReader reader = null;
		String line = null;
		ITile temp;
		String[] tempArr = null;
		try 
		{
			inputStream = getClass().getResourceAsStream(filepath);
			if (inputStream == null) {
			    throw new RuntimeException("File not found: " + filepath);
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));
			int i = 0;
			while((line = reader.readLine()) != null) 
			{
				tempArr = line.split(" ");
				temp = obtainITileHelper(tempArr[0]);
				if(temp == null) {
					throw new RuntimeException("ITile Type Not Found: " + temp);
				}
				res.add(temp);
				res.get(i).worldPosX = Integer.parseInt(tempArr[1])*gp.tileSize;
				res.get(i).worldPosY = Integer.parseInt(tempArr[2])*gp.tileSize;
				i++;
			}
			
			reader.close();
			inputStream.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return res.toArray(new ITile[res.size()]);
	}
	
	public ITile obtainITileHelper(String entityType) {
		return null;
	}
	
	public Entity obtainEntityHelper(String entityType) {
		Entity res = null;
		
		switch(entityType) 
		{
		// NON-PLAYABLE CHARACTERS --------------------------------------------
		case "OldMan":
			res = new NPCOldman(gp);
			break;
		case "Seller":
			res = new NPCSeller(gp);
			break;
		// MONSTERS -----------------------------------------------------------
		case "Ex":
			res = new EnemyEx(gp);
			break;
		case "ExShooter":
			res = new EnemyShooterEx(gp);
			break;
		// GAME OBJECTS -------------------------------------------------------
		case "Chest":
			res = new GObjChest(gp);
			break;
		case "Coin":
			res = new GObjCoin(gp);
			break;
		case "Door":
			res = new GObjDoor(gp);
			break;
		case "Heart":
			res = new GObjHeart(gp);
			break;
		case "IronShield":
			res = new GObjIronShield(gp);
			break;
		case "Key":
			res = new GObjKey(gp);
			break;
		case "LongSword":
			res = new GObjLongSword(gp);
			break;
		case "Magic":
			res = new GObjMagic(gp);
			break;
		case "MagicBall":
			res = new GObjMagicBall(gp);
			break;
		case "Pebble":
			res = new GObjPebble(gp);
			break;
		case "RedPotion":
			res = new GObjRedPotion(gp);
			break;
		case "SmallSword":
			res = new GObjSmallSword(gp);
			break;
		case "WoodenShield":
			res = new GObjWoodenShield(gp);
			break;
		}
		
		return res;
	}
	
}
