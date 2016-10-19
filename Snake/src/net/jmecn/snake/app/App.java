package net.jmecn.snake.app;

import net.jmecn.snake.MusicState;

import com.jme3.app.SimpleApplication;

public class App extends SimpleApplication {

	public App() {
		super(new MusicState());
	}
	@Override
	public void simpleInitApp() {
		stateManager.attach(new MainMenuState());
	}
	
	public void simpleUpdate(float tpf) {
		
	}

	public static void main(String[] args) {
		new App().start();
	}

}
