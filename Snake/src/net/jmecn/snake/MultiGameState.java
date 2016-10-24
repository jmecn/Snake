package net.jmecn.snake;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.es.EntityId;

public class MultiGameState extends BaseAppState {

	private EntityId player;

	public MultiGameState(EntityId player) {
		this.player = player;
	}

	@Override
	protected void initialize(Application app) {
		getStateManager().getState(MusicState.class).setSong("Sounds/bg.ogg");
	}

	@Override
	protected void cleanup(Application app) {
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
