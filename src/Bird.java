import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Bird implements KeyListener {

	private BufferedImage birdImage;
	private int degreesOfRotation;
	
	private boolean wasPressedSpace;
	private long timeOnClick;
	
	private static final int SPACE_VALIDITY_TIME = 2;
	
	// Rotation limits
	private static final int NEGATIVE_ROTATION_LIMIT = 25;
	private static final int POSITIVE_ROTATION_LIMIT = 70;
	private static final int NEGATIVE_ROTATION_SPEED = 25;
	private static final int POSITIVE_ROTATION_SPEED = 5;
	
	private static final int upSpeed = 3;
	private static final int fallSpeed = 5;
	private static final int deadFallSpeed = 10;
	
	private static final int x = 100; // the bird stays in one spot x-wise
	private int y;
	
	private boolean live;
	
	private int score;
	
	/**
	 * Loads the image of the bird, which is static, not a spritesheet
	 */
	public Bird() {
		try {
			birdImage = ImageIO.read(
					new File(
							"../res/flappy.png"
					)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		// In the beginning y is the middle of the screen vertically
		y = Game.HEIGHT / 2 - birdImage.getHeight();
				
		degreesOfRotation = 0;
	
		score = 0;
		live = true;
	}
	
	/**
	 * Handles the bird's vertical movement, as well as its rotation
	 */
	public void update() {
		if(live) {
			// If SPACE_VALIDITY_TIME seconds since the space press have elapsed
			if(System.nanoTime() - timeOnClick >= 1000000000 / SPACE_VALIDITY_TIME) {
				// Space is no longer active
				wasPressedSpace = false;
			}
			
			if(wasPressedSpace) {
				// Rotate up
				degreesOfRotation -= NEGATIVE_ROTATION_SPEED;
				
				// But not more than acceptable
				if(degreesOfRotation < -NEGATIVE_ROTATION_LIMIT) {
					degreesOfRotation = -NEGATIVE_ROTATION_LIMIT;
				}
			} else {
				// Rotate down
				degreesOfRotation += POSITIVE_ROTATION_SPEED;
				
				// But not more than acceptable
				if(degreesOfRotation > POSITIVE_ROTATION_LIMIT) {
					degreesOfRotation = POSITIVE_ROTATION_LIMIT;
				}
			}
			
			// Vertical movement
			if(wasPressedSpace) {
				y -= upSpeed;
			} else {
				y += fallSpeed;
			}
		} else {
			y += deadFallSpeed;
		}
		
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Code magic below to rotate the image being drawn
		// at degreesOfRotation degrees
		AffineTransform tx = AffineTransform.getRotateInstance(
				Math.toRadians(degreesOfRotation), 
				birdImage.getWidth() / 2, 
				birdImage.getHeight() / 2 + 5);
		
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		g2d.drawImage(op.filter(birdImage, null), x, y, null);
		// drawCollisionBox(g2d); // for debug purposes
		
	}
	
	// For debug purposes
	@SuppressWarnings("unused")
	private void drawCollisionBox(Graphics2D g2d) {
		g2d.drawRect(x, y, birdImage.getWidth(), birdImage.getHeight());
	}

	public boolean isDead() {
		return !live;
	}
	
	public boolean isDeadAndOnGround() {
		return y >= Game.HEIGHT - birdImage.getHeight() - 70;
	}
	
	public void addPointToScore() {
		score++;
	}
	
	public int getScore() {
		return score;
	}
	
	public void kill() {
		live = false;
		degreesOfRotation = 70;
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			wasPressedSpace = true;
			timeOnClick = System.nanoTime();
		}
	}
	
	public void keyReleased(KeyEvent arg0) { }
	public void keyTyped(KeyEvent arg0) { }
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getImageWidth() { return birdImage.getWidth(); }
	public int getImageHeight() { return birdImage.getHeight(); }
	
}
