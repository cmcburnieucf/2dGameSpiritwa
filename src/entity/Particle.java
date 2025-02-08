package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

public class Particle extends Entity{

	
	Entity generator; //entity that produces the particle
	Color color;
	int size, dx, dy;
	
	public Particle(GamePanel gp, Entity generator, Color color, 
					int size, int speed, int maxLife, int dx, int dy) {
		super(gp);
		this.generator = generator;
		this.color = color;
		this.size = size;
		this.speed = speed;
		life = this.maxLife = maxLife;
		int offset = (gp.tileSize/2) - (size/2);
		this.dx = dx + offset;
		this.dx = dy + offset;
		
		worldPosX = generator.worldPosX;
		worldPosY = generator.worldPosY;
	}
	
	public void update() 
	{
		life--;
		
		if(life < maxLife/3) 
		{
			dy++;
		}
		worldPosX = dx*speed;
		worldPosY = dy*speed;
		
		if(life <= 0) 
			alive = false;
	}
	
	public void draw(Graphics2D g2) 
	{
		int screenX = worldPosX - gp.player.worldPosX + gp.player.screenPosX;
		int screenY = worldPosY - gp.player.worldPosY + gp.player.screenPosY;
		
		g2.setColor(color);
		g2.fillRect(screenX, screenY, size, size);
		
	}

}
