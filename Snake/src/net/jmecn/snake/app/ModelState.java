package net.jmecn.snake.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jmecn.snake.server.Entity;
import net.jmecn.snake.server.Game;
import net.jmecn.snake.server.Snake;

import org.apache.log4j.Logger;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ModelState extends BaseAppState {

	static Logger log = Logger.getLogger(ModelState.class);
	
	private SimpleApplication simpleApp;

	private final Node rootNode;
	
	private Game game;
	
	private final Map<Integer, Spatial> models;
	private ModelFactory modelFactory;

	public ModelState(Game game) {
		this.game = game;
		
		rootNode = new Node("VisualNode");
		models = new HashMap<Integer, Spatial>();
	}

	@Override
	protected void initialize(Application app) {
		simpleApp = (SimpleApplication) app;
		Camera cam = app.getCamera();
		float x = cam.getWidth() * 0.5f;
		float y = cam.getHeight() * 0.5f;
		cam.setLocation(new Vector3f(x, y, 500));
		cam.lookAt(new Vector3f(x, y, 0), Vector3f.UNIT_Y);
		
		simpleApp.getViewPort().setBackgroundColor(new ColorRGBA(0.9179f, 0.9213f, 0.9345f, 1f));

		modelFactory = new ModelFactory(simpleApp.getAssetManager());
		
		Spatial bg = modelFactory.createBackground();
		bg.move(0, 0, -3);
		simpleApp.getRootNode().attachChild(bg);
		
	}

	@Override
	protected void cleanup(Application app) {
	}
	

	@Override
	protected void onEnable() {
		simpleApp.getRootNode().attachChild(rootNode);
		addModels(game.getFoods());
		for(Snake s : game.getSnakes()) {
			addModels(s.getBodys());
		}
	}

	@Override
	protected void onDisable() {
		rootNode.removeFromParent();
		removeModels(game.getFoods());
		for(Snake s : game.getSnakes()) {
			removeModels(s.getBodys());
		}
	}

	Vector3f camLoc = new Vector3f();
	@Override
	public void update(float tpf) {
		if (game.applyChanges()) {
			removeModels(game.getRemovedEntities());
			addModels(game.getAddedEntities());
			game.freeChanges();
		}
		updateModels(game.getChangedEntities());
	}

	private void removeModels(List<Entity> entities) {
		
		if (entities == null)
			return;
		
		log.info("removed entitie " + entities.size());
		for (Entity e : entities) {
			Spatial s = models.remove(e.getId());
			if (s == null) {
				log.warn("Spatial is null for Entity@"+e.getId());
			} else {
				s.removeFromParent();
			}
		}
	}

	private void addModels(List<Entity> entities) {
		if (entities == null)
			return;
		
		for (Entity e : entities) {
			Spatial s = createVisual(e);
			models.put(e.getId(), s);
			updateModelSpatial(e, s);
			rootNode.attachChild(s);
		}
	}

	private void updateModels(List<Entity> entities) {
		if (entities == null)
			return;
		
		for (Entity e : entities) {
			Spatial s = models.get(e.getId());
			updateModelSpatial(e, s);
		}
	}

	private void updateModelSpatial(Entity e, Spatial s) {
		s.setLocalTranslation(e.getLocation());
		s.setLocalRotation(e.getFacing());
	}

	private Spatial createVisual(Entity e) {
		Spatial s = modelFactory.create(e);
		
		return s;
	}
}