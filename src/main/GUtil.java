package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GUtil {
	
	public static BufferedImage scaleImage(BufferedImage orig, int w, int h) 
	{
		BufferedImage scaledImage = new BufferedImage(w, h, orig.getType());
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(orig, 0, 0, w, h, null);
		g2.dispose();
		
		return scaledImage;
	}
}
