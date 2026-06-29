package application;

import java.util.Queue;
import java.util.Scanner;

import org.unibl.etf.simulacija.Simulacija;
import org.unibl.etf.util.Util;
import org.unibl.etf.vozila.Vozilo;

public class IzvrsavacSimulacije {

	static Simulacija simulacija;
	
	static {
		Queue<Vozilo> vozila = Util.generateQueueVozila();
		simulacija = new Simulacija(vozila);
	}
	
	static void pokreni() {
		
		
//		for(Vozilo v : vozila) {
//			System.out.println(v);
////			System.out.println(v.getPutnici());
//		}
			
//		System.out.println("---------------------------------------------------------------------");
//		System.out.println("---------------------------------------------------------------------");
		
		
		Thread thread = new Thread(() -> {
			simulacija.simuliraj();
		});
		
		thread.start();
	}
}
