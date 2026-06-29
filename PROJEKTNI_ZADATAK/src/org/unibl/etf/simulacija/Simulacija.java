package org.unibl.etf.simulacija;

import static org.unibl.etf.util.SimulacioniParametri.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.terminali.*;
import org.unibl.etf.util.Util;
import org.unibl.etf.vozila.Kamion;
import org.unibl.etf.vozila.Vozilo;

import java.util.logging.Level;
import static org.unibl.etf.simulacija.MyLogger.logger;

public class Simulacija {
	private Object[][] mapa;
	private AtomicInteger kraj;
	private ArrayList<Vozilo> svaVozila;	// sva vozila - kopija svih vozila kako bi mogla izvrsavati start/stop mehanizam (vozila je queue pa se iz njih brisu vozila)
	private Queue<Vozilo> vozila;	// vozila u redu
	private static ArrayList<Putnik> listaKaznjenihOsoba = new ArrayList<>();	// lista osoba koje ne mogu preci granicu
	private int duzinaStaze;	// duzina staze koja se prikazuje na gui-u (zajedo sa terminalima)
	private int brojTerminala;	// broj terminala -> provjerava da li ima vise policijskih ili carinskih terminala - uzimam veci broj i to ce mi oznacavati sirinu matrice 
	private int duzinaStazeZaVozila;	// duzina staze za vozila (bez terminala)
	private int stazaIndexZaKretanjeVozila;		// staza na kojoj se krecu vozila
	
	private ArrayList<PolicijskiTerminal> ptVozila;	// policijski terminali za vozila
	private ArrayList<PolicijskiTerminal> ptKamioni;	// policijski terminali za kamione
	private ArrayList<CarinskiTerminal> ctVozila;	// carinski terminali za vozila
	private ArrayList<CarinskiTerminal> ctKamioni;	// carinski terminali za kamione
	
	private List<Integer> ptVozilaIndexi;	// indexi gdje se nalaze policijski terminali za vozila
	private List<Integer> ctVozilaIndexi;	// indexi gdje se nalazi carinsli terminal za vozila
	private List<Integer> ptKamioniIndexi;	// indexi gdje se nalazi policijski terminal za kamione
	private List<Integer> ctKamioniIndexi;	// indexi gdje se nalazi carinski terminal za kamione
	
	private Object zvoncePtVozila;	// objekat koji saljem autima i busevima preko kojeg se vrsi komunikacija sa policijskim terminalom
	private Object zvonceCtVozila;	// objekat koji saljem autima i busevima preko kojeg se vrsi komunikacija sa carinskim terminalom
	private Object zvoncePtKamioni;	// objekat koji saljem kamionima preko kojeg se vrsi komunikacija sa policijskim terminalom
	private Object zvonceCtKamioni;	// objekat koji saljem kamionima preko kojeg se vrsi komunikacija sa carinskim terminalom
	
	private Map<Vozilo, List<String> > incidentiLog; // za prikaz svih vozila u zasebnom gui
	
	private static int brojPredjenihVozila;
	private static int brojNepredjenihVozila;
	private static int ukupanBrojVozila;
	
	{
		this.kraj = new AtomicInteger(0);
	}
	
