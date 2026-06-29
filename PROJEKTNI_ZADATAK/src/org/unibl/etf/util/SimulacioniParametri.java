package org.unibl.etf.util;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;

import static org.unibl.etf.simulacija.MyLogger.logger;

public class SimulacioniParametri {
	private SimulacioniParametri() {
		super();
	}
	
	static {
		try {
			FileReader reader = new FileReader(Util.buildPath("../PROJEKTNI_ZADATAK/simulacioniParametri.txt")); 
			Properties p = new Properties(); //kreiranje Properties objekta
			p.load(reader); 
			VJEROVATNOCA_NEISPRAVNOG_DOKUMENTA = Integer.valueOf(p.getProperty("VJEROVATNOCA_NEISPRAVNOG_DOKUMENTA"));
			VJEROVATNOCA_GENERISANJA_DOKUMENTACIJE = Integer.valueOf(p.getProperty("VJEROVATNOCA_GENERISANJA_DOKUMENTACIJE")); 
			VJEROVATNOCA_DA_PUTNIK_IMA_KOFER = Integer.valueOf(p.getProperty("VJEROVATNOCA_DA_PUTNIK_IMA_KOFER"));
			VJEROVATNOCA_NEDOZVOLJENIH_STVARI_U_KOFERU = Integer.valueOf(p.getProperty("VJEROVATNOCA_NEDOZVOLJENIH_STVARI_U_KOFERU"));
			POSTOTAK_KAMIONA_SA_VECOM_MASOM_TERETA = Integer.valueOf(p.getProperty("POSTOTAK_KAMIONA_SA_VECOM_MASOM_TERETA"));
			POSTOTAK_UVECANJA_MASE_TERETA = Integer.valueOf(p.getProperty("POSTOTAK_UVECANJA_MASE_TERETA"));
			UKUPAN_BROJ_LICNIH_VOZILA = Integer.valueOf(p.getProperty("UKUPAN_BROJ_LICNIH_VOZILA"));
			UKUPAN_BROJ_KAMIONA = Integer.valueOf(p.getProperty("UKUPAN_BROJ_KAMIONA"));
			UKUPAN_BROJ_AUTOBUSA = Integer.valueOf(p.getProperty("UKUPAN_BROJ_AUTOBUSA"));
			MINIMALNA_MASA_TERETA = Integer.valueOf(p.getProperty("MINIMALNA_MASA_TERETA"));
			MAKSIMALNA_MASA_TERETA = Integer.valueOf(p.getProperty("MAKSIMALNA_MASA_TERETA"));
			BROJ_POLICIJSKIH_TERMINALA = Integer.valueOf(p.getProperty("BROJ_POLICIJSKIH_TERMINALA"));
			BROJ_CARINSKIH_TERMINALA = Integer.valueOf(p.getProperty("BROJ_CARINSKIH_TERMINALA"));
			DUZINA_STAZE = Integer.valueOf(p.getProperty("DUZINA_STAZE"));
			VRIJEME_PROCESIRANJA_PUTNIKA_AUTOBUS = Double.valueOf(p.getProperty("VRIJEME_PROCESIRANJA_PUTNIKA_AUTOBUS"));
			VRIJEME_PROCESIRANJA_PUTNIKA_LICNO_VOZILO_KAMION = Double.valueOf(p.getProperty("VRIJEME_PROCESIRANJA_PUTNIKA_LICNO_VOZILO_KAMION"));
			MAX_BROJ_PUTNIKA_LICNO_VOZILO = Integer.valueOf(p.getProperty("MAX_BROJ_PUTNIKA_LICNO_VOZILO"));
			MAX_BROJ_PUTNIKA_KAMION = Integer.valueOf(p.getProperty("MAX_BROJ_PUTNIKA_KAMION"));
			MAX_BROJ_PUTNIKA_AUTOBUS = Integer.valueOf(p.getProperty("MAX_BROJ_PUTNIKA_AUTOBUS"));
			VRIJEME_ZADRZAVANJA_LICNOG_VOZILA_NA_CARINSKOM_TERMINALU = Integer.valueOf(p.getProperty("VRIJEME_ZADRZAVANJA_LICNOG_VOZILA_NA_CARINSKOM_TERMINALU"));
			BROJ_POLICIJSKIH_TERMINALA_ZA_KAMIONE = Integer.valueOf(p.getProperty("BROJ_POLICIJSKIH_TERMINALA_ZA_KAMIONE"));
			BROJ_CARINSKIH_TERMINALA_ZA_KAMIONE = Integer.valueOf(p.getProperty("BROJ_CARINSKIH_TERMINALA_ZA_KAMIONE"));
			KAZNJENI_PUTNICI_SERIJALIZACIJA_PUTANJA = Util.buildPath(p.getProperty("KAZNJENI_PUTNICI_SERIJALIZACIJA_PUTANJA"));
			LOGGER_PUTANJA = Util.buildPath(p.getProperty("LOGGER_PUTANJA"));
			RAD_TERMINALA = Util.buildPath(p.getProperty("RAD_TERMINALA"));
			
			
		}catch(IOException e) {
//			e.printStackTrace();
			logger.log(Level.WARNING, "Exception: ", e);
		}
	}
	
	
//	Od svih dokumenata putnika, 3% je neispravno i isti ne mogu preći granicu.
	public static int VJEROVATNOCA_NEISPRAVNOG_DOKUMENTA; 
	
//	vjerovatnoca da je potrebno generisati carisnu dokumentaciju je 50%
	public static int VJEROVATNOCA_GENERISANJA_DOKUMENTACIJE; 
	
