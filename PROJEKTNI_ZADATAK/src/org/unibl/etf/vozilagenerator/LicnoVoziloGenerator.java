package org.unibl.etf.vozilagenerator;

import java.util.ArrayList;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.putnikgenerator.PutnikGenerator;
import org.unibl.etf.util.Util;
import org.unibl.etf.vozila.LicnoVozilo;

//import org.unibl.etf.projektni.Util.*;

public class LicnoVoziloGenerator {
	private static String[] proizvodjac = {"Mercedes", "Peugeot", "Audi", "Volkswagen", "BMW", "Renault", "Citroen", "Fiat"};
	private static String[] model = {"E350", "A class", "GLA", "208", "206", "508", "A2", "A8", "Q8", "Arteon", "Golf", "Beetle",
								"315", "420", "525", "Clio", "Laguna", "Twingo", "C3", "C4 Picasso", "Saxo", "500", "Punto", "Panda"};
	
	private LicnoVoziloGenerator() {
		super();
	}
	
	public static LicnoVozilo generateLicnoVozilo() {
		int x = Util.generateRandomInt(0, proizvodjac.length);
		String proizvodjacLicnogVozila = proizvodjac[x];
		String modelLicnogVozila = null;
		x = Util.generateRandomInt(x * 3, x * 3 + 2);
		modelLicnogVozila = model[x];
		
		
		int brojPutnika = Util.generateRandomInt(1, LicnoVozilo.MAX_BROJ_PUTNIKA);		// minimalno 1 putnik -> vozac
		ArrayList<Putnik> putnici = PutnikGenerator.generateNPutnika(brojPutnika);
		
		return new LicnoVozilo(proizvodjacLicnogVozila, modelLicnogVozila, putnici);
	}
	
	public static ArrayList<LicnoVozilo> generateNLicnihVozila(int brojLicnihVozila){
		ArrayList<LicnoVozilo> licnaVozila = new ArrayList<>();
		for(int i = 0; i < brojLicnihVozila; i++) {
			LicnoVozilo lv = LicnoVoziloGenerator.generateLicnoVozilo();
			licnaVozila.add(lv);
		}
		return licnaVozila;
	}
}

