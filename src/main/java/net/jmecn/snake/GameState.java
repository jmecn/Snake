package net.jmecn.snake;

import net.jmecn.snake.core.Game;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.es.EntityId;

public class GameState extends BaseAppState {

	private Game game;
	private EntityId player;

	public GameState(Game game, EntityId player) {
		this.game = game;
		this.player = player;
	}

	@Override
	protected void initialize(Application app) {
		getStateManager().getState(MusicState.class).setSong("Sounds/bg.ogg");
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

	public void setPlayer(EntityId player) {
		this.player = player;
	}

	public EntityId getPlayer() {
		return player;
	}
}
