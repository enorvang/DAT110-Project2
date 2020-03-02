package no.hvl.dat110.broker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messagetransport.Connection;

public class Storage {

	//maps topics to a set of users subscriped to that topic
	protected ConcurrentHashMap<String, Set<String>> subscriptions;

	//mapping from user to clientsession object that represents the connection/session with the client
	protected ConcurrentHashMap<String, ClientSession> clients;

	protected ConcurrentHashMap<String, Set<Message>> bufferedMessages;

	public Storage() {
		subscriptions = new ConcurrentHashMap<>();
		clients = new ConcurrentHashMap<>();
		bufferedMessages = new ConcurrentHashMap<>();
	}

	public Collection<ClientSession> getSessions() {
		return clients.values();
	}

	public Set<String> getTopics() {

		return subscriptions.keySet();

	}

	public ClientSession getSession(String user) {

		ClientSession session = clients.get(user);

		return session;
	}

	public Set<String> getSubscribers(String topic) {

		return (subscriptions.get(topic));

	}

	public Set<Message> getBufferedMessages(String user){
		return bufferedMessages.get(user);
	}

	public void addBufferedMessage(String user, Message message){
		Set<Message> m;
		if(!bufferedMessages.containsKey(user)) {
			m = new HashSet<>();
		}else {
			m = bufferedMessages.get(user);
		}
		m.add(message);
		bufferedMessages.put(user, m);

	}

	public void clearBufferedMessages(String user){
		bufferedMessages.remove(user);
	}

	public void addClientSession(String user, Connection connection) {

		ClientSession cs = new ClientSession(user, connection);
		clients.put(user, cs);
		
	}

	public void removeClientSession(String user) {

		clients.remove(user);
		
	}

	public void createTopic(String topic) {

		Set<String> subscribers = new HashSet<>();
		subscriptions.put(topic, subscribers);

	}

	public void deleteTopic(String topic) {

		subscriptions.remove(topic);
		
	}

	public void addSubscriber(String user, String topic) {

		HashSet<String> hs = (HashSet<String>) getSubscribers(topic);
		hs.add(user);

		subscriptions.put(topic, hs);
		
	}

	public void removeSubscriber(String user, String topic) {

		HashSet<String> hs = (HashSet<String>) getSubscribers(topic);
		hs.remove(user);

		subscriptions.put(topic, hs);
	}
}
