package net.jmecn.snake;

import net.jmecn.snake.core.Game;
import net.jmecn.snake.core.SnakeConstants;
import net.jmecn.snake.core.Tail;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

public class Main extends SimpleApplication {


	public Main() {
		super(new StatsAppState());
	}
	@Override
	public void simpleInitApp() {
		stateManager.attachAll(new MusicState());
		
		startSingleGame();
	}
	
	public void startSingleGame() {
		Game game = new Game();
		game.start();
		
		for (int i = 0; i < 200; i++) {
			game.getFactory().createFood();
		}

		Vector3f loc = new Vector3f(SnakeConstants.width/2, SnakeConstants.height/2, 0);
		EntityId player = game.getFactory().createHead(loc, 1);
		EntityId tail = null;
		for (int i = 0; i < SnakeConstants.snakeMinLength; i+= 6) {
			if (tail == null) {
				tail = game.getFactory().createBody(loc, 1, player, player);
			} else {
				tail = game.getFactory().createBody(loc, 1, player, tail);
			}
		}
		game.getEntityData().setComponent(player, new Tail(tail));
		
		getStateManager().attachAll(
				new GameState(game, player),
				new EntityDataState(game.getEntityData()),
				new ModelState(),
				new HudState(player));
	}

	public static void main(String[] args) {
		Main app = new Main();
		app.start();
	}
}
