package net.jmecn.snake.core;

import com.simsilica.es.EntityComponent;

public class Collision implements EntityComponent {

	private float radius;

	public Collision(float radius) {
		super();
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	@Override
	public String toString() {
		return "Collision [radius=" + radius + "]";
	}

}
