package net.jmecn.snake.server;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class Entity {

	private Integer id;
	private int type;
	private Vector3f location;// 位置
	private Quaternion facing;// 朝向
	private Vector3f linear;// 线速度
	private float radius;
	private int skinId;// 皮肤

	public Entity(Integer id) {
		this.id = id;
		reset();
	}

	public void reset() {
		this.type = 0;
		this.location = new Vector3f(0, 0, 0);
		this.facing = new Quaternion();
		this.linear = new Vector3f(1, 0, 0);
		this.radius = 0;
		this.skinId = -1;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		this.location.set(location);
	}

	public Quaternion getFacing() {
		return facing;
	}

	public void setFacing(Quaternion facing) {
		this.facing.set(facing);
	}

	public Vector3f getLinear() {
		return linear;
	}

	public void setLinear(Vector3f linear) {
		this.linear.set(linear);
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getSkinId() {
		return skinId;
	}

	public void setSkinId(int skinId) {
		this.skinId = skinId;
	}

	@Override
	public String toString() {
		return "Entity [id=" + id + ", type=" + type + ", location=" + location
				+ ", facing=" + facing + ", linear=" + linear + ", radius="
				+ radius + ", skinId=" + skinId + "]";
	}

}
