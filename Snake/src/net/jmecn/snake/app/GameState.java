package net.jmecn.snake.app;

import net.jmecn.snake.MusicState;
import net.jmecn.snake.server.Game;
import net.jmecn.snake.server.Snake;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

public class GameState extends BaseAppState {

	private Game game;
	private Snake me;

	public GameState() {
		game = new Game();
		game.start();
	}

	@Override
	protected void initialize(Application app) {
		me = game.createSnake("www.jmecn.com");

		getStateManager().getState(MusicState.class).setSong("Sounds/bg.ogg");
		getStateManager().attachAll(new HudState(me), new ModelState(game));
	}

	@Override
	protected void cleanup(Application app) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDisable() {
		// TODO game.disconnect();
		game.disconnect(me);
		game.stop();

	}

}
