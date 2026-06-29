package org.unibl.etf.vozilagenerator;

import static org.unibl.etf.util.SimulacioniParametri.*;

import java.util.ArrayList;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.putnikgenerator.PutnikGenerator;
import org.unibl.etf.util.Util;
import org.unibl.etf.vozila.*;
import org.unibl.etf.vozila.Kamion;

public class KamionGenerator {
	private static String[] proizvodjac = {"Mercedes", "MAN", "Volvo", "Iveco", "Mack", "Scania", "Kenworth"};
	private static String[] model = {"Actros", "TGX", "FH", "Stralis", "Anthem", "R-series", "W990"};
	
	private KamionGenerator() {
		super();
	}
	
	public static Kamion generateKamion() {	// generisanje jednog kamion
		int x = Util.generateRandomInt(0, proizvodjac.length);
		String proizvodjacKamiona= proizvodjac[x];
		String modelKamiona = null;
		
		modelKamiona = model[x];
		
		int brojPutnika = Util.generateRandomInt(1, Kamion.MAX_BROJ_PUTNIKA);		// minimalno 1 putnik -> vozac
		ArrayList<Putnik> putnici = PutnikGenerator.generateNPutnika(brojPutnika);
		double masaTereta = Util.generateMasaTereta(MINIMALNA_MASA_TERETA, MAKSIMALNA_MASA_TERETA);
		
		return new Kamion(proizvodjacKamiona, modelKamiona, putnici, masaTereta, masaTereta);
	}
	
	public static ArrayList<Kamion> generateNKamiona(int brojKamiona){
		ArrayList<Kamion> kamioni = new ArrayList<>();
		for(int i = 0; i < brojKamiona; i++) {
			Kamion k = KamionGenerator.generateKamion();
			kamioni.add(k);
		}
		// (osmisliti algoritam tako da u 20% kamiona stvarna masa tereta bude do 30% veća od deklarisane)
		int brojKamionaSaVecomMasom = (int)(brojKamiona * (((double)POSTOTAK_KAMIONA_SA_VECOM_MASOM_TERETA)/100));
		double stvarnaMasa = 0, deklarisanaMasa = 0;
		for(int i = 0; i < brojKamionaSaVecomMasom; i++) {
			deklarisanaMasa = kamioni.get(i).getStvarnaMasa();
			stvarnaMasa = deklarisanaMasa + (deklarisanaMasa * ((double)POSTOTAK_UVECANJA_MASE_TERETA/100));
			kamioni.get(i).setStvarnaMasa(stvarnaMasa);
		}
		return kamioni;
	}
}

