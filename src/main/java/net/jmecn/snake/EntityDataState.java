package net.jmecn.snake;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.es.EntityData;
import com.simsilica.es.base.DefaultEntityData;

public class EntityDataState extends BaseAppState {

	private EntityData ed;
	
	public EntityDataState() {
		ed = new DefaultEntityData();
	}
	
	public EntityDataState(EntityData ed) {
		this.ed = ed;
	}
	
	public EntityData getEntityData() {
		return ed;
	}
	
	@Override
	protected void initialize(Application app) {
	}

	@Override
	protected void cleanup(Application app) {
		ed.close();
		ed = null;
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

}
