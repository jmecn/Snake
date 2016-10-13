package net.jmecn.snake;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class EntityFactory {

	EntityData ed;
	
	public EntityFactory(EntityData ed) {
		this.ed = ed;
	}
	
	public EntityId createFood(Vector3f location) {
		EntityId id = ed.createEntity();
		ed.setComponents(id,
				new Position(location),
				new Type(Type.FOOD),
				new Collision(2));
		return id;
	}
	
	public EntityId createBody(Vector3f location) {
		EntityId id = ed.createEntity();
		ed.setComponents(id,
				new Position(location),
				new Type(Type.FOOD),
				new Collision(2));
		return id;
	}
}
