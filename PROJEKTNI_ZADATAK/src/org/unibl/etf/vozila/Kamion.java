package org.unibl.etf.vozila;

import static org.unibl.etf.util.SimulacioniParametri.*;

import java.util.*;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.util.Util;

public class Kamion extends Vozilo {
	
	// max kapacitet 3 putnika (ukljucujuci vozaca)
	public static int MAX_BROJ_PUTNIKA = MAX_BROJ_PUTNIKA_KAMION;
	
	// podaci vezani za teret
	private boolean carinskaDokumentacija;
	private double deklarisanaMasa;
	private double stvarnaMasa;
	
	
	public Kamion() {
		super();
		this.carinskaDokumentacija = Util.generateVjerovatnoca(VJEROVATNOCA_GENERISANJA_DOKUMENTACIJE);
	}
	
	public Kamion(String proizvodjac, String model, ArrayList<Putnik>putnici, double deklarisanaMasa, double stvarnaMasa) {
		super(proizvodjac, model, MAX_BROJ_PUTNIKA, putnici);
		this.carinskaDokumentacija = Util.generateVjerovatnoca(VJEROVATNOCA_GENERISANJA_DOKUMENTACIJE);
		this.deklarisanaMasa = deklarisanaMasa;
		this.stvarnaMasa = stvarnaMasa;
	}
	
	@Override
	public String toString() {
		return "Kamion: " + super.toString();
//		return "Kamion: " + super.toString() + "\n ~ Carinska Dokumentacija: " + carinskaDokumentacija + "\n ~ Deklarisana masa: " + String.format("%06.2f", deklarisanaMasa) + "\n ~ Stvarna masa: " + String.format("%06.2f", stvarnaMasa);  
	}

	public boolean isCarinskaDokumentacija() {
		return carinskaDokumentacija;
	}

	public void setCarinskaDokumentacija(boolean carinskaDokumentacija) {
		this.carinskaDokumentacija = carinskaDokumentacija;
	}

	public double getDeklarisanaMasa() {
		return deklarisanaMasa;
	}

	public void setDeklarisanaMasa(double deklarisanaMasa) {
		this.deklarisanaMasa = deklarisanaMasa;
	}

	public double getStvarnaMasa() {
		return stvarnaMasa;
	}

	public void setStvarnaMasa(double stvarnaMasa) {
		this.stvarnaMasa = stvarnaMasa;
	}
	
	@Override
	public String voziloInfo() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < super.getPutnici().size(); i++) {
			sb.append(super.getPutnici().get(i).getIme() + " " + super.getPutnici().get(i).getPrezime()  + " (ID: " + super.getPutnici().get(i).getIdDokumenta() + ")" + "\n");
		}
		return "Kamion: " + super.voziloInfo() + "\n ~ Carinska Dokumentacija: " + carinskaDokumentacija + "\n ~ Deklarisana masa: " 
					+ String.format("%06.2f", deklarisanaMasa) + "\n ~ Stvarna masa: " 
				+ String.format("%06.2f", stvarnaMasa) + "\n" +  "Putnici:\n" + sb.toString();  
	}
}