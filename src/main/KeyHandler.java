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
		else if(code == KeyEvent.VK_UP) 
		{
			upPressed = true;
		}
		else if(code == KeyEvent.VK_RIGHT) 
		{
			rightPressed = true;
		}
		else if(code == KeyEvent.VK_LEFT) 
		{
			leftPressed = true;
		}
		else if(code == KeyEvent.VK_UP && code == KeyEvent.VK_RIGHT) 
		{
			upPressed = true;
			rightPressed = true;
		}
		else if(code == KeyEvent.VK_ENTER) 
		{
			actionPressed = true;
		}
		else if(code == KeyEvent.VK_V) 
		{
			shotPressed = true;
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
		else if(code == KeyEvent.VK_DOWN) 
		{
			gp.ui.slotRow++;
			if(gp.ui.slotRow >= gp.ui.maxSlotRow)
				gp.ui.slotRow = 0;
		}
		else if(code == KeyEvent.VK_UP) 
		{
			gp.ui.slotRow--;
			if(gp.ui.slotRow < 0)
				gp.ui.slotRow = gp.ui.maxSlotRow-1;
		}
		else if(code == KeyEvent.VK_RIGHT) 
		{
			gp.ui.slotCol++;
			if(gp.ui.slotCol >= gp.ui.maxSlotCol)
				gp.ui.slotCol = 0;
		}
		else if(code == KeyEvent.VK_LEFT) 
		{
			gp.ui.slotCol--;
			if(gp.ui.slotRow < 0)
				gp.ui.slotRow = gp.ui.maxSlotCol-1;
		}
		else if(code == KeyEvent.VK_F) 
		{
			gp.player.selectItem();
		}
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
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_DOWN) 
		{
			downPressed = false;
		}
		else if(code == KeyEvent.VK_UP) 
		{
			upPressed = false;
		}
		else if(code == KeyEvent.VK_RIGHT) 
		{
			rightPressed = false;
		}
		else if(code == KeyEvent.VK_LEFT) 
		{
			leftPressed = false;
		}
		else if(code == KeyEvent.VK_UP && code == KeyEvent.VK_RIGHT) 
		{
			upPressed = false;
			rightPressed = false;
		}
		else if(code == KeyEvent.VK_V) 
		{
			shotPressed = false;
		}
	}

}