	public static int VJEROVATNOCA_DA_PUTNIK_IMA_KOFER;	// vjerovatnoca da putnik ima kofer je 70%
	public static int VJEROVATNOCA_NEDOZVOLJENIH_STVARI_U_KOFERU;	//nedozvoljene stvari su u 10% kofer
	
//	20% kamiona stvarna masa tereta bude do 30% veća od deklarisane
	public static int POSTOTAK_KAMIONA_SA_VECOM_MASOM_TERETA;
	public static int POSTOTAK_UVECANJA_MASE_TERETA;
	
//	broj voizila za simulaciju
	public static int UKUPAN_BROJ_LICNIH_VOZILA;
	public static int UKUPAN_BROJ_KAMIONA;
	public static int UKUPAN_BROJ_AUTOBUSA;
	
//	minimalana i maksimalana masa tereta kamiona
	public static int MINIMALNA_MASA_TERETA;
	public static int MAKSIMALNA_MASA_TERETA;
	
//	ukupan broj policijskih i carinskih terminala
	public static int BROJ_POLICIJSKIH_TERMINALA;
	public static int BROJ_CARINSKIH_TERMINALA;
	
//	duzina staze - koliko ce polja na matrici biti prikazano zajendo sa terminalima i stazama
	public static int DUZINA_STAZE;
	
//	vrijeme procesiranja putnika 
	public static double VRIJEME_PROCESIRANJA_PUTNIKA_AUTOBUS;
	public static double VRIJEME_PROCESIRANJA_PUTNIKA_LICNO_VOZILO_KAMION;
	
//  maksimalan dozvoljen broj putnika u vozilu
	public static int MAX_BROJ_PUTNIKA_LICNO_VOZILO;
	public static int MAX_BROJ_PUTNIKA_KAMION;
	public static int MAX_BROJ_PUTNIKA_AUTOBUS;
		
// vrijeme procesiranja licnog vozila na carinskom terminalu - nema provjera
	public static int VRIJEME_ZADRZAVANJA_LICNOG_VOZILA_NA_CARINSKOM_TERMINALU; 
	
//	broj policijskih i carinskih terminala za kamione
	public static int BROJ_POLICIJSKIH_TERMINALA_ZA_KAMIONE;
	public static int BROJ_CARINSKIH_TERMINALA_ZA_KAMIONE;
	
//	putanja do fajla gdje ce se serijalizovati kaznjeni putnici
	public static String KAZNJENI_PUTNICI_SERIJALIZACIJA_PUTANJA;
	
//	putanja do fajla za logove
	public static String LOGGER_PUTANJA;
	
//	putanja do fajla za rad terminala
	public static String RAD_TERMINALA;
	
}
