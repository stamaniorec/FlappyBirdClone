
public class PlayButton extends GameButton {
	
	public PlayButton(String pathName) {
		super(pathName);
		
		x = Game.WIDTH / 2 - buttonImage.getWidth() / 2 - 5;
		y = 300;
	}

}
