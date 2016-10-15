package net.jmecn.snake.core;

import com.simsilica.es.EntityComponent;

public class Length implements EntityComponent {

	private int value;

	public Length(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Length [value=" + value + "]";
	}

}
