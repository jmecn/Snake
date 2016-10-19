package net.jmecn.snake.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 实体池
 * @author yanmaoyuan
 *
 */
public class EntityPool {

	static Logger log = Logger.getLogger(EntityPool.class);

	private int capacity = 100; // 对象池的大小

	private Entity[] elements = null;
	private boolean[] inUse = null;

	private List<Entity> addedEntities;
	private List<Entity> removedEntities;
	
	public EntityPool() {
		createPool(capacity);
	}

	/**
	 * 初始化对象池
	 * 
	 * @param capacity
	 */
	public EntityPool(int capacity) {
		createPool(capacity);
	}

	/**
	 * 创建对象池
	 * @param capacity
	 */
	public void createPool(int capacity) {
		if (elements != null) {
			log.warn("EntityPool already created.");
			return;
		}
		
		addedEntities = new ArrayList<Entity>();
		removedEntities = new ArrayList<Entity>();
		
		this.capacity = capacity;
		elements = new Entity[capacity];
		inUse = new boolean[capacity];
		// 根据 capacity的值，循环创建指定数目的对象
		for (int i = 0; i < capacity; i++) {
			elements[i] = new Entity(i);
			inUse[i] = false;
		}

		log.info("initailize EntityPool, capacity=" + capacity);
	}
	
	/**
	 * 关闭对象池中所有的对象，并清空对象池。
	 */
	public synchronized void releasePool() {
		for (int i = 0; i < capacity; i++) {
			if (inUse[i]) {
				log.warn("Entity@" + i + " still in use:" + elements[i]);
			}

			elements[i] = null;
			inUse[i] = false;
		}

		elements = null;
		inUse = null;
	}
	
	/**
	 * 扩容
	 * 
	 * @param minCapacity
	 */
	private void grow() {
		// 默认扩容一半的容量
		int oldCapacity = elements.length;
		int newCapacity = oldCapacity + (oldCapacity >> 1);
		elements = Arrays.copyOf(elements, newCapacity);
		inUse = Arrays.copyOf(inUse, newCapacity);

		// 创建新的对象，以填充扩容的部分
		for (int i = capacity; i < newCapacity; i++) {
			elements[i] = new Entity(i);
			inUse[i] = false;
		}
		log.info("grow, capacity=" + capacity + " newCapacity=" + newCapacity);
		capacity = newCapacity;
	}

	public synchronized Entity newEntity() {

		Entity e = getFreeObject();

		// 没有可用的对象，增加内存空间
		if (e == null) {
			grow();// 如果目前对象池中没有可用的对象，创建一些对象
			e = getFreeObject();
		}
		
		return e;// 返回获得的可用的对象
	}

	/**
	 * 查找对象池中所有的对象，查找一个可用的对象， 如果没有可用的对象，返回 null
	 */
	private Entity getFreeObject() {

		Entity e = null;
		for (int i = 0; i < capacity; i++) {
			if (inUse[i] == false) {
				e = elements[i];
				
				// 防止App端在尚未移除这个实体之前，就被EntityPool干掉了。
				if (removedEntities.contains(e))
					continue;
				inUse[i] = true;
				addedEntities.add(e);
				break;
			}
		}

		return e;// 返回找到到的可用对象
	}

	/**
	 * 此函数返回一个对象到对象池中，并把此对象置为空闲。 所有使用对象池获得的对象均应在不使用此对象时返回它。
	 */
	public void freeEntity(Entity e) {
		log.info("free Entity@" + e.getId());
		
		boolean find = false;
		for (int i = 0; i < capacity; i++) {
			if (elements[i] == e) {
				e.reset();
				inUse[i] = false;
				removedEntities.add(e);
				
				find = true;
				break;
			}
		}
		
		if (!find) {
			log.warn("Entity@" + e.hashCode() + " is not an instance of EntityPool");
		}
	}

	/**
	 * 释放集合中所有的实体
	 * @param entities
	 */
	public void freeAll(List<Entity> entities) {
		int len = entities.size();
		for(int i=0; i<len; i++) {
			freeEntity(entities.get(i));
		}
		entities.clear();
	}

	public boolean applyChanges() {
		return (addedEntities.size() > 0 || removedEntities.size() > 0);
	}
	
	public synchronized List<Entity> getAddedEntities() {
		return addedEntities;
	}
	public synchronized List<Entity> getRemovedEntities() {
		return removedEntities;
	}

	public void freeChanges() {
		addedEntities.clear();
		removedEntities.clear();
	}
}