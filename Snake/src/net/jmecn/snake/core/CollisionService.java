package net.jmecn.snake.core;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.jme3.math.Vector3f;
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
	static Logger log = Logger.getLogger(CollisionService.class);
	
	private Game game = null;
	
	private EntityData ed;
	private EntitySet bodys;
	private EntitySet heads;
	private EntitySet foods;
	
	private ArrayList<Entity> bodyList;
	private ArrayList<Entity> foodList;
	private ArrayList<Entity> headList;
	
	public CollisionService() {
		bodyList = new ArrayList<Entity>();
		foodList = new ArrayList<Entity>();
		headList = new ArrayList<Entity>();
	}

	@Override
	public void initialize(Game game) {
		ed = game.getEntityData();
		this.game = game;
		
		foods = ed.getEntities(
				new FieldFilter<Type>(Type.class, "value", Type.FOOD),
				Type.class, Position.class, Collision.class);
		
		heads = ed.getEntities(
				new FieldFilter<Type>(Type.class, "value", Type.HEAD),
				Type.class, Position.class, Collision.class);
		
		bodys = ed.getEntities(
				new FieldFilter<Type>(Type.class, "value", Type.BODY),
				Type.class, Position.class, Collision.class);

		
		foodList.addAll(foods);
		headList.addAll(heads);
		bodyList.addAll(bodys);
	}

	@Override
	public void terminate(Game game) {
		// 释放对象
		foodList.clear();
		foodList = null;
		
		headList.clear();
		headList = null;
		
		bodyList.clear();
		bodyList = null;
		
		foods.release();
		foods = null;
		
		heads.release();
		heads = null;
		
		bodys.release();
		bodys = null;

	}

	@Override
	public void update(long time) {
		if (foods.applyChanges()) {
			foodList.addAll(foods.getAddedEntities());
			foodList.removeAll(foods.getRemovedEntities());
		}
		
		if (heads.applyChanges()) {
			headList.addAll(heads.getAddedEntities());
			headList.removeAll(heads.getRemovedEntities());
		}
		
		if (bodys.applyChanges()) {
			bodyList.addAll(bodys.getAddedEntities());
			bodyList.removeAll(bodys.getRemovedEntities());
		}

		int lenF = foodList.size();
		int lenH = headList.size();
		int lenB = bodyList.size();

		// eat food
		for(int i=0; i<lenF; i++) {
			Entity food = foodList.get(i);
			for(int j=0; j<lenH; j++) {
				Entity head = headList.get(j);
				float delta = collision(food, head);
				if (delta < 0) {
					Length len = ed.getComponent(head.getId(), Length.class);
					int value = len.getValue() + 1;
					ed.setComponent(head.getId(), new Length(value));
					
					if (value % SnakeConstants.snakeBodyGrow == 0) {
						Tail tail = ed.getComponent(head.getId(), Tail.class);
						log.info(tail);
						
						Position p = ed.getComponent(tail.getId(), Position.class);
						log.info(p);
						
						int skin = head.get(Type.class).getSkin();
						EntityId newTail = game.getFactory().createBody(p.getLocation(), skin, head.getId(), tail.getId());
						ed.setComponent(head.getId(), new Tail(newTail));
						log.info("create new tail" + newTail + " with skin: " + skin);
					}
					
					ed.removeEntity(food.getId());
					game.getFactory().createFood();
				} else if (delta >0 && delta <= SnakeConstants.foodMovingDistance) {
					// 蛇头吸附附近的食物
					Position p1 = food.get(Position.class);
					Position p2 = head.get(Position.class);
					Vector3f linear = p2.getLocation().subtract(p1.getLocation()).normalize();
					linear.multLocal(SnakeConstants.speed);
					
					food.set(new Velocity(linear));
				}
			}
		}
		
		// head collision with body
		for(int i=0; i<lenB; i++) {
			Entity body = bodyList.get(i);
			for(int j=0; j<lenH; j++) {
				Entity head = headList.get(j);
				EntityId belongsHead = ed.getComponent(body.getId(), Belongs.class).getParent();
				
				// 自身不检测
				if (head.getId() == belongsHead)
					continue;
				
				// 身体相撞
				if (collision(body, head) < 0) {
					log.info(head.getId() + " dead");
					head.set(new Dead());
					
				}
			}
		}
		
	}

	/**
	 * 碰撞检测
	 * @param e1
	 * @param e2
	 * @return
	 */
	private float collision(Entity e1, Entity e2) {
		Position p1 = e1.get(Position.class);
		Position p2 = e2.get(Position.class);

		Collision s1 = e1.get(Collision.class);
		float r1 = s1.getRadius();
		Collision s2 = e2.get(Collision.class);
		float r2 = s2.getRadius();

		float threshold = r1 + r2;
		threshold *= threshold;

		float distSq = p1.getLocation().distanceSquared(p2.getLocation());
		
		return distSq - threshold;
	}
	
}
