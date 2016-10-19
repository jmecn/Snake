package net.jmecn.snake.core;

/**
 * 计时器
 * @author yanmaoyuan
 *
 */
public class Timer {

	private long startTime;
	private long previousTime;
	private long timePerFrame;
	
	public Timer() {
		startTime = System.nanoTime();
	}
	
	public long getTime() {
		return System.nanoTime() - startTime;
	}
	
	/**
	 * 更新时间
	 */
	public void update() {
		timePerFrame = getTime() - previousTime;
		previousTime = getTime();
	}
	
	/**
	 * 每帧经过的时间
	 * @return
	 */
	public long getTimePerFrame() {
		return timePerFrame;
	}
	
	/**
	 * FPS
	 * @return
	 */
	public float getFramePerSecond() {
		return 1000000000f / timePerFrame;
	}
	
	/**
	 * 重置定时器
	 */
	public void reset() {
		startTime = System.nanoTime();
		previousTime = getTime();
	}
}
