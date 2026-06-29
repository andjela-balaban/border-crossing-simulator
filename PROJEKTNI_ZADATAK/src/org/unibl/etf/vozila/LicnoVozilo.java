package org.unibl.etf.vozila;

import static org.unibl.etf.util.SimulacioniParametri.*;

import java.util.ArrayList;

import org.unibl.etf.putnik.Putnik;

public class LicnoVozilo extends Vozilo {
	// max 5 putnika(ukljucujuci vozaca)
	public static int MAX_BROJ_PUTNIKA = MAX_BROJ_PUTNIKA_LICNO_VOZILO;
	
	public LicnoVozilo() {
		super();
	}
	
	public LicnoVozilo(String proizvodjac, String model, ArrayList<Putnik>putnici) {
		super(proizvodjac, model, MAX_BROJ_PUTNIKA, putnici);
	}
	
	@Override
	public String toString() {
		return "Licno vozilo: " + super.toString();  
	}
	
	@Override
	public String voziloInfo() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < super.getPutnici().size(); i++) {
			sb.append(super.getPutnici().get(i).getIme() + " " + super.getPutnici().get(i).getPrezime()  + " (ID: " + super.getPutnici().get(i).getIdDokumenta() + ")" + "\n");
		}
		return "Licno vozilo: " + super.voziloInfo() + "\n" +  "Putnici:\n" + sb.toString(); 
	}
}
