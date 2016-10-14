package net.jmecn.snake.core;

import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

public class FollowService implements Service {

	private EntityData ed;
	private EntitySet entities;
	@Override
	public void initialize(Game game) {
		ed = game.getEntityData();
		entities = ed.getEntities(Follow.class, Position.class);
	}

	@Override
	public void update(long time) {
		entities.applyChanges();

		for(Entity e : entities) {
			Follow follow = e.get(Follow.class);
			EntityId parent = follow.getParent();
			float maxDist = follow.getDist();
			Position position = e.get(Position.class);
			Position parentPosition = ed.getComponent(parent, Position.class);
			
			Vector3f loc1 = position.getLocation();
			Vector3f loc2 = parentPosition.getLocation();
			
			maxDist *= maxDist;
			float dist = loc1.distanceSquared(loc2);
			if (dist > maxDist) {
				Vector3f linear = loc2.subtract(loc1).normalize();
				
				// TODO use a speed component
				linear.multLocal(SnakeConstants.speed);
				ed.setComponents(e.getId(), new Velocity(linear));
			}
		}
	}

	@Override
	public void terminate(Game game) {
		entities.release();
		entities = null;
	}

}
