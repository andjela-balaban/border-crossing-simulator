package org.unibl.etf.vozilagenerator;

import java.util.*;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.putnikgenerator.PutnikGenerator;
import org.unibl.etf.util.Util;
import org.unibl.etf.vozila.Autobus;

public class AutobusGenerator {
	private static String[] proizvodjac = {"Mercedes", "MAN", "Volvo", "Iveco", "Setra"};
	private static String[] model = {"Tourismo", "Lion's Coach", "9700", "Crossway", "S 431 DT"};
	
	private AutobusGenerator() {
		super();
	}
	
	public static Autobus generateAutobus() {	// generisanje jednog autobusa
		int x = Util.generateRandomInt(0, proizvodjac.length);
		String proizvodjacAutobusa = proizvodjac[x];
		String modelAutobusa = null;

		modelAutobusa = model[x];		
		
		int brojPutnika = Util.generateRandomInt(1, Autobus.MAX_BROJ_PUTNIKA);		// minimalno 1 putnik -> vozac
		ArrayList<Putnik> putnici = PutnikGenerator.generateNPutnika(brojPutnika);
		
		return new Autobus(proizvodjacAutobusa, modelAutobusa, putnici);
	}
	
	public static ArrayList<Autobus> generateNAutobusa(int brojAutobusa){
		ArrayList<Autobus> autobusi = new ArrayList<>();
		for(int i = 0; i < brojAutobusa; i++) {
			Autobus a = AutobusGenerator.generateAutobus();
			autobusi.add(a);
		}
		return autobusi;
	}
}
