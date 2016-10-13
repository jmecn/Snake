package net.jmecn.snake;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;

public class Main extends SimpleApplication {


	public Main() {
		super(new StatsAppState());
	}
	@Override
	public void simpleInitApp() {
		Game game = new Game();
		game.start();
		
		stateManager.attach(new GameState(game));
	}

	public static void main(String[] args) {
		Main app = new Main();
		app.start();
	}
}
