package net.jmecn.snake.tools;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;

public class Rotations {

	public static void main(String[] args) {
		float anglurVelocity = FastMath.QUARTER_PI;// 角速度 90°
		System.out.println("right:" + new Quaternion().fromAngles(0, 0, -anglurVelocity));
		System.out.println("left:" + new Quaternion().fromAngles(0, 0, anglurVelocity));
	}
}
