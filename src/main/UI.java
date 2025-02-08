package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class UI {
	GamePanel gp;
	Graphics2D g2;
	Font defaultFont, inventoryFont, msgFont;
	public boolean messageOn = false;
	ArrayList<String> msgs = new ArrayList<>();
	ArrayList<Integer> msgCtrs = new ArrayList<>();
	public String currentDialog = "";
	public int commandNum = 0;
	public int slotCol = 0, slotRow = 0, maxSlotRow = 4, maxSlotCol = 5;
	int subState = 0;
	int ctr = 0;
	
	
	public UI(GamePanel gp) {
		
		this.gp = gp;
		defaultFont = new Font("Arial", Font.PLAIN, 28);
		inventoryFont = new Font("Arial", Font.PLAIN, 28);
		msgFont = new Font("Arial", Font.PLAIN, 14);
	}
	
	
	public void addMessage(String msg) {
		msgs.add(msg);
		msgCtrs.add(0);
	}
	
	
	public void draw(Graphics2D g2) 
	{
		this.g2 = g2;
		g2.setFont(defaultFont);
		g2.setColor(Color.white);
		
		if(gp.gameState == gp.mainMenuState) {
			drawMainMenuScreen();
		}
		else if(gp.gameState == gp.playState) {
			drawPlayerLife();
			drawMsgs();
		}
		else if(gp.gameState == gp.pauseState) {
			drawPauseScreen();
			drawPlayerLife();
		}
		else if(gp.gameState == gp.dialogState) {
			drawDialogScreen();
			drawPlayerLife();
		}
		else if(gp.gameState == gp.statusState) {
			drawStatusScreen();
			drawInventory();
		}
		else if(gp.gameState == gp.optionsState) {
			drawOptionsScreen();
		}
		else if(gp.gameState == gp.gameOverState) {
			drawGameOverScreen();
		}
		else if(gp.gameState == gp.transitionState) {
			drawTransition();
		}
	}

	
	public void drawMsgs() {
		int msgPosX = gp.tileSize, msgPosY = gp.tileSize;
		g2.setFont(defaultFont);
		int len = msgs.size();
		
		for(int i = 0; i < len; i++) {	
			if(msgs.get(i) != null) {
				
				g2.setColor(Color.white);
				g2.drawString(msgs.get(i), msgPosX, msgPosY);
				msgCtrs.set(i, (msgCtrs.get(i)+1));
				msgPosY += 40;
				
				if(msgCtrs.get(i) > 60) {
					msgs.remove(i);
					msgCtrs.remove(i);
				}
			}
		}
	}
	

	public void drawPlayerLife() {
		
		int screenX = gp.player.worldPosX + gp.player.screenPosX;
		int screenY = gp.player.worldPosY + gp.player.screenPosY;
		
		//draw Health Bar
		//the length of 1 hp
		double scale = (double) gp.tileSize / gp.player.maxLife;
		//cur length of hp bar
		double bar = scale * gp.player.life;
		
		g2.setColor(Color.black);
		g2.fillRect(screenX-1, screenY-16, gp.tileSize, 12);
		
		g2.setColor(Color.pink);
		g2.fillRect(screenX, screenY-15, (int)bar, 10);
		
		//move down to draw magic bar
		screenY += 20;
		
		//draw Magic Bar
		//the length of 1 hp
		scale = (double) gp.tileSize / gp.player.maxLife;
		//cur length of magic bar
		bar = scale * gp.player.life;
		
		g2.setColor(Color.black);
		g2.fillRect(screenX-1, screenY-16, gp.tileSize, 12);
		
		g2.setColor(Color.cyan);
		g2.fillRect(screenX, screenY-15, (int)bar, 10);
	}
	

	public void drawMainMenuScreen() {
		//set background color
		//g2.setColor(Color.black);
		//g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		//set Title
		g2.setFont(defaultFont);
		String text = "game title";
		int x = getXForCenterText(text), y = 3*gp.tileSize;
		
		//create text shadow
		//g2.setColor(Color.black);
		//g2.drawString(text, x+5, y+5);
		
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//menu
		text = "Continue Game";
		x = getXForCenterText(text);
		y = 4*gp.tileSize;
		g2.drawString(text, x, y);
		
		if(commandNum == 0) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "New Game";
		x = getXForCenterText(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		
		if(commandNum == 1) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "Quit";
		x = getXForCenterText(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		
		if(commandNum == 2) {
			g2.drawString(">", x-gp.tileSize, y);
		}
	}
	
	
	public void drawDialogScreen() {
		//Dialogue Window
		int x = gp.tileSize*2, y = gp.tileSize/2;
		int width = (gp.screenWidth - (gp.tileSize*4));
		int height = gp.tileSize*3;
		drawSubWindow(x, y, width, height);
		
		x += gp.tileSize;
		y += gp.tileSize;
		int numChars = 35;
		int len = currentDialog.length();
		for(int i = 0; i < len; i += numChars) {
			
			int end = Math.min(i + numChars, len);
			String substr = currentDialog.substring(i, end);
			g2.drawString(substr, x, y);
			y += 40;
		}
		
	}
	
	
	
	public void drawStatusScreen() {
		
		final int frameX = gp.tileSize/2, frameY = gp.tileSize/2;
		final int frameWidth = 5*gp.tileSize, frameHeight = 10*gp.tileSize;
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//display text
		g2.setColor(Color.white);
		g2.setFont(defaultFont);
		
		int textPosX = frameX+20, textPosY = frameY+gp.tileSize;
		final int lineHeight = 40;
		
		String[] stats = {"Level", "Life", "Mana", "Strength", "Dexterity", 
				"Speed", "Next Level", "Wealth", 
				"Current Weapon", "Current Shield"};
		
		for(int i = 0; i < stats.length; i++) {
			g2.drawString(stats[i], textPosX, textPosY);
			if(stats[i].contentEquals("Wealth"))
				textPosY += lineHeight+15;
			else if(stats[i].contentEquals("Weapon"))
				textPosY += lineHeight+10;
			else
				textPosY += lineHeight;
		}
		
		int tailX = (frameX + frameWidth) - 32;
		textPosY = frameY+gp.tileSize;
		
		String[] statVals = {
				String.valueOf(gp.player.level), 
				String.valueOf(gp.player.life+"/"+gp.player.maxLife), 
				String.valueOf(gp.player.mana+"/"+gp.player.maxMana), 
				String.valueOf(gp.player.strength), 
				String.valueOf(gp.player.dext), 
				String.valueOf(gp.player.speed), 
				String.valueOf(gp.player.nextLvlExp), 
				String.valueOf(gp.player.wealth), 
				String.valueOf(gp.player.curWeapon), 
				String.valueOf(gp.player.curShield)
			};
		
		for(int i = 0; i < statVals.length-2; i++) {
			g2.drawString(statVals[i], 
					getXForRightAlignedText(statVals[i], tailX), 
					textPosY);
			textPosY += lineHeight;
		}
		
		//display current weapon and shield as images
		g2.drawImage(gp.player.curWeapon.movement.displayStanding("down"), (tailX - gp.tileSize), 
				textPosY-15, null);
		textPosY += gp.tileSize;
		g2.drawImage(gp.player.curShield.movement.displayStanding("down"), (tailX - gp.tileSize), 
				textPosY-15, null);
	}
	
	
	public void drawInventory() {
		//draw window frame
		final int frameX = gp.tileSize*10, frameY = gp.tileSize/2;
		final int frameWidth = 10*gp.tileSize, frameHeight = 5*gp.tileSize;
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//draw inventory slots
		final int slotBeginX = frameX+20, slotBeginY = frameY+20;
		int slotX=slotBeginX, slotY=slotBeginY, slotSize = gp.tileSize+4;
		
		//draw player's items
		for(int i = 0; i < gp.player.inventoryList.size(); i++) {
			//for equipping
			if(gp.player.inventoryList.get(i) == gp.player.curWeapon ||
				gp.player.inventoryList.get(i) == gp.player.curShield) {
				g2.setColor(Color.yellow);
				g2.drawRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
			}
			
			g2.drawImage(gp.player.inventoryList.get(i).movement.displayStanding("down"), slotX, slotY, null);
			slotX += slotSize;
			
			if(((i+1) / maxSlotRow) == 0) {
				slotX = slotBeginX;
				slotY += slotSize;
			}
		}
		
		//draw cursor
		int cursorX = (slotBeginX+(slotSize*slotCol)), 
			cursorY = (slotBeginY+(slotSize*slotRow)),
			cursorWidth = gp.tileSize, 
			cursorHeight = gp.tileSize;
		
		g2.setColor(Color.blue);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		
		//draw description window
		int dWindowX = frameX, dWindowY = frameY+gp.tileSize, 
				dWindowWidth = frameWidth, dWindowHeight = frameHeight/2;
		
		//drawSubWindow(dWindowX, dWindowY, dWindowWidth, dWindowHeight);
		
		//draw description text
		int textPosX = dWindowX+20, textPosY = dWindowY+gp.tileSize;
		
		g2.setFont(defaultFont);
		
		int itemIndex = getItemIndex();
		if(itemIndex < gp.player.inventoryList.size()) {
			//this creates a subwindow only if there is an item selected
			drawSubWindow(dWindowX, dWindowY, dWindowWidth, dWindowHeight);
			
			int numChars = 35;
			String temp = gp.player.inventoryList.get(itemIndex).description;
			int len = temp.length();
			
			for(int i = 0; i < len; i += numChars) {
				
				int end = Math.min(i + numChars, len);
				String substr = temp.substring(i, end);
				g2.drawString(substr, textPosX, textPosY);
				textPosY += 40;
			}
		}
	}
	
	
	public int getItemIndex() {
		return slotCol + (slotRow*maxSlotRow);
	}
	
	
	public void drawOptionsScreen() {
		g2.setColor(Color.white);
		g2.setFont(defaultFont);
		
		final int frameX = gp.tileSize*6, frameY = gp.tileSize;
		final int frameWidth = 8*gp.tileSize, frameHeight = 10*gp.tileSize;
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		switch(subState) {
		//
		case 0:
			optionsTop(frameX, frameY);
			break;
		//
		case 1:
			fullScreenOptionNotify(frameX, frameY);
			break;
		case 2:
			handleControlsOptions(frameX, frameY); 
			break;
		case 3:
			handleEndGameOption(frameX, frameY);
			break;
		}
		
		gp.keyH.actionPressed = false;
	}
	
	
	public void optionsTop(int fx, int fy) {
		
		int textX, textY;
		
		String text = "Options";
		textX = getXForCenterText(text); 
		textY = fy + gp.tileSize;
		
		g2.drawString(text, textX, textY);
		
		//Full Screen Option
		textX = fx + gp.tileSize;
		textY += gp.tileSize * 2;
		g2.drawString("Full Screen", textX, textY);
		if(commandNum == 0){
			
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.actionPressed) {
				if(gp.fullScreenOn)
					gp.fullScreenOn = false;
				else
					gp.fullScreenOn = true;
				subState = 1;
			}
		}
		
		//Controls Option
		textY += gp.tileSize;
		g2.drawString("Controls", textX, textY);
		if(commandNum == 1){
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.actionPressed) {
				subState = 2;
				commandNum = 0;
			}
		}
		
		//End Game Option
		textY += gp.tileSize;
		g2.drawString("End Game", textX, textY);
		if(commandNum == 2){
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.actionPressed) {
				subState = 3;
				commandNum = 0;
			}
			
		}
		
		//Back Option
		textY += gp.tileSize*2;
		g2.drawString("Back", textX, textY);
		if(commandNum == 3){
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.actionPressed) {
				gp.gameState = gp.playState;
				//subState = 0; //tutorial did not reset subState
				commandNum = 0;
			}
		}
		
		//Right Side Option Values
		
		//TextBox for FullScreen Option
		textX = fx+(gp.tileSize*4);
		textY = fy+(gp.tileSize*2)+24;
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(textX, textY, gp.tileSize/2, gp.tileSize/2);
		if(gp.fullScreenOn) {
			g2.fillRect(textX, textY, gp.tileSize/2, gp.tileSize/2);
		}
		
		//Slider for Music Option
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 120, gp.tileSize/2);
		
		//Slider for Sound Effect Option
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 120, gp.tileSize/2);
		
		gp.configH.saveConfig();
	}
	
	public void fullScreenOptionNotify(int fx, int fy) {
		
		int textX = fx + gp.tileSize;
		int textY = fy+ gp.tileSize * 2;
		
		currentDialog = "Restart Game to See Changes";
		
		textY += fy+ gp.tileSize * 9;
		g2.drawString("Back", textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.actionPressed) {
				subState = 0;
				commandNum = 0;
			}
		}
	}
	
	
	public void handleControlsOptions(int fx, int fy) {
		int textX, textY;
		
		String text = "Controlss";
		textX = getXForCenterText(text); 
		textY = fy + gp.tileSize;
		
		g2.drawString(text, textX, textY);
		
		textX = fx + gp.tileSize;
		textY += gp.tileSize;
		
		//Controls Option
		String[] controlOptions = {"Move", 
								   "Confirm/Attack", 
								   "Shoot/Cast",
								   "Change to Status Screen",
								   "Pause",
								   "Options"};
		
		for(int i = 0; i < controlOptions.length; i++) {
			g2.drawString(controlOptions[i], textX, textY);
			textY += gp.tileSize;
		}
		controlOptions = null;
		
		textX = fx + gp.tileSize*6;
		textY += gp.tileSize*2;
		
		String[] controlValues = {"WASD", 
				   				  "ENTER", 
				   				  "V",
				   				  "F",
				   				  "CAPS_LOCK",
				   				  "ESCAPE"};
		for(int i = 0; i < controlValues.length; i++) {
			g2.drawString(controlValues[i], textX, textY);
			textY += gp.tileSize;
		}
		controlValues = null;
		
		textX = fx + gp.tileSize;
		textY += gp.tileSize*9;
		g2.drawString("Back", textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.actionPressed) {
				subState = 0;
				commandNum = 1; //original commandNum for controls Option
			}
		}
	}
	
	
	public void handleEndGameOption(int fx, int fy) {
		int textX = fx + gp.tileSize;
		int textY = fy + gp.tileSize*3;
		
		currentDialog = "Quit Game and Return to Title Screen?";
		textY += 40;
		
		String text = "Yes";
		textX = getXForCenterText(text);
		textY += gp.tileSize*3;
		g2.drawString(text, textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.actionPressed) {
				subState = 0;
				gp.gameState = gp.mainMenuState;
			}
		}
		
		text = "No";
		textX = getXForCenterText(text);
		textY += gp.tileSize;
		g2.drawString(text, textX, textY);
		if(commandNum == 1) {
			g2.drawString(">", textX-25, textY);
			if(gp.keyH.actionPressed) {
				subState = 0;
				commandNum = 2; //original commandNum for EndGame option
			}
		}
		
	}
	
	
	public void drawGameOverScreen() {
		g2.setColor(Color.black);
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x, y;
		String text;
		g2.setFont(defaultFont);
		
		text = "Game Over";
		
		x = getXForCenterText(text);
		y = gp.tileSize*4;
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//Display Options
		text = "Retry";
		g2.setFont(defaultFont);
		x = getXForCenterText(text);
		y += gp.tileSize*4;
		g2.drawString(text, x, y);
		if(commandNum == 0)
			g2.drawString(">", x-40, y);
		
		text = "Quit";
		x = getXForCenterText(text);
		y += 50;
		g2.drawString(text, x, y);
		if(commandNum == 1)
			g2.drawString(">", x-40, y);
		
	}
	
	
	public void drawTransition() {
		ctr++;
		g2.setColor(new Color(0, 0, 0, ctr*5));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		if(ctr >= 50) 
		{
			ctr = 0;
			gp.gameState = gp.playState;
			gp.curMap = gp.eventH.tempMap;
			gp.eventH.prevEventX = gp.player.worldPosX = gp.eventH.tempCol*gp.tileSize;
			gp.eventH.prevEventY = gp.player.worldPosY = gp.eventH.tempRow*gp.tileSize;
		}
	}
	
	
	// ---------------------- ================ ------------------------
	// ---------------------- Helper Functions ------------------------
	// ---------------------- ================ ------------------------
	
	
	public void drawSubWindow(int x, int y, int w, int h) {
		//for reference you could have also done:
		//color c = new Color(0,0,0,0) (for RGBA)
		//and used c as parameter for setColor().
		g2.setColor(Color.black);
		g2.fillRoundRect(x, y, w, h, 35, 35);
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, w-10, h-10, 25, 25);
	}
	

	public void drawPauseScreen() {
		String text = "PAUSED";
		int x = getXForCenterText(text), y = gp.screenHeight/2;
		
		g2.drawString(text, x, y);
	}
	
	
	public int getXForCenterText(String text) {
		int length = ((int)g2.getFontMetrics().getStringBounds(text, g2).getWidth());
		return (gp.screenWidth/2 - length/2);
	}
	
	
	public int getXForRightAlignedText(String text, int tailX) {
		int length = ((int)g2.getFontMetrics().getStringBounds(text, g2).getWidth());
		return (tailX - length);
	}
}

































