package org.unibl.etf.util;

import org.unibl.etf.vozilagenerator.AutobusGenerator;
import org.unibl.etf.vozilagenerator.KamionGenerator;
import org.unibl.etf.vozilagenerator.LicnoVoziloGenerator;
import java.util.concurrent.*;

import static org.unibl.etf.util.SimulacioniParametri.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.unibl.etf.vozila.Vozilo;

public class Util {
	
	private Util() {
		super();
	}

	
	public static Queue<Vozilo> generateQueueVozila() {
		
		ArrayList<Vozilo> vozila = new ArrayList<>();
		
		vozila.addAll(AutobusGenerator.generateNAutobusa(UKUPAN_BROJ_AUTOBUSA));
		vozila.addAll(KamionGenerator.generateNKamiona(UKUPAN_BROJ_KAMIONA));
		vozila.addAll(LicnoVoziloGenerator.generateNLicnihVozila(UKUPAN_BROJ_LICNIH_VOZILA));

		Collections.shuffle(vozila);	// postavljanje vozila na slucajno generisane pozicije u redu
		
		Queue<Vozilo> vozilaQueue = new LinkedBlockingQueue<>();
		vozilaQueue.addAll(vozila);
		
		return vozilaQueue;
	}
	
	
	public static int generateRandomInt(int min, int max) {
		return (int)(Math.random() * (max - min) + min);       
	}
	
	
	public static boolean generateRandomBoolean() { 
		Random random = new Random();
		return random.nextBoolean();
	}
	
	
	public static double generateMasaTereta(double min, double max) { 
		return (Math.random() * (max - min) + min); 
	}
	
	
	public static boolean generateVjerovatnoca(int vjerovatnoca) {
		double vjerovatnoca2 = Math.random();
		
		return (vjerovatnoca2 > (double)vjerovatnoca/100);		
	}
	
	public static Path generateStringToPath(String path) {
		String[] parts = path.split("/"); 
		return Paths.get(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
	}
	
	public static String buildPath(String input) {
        // Zamjena svih pojava "\" i "/" s odgovarajućim separatorom za trenutni OS
        String separator = File.separator;
        String path = input.replaceAll("[\\\\/]", separator);
        
        // Ako je zadnji znak separator, ukloni ga
        if (path.endsWith(separator)) {
            path = path.substring(0, path.length() - 1);
        }
        
        return path;
    }
	
}

