package net.jmecn.snake.core;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

public class Tail implements EntityComponent {
	private EntityId id;

	public Tail(EntityId id) {
		this.id = id;
	}

	public EntityId getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Tail [id=" + id + "]";
	}
}
