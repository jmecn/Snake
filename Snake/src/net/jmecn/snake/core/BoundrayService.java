package net.jmecn.snake.core;

import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

public class BoundrayService implements Service {

	EntityData ed;
	EntitySet entities;
	
	Vector3f min = new Vector3f(0, 0, 0);
	Vector3f max = new Vector3f(SnakeConstants.width, SnakeConstants.height, 0);
	public void initialize(Game game) {
		ed = game.getEntityData();
		entities = ed.getEntities(Position.class);
	}

	public void update(long time) {
		if (entities.applyChanges()) {
			for(Entity e : entities) {
				Vector3f loc = e.get(Position.class).getLocation();
				
				if (loc.x < min.x || loc.x> max.x || loc.y<min.y || loc.y > max.y) {
					e.set(new Decay(0));
				}
			}
		}
	}

	public void terminate(Game game) {
		entities.release();
		entities = null;
	}

}
