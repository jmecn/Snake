package net.jmecn.snake.server;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.jmecn.snake.core.SnakeConstants;
import net.jmecn.snake.core.Timer;

import org.apache.log4j.Logger;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class Game {

	static Logger log = Logger.getLogger(Game.class);

	private Timer timer;
	private boolean started;
	private boolean enabled;

	private ScheduledExecutorService executor;
	private Runner runner;

	private EntityPool entityPool;

	private ArrayList<Entity> foods = new ArrayList<Entity>();
	private ArrayList<Entity> addedFoods = new ArrayList<Entity>();
	private ArrayList<Entity> removedFoods = new ArrayList<Entity>();

	private ArrayList<Snake> snakes = new ArrayList<Snake>();
	private ArrayList<Snake> addedSnakes = new ArrayList<Snake>();
	private ArrayList<Snake> removedSnakes = new ArrayList<Snake>();

	/**
	 * 一些临时变量
	 */
	private int lenSnake = 0;// 蛇的数量
	private int lenBody = 0;// 蛇身长度
	private int lenFood = 0;// 食物的数量

	public Game() {
		entityPool = new EntityPool();
		timer = new Timer();

		executor = Executors.newScheduledThreadPool(1);
		runner = new Runner();
	}

	/**
	 * 开始游戏
	 */
	public void start() {
		if (started) {
			return;
		}
		
		initialize();

		executor.scheduleAtFixedRate(runner, 0, 10, TimeUnit.MILLISECONDS);
		started = true;

		enabled = true;
		log.info("Game start");
	}

	public void stop() {
		if (!started) {
			return;
		}

		clear();
		
		executor.shutdown();
		started = false;
		enabled = false;
		log.info("Game stop");
	}

	public void pause() {
		enabled = !enabled;
	}
	
	protected void initialize() {
		// 随机刷新200个食物
		for(int i=0; i<200; i++) {
			makeNewFood();
		}
	}

	public synchronized void update(float tpf) {
		/**
		 * 添加食物
		 */
		if (addedFoods.size() > 0) {
			foods.addAll(addedFoods);
			addedFoods.clear();
		}
		
		/**
		 * 加入新蛇
		 */
		if (addedSnakes.size() > 0) {
			snakes.addAll(addedSnakes);
			addedSnakes.clear();
		}
		
		lenSnake = snakes.size();
		if (lenSnake == 0)
			return;

		/**
		 * 使蛇运动
		 */
		for (int i = 0; i < lenSnake; i++) {
			Snake s = snakes.get(i);
			s.follow();
			s.move(tpf);

			/**
			 * 边界检测
			 */
			Vector3f loc = s.bodys.getFirst().getLocation();
			if (loc.x < s.collisionRadius
					|| loc.x > SnakeConstants.width - s.collisionRadius
					|| loc.y < s.collisionRadius
					|| loc.y > SnakeConstants.height - s.collisionRadius) {
				// 这条蛇死啦
				s.isDead = true;
				removedSnakes.add(s);
			}

		}

		// 碰撞检测
		hitBody();
		eatFood();
		
		/**
		 * 移除被吃掉的食物
		 */
		if (removedFoods.size() > 0) {
			foods.removeAll(removedFoods);
			for(Entity e : removedFoods) {
				entityPool.freeEntity(e);
			}
			removedFoods.clear();
		}
		
		/**
		 * 移除死蛇
		 */
		if (removedSnakes.size() > 0) {
			snakes.removeAll(removedSnakes);
			for(Snake s : removedSnakes) {
				entityPool.freeAll(s.bodys);
			}
			removedSnakes.clear();
		}
		
	}

	/**
	 * 释放所有内存
	 */
	protected void clear() {
		// 清空
		entityPool.freeAll(foods);
		foods = null;
		entityPool.freeAll(addedFoods);
		addedFoods = null;
		
		// 清空蛇
		for(Snake s:snakes) {
			entityPool.freeAll(s.bodys);
		}
		snakes.clear();
		
		for(Snake s:addedSnakes) {
			entityPool.freeAll(s.bodys);
		}
		addedSnakes.clear();

		entityPool.release();
	}
	
	/**
	 * 蛇头与其他蛇的身体发生碰撞，这条蛇就撞死了。
	 */
	protected void hitBody() {
		// 蛇的数量过少，不需要进行碰撞检测
		if (lenSnake < 2)
			return;

		boolean hit = false;
		
		for (int i = 0; i < lenSnake - 1; i++) {
			// 获取蛇头的坐标和碰撞半径
			Snake s1 = snakes.get(i);
			
			if (s1.isDead) // 这条蛇已经死了..
				continue;
			
			Vector3f loc1 = s1.bodys.getFirst().getLocation();
			float r1 = s1.collisionRadius;

			for (int j = i + 1; j < lenSnake; j++) {
				// 获取蛇身坐标
				Snake s2 = snakes.get(j);
				
				if (s2.isDead) // 这条蛇已经死了
					continue;

				hit = false;
				lenBody = s2.bodys.size();
				for (int k = 1; // 忽略第一个节点(即蛇头)
				k < lenBody; k++) {
					Entity e = s2.bodys.get(k);
					Vector3f loc2 = e.getLocation();
					float r2 = s2.collisionRadius;

					// 平方和
					float lengthSquare = loc1.x * loc2.x + loc1.y * loc2.y;
					float threshold = r1 + r2;
					threshold *= threshold;

					if (lengthSquare <= threshold) {// 发生了碰撞
						s1.isDead = true;
						removedSnakes.add(s1);
						hit = true;
						
						// TODO 记录是谁杀了这条蛇
						log.info(s1.name + " killed by " + s2.name);
						break;
					}
				}
				
				if (hit) // 这条蛇已死，不用再和其它蛇做碰撞检测
					break;
			}
		}
	}

	/**
	 * 蛇头与食物发生碰撞，就说明可以吃掉这个食物
	 */
	protected void eatFood() {
		lenFood = foods.size();
		for (int i = 0; i < lenSnake; i++) {
			// 获取蛇头的坐标和碰撞半径
			Snake snake = snakes.get(i);
			if (snake.isDead)// 死掉的蛇不会吃食物!
				continue;
			
			Vector3f loc1 = snake.bodys.getFirst().getLocation();
			float r1 = snake.collisionRadius;

			// 获取食物的坐标
			for (int j = 0; j < lenFood; j++) {
				Entity food = foods.get(j);
				Vector3f loc2 = food.getLocation();
				float r2 = SnakeConstants.foodRadius;

				// 平方和
				float lengthSquare = loc1.x * loc2.x + loc1.y * loc2.y;
				float threshold = r1 + r2;
				threshold *= threshold;

				if (lengthSquare <= threshold) {// 发生了碰撞
					// 蛇吃到了这个食物
					changeLength(snake, 1);
					removedFoods.add(food);
					makeNewFood();
				}
			}
		}
	}

	/**
	 * 创建一条蛇
	 * @param name
	 * @return
	 */
	public Snake createSnake(String name) {
		Snake snake = new Snake(name);

		/**
		 * 找个安全的位置创建蛇头。
		 */
		Entity head = createSafeEntity(snake.collisionRadius);
		snake.bodys.add(head);
		
		/**
		 * 初始化蛇身
		 */
		changeLength(snake, SnakeConstants.snakeMinLength);
		
		/**
		 * 设置蛇的皮肤
		 */
		
		addedSnakes.add(snake);
		
		return snake;
	}

	/**
	 * 在安全的位置创建一个实体
	 * 
	 * @param radius
	 *            实体的碰撞半径
	 * @return
	 */
	protected Entity createSafeEntity(float radius) {
		Entity e = entityPool.newEntity();

		Random rand = FastMath.rand;
		// 地图高宽
		float w = SnakeConstants.width - radius * 2;
		float h = SnakeConstants.height - radius * 2;
		
		boolean unsafe = true;
		outer: while(unsafe) {
			// 随机生成一组坐标
			float x = rand.nextFloat() * w + radius;
			float y = rand.nextFloat() * h + radius;
			
			/**
			 * 避免直接撞在别的蛇身上。
			 */
			lenSnake = snakes.size();
			for(int i=0; i<lenSnake; i++) {
				Snake s = snakes.get(i);
				float threshold = s.collisionRadius + radius;
				threshold *= threshold;
				lenBody = s.bodys.size();
				for(int j=0; j<lenBody; j++) {
					Vector3f loc = s.bodys.get(j).getLocation();
					float dx = loc.x - x;
					float dy = loc.y - y;
					double distanceSquare = dx * dx + dy * dy;
					if (distanceSquare <= threshold) {
						break outer;
					}
				}
			}
			
			// 安全
			unsafe = false;
			
			e.getLocation().x = x;
			e.getLocation().y = y;
		}
		
		
		return e;
	}

	/**
	 * 蛇身变长
	 * 
	 * @param l
	 */
	public void changeLength(Snake snake, int l) {
		// 没有变化
		if (l == 0)
			return;

		if (l > 0) {// 增长
			for (int i = 0; i < l; i++) {
				snake.length++;
				if (snake.length % SnakeConstants.snakeBodyGrow == 0) {
					// 从对象池中获得一个新的Entity
					Entity e = entityPool.newEntity();

					Entity last = snake.bodys.getLast();
					e.setLocation(last.getLocation());
					e.setFacing(last.getFacing());
					e.setLinear(last.getLinear());
					e.setSkinId(last.getSkinId());

					snake.bodys.add(e);
					// TODO addedEntitys.add(e);
				}
			}
		} else {// 缩短
			for (int i = l; i < 0; i++) {
				snake.length--;

				Entity last = snake.bodys.removeLast();
				entityPool.freeEntity(last);
				last = null;
			}
		}
	}

	protected Entity makeNewFood() {
		Entity e = createSafeEntity(SnakeConstants.foodRadius);
		addedFoods.add(e);
		return e;
	}

	private class Runner implements Runnable {
		public void run() {
			try {
				timer.update();
				if (!enabled)
					return;

				long delta = timer.getTimePerFrame();
				if (delta == 0)
					return;

				float time = delta / 1000000000.0f;
				update(time);
			} catch (RuntimeException e) {
				log.error("", e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Game game = new Game();
		game.start();
		
		game.createSnake("a");
		game.createSnake("b");
		game.createSnake("c");
		
		Thread.sleep(5000);
		game.stop();
	}
}
