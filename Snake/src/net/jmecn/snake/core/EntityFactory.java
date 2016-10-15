package net.jmecn.snake.core;

import org.apache.log4j.Logger;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class EntityFactory {

	static Logger log = Logger.getLogger(EntityFactory.class);
	
	EntityData ed;
	
	public EntityFactory(EntityData ed) {
		this.ed = ed;
	}
	
	public EntityId createFood() {
		float x = FastMath.rand.nextFloat() * SnakeConstants.width;
		float y = FastMath.rand.nextFloat() * SnakeConstants.height;
		
		EntityId id = ed.createEntity();
		ed.setComponents(id,
				new Position(new Vector3f(x, y, 0)),
				new Type(Type.FOOD),
				new Collision(SnakeConstants.foodRadius));
		return id;
	}
	
	public EntityId createHead(Vector3f location, int skin) {
		EntityId id = ed.createEntity();
		ed.setComponents(id,
				new Position(location),
				new Type(Type.HEAD, skin),
				new Length(SnakeConstants.snakeMinLength),
				new Velocity(new Vector3f(SnakeConstants.speed, 0, 0)),
				new Belongs(id),
				new Collision(SnakeConstants.snakeBodyRadius));
		return id;
	}
	
	public EntityId createBody(Vector3f location, int skin, EntityId parent, EntityId follow) {
		EntityId id = ed.createEntity();
		log.info("create body " + id);
		ed.setComponents(id,
				new Position(location),
				new Type(Type.BODY, skin),
				new Collision(SnakeConstants.snakeBodyRadius),
				new Belongs(parent),
				new Follow(follow, SnakeConstants.snakeBodyRadius * 1.4f)
		);
		return id;
	}
	
	public EntityId createDeadBody(Vector3f loc, int skin) {
		
		EntityId id = ed.createEntity();
		ed.setComponents(id,
				new Position(loc),
				new Type(Type.FOOD, skin),
				new Collision(SnakeConstants.deadBodyRadius));
		return id;
	}
	
	public EntityId createPlayer() {
		float x = FastMath.rand.nextFloat() * SnakeConstants.width;
		float y = FastMath.rand.nextFloat() * SnakeConstants.height;
		
		Vector3f aiLoc = new Vector3f(x, y, 0);
		EntityId ai = createHead(aiLoc, 1);
		EntityId aiTail = null;
		for (int i = 0; i < 10; i++) {
			if (aiTail == null) {
				aiTail = createBody(aiLoc, 1, ai, ai);
			} else {
				aiTail = createBody(aiLoc, 1, ai, aiTail);
			}
		}
		
		ed.setComponents(ai, new Tail(aiTail), new AI());
		
		return ai;
	}
	
	public EntityId createAI() {
		float x = FastMath.rand.nextFloat() * SnakeConstants.width;
		float y = FastMath.rand.nextFloat() * SnakeConstants.height;
		
		Vector3f loc = new Vector3f(x, y, 0);
		EntityId head = createHead(loc, 1);
		EntityId tail = null;
		for (int i = 0; i < 10; i++) {
			if (tail == null) {
				tail = createBody(loc, 1, head, head);
			} else {
				tail = createBody(loc, 1, head, tail);
			}
		}
		
		ed.setComponents(head, new Tail(tail), new AI());
		
		return head;
	}
}
