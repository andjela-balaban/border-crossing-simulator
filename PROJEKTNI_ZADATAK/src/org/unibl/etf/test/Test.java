package org.unibl.etf.test;

import static org.unibl.etf.util.SimulacioniParametri.*;
import org.unibl.etf.simulacija.*;

import static org.unibl.etf.simulacija.MyLogger.logger;

import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;

import org.unibl.etf.putnik.Dokument;
import org.unibl.etf.putnik.Kofer;
import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.putnikgenerator.DokumentGenerator;
import org.unibl.etf.putnikgenerator.KoferGenerator;
import org.unibl.etf.putnikgenerator.PutnikGenerator;
import org.unibl.etf.util.Util;
import org.unibl.etf.vozila.Autobus;
import org.unibl.etf.vozila.Kamion;
import org.unibl.etf.vozila.LicnoVozilo;
import org.unibl.etf.vozila.Vozilo;
import org.unibl.etf.vozilagenerator.AutobusGenerator;
import org.unibl.etf.vozilagenerator.KamionGenerator;
import org.unibl.etf.vozilagenerator.LicnoVoziloGenerator;

public class Test {
	public static void main(String[] args) {

		
		
		Queue<Vozilo> vozila = Util.generateQueueVozila();
		
		for(Vozilo v : vozila) {
			System.out.println(v.voziloInfo());
		}
			
		System.out.println("---------------------------------------------------------------------");
		System.out.println("---------------------------------------------------------------------");
		Simulacija simulacija = new Simulacija(vozila);
		
		Thread t = new Thread(() -> {
			Scanner in = new Scanner(System.in);
			String input = null;
			while(!"kraj".equalsIgnoreCase(input)) {
				input = in.nextLine();
				switch(input) {
				case "stop":
					simulacija.pauzirajVozila();
					break;
				case "start":
					simulacija.pokreniVozila();
					break;
				}		
				
			}
			in.close();
		});
		t.setDaemon(true);
		t.start();
		
		simulacija.simuliraj();
		
	
	}
}

