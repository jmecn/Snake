package net.jmecn.snake;

import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

public class MovementService implements Service {

	private EntityData ed;
	private EntitySet entities;
	@Override
	public void initialize(Game game) {
		ed = game.getEntityData();
		entities = ed.getEntities(Position.class, Velocity.class);
	}

	/**
	 * ��λ�� = ��λ�� + �ٶ� * ʱ��;
	 */
	@Override
	public void update(long time) {
		float tpf = time / 1000000000f;
        entities.applyChanges();
        for( Entity e : entities ) {
            Position pos = e.get(Position.class);
            Velocity vel = e.get(Velocity.class);

            Vector3f loc = pos.getLocation();
            Vector3f linear = vel.getLinear();
            Vector3f newloc = loc.add( (float)(linear.x * tpf),
                           (float)(linear.y * tpf),
                           (float)(linear.z * tpf) );

            e.set(new Position(newloc, null));
        }
	}

	@Override
	public void terminate(Game game) {
		entities.release();
		entities = null;

	}

}
