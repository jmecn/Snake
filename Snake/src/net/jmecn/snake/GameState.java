package net.jmecn.snake;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

public class GameState extends BaseAppState {
	
	private Game game;
	private EntityId player;
	
	public GameState(Game game) {
		this.game = game;
		
		for( int i=0; i<100; i++) {
			int x = FastMath.rand.nextInt(1024);
			int y = FastMath.rand.nextInt(768);
			game.getFactory().createFood(new Vector3f(x, y, 0));
		}
		
		player = game.getFactory().createBody(new Vector3f(512, 384, 0));
	}

	@Override
	protected void initialize(Application app) {
		getStateManager().attachAll(
				new EntityDataState(game.getEntityData()),
				new ModelState(),
				new InputState(player));
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
