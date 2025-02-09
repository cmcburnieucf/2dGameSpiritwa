package entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import main.GUtil;

public class SpriteSheet {
	
	int ctrX, ctrY; //counter to keep track of which sprite frame we are on
	int frameHeight, frameWidth, numFrames;
	boolean changeFrame;
	ArrayList<String> directions;
	BufferedImage sheet;
	
	public SpriteSheet(BufferedImage sheet, int numFrames, ArrayList<String> directions) {
		ctrX = ctrY = -1;
		this.sheet = sheet;
		// every row has a specific animation depending on the direction
		//this.frameHeight = sheet.getHeight()/directions.size();
		this.frameHeight = sheet.getHeight();
		//depending on the frame, the width of the actual sprite might be different
		this.frameWidth = sheet.getWidth()/numFrames;
		this.numFrames = numFrames;
		this.directions = directions;
	}
	
	public BufferedImage nextFrame(String direction) 
	{
		//if the sprite can move the given direction
		if(directions.contains(direction)) {
			
			ctrY = (directions.indexOf(direction)) % directions.size();
			ctrX = (ctrX+1)%numFrames;
			//ctrX*frameWidth is the current leftmost part of the frame
			//if ctrX = 0, then ctrX*frameWidth = 0, starting at the edge of the animation
			// sheet.getSubimage(ctrX*frameWidth, ctrY*frameHeight, frameWidth, frameHeight);
			//For Testing
			return GUtil.scaleImage(sheet.getSubimage(ctrX*frameWidth, 0, frameWidth, frameHeight), 48, 48);
		}
		return null;
	}
	
	public BufferedImage displayStanding(String direction) 
	{
		ctrY = (directions.indexOf(direction)) % directions.size();
		//sheet.getSubimage(0, ctrY*frameHeight, frameWidth, frameHeight);
		//For Testing
		return GUtil.scaleImage(sheet.getSubimage(0, 0, frameWidth, frameHeight), 48, 48);
	}
}


































