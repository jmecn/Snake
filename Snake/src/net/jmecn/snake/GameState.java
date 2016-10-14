package net.jmecn.snake;

import net.jmecn.snake.core.Game;
import net.jmecn.snake.core.Tail;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

public class GameState extends BaseAppState {

	private Game game;
	private EntityId player;
	private EntityId tail;// 尾巴

	public GameState(Game game) {
		this.game = game;

		for (int i = 0; i < 200; i++) {
			game.getFactory().createFood();
		}

		Vector3f loc = new Vector3f(512, 384, 0);
		player = game.getFactory().createHead(loc);
		for (int i = 0; i < 5; i++) {
			if (tail == null) {
				tail = game.getFactory().createBody(loc, player, player);
			} else {
				tail = game.getFactory().createBody(loc, player, tail);
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
