import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class BackgroundFooter {

	private BufferedImage footerImage;
	private int imageWidth;
	private int imageHeight;
	
	private int x;
	private final int y;
	private int moveSpeed = 2;
	
	public BackgroundFooter(String imagePath) {
		try {
			footerImage = ImageIO.read(new File(imagePath));
			
			imageWidth = footerImage.getWidth();
			imageHeight = footerImage.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		y = Game.HEIGHT - imageHeight;
	}
	
	public void update() {
		x -= moveSpeed;
		if(x + imageWidth < 0) {
			x = 0;
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(footerImage, x, y, null);
		if(x + imageWidth < Game.WIDTH) {
			g.drawImage(footerImage, x + imageWidth, y, null);
		}
	}
	
}
