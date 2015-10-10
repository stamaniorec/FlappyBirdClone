import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class PipeManager {

	private ArrayList<Pipe> normalPipes;
	private ArrayList<Pipe> rotatedPipes;
	
	private static final int pipeHorizontalSpacing = 150;
	private static final int pipeVerticalSpacing = 150;
	
	// 2 is the actual number, but I doubled it just in case
	private static final int MAX_PIPE_NUM = 4;
	
	public PipeManager() {
		normalPipes = new ArrayList<Pipe>();
		rotatedPipes = new ArrayList<Pipe>();
	}
	
	/**
	 * Clears the ArrayLists, and adds MAX_PIPE_NUM pairs of pipes
	 */
	public void init() {
		// Clear the ArrayLists
		normalPipes.clear();
		rotatedPipes.clear();
		
		// Add the first pipe
		int generatedY = generatePipeY();
		normalPipes.add(new Pipe(Game.WIDTH, generatedY, true));
		rotatedPipes.add(new Pipe(
				Game.WIDTH, generatedY + Pipe.pipeImageHeight + pipeVerticalSpacing, false));
		
		// Then add another three
		// There will never be more than MAX_PIPE_NUM pipes on the screen
		for(int i = 0; i < MAX_PIPE_NUM - 1; ++i) {
			addPipePair();
		}
	}
	
	/**
	 * @return if the bird has collided with any pipe of any pair 
	 */
	public boolean collision(Bird bird) {
		for(Pipe p : normalPipes) {
			if(p.collision(bird)) {
				return true;
			}
		}
		for(Pipe p : rotatedPipes) {
			if(p.collision(bird)) {
				return true;
			}
		}
		return false;
	}
	
	public void addPipePair() {
		int generatedY = generatePipeY();
		Pipe lastRotatedPipe = rotatedPipes.get(rotatedPipes.size()-1);
		// Add a new RotatedPipe with an X of the last pipe's X + the 
		// necessary spacing applied
		rotatedPipes.add(new Pipe(
				lastRotatedPipe.getX() + Pipe.pipeImageWidth + pipeHorizontalSpacing, 
				generatedY, true)
		);
		
		// Same with the normal pipes
		Pipe lastNormalPipe = normalPipes.get(normalPipes.size()-1);
		normalPipes.add(new Pipe(
				lastNormalPipe.getX() + Pipe.pipeImageWidth + pipeHorizontalSpacing, 
				generatedY + Pipe.pipeImageHeight + pipeVerticalSpacing, false)
		);
	}
	
	/**
	 * @return an appropriate y value for a rotated pipe
	 */
	private int generatePipeY() {
		Random r = new Random();
		
		// graphically speaking
		int upperLimitForPipeY = -(Pipe.pipeImageHeight - Game.HEIGHT / 5); 
		int lowerLimitForPipeY = Game.HEIGHT - Game.HEIGHT / 4 - 
								pipeVerticalSpacing - Pipe.pipeImageHeight;
				
		return r.nextInt(lowerLimitForPipeY - upperLimitForPipeY + 1) 
				+ upperLimitForPipeY;
				
	}
	
	// For debug purposes
	public void printCoordinates() {
		for(Pipe p : normalPipes) {
			System.out.println(p.getX());
		}
		for(Pipe p : rotatedPipes) {
			System.out.println(p.getX());
		}
	}
	
	/**
	 * Draws all pipes in existance 
	 */
	public void drawPipes(Graphics g) {
		// Using an Iterator for better thread-safety
		// If you encounter thread-related problems, try making this method
		// synchronized, although you shouldn't, since it's expensive
		Iterator<Pipe> itr = normalPipes.iterator();
		while(itr.hasNext()) {
			Pipe p = itr.next();
			p.draw(g);
		}
		itr = rotatedPipes.iterator();
		while(itr.hasNext()) {
			Pipe p = itr.next();
			p.draw(g);
		}
	}
	
	/**
	 * Updates all pipes accordingly
	 * Handles scrolled off pipes by removing them and adding new ones
	 */
	public void updatePipes() {
		for(Pipe p : normalPipes) {
			p.update();
		}
		
		for(Pipe p : rotatedPipes) {
			p.update();
		}
		
		// If the first shown pipe is scrolled off the screen
		if(normalPipes.size() > 0) {
			if(normalPipes.get(0).isScrolled()) {
				// Add another pipe at the end and get rid of the first
				addPipePair();
				normalPipes.remove(0);
				rotatedPipes.remove(0);
			}
		}
		
	}
	
	/**
	 * Collects all uncollected points for all pipes more left than the bird
	 */
	public void collectPoints(Bird b) {
		for(Pipe p : normalPipes) {
			if(b.getX() > p.getX() + Pipe.pipeImageWidth) {
				if(!p.hasCollectedPoint()) {
					p.collectPoint();
					b.addPointToScore();
				}
			}
		}
	}
	
}
