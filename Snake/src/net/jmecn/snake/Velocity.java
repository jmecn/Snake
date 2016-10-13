package net.jmecn.snake;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

public class Velocity implements EntityComponent {

	private Vector3f linear;
	
	public Velocity() {
		linear = new Vector3f();
	}

	public Velocity(Vector3f linear) {
		this.linear = linear;
	}

	public Vector3f getLinear() {
		return linear;
	}

	@Override
	public String toString() {
		return "Velocity [linear=" + linear + "]";
	}
	
}
