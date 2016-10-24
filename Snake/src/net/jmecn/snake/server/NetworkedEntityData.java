package net.jmecn.snake.server;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.MessageConnection;
import com.jme3.network.Network;
import com.simsilica.es.EntityData;
import com.simsilica.es.client.EntityDataClientService;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkedEntityData {

	private EntityData ed;

	public NetworkedEntityData(String name, Integer version, String host, Integer port) {
		Client client;
		try {
			client = Network.connectToServer(name, version, host, port, port);
			client.getServices().addService(
					new EntityDataClientService(
							MessageConnection.CHANNEL_DEFAULT_RELIABLE));
			this.ed = client.getServices()
					.getService(EntityDataClientService.class).getEntityData();
			final CountDownLatch startedSignal = new CountDownLatch(1);
			client.addClientStateListener(new ClientStateListener() {
				@Override
				public void clientConnected(Client c) {
					startedSignal.countDown();
				}

				@Override
				public void clientDisconnected(Client c,
						ClientStateListener.DisconnectInfo info) {
					System.out.println("Client disconnected.");
				}
			});
			client.start();

			// Wait for the client to start
			System.out.println("Waiting for connection setup.");
			startedSignal.await();
			System.out.println("Connected.");
			System.out.println("Press Ctrl-C to stop.");
		} catch (IOException ex) {
			Logger.getLogger(NetworkedEntityData.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(NetworkedEntityData.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	public EntityData getEntityData() {
		return ed;
	}
}