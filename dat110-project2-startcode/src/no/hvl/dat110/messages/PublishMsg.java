package no.hvl.dat110.messages;

public class PublishMsg extends Message {
	
	// TODO: 
	// Implement objectvariables, constructor, get/set-methods, and toString method

    private String topic;

    public PublishMsg(String user, String topic){
        super(MessageType.PUBLISH, user);
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String toString(){
        return "[New publication. User=" + getUser() + " Topic=" + getTopic() + "]";
    }
}
