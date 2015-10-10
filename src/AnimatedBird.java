
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class AnimatedBird {

	private BufferedImage flappySprite;
	private final int maxY = 205;
	private final int minY = 195;
	private final int startY = 200;
	private int y;
	private int dir = -1;
	private int velocityY = 7;
	private final double frameWidth = 72.5;
	private final int frameHeight = 50;
	private final int maxFrames = 4;
	private final int frameDelay = 7;
	private int frameCount = 0;
	private int curFrame;
	
	public AnimatedBird() {
		try {
			flappySprite = ImageIO.read(
					new File(
							"../res/flappyBirdSpriteSheet.png"
					)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		y = startY;
	}
	
	public void update() {
		if(++frameCount >= frameDelay) {
			if(++curFrame >= maxFrames) {
				curFrame = 0;
			}
			frameCount = 0;
			y += velocityY * dir;
			if(y <= minY) {
				y = minY;
				dir *= -1;
			}
			if(y >= maxY) {
				y = maxY;
				dir *= -1;
			}
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(
				flappySprite.getSubimage(
						(int)(curFrame * frameWidth), 0, (int)frameWidth, frameHeight
				),
				Game.WIDTH / 2 - (int)frameWidth / 2, y, null
		);	
	}
	
}
