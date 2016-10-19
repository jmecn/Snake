package net.jmecn.snake.server;

import java.util.LinkedList;
import java.util.List;

import net.jmecn.snake.core.SnakeConstants;

import org.apache.log4j.Logger;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class Snake {

	static Logger log = Logger.getLogger(Snake.class);
	
	public String name;// 蛇的名字
	public int skinId;// 皮肤
	public int length;// 长度
	
	public float collisionRadius;// 碰撞半径
	public LinkedList<Entity> bodys;// 蛇身
	
	protected boolean isSpeedUp;// 加速状态
	protected float speed;// 移动速度
	
	public boolean isDead;// 死亡状态
	
	public Snake(String name) {
		this.name = name;
		bodys = new LinkedList<Entity>();
		collisionRadius = SnakeConstants.snakeBodyRadius;
		length = 0;
		speed = SnakeConstants.speed;
		isSpeedUp = false;
		isDead = false;
		skinId = 0;
	}
	
	/**
	 * 调整蛇身每个位置的运动方向，使其紧随前一截身体。
	 */
	Vector3f tmp = new Vector3f();
	public void follow() {
		int len = bodys.size();
		if (len < 2)// 只有蛇头，不需要跟随
			return;
		
		for(int i=len-1; i>0; i--) {
			Entity last = bodys.get(i);
			Entity front = bodys.get(i - 1);
			
			// Follow
			float maxDist = collisionRadius;
			
			Vector3f loc1 = last.getLocation();
			Vector3f loc2 = front.getLocation();
	        float dx = loc2.x - loc1.x;
	        float dy = loc2.y - loc1.y;
	        float distSquared = dx * dx + dy * dy;
	        
			if (distSquared > maxDist * maxDist) {
				
				// normalize
				if (distSquared != 1f && distSquared != 0f) {
					distSquared = 1 / FastMath.sqrt(distSquared);
					dx *= distSquared;
					dy *= distSquared;
				}
				
				last.getLinear().set(dx, dy, 0);
			} else {
				last.getLinear().set(0, 0, 0);
			}
		}
	}
	
	/**
	 * 计算位移
	 * @param time
	 */
	public void move(float time) {
		
		float delta = speed * time;
		if (isSpeedUp) {// 玩家按下加速键
			delta *= 2;
		}
		
		int len = bodys.size();
		for(int i=len-1; i>=0; i--) {
			Entity e = bodys.get(i);
			
			Vector3f loc = e.getLocation();
			Vector3f linear = e.getLinear();
			
			loc.addLocal(linear.x * delta, linear.y * delta, 0);

			// 让模型的脸朝向线速度的方向
			getFacing(linear.x, linear.y, e.getFacing());
		}
	}

	/**
	 * 在XOY平面上，根据线速度的方向，计算图片当前的朝向。
	 * 
	 * <pre>
	 * (x*x + y*y) == 1
	 * vec3 xAxis = (y, -x, 0)
	 * vec3 yAxis = (x, y, 0)
	 * vec3 zAxis = (0, 0, 1)
	 * 
	 * Matrix3 rot = (
	 * y,  x,  0,
	 * -x, y,  0,
	 * 0,  0,  1)
	 * </pre>
	 *
	 * @see <code>com.jme3.math.Quaternion.fromRotationMatrix</code>
	 * @param x
	 * @param y
	 * @return
	 */
	protected Quaternion getFacing(float x, float y, Quaternion result) {
		// normalize
		float length = x * x + y * y;
		if (length != 1f && length != 0f) {
			length = 1f / FastMath.sqrt(length);
			x *= length;
			y *= length;
		}
		
		float z = 0, w = 1;
		float t = y + y + 1;
		if (t >= 0) {
			float s = FastMath.sqrt(t + 1);
			w = 0.5f * s;
			s = 0.5f / s;
			z = -2 * x * s;
		} else {
			float s = FastMath.sqrt(3f - t);
			z = s * 0.5f;
			s = 0.5f / s;
			w = -2 * x * s;
		}

		result.set(0, 0, z, w);

		return result;
	}

	/**
	 * 
	 * @return
	 */
	public Entity getHead() {
		if (bodys == null && bodys.size() == 0){
			log.info("木有蛇头了!");
			return null;
		}
		return bodys.getFirst();
	}
	
	public String getName() {
		return name;
	}
	
	public int getLength() {
		return length;
	}

	public boolean isDead() {
		return isDead;
	}

	public List<Entity> getBodys() {
		return bodys;
	}
}
