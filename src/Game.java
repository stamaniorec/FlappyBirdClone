// --- --- --- --- --- IMPORTS ---  --- --- --- --- 
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * @author Stanimir Bogdanov, a.k.a. Stamaniorec
 * @since 19.08.2014
 */

public class Game extends Canvas implements KeyListener {

	private static final long serialVersionUID = 1L;
	public static int WIDTH = 400;
	public static int HEIGHT = 600;

	private Thread gameThread;
	private JFrame frame;
	private boolean running;
	
	private GameState gameState = GameState.MENU;
	
	private MenuScreen menuScreen;

	private Background background;
	private BackgroundFooter backgroundFooter;
	private PipeManager pipes;
	private Bird bird;
	
	private long timeAtEndOfGame;
	
	private GameOverScreen gameOverScreen;
	private int highScore;
	private int prevHighScore;
	private boolean hasImprovedHighScore;
	
	private Font font04B_19;
	
	private BufferedImage blackScreen;
	private int[] blackScreenPixels;

	// --- --- --- --- --- GAME LOOP ---  --- --- --- --- 
	private class GameLoop implements Runnable {
		public void run() {
			Timer timer = new Timer();
			
			while(running) {
				if(timer.update()) {
					update();
					render();
				}
			}
			
			stop();
		}
	}

	// --- --- --- --- --- UPDATE SECTION ---  --- --- --- --- 
	private void update() {
		if(gameState == GameState.MENU) {
			backgroundFooter.update();
			menuScreen.update();
			if(menuScreen.wasPlayButtonClicked()) {
				enterPlayingState();
			}
			if(menuScreen.wasExitButtonClicked()) {
				running = false;
			}
		} else if(gameState == GameState.PLAYING) {
			bird.update();
			
			if(pipes.collision(bird)) {
				bird.kill();
				if(bird.getScore() > highScore) {
					saveHighScore(bird.getScore());
					prevHighScore = highScore;
					highScore = bird.getScore();
					hasImprovedHighScore = true;
				}
			} 
			
			if(!bird.isDead()) {
				pipes.updatePipes();
				backgroundFooter.update();
			}
			
			if(bird.isDeadAndOnGround()) {
				enterGameOverState();
			}
			
			pipes.collectPoints(bird);
		} else if(gameState == GameState.GAME_OVER) {
			gameOverScreen.update();
			if(gameOverScreen.wasMenuButtonClicked()) {
				enterMenuState();
			}
		}
		
	}

	/**
	 * Writes the passed score to the high score file
	 * @param score - current high score
	 */
	private void saveHighScore(int score) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(
					new FileWriter(
							"../res/highscore.txt"
					)
			);
			
