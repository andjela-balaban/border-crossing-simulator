package org.unibl.etf.vozila;

import static org.unibl.etf.util.SimulacioniParametri.*;

import java.util.*;
import org.unibl.etf.putnik.*;

public class Autobus extends Vozilo {
	// max kapcitet 52 putnika(ukljucujuci vozaca)
	public static int MAX_BROJ_PUTNIKA = MAX_BROJ_PUTNIKA_AUTOBUS;
	private Collection<Kofer> gepek;
	
	public Autobus() {
		super();
	}
	
	public Autobus(String proizvodjac, String model, ArrayList<Putnik>putnici) {
		super(proizvodjac, model, MAX_BROJ_PUTNIKA, putnici);
		this.gepek = new ArrayList<>();
		for(Putnik p : putnici) {
			if(p.isImaKofer() == true)
				gepek.add(p.getKofer());
		}
	}
	
	@Override
	public String toString() {
		return "Autobus: " + super.toString();
	}
	
	@Override
	public String voziloInfo() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < super.getPutnici().size(); i++) {
			sb.append(super.getPutnici().get(i).getIme() + " " + super.getPutnici().get(i).getPrezime()  + " (ID: " + super.getPutnici().get(i).getIdDokumenta() + ")" + "\n");
		}
		return "Autobus: " + super.voziloInfo() + "\n" +  "Putnici:\n" + sb.toString();  
	}
}
