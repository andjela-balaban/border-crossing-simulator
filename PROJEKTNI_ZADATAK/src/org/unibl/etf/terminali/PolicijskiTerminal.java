package org.unibl.etf.terminali;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.simulacija.Simulacija;
import org.unibl.etf.vozila.*;

import static org.unibl.etf.simulacija.MyLogger.logger;
import static org.unibl.etf.util.SimulacioniParametri.*;
import java.util.*;
import java.util.logging.Level;

public class PolicijskiTerminal extends Terminal{

	public PolicijskiTerminal(Map<Vozilo, List<String> > incidentiLog) {
		super(incidentiLog);
	}
	
	private boolean provjeriDokumentaciju(Putnik p) {
		if(p.getIdDokumenta().isVazeciDokument())	// ako je putniku vazeci dokument - true
			return true;
		else
			return false;
	}
	
	@Override
	public void obradiVozilo(Vozilo vozilo) {
		System.out.println(this + " obradjuje - " + vozilo);
		if(vozilo instanceof Kamion || vozilo instanceof LicnoVozilo) {
			obrada(vozilo,(int)(VRIJEME_PROCESIRANJA_PUTNIKA_LICNO_VOZILO_KAMION * 1000));
		}
		else {	// vozilo je autobus
			obrada(vozilo,(int)(VRIJEME_PROCESIRANJA_PUTNIKA_AUTOBUS * 1000));
		}
	}
	
	private void obrada(Vozilo v, int vrijemeObrade) {
		Putnik p = null;
		for(int i = 0; i < v.getPutnici().size(); i++) {
			try {
				Thread.sleep(vrijemeObrade);
			}catch(InterruptedException e) {
				logger.log(Level.WARNING, "Exception: ", e);
			}
			p = ((ArrayList<Putnik>)v.getPutnici()).get(i);
			if(!provjeriDokumentaciju(p)) {
				if(p == v.getVozac()) {
					System.out.println("Vozac " + p + " nema ispravne dokumente i samim tim ne moze da predje granicu !!! - VOZILO:\n" + v);
//					v.zabraniPrelazakVozila();
					zabraniPrelazakVozila(v, "Vozac " + p + " nema ispravne dokumente.");
				}
				
				Simulacija.getListaKaznjenihOsoba().add(p);
				zabraniPrelazakPutnika(v, p, "Putnik " + p.getIme() + " " + p.getPrezime() + ", ID: " + p.getIdDokumenta() + " nema ispravne dokumente.");
				v.getPutnici().remove(p);
			}
		}
	}
	
	@Override
	public String toString() {
		return "Policijski " + super.toString();				
	}
}
