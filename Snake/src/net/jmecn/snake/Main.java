package net.jmecn.snake;

import net.jmecn.snake.core.EntityFactory;
import net.jmecn.snake.core.Game;
import net.jmecn.snake.core.SnakeConstants;
import net.jmecn.snake.core.Tail;
import net.jmecn.snake.server.NetworkedEntityData;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class Main extends SimpleApplication {

	public Main() {
		super(new MusicState(), new MainMenuState());
	}
	
	@Override
	public void simpleInitApp() {
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
	
	public void startNetGame() {
		EntityData ed = new NetworkedEntityData("My Game Server", 1, "localhost", 9942).getEntityData();
		EntityFactory factory = new EntityFactory(ed);
		
		Vector3f loc = new Vector3f(SnakeConstants.width/2, SnakeConstants.height/2, 0);
		EntityId player = factory.createHead(loc, 1);
		EntityId tail = null;
		for (int i = 0; i < SnakeConstants.snakeMinLength; i+= 6) {
			if (tail == null) {
				tail = factory.createBody(loc, 1, player, player);
			} else {
				tail = factory.createBody(loc, 1, player, tail);
			}
		}
		ed.setComponent(player, new Tail(tail));
		
		getStateManager().attachAll(
				new MultiGameState(player),
				new EntityDataState(ed),
				new ModelState(),
				new HudState(player));
	}

	public static void main(String[] args) {
		Main app = new Main();
		app.setPauseOnLostFocus(false);
		app.start();
	}
}