			writer.write(Integer.toString(score));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the high score, read from the high score file
	 */
	private int getHighScore() {
		int highScore = 0;
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(
					new FileReader(
							"../res/highscore.txt"
					)
			);
			
			String line;
			try {
				while((line = fileReader.readLine()) != null) {
					highScore = Integer.parseInt(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return highScore;
	}

	private void enterMenuState() {
		gameState = GameState.MENU;
		menuScreen.init();
	}
	
	private void enterPlayingState() {
		gameState = GameState.PLAYING;
		hasImprovedHighScore = false;

		bird.init();
		pipes.init();
	}
	
	private void enterGameOverState() {
		gameState = GameState.GAME_OVER;
		timeAtEndOfGame = System.currentTimeMillis();
		
		// If the high score has been improved, as a high score on the scorePanel
		// show the PREVIOUS high score, not this one
		// One the next playing though, display the "true" high score
		if(hasImprovedHighScore)
			gameOverScreen.init(timeAtEndOfGame, bird.getScore(), prevHighScore);
		else
			gameOverScreen.init(timeAtEndOfGame, bird.getScore(), highScore);
	}
	
	// --- --- --- --- --- RENDER SECTION ---  --- --- --- --- 
	private synchronized void render() {
		// --- --- --- --- --- TRIPLE BUFFERING SETUP --- --- --- --- --- 
		BufferStrategy buffStrategy = this.getBufferStrategy();
		if(buffStrategy == null) {
			createBufferStrategy(3); 
			return;
		}
		Graphics g = buffStrategy.getDrawGraphics();

		// --- --- --- --- --- CLEARING THE SCREEN ---  --- --- --- --- 
		g.drawImage(blackScreen, 0, 0, WIDTH, HEIGHT, null);

		// --- --- --- --- --- RENDER OBJECTS BELOW ---  --- --- --- --- 
		if(gameState == GameState.MENU) {
			background.draw(g);
			menuScreen.draw(g);
			backgroundFooter.draw(g);
		} else if(gameState == GameState.PLAYING) {
			background.draw(g);
			pipes.drawPipes(g);
			bird.draw(g);
			backgroundFooter.draw(g);
			
			g.setFont(font04B_19);
			g.setColor(Color.RED);
			g.drawString(Integer.toString(bird.getScore()), Game.WIDTH / 2, Game.HEIGHT / 5);
		} else if(gameState == GameState.GAME_OVER) {
			background.draw(g);
			backgroundFooter.draw(g);

			gameOverScreen.draw(g);
		}
		
		// --- --- --- --- --- RENDER OBJECTS ABOVE ---  --- --- --- --- 

		g.dispose(); 
		buffStrategy.show(); 
	}
	
	// --- --- --- --- --- START METHOD ---  --- --- --- --- 
	public synchronized void start() {
		if(!running) {		
			running = true;
			frame.setVisible(true);
			
			menuScreen = new MenuScreen();
			menuScreen.init();
			addMouseListener(menuScreen.getPlayButton());
			addMouseListener(menuScreen.getExitButton());
			
			background = new Background(
					"../res/background.png"
			);
			
			backgroundFooter = new BackgroundFooter(
					"../res/background_footer.png"
			);
			
			pipes = new PipeManager();
			pipes.init();
			
			bird = new Bird();
			bird.init();
			
			addKeyListener(this);
			addKeyListener(bird);
			
			// Import the custom font
			try {
				font04B_19 = Font.createFont(
						Font.TRUETYPE_FONT, 
						new File(
								"../res/04B_19.TTF")
						).deriveFont(50f);
			} catch (FontFormatException | IOException e1) {
				e1.printStackTrace();
			}
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			try {
				ge.registerFont(Font.createFont(
						Font.TRUETYPE_FONT, 
						new File(
								"../res/04B_19.TTF"
						)
				));
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
			
			highScore = getHighScore();
			prevHighScore = highScore;
			
			gameOverScreen = new GameOverScreen(highScore, font04B_19);
			addMouseListener(gameOverScreen.getMenuButton());
			
			blackScreen = new BufferedImage(
					WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			blackScreenPixels = ((DataBufferInt) 
					blackScreen.getRaster().getDataBuffer()).getData();

			for(int i = 0; i < blackScreenPixels.length; ++i) {
				blackScreenPixels[i] = 0x000000;
			}

			gameThread = new Thread(new GameLoop());
			gameThread.start();
		}
	}

	// --- --- --- --- --- CONSTRUCTOR ---  --- --- --- --- 
	Game() {
		running = false;

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true); 
		requestFocus(); 

		frame = new JFrame("Flappy Bird Clone By Stamaniorec");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT); 
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}

	// --- --- --- --- --- STOP METHOD ---  --- --- --- --- 
	public synchronized void stop() {
		if(!running) {		
			frame.setVisible(false);
			try {
				gameThread.join();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// --- --- --- --- --- MAIN METHOD - STARTER ---  --- --- --- --- 
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.add(game);
		game.start();
	}

	public synchronized void keyPressed(KeyEvent e) {
		if(gameState == GameState.GAME_OVER) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				enterPlayingState();
			}
		}
	}
	
	public void keyReleased(KeyEvent arg0) { }
	public void keyTyped(KeyEvent arg0) { }
}
