package net.jmecn.snake.core;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.filter.FieldFilter;

/**
 * 
 * @author yanmaoyuan
 * 
 */
public class CollisionService implements Service {

	private EntityData ed;
	private EntitySet bodys;
	private EntitySet heads;
	private EntitySet foods;

	@Override
	public void initialize(Game game) {
		ed = game.getEntityData();
		
		foods = ed.getEntities(
				new FieldFilter<Type>(Type.class, "value", Type.FOOD),
				Type.class, Position.class, Collision.class);
		
		heads = ed.getEntities(
				new FieldFilter<Type>(Type.class, "value", Type.HEAD),
				Type.class, Position.class, Collision.class);
		
		bodys = ed.getEntities(
				new FieldFilter<Type>(Type.class, "value", Type.BODY),
				Type.class, Position.class, Collision.class);

	}

	@Override
	public void terminate(Game game) {
		foods.release();
		foods = null;
		
		heads.release();
		heads = null;
		
		bodys.release();
		bodys = null;
	}

	@Override
	public void update(long time) {
		foods.applyChanges();
		heads.applyChanges();
		bodys.applyChanges();

		// eat food
		for(Entity food : foods) {
			for(Entity head : heads) {
				if (collision(food, head)) {
					System.out.println(head.getId() + "eat " + food.getId());
					ed.removeEntity(food.getId());
				}
			}
		}
		
		// head collision with body
		for(Entity body : bodys) {
			for(Entity head : heads) {
				EntityId belongsHead = ed.getComponent(body.getId(), Belongs.class).getParent();
				
				// 自身不检测
				if (head.getId() == belongsHead)
					continue;
				
				// 身体相撞
				if (collision(body, head)) {
					System.out.println(head.getId() + " dead");
					//ed.removeEntity(head.getId());
				}
			}
		}
		
	}

	protected void generateContacts(Entity e1, Entity e2) {
		if (e1 == e2)
			return;
		
		// 身体相撞
		EntityId p1 = ed.getComponent(e1.getId(), Belongs.class).getParent();
		EntityId p2 = ed.getComponent(e2.getId(), Belongs.class).getParent();
		
		if (p1 == p2) {
			return ;
		}
		
		Follow f1 = ed.getComponent(e1.getId(), Follow.class);
		Follow f2 = ed.getComponent(e2.getId(), Follow.class);
		
		if (f1 == null && f2 != null) {
			// e1 is head
			if (collision(e1, e2)) {
				System.out.println(e1.getId() + " dead");
			}
		} else if (f2 == null && f1 != null){
			// e2 is head
			if (collision(e1, e2)) {
				System.out.println(e2.getId() + " dead");
			}
		}
	}
	
	private boolean collision(Entity e1, Entity e2) {
		Position p1 = e1.get(Position.class);
		Position p2 = e2.get(Position.class);

		Collision s1 = e1.get(Collision.class);
		float r1 = s1.getRadius();
		Collision s2 = e2.get(Collision.class);
		float r2 = s2.getRadius();

		float threshold = r1 + r2;
		threshold *= threshold;

		float distSq = p1.getLocation().distanceSquared(p2.getLocation());
		
		return distSq <= threshold;
	}
	
}