	public Simulacija(Queue<Vozilo> vozila) {
		super();
		this.svaVozila = new ArrayList<>(vozila);
		this.vozila = vozila;
		this.duzinaStaze = DUZINA_STAZE;
		this.duzinaStazeZaVozila = this.duzinaStaze - 2;
		this.brojTerminala = (BROJ_POLICIJSKIH_TERMINALA > BROJ_CARINSKIH_TERMINALA ? BROJ_POLICIJSKIH_TERMINALA : BROJ_CARINSKIH_TERMINALA);
		this.mapa = new Object[brojTerminala][duzinaStaze];
		this.incidentiLog = new Hashtable<>();
		ukupanBrojVozila = vozila.size();
		
		this.ptVozila = new ArrayList<>();
		this.ptKamioni = new ArrayList<>();
		this.ctVozila = new ArrayList<>();	
		this.ctKamioni = new ArrayList<>();
		
		this.ptVozilaIndexi = new ArrayList<>();
//		dodavanje indexa (pozicija) terminala u listu
		for(int i = 0; i < (BROJ_POLICIJSKIH_TERMINALA - BROJ_POLICIJSKIH_TERMINALA_ZA_KAMIONE); i++) {
			ptVozilaIndexi.add(i);
			ptVozila.add(new PolicijskiTerminal(incidentiLog));
			
		}
		this.ctVozilaIndexi = new ArrayList<>();
		for(int i = 0; i < (BROJ_CARINSKIH_TERMINALA - BROJ_CARINSKIH_TERMINALA_ZA_KAMIONE); i++) {
			ctVozilaIndexi.add(i);
			ctVozila.add(new CarinskiTerminal(incidentiLog));
			
		}
		this.ptKamioniIndexi = new ArrayList<>();
		for(int i = (BROJ_POLICIJSKIH_TERMINALA - BROJ_POLICIJSKIH_TERMINALA_ZA_KAMIONE); i < brojTerminala; i++) {
			ptKamioniIndexi.add(i);
			ptKamioni.add(new PolicijskiTerminal(incidentiLog));
			
		}
		this.ctKamioniIndexi = new ArrayList<>(); 
		for(int i = (BROJ_POLICIJSKIH_TERMINALA - BROJ_POLICIJSKIH_TERMINALA_ZA_KAMIONE); i < brojTerminala; i++) {
			ctKamioniIndexi.add(i);
			ctKamioni.add(new CarinskiTerminal(incidentiLog));
			
		}

		this.stazaIndexZaKretanjeVozila = brojTerminala/2;
		
		// zvone - sluzi da obavjesti vozilo da moze da dodje na terminal
		this.zvoncePtVozila = new Object();
		this.zvonceCtVozila = new Object();
		this.zvoncePtKamioni = new Object();
		this.zvonceCtKamioni = new Object();
	}
	
	private void postaviTerminale() {
		int i = 0;
		for(PolicijskiTerminal pt : ptVozila) {		// postavljanje policijskih terminala za auta i buseve
			mapa[ptVozilaIndexi.get(i)][mapa[i].length - 2] = pt;
			i++;
		}
		i = 0;
		for(PolicijskiTerminal pt : ptKamioni) {		// postavljanje policijskih terminala za kamione
			mapa[ptKamioniIndexi.get(i)][mapa[i].length - 2] = pt;
			i++;
		}
		i = 0;
		for(CarinskiTerminal ct : ctVozila) {		// postavljanje carisnkih terminala za auta i buseve
			mapa[ctVozilaIndexi.get(i)][mapa[i].length - 1] = ct;
			i++;
		}
		i = 0;
		for(CarinskiTerminal ct : ctKamioni) {		// postavljanje carisnkih terminala za kamione
			mapa[ctKamioniIndexi.get(i)][mapa[i].length - 1] = ct;
			i++;
		}
	}
	
	private void setListaKaznjebihOsoba() {
		for(int i = 0; i < ptVozila.size(); i++)
			this.ptVozila.get(i).setListaKaznjenihOsoba(this.listaKaznjenihOsoba);
		for(int i = 0; i < ctVozila.size(); i++)
			this.ctVozila.get(i).setListaKaznjenihOsoba(this.listaKaznjenihOsoba);
		for(int i = 0; i < ptKamioni.size(); i++)
			this.ptKamioni.get(i).setListaKaznjenihOsoba(this.listaKaznjenihOsoba);
		for(int i = 0; i < ctKamioni.size(); i++)
			this.ctKamioni.get(i).setListaKaznjenihOsoba(this.listaKaznjenihOsoba);
	}
	
