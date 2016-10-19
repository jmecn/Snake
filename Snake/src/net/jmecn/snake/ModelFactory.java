package net.jmecn.snake;

import static net.jmecn.snake.core.SnakeConstants.*;
import net.jmecn.snake.core.Collision;
import net.jmecn.snake.core.SnakeConstants;
import net.jmecn.snake.core.Type;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.simsilica.es.Entity;

/**
 * @author yanmaoyuan
 * 
 */
public class ModelFactory {

	private AssetManager assetManager;

	private MaterialFactory materialFactory;
	
	public ModelFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
		this.materialFactory = new MaterialFactory(assetManager);
		
		materialFactory.initFoodMaterials();
	}
	
	/**
	 * @param name
	 * @return
	 */
	public Spatial create(String name) {
		return assetManager.loadModel(name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Texture createTexture(String name) {
		return assetManager.loadTexture(name);
	}
	
	public Material getUnshadedMaterial() {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		mat.setFloat("AlphaDiscardThreshold", 0.01f);
		return mat;
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
				int skin = type.getSkin();
				return createWreck(radius, Skin.values()[skin-1]);
			}
		}
		case Type.HEAD: {
			float radius = collision == null ? SnakeConstants.snakeBodyRadius : collision.getRadius();
			int skin = type.getSkin();
			return createHead(radius, Skin.values()[skin-1]);
		}
		case Type.BODY: {
			float radius = collision == null ? SnakeConstants.snakeBodyRadius : collision.getRadius();
			int skin = type.getSkin();
			return createBody(radius, Skin.values()[skin-1]);
		}
		default:
			return null;
		}
	}
	
	public Spatial createFood(float radius) {
		Quad mesh = new Quad(radius*2, radius*2);
		
		Geometry geom = new Geometry("Food", mesh);

		geom.setMaterial(materialFactory.getRandomFoodMaterail());
		geom.setQueueBucket(Bucket.Translucent);
		
		Node node = new Node("food");
		node.attachChild(geom);
		geom.setLocalTranslation(-radius, -radius, 0);
		return node;
	}
	
	public Spatial createHead(float radius, Skin skin) {
		float width = radius * 2 * skin.headXScale;
		float height = radius * 2 * skin.headYScale;
		Quad mesh = new Quad(width, height);
		Geometry geom = new Geometry("body", mesh);

		Material mat = materialFactory.getHeadMat(skin);
		geom.setMaterial(mat);
		geom.setQueueBucket(Bucket.Translucent);
		
		Node node = new Node("head");
		node.attachChild(geom);
		geom.setLocalTranslation(-radius * skin.headXScale, -radius * skin.headYScale, 0);
		return node;
	}
	
	public Spatial createBody(float radius, Skin skin) {
		float width = radius * 2;
		float height = radius * 2;
		Quad mesh = new Quad(width, height);
		Geometry geom = new Geometry("body", mesh);

		Material mat = materialFactory.getBodyMat(skin, 1);
		geom.setMaterial(mat);
		geom.setQueueBucket(Bucket.Translucent);
		
		Node node = new Node("body");
		node.attachChild(geom);
		geom.setLocalTranslation(-radius, -radius, 0);
		return node;
	}
	
	public Spatial createWreck(float radius, Skin skin) {
		float width = radius * 2;
		float height = radius * 2;
		Quad mesh = new Quad(width, height);
		Geometry geom = new Geometry("body", mesh);

		Material mat;
		if (skin.wreck == 0) {// 没有残骸则使用身体的材质
			mat = materialFactory.getWreckMat(skin, 1);
		} else {
			mat = materialFactory.getBodyMat(skin, 1);
		}
		geom.setMaterial(mat);
		geom.setQueueBucket(Bucket.Translucent);
		
		Node node = new Node("body");
		node.attachChild(geom);
		geom.setLocalTranslation(-radius, -radius, 0);
		return node;
	}
	
	public Spatial createBackground() {
		Quad mesh = new Quad(width, height);
		mesh.scaleTextureCoordinates(new Vector2f(width/UNIT, height/UNIT));
		Geometry geom = new Geometry("BG", mesh);
		Material mat = getUnshadedMaterial();
		
		Texture tex = createTexture("Interface/tile.png");
		tex.setWrap(WrapMode.Repeat);
		mat.setTexture("ColorMap", tex);
		
		geom.setMaterial(mat);
		geom.setQueueBucket(Bucket.Translucent);
		return geom;
	}
}
