package net.jmecn.snake;

import net.jmecn.snake.core.Game;
import net.jmecn.snake.core.SnakeConstants;
import net.jmecn.snake.core.Tail;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

public class GameState extends BaseAppState {

	private Game game;
	private EntityId player;

	public GameState(Game game) {
		this.game = game;

		for (int i = 0; i < 200; i++) {
			game.getFactory().createFood();
		}

		Vector3f loc = new Vector3f(SnakeConstants.width/2, SnakeConstants.height/2, 0);
		player = game.getFactory().createHead(loc, 1);
		EntityId tail = null;
		for (int i = 0; i < SnakeConstants.snakeMinLength; i+= 6) {
			if (tail == null) {
				tail = game.getFactory().createBody(loc, 1, player, player);
			} else {
				tail = game.getFactory().createBody(loc, 1, player, tail);
			}
		}
		game.getEntityData().setComponent(player, new Tail(tail));
	}

	@Override
	protected void initialize(Application app) {
		getStateManager().attachAll(
				new EntityDataState(game.getEntityData()),
				new ModelState(),
				new HudState(player));
	}

	@Override
	protected void cleanup(Application app) {
		game.stop();
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	public EntityId getPlayer() {
		return player;
	}
}
