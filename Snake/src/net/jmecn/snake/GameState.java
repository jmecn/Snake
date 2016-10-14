package net.jmecn.snake;

import net.jmecn.snake.core.Game;
import net.jmecn.snake.core.Tail;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityId;

public class GameState extends BaseAppState {

	private Game game;
	private EntityId player;
	private EntityId tail;// 尾巴

	private EntityId ai;
	public GameState(Game game) {
		this.game = game;

		for (int i = 0; i < 100; i++) {
			int x = FastMath.rand.nextInt(1024);
			int y = FastMath.rand.nextInt(768);
			game.getFactory().createFood(new Vector3f(x, y, 0));
		}

		Vector3f loc = new Vector3f(512, 384, 0);
		player = game.getFactory().createBody(loc);
		for (int i = 0; i < 10; i++) {
			if (tail == null) {
				tail = game.getFactory().createBody(loc, player, player);
			} else {
				tail = game.getFactory().createBody(loc, player, tail);
			}
		}
		game.getEntityData().setComponent(player, new Tail(tail));
		
		Vector3f aiLoc = new Vector3f(0, 0, 0);
		ai = game.getFactory().createBody(aiLoc);
		EntityId aiTail = null;
		for (int i = 0; i < 10; i++) {
			if (aiTail == null) {
				aiTail = game.getFactory().createBody(aiLoc, ai, ai);
			} else {
				aiTail = game.getFactory().createBody(aiLoc, ai, aiTail);
			}
		}
		game.getEntityData().setComponent(ai, new Tail(aiTail));
	}

	@Override
	protected void initialize(Application app) {
		getStateManager().attachAll(
				new EntityDataState(game.getEntityData()),
				new ModelState(),
				new HudState(player),
				new AiState(ai));
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
