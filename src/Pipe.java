import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pipe {

	public static final int pipeImageWidth = 102;
	public static final int pipeImageHeight = 500;
	
	private BufferedImage pipeImage;
	
	private static int moveSpeed = 2;
	
	protected int x;
	protected int y;
	
	private boolean hasCollectedPoint;
	
	public Pipe(int x, int y, boolean rotated) {
		this.x = x;
		this.y = y;
		
		try {
			if(rotated) {
				pipeImage = ImageIO.read(new File(
						"../res/rotated.png"));
			} else {
				pipeImage = ImageIO.read(new File(
						"../res/normal.png"));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(pipeImage, x, y, null);
		// drawCollisionBox(g); // for debug purposes
	}
	
	// For debug purposes
	public void drawCollisionBox(Graphics g) {
		g.drawRect(x, y, pipeImageWidth, pipeImageHeight);
	}
	
	/**
	 * @param Bird b
	 * @return if collision has occurred 
	 * Uses Rectangle collision
	 */
	public boolean collision(Bird b) {
		return 
		(b.getX() + b.getImageWidth() > this.x) && 
		(b.getX() < this.x + Pipe.pipeImageWidth && 
		(b.getY() + b.getImageHeight() > this.y) && 
		(b.getY() < this.y + Pipe.pipeImageHeight));
	}
	
	/**
	 * Scrolls the pipe to the left
	 */
	public void update() {
		x -= moveSpeed; 
	}
	
	public boolean isScrolled() {
		return (x + pipeImageWidth) < 0;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean hasCollectedPoint() {
		return hasCollectedPoint;
	}
	
	public void collectPoint() {
		hasCollectedPoint = true;
	}
}
