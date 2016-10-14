package net.jmecn.snake.core;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

public class MovementService implements Service {

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
			loc = loc.add((float) (linear.x * tpf), (float) (linear.y * tpf), (float) (linear.z * tpf));

			// A little quaternion magic for adding rotational
			// velocity to orientation
			Quaternion orientation = pos.getFacing();
			orientation = addScaledVector(orientation, vel.getAngular(), tpf);
			orientation.normalizeLocal();

			e.set(new Position(loc, orientation));
		}
	}

	private Quaternion addScaledVector(Quaternion orientation, Vector3f v, float scale) {

		float x = orientation.getX();
		float y = orientation.getY();
		float z = orientation.getZ();
		float w = orientation.getW();

		Quaternion q = new Quaternion( v.x * scale,  v.y * scale,  v.z * scale, 0);
		q.multLocal(orientation);

		x = x + q.getX() * 0.5f;
		y = y + q.getY() * 0.5f;
		z = z + q.getZ() * 0.5f;
		w = w + q.getW() * 0.5f;

		return new Quaternion((float) x, (float) y, (float) z, (float) w);
	}

}
