import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public abstract class GameButton implements MouseListener {
	protected BufferedImage buttonImage;
	protected int x;
	protected int y;
	private boolean wasClicked;
	protected int buttonWidth;
	protected int buttonHeight;
	
	public GameButton(String pathName) {
		try {
			buttonImage = ImageIO.read(new File(pathName));
			
			buttonWidth = buttonImage.getWidth();
			buttonHeight = buttonImage.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		wasClicked = false;
	}
	
	public boolean wasClicked() {
		return wasClicked;
	}
	
	public void draw(Graphics g) {
		g.drawImage(buttonImage, x, y, null);
	}
	
	public void drawCollisionBox(Graphics g) {
		g.drawRect(x, y, buttonWidth, buttonHeight);
	}
	
	public void mouseClicked(MouseEvent e) {
		if((e.getX() > x) && (e.getX() < x + buttonImage.getWidth()) &&
				(e.getY() > y) && (e.getY() < y + buttonImage.getHeight())) {
			wasClicked = true;
		}
	}

	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent arg0) { }
	public void mouseReleased(MouseEvent arg0) { }
}
