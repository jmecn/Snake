package net.jmecn.snake.core;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

public class Follow implements EntityComponent {

	private EntityId parent;
	private float dist;

	public Follow(EntityId parent, float dist) {
		this.parent = parent;
		this.dist = dist;
	}

	public EntityId getParent() {
		return parent;
	}

	public float getDist() {
		return dist;
	}

	@Override
	public String toString() {
		return "Follow [parent=" + parent + ", dist=" + dist + "]";
	}

}
