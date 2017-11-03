package net.jmecn.snake.core;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

public class AiService implements Service {
	private EntityData ed;
	private EntitySet entities;
	
	Vector3f linear = new Vector3f(SnakeConstants.speed, 0, 0);


	public void initialize(Game game) {
		ed = game.getEntityData();
		entities = ed.getEntities(AI.class, Position.class, Velocity.class);
		
		for(int i=0; i<5; i++) {
			game.getFactory().createAI();
		}
	}

	float t = 0;
	public void update(long time) {
		float tpf = time / 1000000000.0f;
		t += tpf;
		
		if (t >= 5.0f) {
			t = 0f;
			for (Entity e : entities) {
				Quaternion q = new Quaternion().fromAngles(0f, 0f, FastMath.rand.nextFloat() * FastMath.TWO_PI);
				Vector3f v = q.mult(linear);
			
				ed.setComponent(e.getId(), new Velocity(v));
			}
		}
	}

	public void terminate(Game game) {
		entities.release();
		entities = null;
	}
}
