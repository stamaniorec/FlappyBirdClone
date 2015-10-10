import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScorePanel {

	private BufferedImage image;
	
	private int y;
	private final int optimalY = 170;
	private final int speed = 3;
	
	private int currentScore;
	private int bestScore;
	
	private Font font;
	
	/**
	 * Initializes the font
	 * Loads the ScorePanel image
	 */
	public ScorePanel(Font font) {
		this.font = font;
		
		try {
			image = ImageIO.read(
					new File(
							"../res/score_panel.png"
					)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init(int currentScore, int bestScore) {
		y = 300;
		this.currentScore = currentScore;
		this.bestScore = bestScore;
	}
	
	/**
	 * Moves the ScorePanel up until it reaches optimalY
	 */
	public void update() {
		y -= speed;
		if(y < optimalY) {
			y = optimalY;
		}
	}
	
	/**
	 * Draws the ScorePanel, the currentScore and the bestScore
	 */
	public void draw(Graphics g) {
		g.drawImage(image, -6, y, null);
		g.setFont(font);
		g.setColor(new Color(255, 255, 37));
		
		// If currentScore has two or more digits (have not calculated for three)
		if(currentScore / 10 > 0) {
			g.drawString(Integer.toString(currentScore), Game.WIDTH / 2 - 27, y + 105);
		} else {
			g.drawString(Integer.toString(currentScore), Game.WIDTH / 2 - 13, y + 105);
		}
		
		if(bestScore / 10 > 0) {
			g.drawString(Integer.toString(bestScore), Game.WIDTH / 2 - 27, y + 185);
		} else {
			g.drawString(Integer.toString(bestScore), Game.WIDTH / 2 - 13, y + 185);
		}
	}
	
}
