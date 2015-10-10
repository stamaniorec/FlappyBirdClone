import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class MenuScreen {

	private BufferedImage logo;
	private PlayButton playButton;
	private AnimatedBird animatedBird;
	private ExitButton exitButton;
	
	public MenuScreen() {
		playButton = new PlayButton(
				"../res/play_button.png"
		);
		animatedBird = new AnimatedBird();
		
		try {
			logo = ImageIO.read(
					new File(
							"../res/logo.png"
					)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		exitButton = new ExitButton("../res/originals/out.png");
	}
	
	public void init() {
		animatedBird.init();
		playButton.init();
	}
	
	public void update() {
		animatedBird.update();
	}
	
	public boolean wasPlayButtonClicked() {
		return playButton.wasClicked();
	}
	
	public boolean wasExitButtonClicked() {
		return exitButton.wasClicked();
	}
	
	public void draw(Graphics g) {
		g.drawImage(logo, 50, 75, null);
		animatedBird.draw(g);
		playButton.draw(g);
		exitButton.draw(g);
	}
	
	public PlayButton getPlayButton() {
		return playButton;
	}
	
	public ExitButton getExitButton() {
		return exitButton;
	}
}
