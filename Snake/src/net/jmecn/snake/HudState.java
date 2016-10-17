package net.jmecn.snake;

import org.apache.log4j.Logger;

import net.jmecn.snake.core.Length;
import net.jmecn.snake.core.Position;
import net.jmecn.snake.core.SnakeConstants;
import net.jmecn.snake.core.Velocity;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.jme3.util.TempVars;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class HudState extends BaseAppState implements ActionListener {

	static Logger log = Logger.getLogger(HudState.class);
	
	public final static String MOVING = "moving";
	public final static String SPEEDUP = "speedup";
	
	private EntityData ed;
	private EntityId player;
	private boolean isMoving = false;
	
	private Camera cam;
	

	private float scalar = 1f;
	
	private Node guiNode;
	
	// 方向控制按钮
	private Vector2f controlCenter = new Vector2f(150, 150);// 控制器的中心点
	private Vector2f realCenter = new Vector2f(150, 150);
	private Node dirControlButton;
	private Node dirControlBackground;
	
	// 加速键
	private Vector3f speedupControlCenter;// 控制器的中心点
	private Vector3f speedupRealCenter;
	private Node speedUpControl;
	
	private Node hudStatus;
	private BitmapFont hudFont;
	private BitmapText lengthTxt;
	private BitmapText killTxt;
	
	public HudState(EntityId player) {
		this.player = player;
		this.guiNode = new Node("Hud");
	}
	
	@Override
	protected void initialize(Application app) {
		ed = getStateManager().getState(EntityDataState.class).getEntityData();
		cam = app.getCamera();
		
		// init hud
		AssetManager assets = app.getAssetManager();
		
		hudStatus = new Node("");
		hudFont = assets.loadFont("Interface/Fonts/hud.fnt");
		
		lengthTxt = hudFont.createLabel("长度: 0");
		lengthTxt.setColor(ColorRGBA.DarkGray);
		
		killTxt = hudFont.createLabel("击杀: 0");
		killTxt.setColor(ColorRGBA.DarkGray);
		
		hudStatus.attachChild(lengthTxt);
		hudStatus.attachChild(killTxt);
		killTxt.move(0, -48, 0);
		
		guiNode.attachChild(hudStatus);
		hudStatus.move(40, 680, -2);
		
		// 
		Picture ctrlBtn = new Picture("ctrlBtn");
		ctrlBtn.setImage(assets, "Interface/ctrlBtn.png", true);
		ctrlBtn.setWidth(80);
		ctrlBtn.setHeight(80);

		Picture ctrlBg = new Picture("ctrlBg");
		ctrlBg.setImage(assets, "Interface/ctrlBg.png", true);
		ctrlBg.setWidth(200);
		ctrlBg.setHeight(200);
		Material mat = ctrlBg.getMaterial();
		mat.setColor("Color", new ColorRGBA(0,0,0,0.5f));
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		dirControlButton = new Node("BtnNode");
		dirControlButton.attachChild(ctrlBtn);
		ctrlBtn.setLocalTranslation(-40, -40, 0);
		
		dirControlBackground = new Node("bgNode");
		dirControlBackground.attachChild(ctrlBg);
		ctrlBg.setLocalTranslation(-100, -100, 0);
		
		guiNode.attachChild(dirControlBackground);
		guiNode.attachChild(dirControlButton);
		dirControlBackground.setLocalTranslation(controlCenter.x, controlCenter.y, -2);
		dirControlButton.setLocalTranslation(controlCenter.x, controlCenter.y, -1);
		
		// 根据720分辨率来进行缩放
		scalar = app.getCamera().getHeight() / 720f;
		guiNode.scale(scalar);
		realCenter.multLocal(scalar);
		
		InputManager inputManager = app.getInputManager();
		inputManager.addMapping(MOVING, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, MOVING);
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
		if (name.equals(MOVING)) {
			this.isMoving = isPressed;
		}
	}
	
	public void update(float tpf) {
		
		// 方向舵
		if (isMoving) {
			TempVars tmpVars = TempVars.get();
			
			// 根据鼠标位置，计算运动方向
			Vector2f loc = this.getApplication().getInputManager().getCursorPosition();
			Vector2f dir = tmpVars.vect2d.set(loc.x, loc.y);
			dir.subtractLocal(realCenter);
			dir.normalizeLocal();
			
			
			// 改变按钮的坐标
			dirControlButton.setLocalTranslation(controlCenter.x+dir.x*50f, controlCenter.y+dir.y*50, -2);

			// 判断当前方向是在原方向的左侧还是右侧
			Velocity oldV = ed.getComponent(player, Velocity.class);
			if (oldV != null) {
				Vector3f oldLinear = oldV.getLinear();
				
				Vector2f vec2 = tmpVars.vect2d2.set(oldLinear.x, oldLinear.y);
				vec2.normalizeLocal();
				
				// 判断方向 ：tmp=0没变, tmp>0右转, tmp<0左转
				float tmp = dir.determinant(vec2);
				if (FastMath.abs(tmp) > 0.000001f) {
					// TODO 计算旋转角度，不要让蛇头骤然大角度旋转
					float angle = dir.smallestAngleBetween(vec2);
					log.info("Snake turn " + (tmp>0?"right":"left") + " angle=" + angle/FastMath.DEG_TO_RAD);
					
					// 线速度
					Vector3f linear = new Vector3f(
							dir.x * SnakeConstants.speed, 
							dir.y * SnakeConstants.speed,
							0);
					ed.setComponent(player, new Velocity(linear, SnakeConstants.rotRight));
				}
			}
			
			
			tmpVars.release();
		} else {
			dirControlButton.setLocalTranslation(controlCenter.x, controlCenter.y, -2);
		}
		
		// 加速键
		
		// 跟踪镜头
		Position p = ed.getComponent(player, Position.class);
		if (p != null) {
			Vector3f loc = p.getLocation();
			cam.setLocation(new Vector3f(loc.x, loc.y, 300));
		}
		
		// 状态值
		Length l = ed.getComponent(player, Length.class);
		if (l != null) {
			lengthTxt.setText("长度: " + l.getValue());
			int kill = 0;
			killTxt.setText("击杀: " + kill);
		}
		
	}
	
}
