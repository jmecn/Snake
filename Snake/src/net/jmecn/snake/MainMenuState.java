package net.jmecn.snake;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioData.DataType;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.ui.Picture;

public class MainMenuState extends BaseAppState {

	static Logger log = Logger.getLogger(MainMenuState.class);
	
	private Picture slash;
	
	Future<Void> future;
	ScheduledThreadPoolExecutor excutor;
	@Override
	protected void initialize(Application app) {
		
		Camera cam = app.getCamera();
		float h = cam.getHeight();
		float w = cam.getWidth();
		
		float scale = h / 720f;
		
		slash = new Picture("Slash");
		slash.setImage(app.getAssetManager(), "Interface/game_name_icon.png", true);
		slash.setWidth(746f * scale);
		slash.setHeight(174f * scale);
		
		float y = h * 0.5f - 87f * scale;
		float x = w * 0.5f - 373f * scale;
		
		log.info("w=" + w + " h=" + h + " scale=" + scale);
		slash.setLocalTranslation(x, y, 0);

	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		SimpleApplication app = (SimpleApplication)getApplication();
		app.getViewPort().setBackgroundColor(ColorRGBA.White);
		app.getGuiNode().attachChild(slash);
		
		excutor = new ScheduledThreadPoolExecutor(1);
	}

	@Override
	protected void onDisable() {
		slash.removeFromParent();
		
		excutor.shutdown();
		
	}
	
	Callable<Void> task = new Callable<Void>() {

		@Override
		public Void call() throws Exception {
			AssetManager assets = getApplication().getAssetManager();
			
			ModelFactory factory = new ModelFactory(assets);
			
			// 预加载所有材质和贴图
			log.info("加载贴图");
			factory.createTexture("Interface/ctrlBg.png");
			factory.createTexture("Interface/ctrlBtn.png");
			factory.createTexture("Interface/tile.png");
			
			factory.createTexture("Interface/circle.png");
			factory.createTexture("Interface/Skins/skin_1_head.png");
			factory.createTexture("Interface/Skins/skin_1_body.png");
			factory.createTexture("Interface/Skins/skin_1_tail.png");
			
			log.info("加载音乐:bg.ogg");
			new AudioNode(assets, "Sounds/bg.ogg", DataType.Buffer);
			log.info("加载音乐:end.ogg");
			new AudioNode(assets, "Sounds/end.ogg", DataType.Stream);
			log.info("加载音乐:killing.ogg");
			new AudioNode(assets, "Sounds/killing.ogg", DataType.Stream);
			log.info("加载音乐:bekilled.ogg");
			new AudioNode(assets, "Sounds/bekilled.ogg", DataType.Buffer);

			return null;
		}
		
	};

	float time = 0;
	@Override
	public void update(float tpf) {
		time += tpf;
		
		if (task != null && future == null) {
			future = excutor.submit(task);
			time = 0;
		}
		
		if (future != null && !future.isDone()) {
			//log.info("loading.." + time);
		}
		
		if (future != null && future.isDone()) {
			task = null;
			future = null;
			log.info("done in " + time + "s");
			getStateManager().detach(this);
			((Main)getApplication()).startSingleGame();
		}
	}
}
