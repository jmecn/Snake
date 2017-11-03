package net.jmecn.snake.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import com.simsilica.es.filter.FieldFilter;

public class DeadService implements Service {
	static Logger log = LoggerFactory.getLogger(DeadService.class);
	private Game game;
	private EntityData ed;
	private EntitySet entities;
	public void initialize(Game game) {
		this.game = game;
		ed = game.getEntityData();
		entities = ed.getEntities(
				new FieldFilter<Type>(Type.class, "value", Type.HEAD),
				Dead.class, Type.class);
	}

	public void update(long time) {
		if (entities.applyChanges()) {
			for(Entity dead : entities) {
				EntitySet deadBodys  = ed.getEntities(
						new FieldFilter<Belongs>(Belongs.class, "parent", dead.getId()),
						Belongs.class, Position.class, Type.class);
				deadBodys.applyChanges();
				
				for(Entity deadBody : deadBodys) {
					log.info("body " + deadBody.getId() + " is dead");
					// 头不要
					if (deadBody.getId() != dead.getId()) {
						Vector3f loc = deadBody.get(Position.class).getLocation();
						int skin = deadBody.get(Type.class).getSkin();
						game.getFactory().createDeadBody(loc, skin);
					}
					
					ed.removeEntity(deadBody.getId());
				}
				
				deadBodys.release();
				deadBodys = null;
				
				ed.removeEntity(dead.getId());
			}
		}
		
	}

	public void terminate(Game game) {
		entities.release();
		entities = null;
	}

}
