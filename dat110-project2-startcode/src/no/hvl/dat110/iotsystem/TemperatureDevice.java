package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.common.TODO;

import static java.lang.Thread.sleep;

public class TemperatureDevice {
	
	private static final int COUNT = 10;
	
	public static void main(String[] args) {
		
		TemperatureSensor sn = new TemperatureSensor();

		Client client = new Client("temperaturesensor" ,Common.BROKERHOST, Common.BROKERPORT);
		client.connect();
		for(int i = 0; i < COUNT; i++) {

			client.publish("temperature", String.valueOf(sn.read()));
			try{
				sleep(1000);
			}catch (InterruptedException e){
				e.printStackTrace();
			}

		}
		client.disconnect();
		
		System.out.println("Temperature device stopping ... ");

	}
}
