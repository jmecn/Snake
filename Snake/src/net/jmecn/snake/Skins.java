package net.jmecn.snake;

import com.jme3.math.Vector3f;

public class Skins {

	public static Skins BEE = new Skins(0, "skin_1_head.png",
			"skin_1_body.png",
			"skin_1_tail.png",
			new Vector3f(1.828f, 1.25f, 1),
			new Vector3f(1, 1, 1),
			new Vector3f(1, 1, 1));
	public static Skins RED = new Skins(0, "skin_9_head.png",
			"skin_9_body.png",
			"skin_9_tail.png",
			new Vector3f(1.147f, 1, 1),
			new Vector3f(1, 1, 1),
			new Vector3f(1, 1, 1));
	
	int value;
	String head;
	String body;
	String tail;
	Vector3f headScale;
	Vector3f bodyScale;
	Vector3f tailScale;
	
	
	public Skins(int value, String head, String body, Vector3f headScale,
			Vector3f bodyScale) {
		this.value = value;
		this.head = head;
		this.body = body;
		this.tail = body;
		this.headScale = headScale;
		this.bodyScale = bodyScale;
		this.tailScale = bodyScale;
	}


	public Skins(int value, String head, String body, String tail,
			Vector3f headScale, Vector3f bodyScale, Vector3f tailScale) {
		this.value = value;
		this.head = head;
		this.body = body;
		this.tail = tail;
		this.headScale = headScale;
		this.bodyScale = bodyScale;
		this.tailScale = tailScale;
	}
}