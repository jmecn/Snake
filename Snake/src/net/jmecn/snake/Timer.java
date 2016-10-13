package net.jmecn.snake;

/**
 * ��ʱ��
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
	 * ���¼�ʱ��
	 */
	public void update() {
		timePerFrame = getTime() - previousTime;
		previousTime = getTime();
	}
	
	/**
	 * ��ȡû֡ʱ����
	 * @return
	 */
	public long getTimePerFrame() {
		return timePerFrame;
	}
	
	/**
	 * ���ÿ��֡��
	 * @return
	 */
	public float getFramePerSecond() {
		return 1000000000f / timePerFrame;
	}
	
	/**
	 * ���ü�ʱ��
	 */
	public void reset() {
		startTime = System.nanoTime();
		previousTime = getTime();
	}
}
