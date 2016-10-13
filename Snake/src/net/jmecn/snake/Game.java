package net.jmecn.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simsilica.es.EntityData;

/**
 * @author yanmaoyuan
 *
 */
public class Game {
	
	private Logger log = LoggerFactory.getLogger(Game.class);
	
	private boolean started;
	private boolean enabled;
	
	private ScheduledExecutorService executor;
	private ServiceRunnable serviceRunner;
	private Timer timer;
	private List<Service> services = new ArrayList<Service>();
	private EntityFactory factory;
	
	public Game() {
		services.add(new EntityDataService());
		services.add(new MovementService());
		services.add(new CollisionService());
		services.add(new DecayService());
		
		timer = new Timer();
		serviceRunner = new ServiceRunnable();
	}

    public EntityData getEntityData() {
        return getService(EntityDataService.class).getEntityData(); 
    }
    
    public <T extends Service> T addService( T s ) {
        if( started ) {
            throw new IllegalStateException("");
        }
        services.add(s);
        return s;
    }
    
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> type) {
		int len = services.size();
		for (int i = 0; i < len; i++) {
			Service s = services.get(i);
			if (type.isInstance(s)) {
				return (T) s;
			}
		}
		return null;
	}
	
	public void removeService(Service service) {
		if (services.contains(service)) {
			service.terminate(this);
			services.remove(service);
		}
	}

	public void start() {
		if (started) {
			return;
		}
		
		for (Service s : services) {
			s.initialize(this);
		}
		
		factory = new EntityFactory(getEntityData());
		
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(serviceRunner, 0, 62, TimeUnit.MILLISECONDS);
		started = true;
		
		enabled = true;
		log.info("");
	}

	public void stop() {
		if (!started) {
			return;
		}
		executor.shutdown();

		for (int i = services.size() - 1; i >= 0; i--) {
			Service s = services.get(i);
			s.terminate(this);
		}
		started = false;
		enabled = false;
		log.info("");
		
		System.exit(0);
	}

	protected void runServices(long gameTime) {
		int len = services.size();
		for (int i = 0; i < len; i++) {
			Service s = services.get(i);
			s.update(gameTime);
		}
	}

    public long getGameTime() {
        return timer.getTime(); 
    }
    
    public Timer getTimer() {
    	return timer;
    }
    
    public EntityFactory getFactory() {
    	return factory;
    }
    
	private class ServiceRunnable implements Runnable {
		public void run() {
			
			try {
				timer.update();
				if (!enabled)
					return;
				runServices(timer.getTimePerFrame());
			} catch (RuntimeException e) {
				log.error("", e);
			}
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
}
