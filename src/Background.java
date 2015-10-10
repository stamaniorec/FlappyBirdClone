import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Background {

	private BufferedImage backgroundImage;
	
	public Background(String imagePath) {
		try {
			backgroundImage = ImageIO.read(
					new File(imagePath)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
	}
	
}
