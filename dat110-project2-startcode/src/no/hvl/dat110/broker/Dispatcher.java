package no.hvl.dat110.broker;

import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.*;
import no.hvl.dat110.messagetransport.Connection;

public class Dispatcher extends Stopable {

	private Storage storage;

	public Dispatcher(Storage storage) {
		super("Dispatcher");
		this.storage = storage;

	}

	@Override
	public void doProcess() {

		Collection<ClientSession> clients = storage.getSessions();

		Logger.lg(".");
		for (ClientSession client : clients) {

			Message msg = null;

			if (client.hasData()) {
				msg = client.receive();
			}

			if (msg != null) {
				dispatch(client, msg);
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void dispatch(ClientSession client, Message msg) {

		MessageType type = msg.getType();

		switch (type) {

		case DISCONNECT:
			onDisconnect((DisconnectMsg) msg);
			break;

		case CREATETOPIC:
			onCreateTopic((CreateTopicMsg) msg);
			break;

		case DELETETOPIC:
			onDeleteTopic((DeleteTopicMsg) msg);
			break;

		case SUBSCRIBE:
			onSubscribe((SubscribeMsg) msg);
			break;

		case UNSUBSCRIBE:
			onUnsubscribe((UnsubscribeMsg) msg);
			break;

		case PUBLISH:
			onPublish((PublishMsg) msg);
			break;

		default:
			Logger.log("broker dispatch - unhandled message type");
			break;

		}
	}

	// called from Broker after having established the underlying connection
	public void onConnect(ConnectMsg msg, Connection connection) {

		String user = msg.getUser();

		Logger.log("onConnect:" + msg.toString());

		storage.addClientSession(user, connection);

		ClientSession cs = storage.getSession(user);

		Set<Message> messages = storage.getBufferedMessages(user);

		if(messages != null){
			messages.forEach(m -> cs.send(m));
			storage.clearBufferedMessages(user);
		}



	}

	// called by dispatch upon receiving a disconnect message 
	public void onDisconnect(DisconnectMsg msg) {

		String user = msg.getUser();

		Logger.log("onDisconnect:" + msg.toString());

		storage.removeClientSession(user);

	}

	public void onCreateTopic(CreateTopicMsg msg) {

		String topic = msg.getTopic();

		Logger.log("onCreateTopic:" + msg.toString());

		storage.createTopic(topic);

	}

	public void onDeleteTopic(DeleteTopicMsg msg) {

		String topic = msg.getTopic();

		Logger.log("onDeleteTopic:" + msg.toString());

		storage.deleteTopic(topic);
		
	}

	public void onSubscribe(SubscribeMsg msg) {

		String topic = msg.getTopic();
		String user = msg.getUser();

		Logger.log("onSubscribe:" + msg.toString());

		storage.addSubscriber(user, topic);

	}

	public void onUnsubscribe(UnsubscribeMsg msg) {

		String topic = msg.getTopic();
		String user = msg.getUser();

		Logger.log("onUnsubscribe:" + msg.toString());

		storage.removeSubscriber(user, topic);
		
	}

	public void onPublish(PublishMsg msg) {

		String topic = msg.getTopic();

		Logger.log("onPublish:" + msg.toString());

		HashSet<String> subscribers = (HashSet<String>) storage.getSubscribers(topic);


		for(String sub : subscribers){
			if(storage.getSession(sub) == null){
				storage.addBufferedMessage(sub, msg);
			}else {
				storage.getSession(sub).send(msg);
			}
		}


		

	}
}
