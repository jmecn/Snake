package net.jmecn.snake.core;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

public class Velocity implements EntityComponent {

	private Vector3f linear;
	private Quaternion anglur;

	public Velocity() {
		this.linear = new Vector3f();
		this.anglur = new Quaternion();
	}

	public Velocity(Vector3f linear) {
		this.linear = linear;
		this.anglur = new Quaternion();
	}
	
	public Velocity(Vector3f linear, Quaternion anglur) {
		this.linear = linear;
		this.anglur = anglur;
	}

	public Vector3f getLinear() {
		return linear;
	}

	public Quaternion getAnglur() {
		return anglur;
	}

	@Override
	public String toString() {
		return "Velocity [linear=" + linear + ", anglur=" + anglur + "]";
	}
}
