package environment;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Lighting {
	GamePanel gp;
	BufferedImage darkness;
	public int dayCtr = 0;
	public float alpha = 0f;
	public final int day = 0, night = 1, dusk = 2, dawn = 3;
	public int dayState = day;
	
	public Lighting(GamePanel gp) {
		this.gp = gp;
		setLightSrc();
	}
	
	public void setLightSrc() {
		darkness = new BufferedImage(gp.screenWidth, 
											gp.screenHeight, 
											BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)darkness.getGraphics();
		
		if(gp.player.curLightSrc == null) {
			g2.setColor(new Color(0, 0, 0, 0.85f));
			
		}
		else {
			//get the center of the circle(aka the area that is lit)
			int centerX = gp.player.screenPosX + (gp.tileSize/2),
				centerY = gp.player.screenPosY + (gp.tileSize/2);
			
			Color colors[] = new Color[10];
			float fractions[] = new float[10];
			
			colors[0] = new Color(0, 0, 0, 0.1f);
			for(int i = 1; i < colors.length; i++) {
				colors[i] = new Color(0, 0, 0, (colors[i-1].getAlpha()+0.10f));
			}
			
			fractions[0] = 0f;
			for(int i = 1; i < fractions.length; i++) {
				fractions[i] = fractions[i-1]+0.10f;
			}
			
			RadialGradientPaint gradientSetting = new RadialGradientPaint(
													centerX, centerY, 
									gp.player.curLightSrc.lightRadius, 
													fractions, colors);
			
			g2.setPaint(gradientSetting);
		}
		
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		g2.dispose();
	}
	
	public void update() {
		if(gp.player.lightSrcUpdated) {
			setLightSrc();
			gp.player.lightSrcUpdated = false;
		}
		
		switch(dayState) {
		case day:
			dayCtr++;
			//for ten seconds
			if(dayCtr > 600) {
				dayState = dusk;
				dayCtr = 0;
			}
			break;
		case night:
			dayCtr++;
			//for ten seconds
			if(dayCtr > 600) {
				dayState = dawn;
				dayCtr = 0;
			}
			break;
		case dusk:
			alpha += 0.001f;
			if(alpha > 0.80f) {
				alpha = 0.80f;
				dayState = night;
			}
			break;
		case dawn:
			alpha -= 0.001f;
			if(alpha < 0f) {
				alpha = 0f;
				dayState = day;
			}
			break;
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha));
		g2.drawImage(darkness, 0, 0, null);
		g2.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1f));
	}
}




































