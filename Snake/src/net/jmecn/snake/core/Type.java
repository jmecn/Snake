package net.jmecn.snake.core;

import com.simsilica.es.EntityComponent;

public class Type implements EntityComponent {

	public final static int FOOD = 0;
	public final static int BODY = 1;
	public final static int HEAD = 2;
	
	private int value;

	public Type(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Type [value=" + value + "]";
	}
}
