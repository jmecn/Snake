package net.jmecn.snake;

import net.jmecn.snake.core.Position;
import net.jmecn.snake.core.Velocity;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class HudState extends BaseAppState implements ActionListener {

	private EntityData ed;
	private EntityId player;
	private boolean isMoving = false;
	
	private Camera cam;
	
	private Vector3f controlCenter;// 控制器的中心点
	private Vector3f realCenter;
	private float scalar = 1f;
	
	private Node guiNode;
	private Node btnNode;
	private Node bgNode;
	
	public HudState(EntityId player) {
		this.player = player;
		this.controlCenter = new Vector3f(150, 150, 0);
		this.realCenter = new Vector3f(150, 150, 0);
		this.guiNode = new Node("Hud");
	}
	
	@Override
	protected void initialize(Application app) {
		ed = getStateManager().getState(EntityDataState.class).getEntityData();
		cam = app.getCamera();
		
		// init hud
		AssetManager assets = app.getAssetManager();
		
		Picture ctrlBtn = new Picture("ctrlBtn");
		ctrlBtn.setImage(assets, "Interface/ctrlBtn.png", true);
		ctrlBtn.setWidth(80);
		ctrlBtn.setHeight(80);

		Picture ctrlBg = new Picture("ctrlBg");
		ctrlBg.setImage(assets, "Interface/ctrlBg.png", true);
		ctrlBg.setWidth(200);
		ctrlBg.setHeight(200);
		
		btnNode = new Node("BtnNode");
		btnNode.attachChild(ctrlBtn);
		ctrlBtn.setLocalTranslation(-40, -40, 0);
		
		bgNode = new Node("bgNode");
		bgNode.attachChild(ctrlBg);
		ctrlBg.setLocalTranslation(-100, -100, 0);
		
		guiNode.attachChild(bgNode);
		guiNode.attachChild(btnNode);
		bgNode.setLocalTranslation(controlCenter.x, controlCenter.y, -2);
		btnNode.setLocalTranslation(controlCenter.x, controlCenter.y, -1);
		
		// 根据720分辨率来进行缩放
		scalar = app.getCamera().getHeight() / 720f;
		guiNode.scale(scalar);
		realCenter.multLocal(scalar, scalar, 1f);
		
		InputManager inputManager = app.getInputManager();
		inputManager.addMapping("Mouse", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, "Mouse");
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		((SimpleApplication)getApplication()).getGuiNode().attachChild(guiNode);
	}

	@Override
	protected void onDisable() {
		guiNode.removeFromParent();
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("Mouse")) {
			this.isMoving = isPressed;
		}
	}
	
	public void update(float tpf) {
		if (isMoving) {
			Vector2f loc = this.getApplication().getInputManager().getCursorPosition();
			Vector3f target = new Vector3f(loc.x, loc.y, 0);
			
			Vector3f linear = target.subtract(realCenter).normalize();
			btnNode.setLocalTranslation(controlCenter.add(linear.mult(50)));
			
			linear.multLocal(200);
			
			ed.setComponent(player, new Velocity(linear));
		} else {
			btnNode.setLocalTranslation(controlCenter);
		}
		
		Vector3f loc = ed.getComponent(player, Position.class).getLocation();
		cam.setLocation(new Vector3f(loc.x, loc.y, 1000));
	}

}
