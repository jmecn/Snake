package net.jmecn.snake;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
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
			float radius = collision == null ? 0.05f : collision.getRadius();

			Sphere sphere = new Sphere(6, 6, radius);
			Geometry geom = new Geometry("Food", sphere);

			Material mat = getUnshadedMaterial();
			mat.setColor("Color", ColorRGBA.Blue);
			geom.setMaterial(mat);
			return geom;
		}
		case Type.BODY: {
			float radius = collision == null ? 0.05f : collision.getRadius();

			Sphere sphere = new Sphere(6, 6, radius);
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
