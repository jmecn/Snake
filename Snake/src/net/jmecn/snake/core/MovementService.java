package net.jmecn.snake.core;

import org.apache.log4j.Logger;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

public class MovementService implements Service {

	static Logger log = Logger.getLogger(MovementService.class);

	private EntityData ed;
	private EntitySet entities;

	private long lastFrame;

	@Override
	public void initialize(Game game) {
		ed = game.getEntityData();
		entities = ed.getEntities(Position.class, Velocity.class);
		lastFrame = System.nanoTime();
	}

	@Override
	public void terminate(Game game) {
		entities.release();
		entities = null;
	}

	@Override
	public void update(long time) {

		// Use our own tpf calculation in case frame rate is
		// running away making this tpf unstable
		long delta = System.nanoTime() - lastFrame;
		lastFrame = System.nanoTime();
		if (delta == 0) {
			return; // no update to perform
		}

		float tpf = time / 1000000000f;

		// Clamp frame time to no bigger than a certain amount
		// to prevent physics errors. A little jitter for slow frames
		// is better than tunneling/ghost objects
		if (tpf > 0.1) {
			tpf = 0.1f;
		}

		// Make sure we have the latest set but we
		// don't really care who left or joined
		entities.applyChanges();
		for (Entity e : entities) {
			Position pos = e.get(Position.class);
			Velocity vel = e.get(Velocity.class);

			Vector3f loc = pos.getLocation();
			Vector3f linear = vel.getLinear();
			loc = loc.add((float) (linear.x * tpf), (float) (linear.y * tpf), 0);

			// 让模型的脸朝向线速度的方向
			Quaternion a = getFacing(linear.x, linear.y);

			e.set(new Position(loc, a));
		}
	}

	/**
	 * 在XOY平面上，根据线速度的方向，计算图片当前的朝向。
	 * 
	 * <pre>
	 * (x*x + y*y) == 1
	 * vec3 xAxis = (y, -x, 0)
	 * vec3 yAxis = (x, y, 0)
	 * vec3 zAxis = (0, 0, 1)
	 * 
	 * Matrix3 rot = (
	 * y,  x,  0,
	 * -x, y,  0,
	 * 0,  0,  1)
	 * </pre>
	 *
	 * @see <code>com.jme3.math.Quaternion.fromRotationMatrix</code>
	 * @param x
	 * @param y
	 * @return
	 */
	protected Quaternion getFacing(float x, float y) {
		// normalize
		float length = x * x + y * y;
		if (length != 1f && length != 0f) {
			length = 1f / FastMath.sqrt(length);
			x *= length;
			y *= length;
		}
		
		float z = 0, w = 1;
		float t = y + y + 1;
		if (t >= 0) {
			float s = FastMath.sqrt(t + 1);
			w = 0.5f * s;
			s = 0.5f / s;
			z = -2 * x * s;
		} else {
			float s = FastMath.sqrt(3f - t);
			z = s * 0.5f;
			s = 0.5f / s;
			w = -2 * x * s;
		}

		Quaternion q = new Quaternion(0, 0, z, w);

		return q;
	}

	/**
	 * 在XOY平面上，根据线速度的方向，计算图片当前的朝向。
	 * 
	 * @param linear
	 * @return
	 */
	protected Quaternion getFacing(Vector3f linear) {
		Vector3f y = linear.normalize();
		Vector3f x = new Vector3f(y.y, -y.x, 0);
		Quaternion q = new Quaternion().fromAxes(x, y, Vector3f.UNIT_Z);
		return q;
	}
}
