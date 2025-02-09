package main;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

public class ConfigHandler {
	GamePanel gp;
	
	public ConfigHandler(GamePanel gp) 
	{
		this.gp = gp;
	}
	
	public void saveConfig() 
	{
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("Config.txt"));
			
			/*For FullScreen Setting
			
			if(gp.fullScreenOn == true)
				writer.write("FullScreen On");
			else
				writer.write("FullScreen Off");
				
			writer.newLine();
			*/
			
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadConfig() 
	{
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("Config.txt"));
			String line = null;
			while((line = reader.readLine()) != null) 
			{
				if(line.contentEquals("FullScreen On"))
					gp.fullScreenOn = true;
				if(line.contentEquals("FullScreen Off"))
					gp.fullScreenOn = false;
			}
			
			reader.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
