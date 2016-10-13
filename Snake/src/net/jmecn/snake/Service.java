package net.jmecn.snake;

/**
 * ��Ϸ����ӿ�
 * @author yanmaoyuan
 *
 */
public interface Service {

	public void initialize(Game game);
	
	public void update(long time);
	
	public void terminate(Game game);
}
