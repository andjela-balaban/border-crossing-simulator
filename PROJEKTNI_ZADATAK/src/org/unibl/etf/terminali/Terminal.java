package org.unibl.etf.terminali;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.vozila.*;
import java.util.logging.Level;
import static org.unibl.etf.simulacija.MyLogger.logger;

public abstract class Terminal extends Thread{
	
	private boolean radi;
	private Object[][] mapa;
	private Vozilo trenutnoVozilo;
	private ArrayList<Putnik> listaKaznjenihOsoba;	// lista osoba koje ne mogu preci granicu
	protected Map<Vozilo, List<String> > incidentiLog; // prikaz u gui-u
	private Object zvonce;
	private Object budjenje = new Object();

	public Terminal(Map<Vozilo, List<String> > incidentiLog) {
		this.radi = true;
		this.setDaemon(true);
		this.incidentiLog = incidentiLog;
	}
	
	@Override
	public void run() {
		while(true) {
			synchronized(budjenje) {	// terminal ne radi ako je u fajlu upisano bilo sta sto nije "RADI"
				while(!radi) {
					System.out.println("\n\t TERMINAL - " + this + " ne radi!!\n\n");
					try {
						budjenje.wait();
					}catch(InterruptedException e) {
						logger.log(Level.WARNING, "Exception: ", e);
					}
				}
			}
			synchronized(this) {
				try {		// ceka na obavjestenje od vozila da krene sa radom - obajvestenje ce dobiti kad vozilo dodje do terminala
					this.wait();						
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}				
			}
			obradiVozilo(trenutnoVozilo);
			
			synchronized(trenutnoVozilo) {
				trenutnoVozilo.notify();					
			}
		}
	}

	public abstract void obradiVozilo(Vozilo vozilo);
	
	public synchronized void postaviSeUTerminal(Vozilo vozilo) {
		this.trenutnoVozilo = vozilo;
		this.notify();
	}
	
	public void napustiTerminal() {
		trenutnoVozilo = null;
		synchronized(zvonce) {
			zvonce.notify();
		}
	}
	
	public void neRadi() {
		this.radi = false;
	}
	
	public void radi() {
		synchronized(budjenje) {
			this.radi = true;
			budjenje.notify();			
		}
		synchronized(zvonce) {
			zvonce.notify();
		}
	}
	
	public Map<Vozilo, List<String> > incidenti(){
		return incidentiLog;
	}
	
	public boolean daLiRadi() {
		return this.radi;
	}

	public boolean daLiJeSlobodno() {	// provjerava da li se na terminalu trenutno procesira vozilo ili je slobodno i da li radi
		return this.trenutnoVozilo == null && this.radi;
	}
	
	public void setTrenutnoVozilo(Vozilo vozilo) {
		this.trenutnoVozilo = vozilo;
	}
	
	public Vozilo getTrenutnoVozilo() {
		return trenutnoVozilo;
	}

	public ArrayList<Putnik> getListaKaznjenihOsoba() {
		return listaKaznjenihOsoba;
	}

	public void setListaKaznjenihOsoba(ArrayList<Putnik> listaKaznjenihOsoba) {
		this.listaKaznjenihOsoba = listaKaznjenihOsoba;
	}

	public Object getZvonce() {
		return zvonce;
	}

	public void setZvonce(Object zvonce) {
		this.zvonce = zvonce;
	}
	
	@Override
	public String toString() {
		return "terminal";				
	}
	
	protected void zabraniPrelazakVozila(Vozilo v, String poruka) {
		v.zabraniPrelazakVozila();
		
		if(incidentiLog.containsKey(v)) { 	// trenutno vozilo je vec imalo neki prekrsaj samo dodajemo novu poruku
			incidentiLog.get(v).add(poruka);
		} else { // trenutno vozilo jos do sad nije imalo prekrsaja, pa moramo napraviti prvo entry sa listom
			List<String> poruke = new ArrayList<>();
			poruke.add(poruka);
			incidentiLog.put(v, poruke);
		}
	}
	
	protected void zabraniPrelazakPutnika(Vozilo v, Putnik p, String poruka) {
		if(incidentiLog.containsKey(v)) {
			incidentiLog.get(v).add(poruka);
		} else {
			List<String> poruke = new ArrayList<>();
			poruke.add(poruka);
			incidentiLog.put(v, poruke);
		}
	}
}
