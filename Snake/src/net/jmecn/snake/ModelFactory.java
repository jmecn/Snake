package net.jmecn.snake;

import net.jmecn.snake.core.Collision;
import net.jmecn.snake.core.SnakeConstants;
import net.jmecn.snake.core.Type;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
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
	
	public Spatial createBackground() {
		Geometry geom = new Geometry("BG", new Quad(SnakeConstants.width, SnakeConstants.height));
		Material mat = getUnshadedMaterial();
		mat.setTexture("ColorMap", assetManager.loadTexture("Interface/background.png"));
		geom.setMaterial(mat);
		return geom;
	}

	public Material getUnshadedMaterial() {
		return new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	}

	public Spatial create(Entity e) {
		Type type = e.get(Type.class);
		Collision collision = e.get(Collision.class);
		
		switch (type.getValue()) {
		case Type.FOOD: {
			float radius = collision == null ? SnakeConstants.foodRadius : collision.getRadius();
			if (type.getSkin() == -1) {
				return createFood(radius);
			} else {
				return createBody(radius, Skins.RED.body);
			}
		}
		case Type.HEAD: {
			float radius = collision == null ? SnakeConstants.snakeBodyRadius : collision.getRadius();
			int skin = type.getSkin();
			return createBody(radius, Skins.RED.head, Skins.RED.headScale);
		}
		case Type.BODY: {
			float radius = collision == null ? SnakeConstants.snakeBodyRadius : collision.getRadius();
			int skin = type.getSkin();
			return createBody(radius, Skins.RED.body, Skins.RED.bodyScale);
		}
		default:
			return null;
		}
	}
	
	private Spatial createFood(float radius) {
		Quad mesh = new Quad(radius*2, radius*2);
		Geometry geom = new Geometry("Food", mesh);

		Material mat = getUnshadedMaterial();
		mat.setTexture("ColorMap", assetManager.loadTexture("Interface/circle.png"));
		mat.setColor("Color", ColorRGBA.randomColor());
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		geom.setMaterial(mat);
		
		Node node = new Node("food");
		node.attachChild(geom);
		geom.setLocalTranslation(-radius, -radius, 0);
		return node;
	}
	
	protected Spatial createBody(float radius, String imgName) {
		return createBody(radius, imgName, Vector3f.UNIT_XYZ);
	}
	
	private Spatial createBody(float radius, String imgName, Vector3f scale) {
		float width = radius * 2 * scale.x;
		float height = radius * 2 * scale.y;
		Quad mesh = new Quad(width, height);
		Geometry geom = new Geometry("body", mesh);

		Material mat = getUnshadedMaterial();
		mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Skin/" + imgName));
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		geom.setMaterial(mat);
		
		Node node = new Node("body");
		node.attachChild(geom);
		geom.setLocalTranslation(-radius * scale.x, -radius * scale.y, 0);
		return node;
	}
}
