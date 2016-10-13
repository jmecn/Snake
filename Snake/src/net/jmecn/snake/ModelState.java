package net.jmecn.snake;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

public class ModelState extends BaseAppState {

	static Logger log = Logger.getLogger(ModelState.class);
	
	private SimpleApplication simpleApp;
	private Camera cam;

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

		ed = getStateManager().getState(EntityDataState.class).getEntityData();
		entities = ed.getEntities(Position.class, Type.class, Collision.class);

		simpleApp.getViewPort().setBackgroundColor(new ColorRGBA(0.75f, 0.875f, 1f, 1f));

		modelFactory = new ModelFactory(simpleApp.getAssetManager());
	}

	@Override
	protected void cleanup(Application app) {
		entities.release();
		entities = null;
	}
	

	@Override
	protected void onEnable() {
		simpleApp.getGuiNode().attachChild(rootNode);
		entities.applyChanges();
		addModels(entities);
	}

	@Override
	protected void onDisable() {
		rootNode.removeFromParent();
		removeModels(entities);
	}

	@Override
	public void update(float tpf) {
		if (entities.applyChanges()) {
			log.info("Model Counts: " + entities.size());
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