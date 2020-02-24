package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.PublishMsg;
import no.hvl.dat110.common.TODO;

import static java.lang.Thread.sleep;

public class DisplayDevice {
	
	private static final int COUNT = 10;
		
	public static void main (String[] args) {
		
		System.out.println("Display starting ...");

		Client client = new Client("display" ,Common.BROKERHOST, Common.BROKERPORT);
		client.connect();
		client.createTopic("temperature");
		client.subscribe("temperature");

		for(int i = 0; i < COUNT; i++) {
			Message msg = client.receive();
			try{
				sleep(1000);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}

		System.out.println("Display stopping ... ");
		client.disconnect();

	}
}
