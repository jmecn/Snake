package net.jmecn.snake;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

import net.jmecn.snake.core.Collision;
import net.jmecn.snake.core.Position;
import net.jmecn.snake.core.Type;

public class ModelState extends BaseAppState {

	static Logger log = LoggerFactory.getLogger(ModelState.class);
	
	private SimpleApplication simpleApp;

	private EntityData ed;
	private EntitySet entities;

	private final Node rootNode;
	
	private final Map<EntityId, Spatial> models;
	private ModelFactory modelFactory;

	public ModelState() {
		rootNode = new Node("VisualNode");
		models = new HashMap<EntityId, Spatial>();
	}

	@Override
	protected void initialize(Application app) {
		simpleApp = (SimpleApplication) app;
		Camera cam = app.getCamera();
		float x = cam.getWidth() * 0.5f;
		float y = cam.getHeight() * 0.5f;
		cam.setLocation(new Vector3f(x, y, 500));
		cam.lookAt(new Vector3f(x, y, 0), Vector3f.UNIT_Y);
		
		ed = getStateManager().getState(EntityDataState.class).getEntityData();
		entities = ed.getEntities(Position.class, Type.class, Collision.class);

		simpleApp.getViewPort().setBackgroundColor(new ColorRGBA(0.9179f, 0.9213f, 0.9345f, 1f));

		modelFactory = new ModelFactory(simpleApp.getAssetManager());
		
		Spatial bg = modelFactory.createBackground();
		bg.move(0, 0, -3);
		simpleApp.getRootNode().attachChild(bg);
		
	}

	@Override
	protected void cleanup(Application app) {
		entities.release();
		entities = null;
	}
	

	@Override
	protected void onEnable() {
		simpleApp.getRootNode().attachChild(rootNode);
		entities.applyChanges();
		addModels(entities);
	}

	@Override
	protected void onDisable() {
		rootNode.removeFromParent();
		removeModels(entities);
	}

	Vector3f camLoc = new Vector3f();
	@Override
	public void update(float tpf) {
		if (entities.applyChanges()) {
			removeModels(entities.getRemovedEntities());
			addModels(entities.getAddedEntities());
			updateModels(entities.getChangedEntities());
		}
	}

	private void removeModels(Set<Entity> entities) {
		for (Entity e : entities) {
			Spatial s = models.remove(e.getId());
			s.removeFromParent();
		}
	}

	private void addModels(Set<Entity> entities) {
		for (Entity e : entities) {
			Spatial s = createVisual(e);
			models.put(e.getId(), s);
			updateModelSpatial(e, s);
			rootNode.attachChild(s);
		}
	}

	private void updateModels(Set<Entity> entities) {
		for (Entity e : entities) {
			Spatial s = models.get(e.getId());
			updateModelSpatial(e, s);
		}
	}

	private void updateModelSpatial(Entity e, Spatial s) {
		Position p = e.get(Position.class);
		s.setLocalTranslation(p.getLocation());
		s.setLocalRotation(p.getFacing());
	}

	private Spatial createVisual(Entity e) {
		Spatial s = modelFactory.create(e);
		
		return s;
	}
}