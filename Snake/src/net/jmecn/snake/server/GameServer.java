package net.jmecn.snake.server;

import net.jmecn.snake.core.AI;
import net.jmecn.snake.core.Belongs;
import net.jmecn.snake.core.Collision;
import net.jmecn.snake.core.Dead;
import net.jmecn.snake.core.Decay;
import net.jmecn.snake.core.Follow;
import net.jmecn.snake.core.Game;
import net.jmecn.snake.core.Length;
import net.jmecn.snake.core.Player;
import net.jmecn.snake.core.Position;
import net.jmecn.snake.core.Tail;
import net.jmecn.snake.core.Type;
import net.jmecn.snake.core.Velocity;

import com.jme3.network.HostedConnection;
import com.jme3.network.MessageConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.FieldSerializer;
import com.simsilica.es.Name;
import com.simsilica.es.base.DefaultEntityData;
import com.simsilica.es.server.EntityDataHostedService;

public class GameServer {

	public static void main(String... args) throws Exception {
		Game game = new Game();
		game.start();
		
		DefaultEntityData ed = (DefaultEntityData) game.getEntityData();
		Server server = Network.createServer("My Game Server", 1, 9942, 9942);
		server.getServices().addService(
				new EntityDataHostedService(
						MessageConnection.CHANNEL_DEFAULT_RELIABLE, ed));
		
		Serializer.registerClass(Position.class, new FieldSerializer());
		Serializer.registerClass(Velocity.class, new FieldSerializer());
		Serializer.registerClass(Type.class, new FieldSerializer());
		Serializer.registerClass(Collision.class, new FieldSerializer());
		Serializer.registerClass(Belongs.class, new FieldSerializer());
		Serializer.registerClass(Follow.class, new FieldSerializer());
		Serializer.registerClass(Length.class, new FieldSerializer());
		
		Serializer.registerClass(Name.class, new FieldSerializer());
		Serializer.registerClass(AI.class, new FieldSerializer());
		Serializer.registerClass(Tail.class, new FieldSerializer());
		Serializer.registerClass(Player.class, new FieldSerializer());
		Serializer.registerClass(Dead.class, new FieldSerializer());
		Serializer.registerClass(Decay.class, new FieldSerializer());
		
		server.start();
		System.out.println("Server started.  Press Ctrl-C to stop.");
		try {
			while (true) {
				server.getServices().getService(EntityDataHostedService.class).sendUpdates();
				Thread.sleep(100); // 10 times a second
			}
		} finally {
			System.out.println("Closing connections...");
			for (HostedConnection conn : server.getConnections()) {
				conn.close("Shutting down.");
			}
			System.out.println("Shutting down server...");
			server.close();
		}
	}
}