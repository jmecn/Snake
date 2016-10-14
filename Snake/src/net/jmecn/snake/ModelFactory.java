package net.jmecn.snake;

import net.jmecn.snake.core.Collision;
import net.jmecn.snake.core.Type;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.simsilica.es.Entity;

/**
 * @author yanmaoyuan
 * 
 */
public class ModelFactory {

	private AssetManager assetManager;

	public ModelFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	/**
	 * @param name
	 * @return
	 */
	public Spatial create(String name) {
		return assetManager.loadModel(name);
	}

	public Material getUnshadedMaterial() {
		return new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	}

	public Spatial create(Entity e) {
		Type type = e.get(Type.class);
		Collision collision = e.get(Collision.class);
		
		switch (type.getValue()) {
		case Type.FOOD: {
			float radius = collision == null ? 6 : collision.getRadius();

			Sphere sphere = new Sphere(3, 5, radius);
			Geometry geom = new Geometry("Food", sphere);

			Material mat = getUnshadedMaterial();
			mat.setColor("Color", ColorRGBA.randomColor());
			geom.setMaterial(mat);
			return geom;
		}
		case Type.HEAD:
		case Type.BODY: {
			float radius = collision == null ? 6 : collision.getRadius();

			Sphere sphere = new Sphere(3, 5, radius);
			Geometry geom = new Geometry("Food", sphere);

			Material mat = getUnshadedMaterial();
			mat.setColor("Color", ColorRGBA.Yellow);
			geom.setMaterial(mat);
			return geom;
		}
		default:
			return null;
		}
	}
}
