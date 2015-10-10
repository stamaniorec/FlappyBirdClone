public class Timer {
	
	// Core Timer variables	
	private int FPS;
	private long lastTime;
	private int timer;

	// Debug Timer variables, i.e. for displaying FPS
	private int frameCount;
	private long lastTimeDebug;
	private boolean debug;

	// default: 60 FPS, no debug
	public Timer() {
		FPS = 60;
		lastTime = System.nanoTime();
		timer = 1000000000 / FPS;

		frameCount = 0;
		lastTimeDebug = System.nanoTime();
		debug = false;
	}

	public Timer(int FPS, boolean debug) {
		this.FPS = FPS;
		lastTime = System.nanoTime();
		timer = 1000000000 / FPS;
		frameCount = 0;
		lastTimeDebug = System.nanoTime();
		this.debug = debug;
	}

	public boolean update() {
		boolean flag;
		if(System.nanoTime() - lastTime >= timer) {
			frameCount++;
			lastTime = System.nanoTime();
			flag = true;
		} else {
			flag = false;
		}
		if(debug)
			printFPS();
		return flag;
	}

	public void printFPS() {
		if(System.nanoTime() - lastTimeDebug >= 1000000000) { // print once a second
			System.out.println("FPS: " + frameCount);
			frameCount = 0;
			lastTimeDebug = System.nanoTime();
		} 
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setFPS(int FPS) {
		this.FPS = FPS;
		timer = 1000000000 / FPS;
	}

	public int getFPS() {
		return FPS;
	}
}