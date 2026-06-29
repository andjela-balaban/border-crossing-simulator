package org.unibl.etf.vozila;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.simulacija.Simulacija;
import org.unibl.etf.terminali.*;
import java.util.logging.Level;
import static org.unibl.etf.simulacija.MyLogger.logger;


public abstract class Vozilo extends Thread {
	private String proizvodjac;
	private String model;
	private int maxBrojPutnika;
	private ArrayList<Putnik> putnici;
	private Putnik vozac;
	
	private AtomicInteger kraj;
	private Object[][] mapa;
	private int napredak;	// napredujem dok ne dodjem do terminala 
	private int stazaIndex;	// sva auta imaju istu stazu jer se krecu u koloni(dok ne dodju do terminala)
	private int duzinaStazeZaVozila; 	// duzina staze za vozila dok se ne dodje do terminala

	private List<Integer> ptIndexi;	// indexi gdje se nalaze policijski terminali 
	private List<Integer> ctIndexi;	// indexi gdje se nalazi carinsli terminal
	
	private Object zvoncePt;	// objekat preko kojeg se vrsi komunikacija sa policijskim terminalom
	private Object zvonceCt;	// objekat preko kojeg se vrsi komunikacija sa carinskim terminalom
	
	private boolean zabranjenPrelazak;
	private boolean pauza;
	
	public Vozilo() {
		super();
	}
	
	public Vozilo(String proizvodjac, String model, int maxBrojPutnika, ArrayList<Putnik>putnici) {
		super();
		this.proizvodjac = proizvodjac;
		this.model = model;
		this.maxBrojPutnika = maxBrojPutnika;
		this.putnici = new ArrayList<>(putnici);
		this.vozac = putnici.get(0);
	}
	
	public Vozilo(String proizvodjac, String model, int maxBrojPutnika, ArrayList<Putnik>putnici, Object[][] mapa, AtomicInteger kraj, int staza) {
		super();
		this.proizvodjac = proizvodjac;
		this.model = model;
		this.maxBrojPutnika = maxBrojPutnika;
		this.putnici = new ArrayList<>(putnici);
		this.vozac = putnici.get(0);
		this.mapa = mapa;
		this.kraj = kraj;
		this.stazaIndex = staza;
	}
	
	public void zabraniPrelazakVozila() {
		this.zabranjenPrelazak = true;
	}
	
	public Putnik getVozac() {
		return vozac;
	}

	public int getDuzinaStazeZaVozila() {
		return duzinaStazeZaVozila;
	}

	public void setDuzinaStazeZaVozila(int duzinaStazeZaVozila) {
		this.duzinaStazeZaVozila = duzinaStazeZaVozila;
	}

	public void setPtIndexi(List<Integer> ptIndexi) {
		this.ptIndexi = ptIndexi;
	}

	public void setCtIndexi(List<Integer> ctIndexi) {
		this.ctIndexi = ctIndexi;
	}

	public String getProizvodjac() {
		return proizvodjac;
	}

