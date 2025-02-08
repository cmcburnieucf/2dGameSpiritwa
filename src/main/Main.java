package main;
import javax.swing.JFrame;
//import java.awt.Color;

public class Main {

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2D Tutorial Game");
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		
		gamePanel.configH.loadConfig();
		
		if(gamePanel.fullScreenOn)
			window.setUndecorated(true);
		else
			window.setUndecorated(false);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.setupGame(false);
		gamePanel.startGameThread();
		
		
		
	}

}
