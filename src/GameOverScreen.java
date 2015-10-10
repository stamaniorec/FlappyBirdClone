import java.awt.Font;
import java.awt.Graphics;

public class GameOverScreen {

	private GameOverPanel gameOverPanel;
	private ScorePanel scorePanel;
	private MenuButton menuButton;
	private long timeAtEndOfGame;
	
	public GameOverScreen(int highScore, Font font04B_19) {
		gameOverPanel = new GameOverPanel();
		scorePanel = new ScorePanel(font04B_19);
		menuButton = new MenuButton("../res/menu_button.png");
	}
	
	/**
	 * Initializes all subcomponents appropriately
	 */
	public void init(long timeAtEndOfGame, int birdScore, int bestScore) {
		this.timeAtEndOfGame = timeAtEndOfGame;
		gameOverPanel.init();
		menuButton.init();
		scorePanel.init(birdScore, bestScore);
	}
	
	/**
	 * Updates gameOverPanel if half a second since timeAtEndOfGame has passed
	 * Updates scorePanel if two seconds since timeAtEndOfGame have passed
	 */
	public void update() {
		if(System.currentTimeMillis() - timeAtEndOfGame > 0.5 * 1000) {
			gameOverPanel.update();
		}
		if(System.currentTimeMillis() - timeAtEndOfGame > 2 * 1000) {
			scorePanel.update();
		}
	}
	
	/**
	 * Draws gameOverPanel if half a second since timeAtEndOfGame has passed
	 * Draws scorePanel if two seconds since timeAtEndOfGame have passed
	 * Draws the "Play again" text if three seconds since timeAtEndOfGame have passed
	 */
	public void draw(Graphics g) {
		if(System.currentTimeMillis() - timeAtEndOfGame > 0.5 * 1000) {
			gameOverPanel.draw(g);
		}
		if(System.currentTimeMillis() - timeAtEndOfGame > 2 * 1000) {
			scorePanel.draw(g);
		}
		if(System.currentTimeMillis() - timeAtEndOfGame > 3 * 1000) {
			g.drawString("Press SPACE to", 7, 450);
			g.drawString("play again!", 75, 500);
		
			menuButton.draw(g);
		}
	}
	
	public boolean wasMenuButtonClicked() {
		return menuButton.wasClicked();
	}
	
	public MenuButton getMenuButton() {
		return menuButton;
	}
	
}
