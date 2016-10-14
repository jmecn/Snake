package net.jmecn.snake;

import net.jmecn.snake.core.Position;
import net.jmecn.snake.core.Velocity;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

public class AiState extends BaseAppState {
	private EntityData ed;
	private EntitySet entities;
	
	private EntityId ai;
	
	Vector3f linear = new Vector3f(200, 0, 0);
	
	public AiState(EntityId ai) {
		this.ai = ai;
	}
	
	@Override
	protected void initialize(Application app) {
		ed = getStateManager().getState(EntityDataState.class).getEntityData();
		entities = ed.getEntities(Position.class, Velocity.class);
	}

	@Override
	protected void cleanup(Application app) {
		entities.release();
		entities = null;
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	float angle = 0;
	public void update(float tpf) {
		angle += tpf * FastMath.QUARTER_PI;
		float x = FastMath.sin(angle) * 200;
		float y = FastMath.cos(angle) * 200;
		linear.set(x, y, 0);
		
		ed.setComponent(ai, new Velocity(linear));
	}
}
