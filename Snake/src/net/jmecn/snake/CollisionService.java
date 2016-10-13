package net.jmecn.snake;

import java.util.Set;

import com.jme3.util.SafeArrayList;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

/**
 * 
 * @author yanmaoyuan
 * 
 */
public class CollisionService implements Service {

	private Game game;
	private EntityData ed;
	private EntitySet entities;
	private SafeArrayList<Entity> colliders = new SafeArrayList<Entity>(Entity.class);

	@Override
	public void initialize(Game game) {
		this.game = game;
		ed = game.getEntityData();
		entities = ed.getEntities(Type.class, Position.class, Collision.class);

	}

	@Override
	public void update(long time) {
		if (entities.applyChanges()) {
			removeColliders(entities.getRemovedEntities());
			addColliders(entities.getAddedEntities());
		}

		Entity[] array = colliders.getArray();
		for (int i = 0; i < array.length; i++) {
			Entity e1 = array[i];
			for (int j = i + 1; j < array.length; j++) {
				Entity e2 = array[j];
				generateContacts(e1, e2);
			}
		}
	}

	@Override
	public void terminate(Game game) {
		entities.release();
		entities = null;
	}

	protected void addColliders(Set<Entity> set) {
		colliders.addAll(set);
	}

	protected void removeColliders(Set<Entity> set) {
		colliders.removeAll(set);
	}

	protected void generateContacts(Entity e1, Entity e2) {
		if (e1 == e2)
			return;
		
		Type m1 = e1.get(Type.class);
		Type m2 = e2.get(Type.class);
		
		if (Type.FOOD == m1.getValue() && Type.BODY == m2.getValue()) {
			Position p1 = e1.get(Position.class);
			Position p2 = e2.get(Position.class);
	
			Collision s1 = e1.get(Collision.class);
			float r1 = s1.getRadius();
			Collision s2 = e2.get(Collision.class);
			float r2 = s2.getRadius();
	
			float threshold = r1 + r2;
			threshold *= threshold;
	
			float distSq = p1.getLocation().distanceSquared(p2.getLocation());
			if (distSq > threshold) {
				return;
			}
			
			System.out.println("eat foor : " + e1);

			ed.removeEntity(e1.getId());
		}
	}

	protected void generateContacts() {

		Entity[] array = colliders.getArray();
		for (int i = 0; i < array.length; i++) {
			Entity e1 = array[i];
			for (int j = i + 1; j < array.length; j++) {
				Entity e2 = array[j];
				generateContacts(e1, e2);
			}
		}
	}
}