	private void setParametreVozila() {
		for(Vozilo vozilo : vozila) {
			vozilo.setKraj(kraj);
			vozilo.setMapa(mapa);
			vozilo.setStazaIndex(stazaIndexZaKretanjeVozila);
			vozilo.setDuzinaStazeZaVozila(duzinaStazeZaVozila);
		}
	}
	
	private void setTerminaliIndexi() {
//		provjerava da li je vozilo kamion ili ne onda saljem odgovarajuce liste indeksa gdje se nalaze odgovarajuci terminali
		for(Vozilo vozilo : vozila) {
			if(vozilo instanceof Kamion) {
				vozilo.setPtIndexi(ptKamioniIndexi);
				vozilo.setCtIndexi(ctKamioniIndexi);
			}
			else {
				vozilo.setPtIndexi(ptVozilaIndexi);
				vozilo.setCtIndexi(ctVozilaIndexi);
			}
		}
	}
	
	private void setZvoncaVozilima() {
		for(Vozilo vozilo : vozila) {
			if(vozilo instanceof Kamion) {
				vozilo.setZvonceCt(zvonceCtKamioni);
				vozilo.setZvoncePt(zvoncePtKamioni);
			}
			else {
				vozilo.setZvonceCt(zvonceCtVozila);
				vozilo.setZvoncePt(zvoncePtVozila);
			}
		}
	}
	
	private void setZvoncaTerminalima() {
		for(Terminal terminal : ptVozila) 
			terminal.setZvonce(zvoncePtVozila);
		for(Terminal terminal : ctVozila) 
			terminal.setZvonce(zvonceCtVozila);
		for(Terminal terminal : ptKamioni) 
			terminal.setZvonce(zvoncePtKamioni);
		for(Terminal terminal : ctKamioni) 
			terminal.setZvonce(zvonceCtKamioni);
	}
	
	private void pokreniTerminale() {
		for(Terminal terminal : ptVozila) 
			terminal.start();
		for(Terminal terminal : ctVozila) 
			terminal.start();
		for(Terminal terminal : ptKamioni) 
			terminal.start();
		for(Terminal terminal : ctKamioni) 
			terminal.start();
	}
	
	public void pokreniVozila() {
		for(Vozilo v : svaVozila)
			v.nastavi();
	}
	
	public void pauzirajVozila() {	// pauziraj metoda
		for(Vozilo v : svaVozila) {
			v.pauziraj();
		}
	}
	
	public Map<Vozilo, List<String>> getIncidentiLog(){
		return this.incidentiLog;
	}
	