	public void setProizvodjac(String proizvodjac) {
		this.proizvodjac = proizvodjac;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public int getMaxBrojPutnika() {
		return maxBrojPutnika;
	}

	public void setMaxBrojPutnika(int maxBrojPutnika) {
		this.maxBrojPutnika = maxBrojPutnika;
	}

	public ArrayList<Putnik> getPutnici() {
		return putnici;
	}

	public void setPutnici(ArrayList<Putnik> putnici) {
		this.putnici = putnici;
	}
	
	public void setKraj(AtomicInteger kraj) {
		this.kraj = kraj;
	}

	public void setMapa(Object[][] mapa) {
		this.mapa = mapa;
	}

	public void setStazaIndex(int stazaIndex) {
		this.stazaIndex = stazaIndex;
	}

	public Object getZvoncePt() {
		return zvoncePt;
	}

	public void setZvoncePt(Object zvoncePt) {
		this.zvoncePt = zvoncePt;
	}

	public Object getZvonceCt() {
		return zvonceCt;
	}

	public void setZvonceCt(Object zvonceCt) {
		this.zvonceCt = zvonceCt;
	}

	@Override
	public String toString(){	
		return proizvodjac + " " + model;
	}
	
	public void pauziraj() {
		this.pauza = true;
	}
	
	public synchronized void nastavi() {
		this.pauza = false;
		this.notify();
	}
	
	private void metodaZaStartStopMehanizam() {
		synchronized(this) {
			while(pauza) {
				try {
					this.wait();
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		}
	}
	
	@Override
	public void run() {
		
		// --------------------------------------------------- DIO STAZE DO TERMINALA -------------------------------------------------------------------------- //
		postaviNaPocetnuPoziciju();
		
		while(napredak < mapa[stazaIndex].length - 1 - 2) {	// -2 je zbog terminala koja se nalaze na posljednjim mjestima u matrici
			metodaZaStartStopMehanizam();
			napreduj();	// vrsimo standardno kretanje do terminala 
		}
		
		// --------------------------------------------------- DIO STAZE OD TERMINALA -------------------------------------------------------------------------- //

		metodaZaStartStopMehanizam();
		Terminal policijskiTerminal = interakcijaSaTerminalom(ptIndexi, zvoncePt);

		this.napredak++;

		metodaZaStartStopMehanizam();
		if(!zabranjenPrelazak) {
			
			metodaZaStartStopMehanizam();
			
			Terminal carinskiTerminal = interakcijaSaTerminalom(ctIndexi, zvonceCt);
			carinskiTerminal.napustiTerminal();		
			if(!zabranjenPrelazak) {
				System.out.println(this + " je presao granicu !!! ");
				Simulacija.uvecajBrojPredjenihVozila();
				System.out.println("-------" + Simulacija.getBrojPredjenihVozila() + " vozila preslo !!!!!!!!");
			}
			else {
				System.out.println("\nVozilo " + this + " ne moze dalje da nastavi kretanje...\n");
				Simulacija.uvecajBrojNepredjenihVozila();
				System.out.println("-------" + Simulacija.getBrojNepredjenihVozila() + " vozila NIJE preslo !!!!!!!!");

			}
				
			
		}
		else {

			metodaZaStartStopMehanizam();
			
			System.out.println("\nVozilo " + this + " ne moze dalje da nastavi kretanje...\n");
			Simulacija.uvecajBrojNepredjenihVozila();
			System.out.println("-------" + Simulacija.getBrojNepredjenihVozila() + " vozila NIJE preslo !!!!!!!!");
			skiniSaPrethodnogPolja(stazaIndex, napredak);
		}
		
		synchronized(this) {	// sad se preko this sinhronizuje ali na kraju treba preko zvonceta
			this.notify();	// oslobadja se mjesto i salje obavjestenje da je to mjesto slobodno da bi vozilo iza moglo da se pomjeri
			synchronized(kraj) {	// kraj je kad sva vozila predju granicu
				kraj.set(kraj.get() + 1);
				kraj.notifyAll();
			}
		}
	}
	
	private Terminal interakcijaSaTerminalom(List<Integer> indexiTerminala, Object zvonce) {
		
		metodaZaStartStopMehanizam();
		
		
		label: while(true) {		// label - moguce je da mi dva vozila cekaju na slobodan terminal pa ih zvonce obavjesti, prvi zauzme terminala drugi dobija -1 index - GRESKA (zato label)
			synchronized(zvonce) {
				while(nadjiSlobodanTerminal(indexiTerminala) == -1) {
					try {
						zvonce.wait();
					}catch(InterruptedException e) {
						logger.log(Level.WARNING, "Exception: ", e);
					}
				}
			}

			synchronized(this) {
				int i = nadjiSlobodanTerminal(indexiTerminala);
				if(i == -1)
					continue label;
				Terminal t = (Terminal) mapa[i][napredak + 1];
				t.postaviSeUTerminal(this);

				
				skiniSaPrethodnogPolja(stazaIndex, napredak);
				this.stazaIndex = i;		// azuririranje indexa staze po kojoj se krecem
				
				try {
					this.wait();							
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
				return t;
			}
			
		}
		
	}
	
	private int nadjiSlobodanTerminal(List<Integer> indexiTerminala) {	//vraca mi index na kojem se nalazi slobodan terminal
		metodaZaStartStopMehanizam();
		for(int i = 0; i < indexiTerminala.size(); i++) {
			Terminal terminal = (Terminal) mapa[indexiTerminala.get(i)][napredak + 1];
			if(terminal.daLiJeSlobodno()) {	// obavjestavam prvi slobodni terminal - ako je slobodan vozilo ulazi na terminal i ide na obradu
				return indexiTerminala.get(i);
			}	
		}
		return -1;
	}
	
	private void skiniSaPrethodnogPolja(int x, int y) {
		if(mapa[x][y] instanceof Terminal) {
			Terminal t = (Terminal) mapa[x][y];
			t.napustiTerminal();	// napustam policijski terminal tek kada budem imala mjesto na carinskom da se obradim
		}
		else if(mapa[x][y] == this) {
			mapa[x][y] = null;	// ciscenje one pozicije prije terminala
			synchronized(this) {
				this.notify();	// vozilo oslobadja mjesto i salje obavjestenje da je to mjesto slobodno da bi vozilo iza njega moglo da se pomjeri
			}
		}			
	}
	
	
	private void postaviNaPocetnuPoziciju() {
		this.napredak = 0;
		if(mapa[stazaIndex][napredak] == null)
			mapa[stazaIndex][napredak] = this;
	}
	
	
	private void napreduj() {
		
		boolean slobodno = provjeriSljedecuPoziciju();
		
		if(slobodno == false) {
			Vozilo narednoVozilo = (Vozilo)mapa[stazaIndex][napredak + 1];
			synchronized(narednoVozilo) {	// sinhronizovani blok - sinhronizujem mjesto ispred, ako nije slobodno cekam dok se mjesto ispred ne oslobodi
				while(provjeriSljedecuPoziciju() == false) {
					try {
						narednoVozilo.wait();	// vozilo ceka dok se vozilo ispred njega ne pomjeri da moze da dodje na njegovo mjesto 
					}catch(InterruptedException e) {
						logger.log(Level.WARNING, "Exception: ", e);
					}
				}	
			}
		}
		if(mapa[stazaIndex][napredak] == this)
			mapa[stazaIndex][napredak] = null;	// vozilo "ocisti" poziciju za sobom
	
		napredak++;	// posto je sljedeca pozicija slobodna mogu se pomjeriti na sljedece mjesto
		
		if(mapa[stazaIndex][napredak] == null)
			mapa[stazaIndex][napredak] = this;	//vozilo se postavlja na sljedece slobodno mjesto
		
		synchronized(this) {
			this.notify();	// oslobadja se mjesto i salje obavjestenje da je to mjesto slobodno da bi vozilo iza moglo da se pomjeri
		}
	}
	
	// ako je true onda je sljedeca pozicija slobodna i vozilo se moze kretati
	private boolean provjeriSljedecuPoziciju() {
		return (mapa[stazaIndex][napredak + 1] == null);
	}
	
	public String voziloInfo() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < getPutnici().size(); i++) {
			sb.append(getPutnici().get(i).getIme() + " " + getPutnici().get(i).getPrezime()  + " (ID: " + getPutnici().get(i).getIdDokumenta() + ")" + "\n");
		}
//		return proizvodjac + " " + model + "\n" +  "Putnici:\n" + sb.toString();
		return proizvodjac + " " + model ;
	}
	
}
