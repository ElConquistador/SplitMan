package elcon.games.splitman;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import elcon.games.splitman.gui.GuiGame;
import elcon.games.splitman.gui.GuiScreen;

public class SplitMan implements Runnable {
	
	public static final int UPS = 30;
	public static final double NANOSECS = 1000000000.0 / (double) UPS;
	
	public static int WIDTH = 960;
	public static int HEIGHT = 540;
	
	public static SplitMan instance;
	
	public GuiScreen currentScreen;
	
	public void setScreen(GuiScreen screen) {
		if(currentScreen != null) {
			currentScreen.stop();
		}
		currentScreen = screen;
		currentScreen.setSize(WIDTH, HEIGHT);
		currentScreen.start();
	}
	
	public void start() {
		System.out.println("SplitMan starting...");
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setResizable(false);
			Display.setVSyncEnabled(true);
			Display.setTitle("SplitMan");
			Display.create();
		} catch(Exception e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}		
		WIDTH = Display.getWidth();
		HEIGHT = Display.getHeight();
		System.out.println("Display: " + WIDTH + " x " + HEIGHT);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		Resources.load();
		
		setScreen(new GuiGame());
		System.out.println("SplitMan started");
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		start();
		long now = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0;
		int frames = 0;
		int updates = 0;
		System.out.println("Initialized in " + ((now - lastTime) / 1000000000.0) + " seconds");
		while(!Display.isCloseRequested()) {
			now = System.nanoTime();
			delta += (now - lastTime) / NANOSECS;
			lastTime = now;
			while(delta >= 1) {
				update(updates);
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				Display.setTitle("SplitMan | FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}			
			Display.update();
			Display.sync(120);
		}
		stop();
	}

	public void update(int tick) {
		if(currentScreen != null) {
			currentScreen.update(tick);
		}
	}
	
	public void render() {
		if(Display.wasResized()) {
			WIDTH = Display.getWidth();
			HEIGHT = Display.getHeight();
			if(currentScreen != null) {
				currentScreen.setSize(WIDTH, HEIGHT);
			}
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		if(currentScreen != null) {
			currentScreen.render();
		}
	}
	
	public void stop() {
		Display.destroy();
		System.out.println("SplitMan stopped");
	}
	
	public static void main(String[] args) {
		//TODO: extract natives
		instance = new SplitMan();
		instance.run();
	}
}
