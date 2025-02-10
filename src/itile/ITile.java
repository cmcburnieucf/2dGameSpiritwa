package itile;

import entity.Entity;
import main.GamePanel;

public class ITile extends Entity{

	public boolean removable = false;
	
	public ITile(GamePanel gp) {
		super(gp);
		
	}
	
	public boolean isRightItem(Entity entity) {return false;}
	public ITile getRemovedForm() {return null;}
	public void update() 
	{
		if(invincible) 
		{
			invincibleCtr++;
			if(invincibleCtr > 20) 
			{
				invincible = false;
				invincibleCtr = 0;
			}
		}
	}
}
