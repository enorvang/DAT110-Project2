package no.hvl.dat110.messages;

public class DeleteTopicMsg extends Message {

    private String topic;

    public DeleteTopicMsg(String user, String topic) {
        super(MessageType.DELETETOPIC, user);
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String toString() {
        return "[Topic deleted. User=" + getUser() + " Topic=" + getTopic() + "]";
    }
}
