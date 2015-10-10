
public class MenuButton extends GameButton {

	public MenuButton(String pathName) {
		super(pathName);
		x = Game.WIDTH / 2 - buttonImage.getWidth() / 2;
		y = Game.HEIGHT - buttonImage.getHeight() - 25;
	}
}
