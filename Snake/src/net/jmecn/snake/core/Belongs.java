package net.jmecn.snake.core;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

public class Belongs implements EntityComponent {
	private EntityId parent;

	public Belongs(EntityId parent) {
		this.parent = parent;
	}

	public EntityId getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return "Belongs [parent=" + parent + "]";
	}
}
