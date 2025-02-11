package entity;

import main.GamePanel;

public class Projectile extends Entity{
	
	Entity user;
	
	public Projectile(GamePanel gp) {
		super(gp);
	}
	
	public void set(int x, int y, String direction, 
					boolean alive, Entity user) 
	{
		this.worldPosX = x;
		this.worldPosY = y;
		this.direction = direction;
		this.alive = alive;
		this.user = user;
		//Reset the life to max every time you shoot it
		this.life = this.maxLife;
	}
	
	public boolean haveMana(Entity user) {return false;}
	public void reduceMana(Entity user)	{}
	
	public void update() 
	{
		if(user == gp.player) 
		{
			int enemyIndex = gp.collisionCheck.checkEntity(this, gp.enemies);
			if(enemyIndex != -1) 
			{
				gp.player.damageEnemy(this, enemyIndex, attack, this.kbPower);
				makeParticle(this, gp.enemies[gp.curMap][enemyIndex]);
				//if shot successful, end the shot
				alive = false;
			}
		}
		else if(user != gp.player) 
		{
			boolean hitPlayer = gp.collisionCheck.checkPlayer(this);
			if(!gp.player.invincible && hitPlayer) 
			{
				damagePlayer(attack);
				makeParticle(this, gp.player);
				//if shot successful, end the shot
				alive = false;
			}
		}
		
		switch(direction) 
		{
		case "down":
			worldPosY += speed;
			break;
		case "up":
			worldPosY -= speed;
			break;
		case "right": 
			worldPosX += speed;
			break;
		case "left":
			worldPosX -= speed;
			break;
		case "up-right":
			worldPosX += (speed * 0.71);
			worldPosY += (speed * 0.71);
			break;
		}
		
		life--;
		if(life <= 0)
			alive = false;
		
		spriteCtr++;
		if(spriteCtr > 20) 
		{
			movement.changeFrame = true;
			spriteCtr = 0;
		}
		
		
	}
}
