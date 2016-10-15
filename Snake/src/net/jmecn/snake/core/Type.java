package net.jmecn.snake.core;

import com.simsilica.es.EntityComponent;

public class Type implements EntityComponent {

	public final static int FOOD = 0;
	public final static int BODY = 1;
	public final static int HEAD = 2;
	public final static int TAIL = 3;
	public final static int DEAD_BODY = 4;

	private int skin;
	private int value;

	public Type(int value) {
		this.value = value;
		this.skin = -1;
	}
	
	public Type(int value, int skin) {
		this.value = value;
		this.skin = skin;
	}

	public int getValue() {
		return value;
	}

	public int getSkin() {
		return skin;
	}

	@Override
	public String toString() {
		return "Type [skin=" + skin + ", value=" + value + "]";
	}
}
