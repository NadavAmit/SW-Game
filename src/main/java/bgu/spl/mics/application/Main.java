package bgu.spl.mics.application;

import bgu.spl.mics.Input;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.services.*;

import java.io.IOException;


/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		String jsonInputPath = args[0];
		String jsonOutputName = args[1];

		Input input = Utils.readJson(jsonInputPath);

		Diary diary= Diary.getInstance();

		Thread Leia= new Thread (new LeiaMicroservice(input.getAttacks()));
		Thread HanSolo = new Thread(new HanSoloMicroservice());
		Thread C3PO = new Thread(new C3POMicroservice());
		Thread R2D2 = new Thread (new R2D2Microservice(input.getR2D2()));
		Thread Lando = new Thread(new LandoMicroservice(input.getLando()));

		Leia.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HanSolo.start();
		C3PO.start();
		R2D2.start();
		Lando.start();

		try {
			Leia.join();
			HanSolo.join();
			C3PO.join();
			R2D2.join();
			Lando.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			Utils.WriteJson(jsonOutputName,diary);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