	public void simuliraj() {
		ispisiMapu();
		FileWatcher fw = new FileWatcher();
		setParametreVozila();
		setTerminaliIndexi();
		setZvoncaVozilima();
		setZvoncaTerminalima();
		setListaKaznjebihOsoba();
		postaviTerminale();
		fw.azurirajTerminale();
		fw.start();	// ocitavavanje fajla terminala
		pokreniTerminale();
		
		
		while(vozila.isEmpty() == false) { 
			Vozilo narednoVozilo = vozila.poll(); 
			synchronized(narednoVozilo) { 
				narednoVozilo.start(); 
				try {
					narednoVozilo.wait();
				} catch (InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				} 
			}
			
		}
		
		synchronized(kraj) {
			while(kraj.get() != svaVozila.size()) {
				try {
					kraj.wait();
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		}

		System.out.println("\n\n\tSIMULACIJA JE ZAVRSENA !!!\n\n");
		System.out.println("- Broj vozila koja su presla granicu: " + brojPredjenihVozila);
		System.out.println("- Broj vozila koja nisu presla granicu: " + brojNepredjenihVozila);
		serijalizujListuKaznjenihPutnika();
		
	}
	
	public Object[][] getMapa(){
		return this.mapa;
	}
	
	public AtomicInteger getKraj() {
		return this.kraj;
	}
	
	public ArrayList<CarinskiTerminal> getCTKamioni(){
		return this.ctKamioni;
	}
	
	public ArrayList<CarinskiTerminal> getCTVozila(){
		return this.ctVozila;
	}
	
	public ArrayList<PolicijskiTerminal> getPTKamioni(){
		return this.ptKamioni;
	}
	
	public ArrayList<PolicijskiTerminal> getPTVozila(){
		return this.ptVozila;
	}
	
	public Queue<Vozilo> getQueueVozila(){
		return this.vozila;
	}
	
	private void ispisiMapu() {
		for(int i = 0; i < mapa.length; i++) {
			for(int j = 0; j < mapa[i].length; j++) {
				Object o = mapa[i][j];
				String s = o == null ? "null" : o.toString().substring(0, 10);
				System.out.print(s + "  ");
			}
			System.out.println();
		}
	}
		
	private void serijalizujListuKaznjenihPutnika() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(KAZNJENI_PUTNICI_SERIJALIZACIJA_PUTANJA));
			out.writeObject(listaKaznjenihOsoba);
			out.close();
		}catch(IOException e) {
			logger.log(Level.WARNING, "Exception: ", e);
		}
	}
		
		
	public static ArrayList<Putnik> getListaKaznjenihOsoba() {
		return listaKaznjenihOsoba;
	}

	public static int getBrojPredjenihVozila() {
		return brojPredjenihVozila;
	}
		
	public static int getBrojNepredjenihVozila(){
		return brojNepredjenihVozila;
	}

	public static void uvecajBrojPredjenihVozila() {
		Simulacija.brojPredjenihVozila++;
	}

	public static void uvecajBrojNepredjenihVozila() {
		Simulacija.brojNepredjenihVozila++;
	}

	public static int getUkupanBrojVozila() {
		return ukupanBrojVozila;
	}

		
	
	
	
	
	
	private class FileWatcher extends Thread {		// klasa za citanje fajla za rad terminala
			
			{
				this.setDaemon(true);
			}
			
			@Override
			public void run() {
				
				try {
					WatchService watchService = FileSystems.getDefault().newWatchService();
					Path dir = Paths.get(Util.buildPath(RAD_TERMINALA)).getParent();
					
					WatchKey watchKey = dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
				
					while(true) {
						for(WatchEvent<?> event : watchKey.pollEvents()) {
							azurirajTerminale();
						}
					}
			
				} catch (IOException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
				
			}
			
			
			public void azurirajTerminale() {
				FileReader reader;
				try {
					File f = Paths.get(Util.buildPath(RAD_TERMINALA)).toFile();
					reader = new FileReader(f);
					Properties p = new Properties(); //kreiranje Properties objekta
					p.load(reader); 
					for(Map.Entry<Object, Object> e : p.entrySet()) {
						String ter = (String) e.getKey();
						boolean radi = "RADI".equals(e.getValue());
						switch(ter) {
							case "PT1":
								azurirajTerminal(ptVozila.get(0), radi);
								break;
							case "PT2":
								azurirajTerminal(ptVozila.get(1), radi);
								break;
							case "PT3":
								azurirajTerminal(ptKamioni.get(0), radi);
								break;
								
							case "CT1":
								azurirajTerminal(ctVozila.get(0), radi);
								break;
							case "CT2":
								azurirajTerminal(ctKamioni.get(0), radi);
								break;
						}
					}
				} catch (FileNotFoundException e) {
//					e.printStackTrace();
					logger.log(Level.WARNING, "Exception: ", e);
				} catch (IOException e) {
//					e.printStackTrace();
					logger.log(Level.WARNING, "Exception: ", e);
				} 
			}
			
			private void azurirajTerminal(Terminal t, boolean radi) {
				if(radi) {
					t.radi();
				}
				else {
					t.neRadi();
				}
			}
			
		}
		
}
