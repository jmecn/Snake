package net.jmecn.snake;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class InputState extends BaseAppState implements RawInputListener {

	public EntityId player;
	public InputState(EntityId player) {
		this.player = player;
	}

	EntityData ed;
	@Override
	protected void initialize(Application app) {
		ed = getStateManager().getState(EntityDataState.class).getEntityData();
		
		InputManager inputManager = app.getInputManager();
		inputManager.addRawInputListener(this);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	public void beginInput() {
	}

	public void endInput() {
	}

	public void onJoyAxisEvent(JoyAxisEvent evt) {
	}

	public void onJoyButtonEvent(JoyButtonEvent evt) {
	}

	public void onMouseMotionEvent(MouseMotionEvent evt) {
		System.out.println(evt);
		int x = evt.getX();
		int y = evt.getY();
		Position p = ed.getComponent(player, Position.class);
		
		System.out.println(p);
		Vector3f target = new Vector3f(x, y, 0);
		Vector3f position = p.getLocation();
		
		Vector3f linear = target.subtract(position).normalize();
		linear.multLocal(200);
		
		ed.setComponent(player, new Velocity(linear));
	}

	public void onMouseButtonEvent(MouseButtonEvent evt) {
	}

	public void onKeyEvent(KeyInputEvent evt) {
	}

	public void onTouchEvent(TouchEvent evt) {
	}

}
