package no.hvl.dat110.messages;

public class PublishMsg extends Message {

    private String topic;
    private String message;

    public PublishMsg(String user, String topic, String message) {
        super(MessageType.PUBLISH, user);
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "[New publication! User=" + getUser() + " Topic=" + topic + "Message=" + message + "]";
    }
}
