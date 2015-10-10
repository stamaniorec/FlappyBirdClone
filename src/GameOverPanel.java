import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class GameOverPanel {

	private BufferedImage image;
	
	private int y;
	private final int optimalY = 0;
	private final int speed = 3;
	
	/**
	 * Loads the "Game Over" image
	 */
	public GameOverPanel() {
		try {
			image = ImageIO.read(
					new File(
							"../res/gameover.png"
					)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		y = 200;
	}
	
	/**
	 * Moves the "Game over" panel up until it reaches optimalY
	 */
	public void update() {
		y -= speed;
		if(y < optimalY) {
			y = optimalY;
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, 0, y, null);
	}
	
}
