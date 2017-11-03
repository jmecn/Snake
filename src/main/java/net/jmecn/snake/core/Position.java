package net.jmecn.snake.core;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

public class Position implements EntityComponent {

	private Vector3f location;
	private Quaternion facing;

	public Position() {
		this.location = new Vector3f();
		this.facing = new Quaternion();
	}
	
	public Position(Vector3f location) {
		this.location = location;
		this.facing = new Quaternion();
	}

	public Position(Vector3f location, Quaternion facing) {
		this.location = location;
		this.facing = facing;
	}

	public Vector3f getLocation() {
		return location;
	}

	public Quaternion getFacing() {
		return facing;
	}

	@Override
	public String toString() {
		return "Position [location=" + location + ", facing=" + facing + "]";
	}

}
