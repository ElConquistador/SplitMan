package elcon.games.splitman.gui;

public abstract class GuiScreen extends Gui {

	public int width;
	public int height;
	
	public abstract void start();
	
	public abstract void update();
	
	public abstract void render();
	
	public abstract void stop();
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
