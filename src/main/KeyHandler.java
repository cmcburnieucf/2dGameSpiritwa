package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

	public boolean upPressed = false, 
				   downPressed = false, 
				   rightPressed = false, 
				   leftPressed = false, 
				   actionPressed = false,
				   shotPressed = false;
	GamePanel gp;
	public int timeCtr = 0;
	
	public KeyHandler(GamePanel gp) 
	{
		this.gp = gp;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(gp.gameState == gp.mainMenuState)
			handleMainMenuState(code);
		else if(gp.gameState == gp.playState)
			handlePlayState(code);
		//Pause Screen Controls
		else if(gp.gameState == gp.pauseState)
			handlePauseState(code);
		//Dialogue Screen Controls
		else if(gp.gameState == gp.dialogState)
			handleDialogState(code);
		//Character Status Screen Controls
		else if(gp.gameState == gp.statusState)
			handleStatusState(code);
		else if(gp.gameState == gp.optionsState)
			handleOptionsState(code);
		else if(gp.gameState == gp.sellState)
			handleSellState(code);
		else if(gp.gameState == gp.mapState)
			handleMapState(code);
		else if(gp.gameState == gp.gameOverState)
			handleGameOverState(code);
	}
	
	public void handleMainMenuState(int code) 
	{
		if(code == KeyEvent.VK_DOWN) 
		{
			gp.ui.commandNum++;
			if(gp.ui.commandNum > 2)
				gp.ui.commandNum = 0;
		}
		else if(code == KeyEvent.VK_UP) 
		{
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0)
				gp.ui.commandNum = 2;	
		}
		else if(code == KeyEvent.VK_ENTER) 
		{
			if(gp.ui.commandNum == 1) 
			{
				gp.gameState = gp.playState;
			}
			else if(gp.ui.commandNum == 0) 
			{
				gp.gameState = gp.playState;
			}
			else if(gp.ui.commandNum == 2) 
			{
				System.exit(0);
			}
		}
	}
	
	public void handlePlayState(int code) 
	{
		if(code == KeyEvent.VK_DOWN) 
		{
			downPressed = true;
		}
		if(code == KeyEvent.VK_UP) 
		{
			upPressed = true;
		}
		if(code == KeyEvent.VK_RIGHT) 
		{
			rightPressed = true;
		}
		if(code == KeyEvent.VK_LEFT) 
		{
			leftPressed = true;
		}
		if(code == KeyEvent.VK_V) 
		{
			shotPressed = true;
		}
		if(code == KeyEvent.VK_ENTER) 
		{
			actionPressed = true;
		}
		else if(code == KeyEvent.VK_CAPS_LOCK) 
		{
			gp.gameState = gp.pauseState;
		}
		else if(code == KeyEvent.VK_E) 
		{
			gp.gameState = gp.statusState;
		}
		else if(code == KeyEvent.VK_ESCAPE) 
		{
			gp.gameState = gp.optionsState;
		}
		else if(code == KeyEvent.VK_M) {
			gp.gameState = gp.mapState;
		}
		/*else if(code == KeyEvent.VK_N) {
			if(!gp.map.miniMapOn) {
				gp.map.miniMapOn = true;
			}
			else {
				gp.map.miniMapOn = false;
			}
		}*/
		System.out.println();
		System.out.println("At Time: "+ (timeCtr/(Math.pow(10, 9))));
		System.out.println("UpPressed is: "+upPressed);
		System.out.println("DownPressed is: "+downPressed);
		System.out.println("LeftPressed is: "+leftPressed);
		System.out.println("RightPressed is: "+rightPressed);
		System.out.println();
		timeCtr++;
	}
	
	public void handlePauseState(int code) 
	{
		if(code == KeyEvent.VK_BACK_SPACE ||
		   code == KeyEvent.VK_CAPS_LOCK) 
		{
			gp.gameState = gp.playState;
		}
	}
	
	public void handleDialogState(int code) 
	{
		if(code == KeyEvent.VK_BACK_SPACE || 
		   code == KeyEvent.VK_ENTER) 
		{
			gp.gameState = gp.playState;
		}
	}
	
	public void handleStatusState(int code) 
	{
		if(code == KeyEvent.VK_BACK_SPACE || 
		   code == KeyEvent.VK_E) 
		{
			gp.gameState = gp.playState;
		}
		else if(code == KeyEvent.VK_F) 
		{
			gp.player.selectItem();
		}
		handlePlayerInventory(code);
	}
	
	public void handleOptionsState(int code) 
	{
		int maxNumCommands = 0;
		switch(gp.ui.subState) 
		{
		case 0:
			maxNumCommands = 3; //for OptionsTop
			break;
		case 1:
			maxNumCommands = 0; //for Fullscreen Option
			break;
		case 2:
			maxNumCommands = 0;
			break;
		case 3:
			maxNumCommands = 1;
			break;
		}
		
		if(code == KeyEvent.VK_BACK_SPACE || 
			code == KeyEvent.VK_ESCAPE) 
		{
			gp.gameState = gp.playState;
		}
		else if(code == KeyEvent.VK_DOWN) 
		{
			gp.ui.commandNum++;
			if(gp.ui.commandNum > maxNumCommands)
				gp.ui.commandNum = 0;
		}
		else if(code == KeyEvent.VK_UP) 
		{
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0)
				gp.ui.commandNum = maxNumCommands;	
		}
		else if(code == KeyEvent.VK_ENTER) 
		{
			actionPressed = true;
		}
	}
	
	public void handleSellState(int code) {
		if(code == KeyEvent.VK_BACK_SPACE || 
			code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.playState;
		}
		else if(code == KeyEvent.VK_ENTER) {
			actionPressed = true;
		}
		if(gp.ui.subState == 0) {
			if(code == KeyEvent.VK_DOWN) {
				gp.ui.commandNum++;
				if(gp.ui.commandNum > 2)
					gp.ui.commandNum = 0;
			}
			else if(code == KeyEvent.VK_UP) {
				gp.ui.commandNum--;
				if(gp.ui.commandNum < 0)
					gp.ui.commandNum = 2;	
			}
		}
		else if(gp.ui.subState == 1) {
			handleSellerInventory(code);
			if(code == KeyEvent.VK_ESCAPE) {
				gp.ui.subState = 0;
			}
		}
		else if(gp.ui.subState == 2) {
			handlePlayerInventory(code);
			if(code == KeyEvent.VK_ESCAPE) {
				gp.ui.subState = 0;
			}
		}
	}
	
	public void handleMapState(int code) {
		if(code == KeyEvent.VK_BACK_SPACE ||
			code == KeyEvent.VK_M) {
			gp.gameState = gp.playState;
		}
		
	}
	
	public void handleGameOverState(int code) 
	{
		if(code == KeyEvent.VK_ENTER) 
		{
			if(gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
				gp.retryGame();
			}
			else if(gp.ui.commandNum == 1) {
				gp.gameState = gp.mainMenuState;
				gp.restartGame();
			}
		}
		else if(code == KeyEvent.VK_DOWN) 
		{
			gp.ui.commandNum++;
			if(gp.ui.commandNum > 1)
				gp.ui.commandNum = 0;
		}
		else if(code == KeyEvent.VK_UP) 
		{
			gp.ui.commandNum--;
			if(gp.ui.commandNum < 0)
				gp.ui.commandNum = 1;	
		}
		
	}
	
	public void handlePlayerInventory(int code) {
		if(code == KeyEvent.VK_DOWN) 
		{
			gp.ui.playerSlotRow++;
			if(gp.ui.playerSlotRow >= gp.ui.maxPlayerSlotRow)
				gp.ui.playerSlotRow = 0;
		}
		else if(code == KeyEvent.VK_UP) 
		{
			gp.ui.playerSlotRow--;
			if(gp.ui.playerSlotRow < 0)
				gp.ui.playerSlotRow = gp.ui.maxPlayerSlotRow-1;
		}
		
		if(code == KeyEvent.VK_RIGHT) 
		{
			gp.ui.playerSlotCol++;
			if(gp.ui.playerSlotCol >= gp.ui.maxPlayerSlotCol)
				gp.ui.playerSlotCol = 0;
		}
		else if(code == KeyEvent.VK_LEFT) 
		{
			gp.ui.playerSlotCol--;
			if(gp.ui.playerSlotRow < 0)
				gp.ui.playerSlotRow = gp.ui.maxPlayerSlotCol-1;
		}
	}
	
	public void handleSellerInventory(int code) {
		if(code == KeyEvent.VK_DOWN) 
		{
			gp.ui.npcSlotRow++;
			if(gp.ui.npcSlotRow >= gp.ui.maxNpcSlotRow)
				gp.ui.npcSlotRow = 0;
		}
		else if(code == KeyEvent.VK_UP) 
		{
			gp.ui.npcSlotRow--;
			if(gp.ui.npcSlotRow < 0)
				gp.ui.npcSlotRow = gp.ui.maxNpcSlotRow-1;
		}
		
		if(code == KeyEvent.VK_RIGHT) 
		{
			gp.ui.npcSlotCol++;
			if(gp.ui.npcSlotCol >= gp.ui.maxNpcSlotCol)
				gp.ui.npcSlotCol = 0;
		}
		else if(code == KeyEvent.VK_LEFT) 
		{
			gp.ui.npcSlotCol--;
			if(gp.ui.npcSlotRow < 0)
				gp.ui.npcSlotRow = gp.ui.maxNpcSlotCol-1;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_DOWN) 
		{
			downPressed = false;
		}
		if(code == KeyEvent.VK_UP) 
		{
			upPressed = false;
		}
		if(code == KeyEvent.VK_RIGHT) 
		{
			rightPressed = false;
		}
		if(code == KeyEvent.VK_LEFT) 
		{
			leftPressed = false;
		}
		if(code == KeyEvent.VK_V) 
		{
			shotPressed = false;
		}
	}

}
