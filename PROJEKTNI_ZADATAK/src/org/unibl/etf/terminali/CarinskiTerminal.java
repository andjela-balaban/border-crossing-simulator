package org.unibl.etf.terminali;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.vozila.*;

import static org.unibl.etf.simulacija.MyLogger.logger;
import static org.unibl.etf.util.SimulacioniParametri.*;
import java.util.*;
import java.util.logging.Level;

public class CarinskiTerminal extends Terminal {
	
	public CarinskiTerminal(Map<Vozilo, List<String> > incidentiLog) {
		super(incidentiLog);
	}
	
	@Override
	public void obradiVozilo(Vozilo vozilo) {
		System.out.println(this + " obradjuje - " + vozilo);
		if(vozilo instanceof LicnoVozilo) {
			try {
				Thread.sleep(VRIJEME_ZADRZAVANJA_LICNOG_VOZILA_NA_CARINSKOM_TERMINALU);
			}catch(InterruptedException e) {
				logger.log(Level.WARNING, "Exception: ", e);
			}
			System.out.println(vozilo + " je preslo carinski terminal");
		}
		else if(vozilo instanceof Kamion) {
			Kamion k = (Kamion) vozilo;
			if(k.isCarinskaDokumentacija()) { //  potrebno generisati carinsku dokumentaciju  
				System.out.println("Generisanje carinske dokumentacije za " + k);
			}
			if(k.getDeklarisanaMasa() != k.getStvarnaMasa()) {
				System.out.println("Kamion " + k.getModel() + " ima prekoracenje mase !!! - ne moze dalje");
				zabraniPrelazakVozila(k, "Kamion " + k.getModel() + " ima prekoracenje mase.");
//				vozilo.zabraniPrelazakVozila();
			}
			System.out.println(vozilo + " je presao carinski terminal");
		}
		else {	// vozilo je autobus
			Autobus a = (Autobus) vozilo;
			Putnik p = null;
			for(int i = 0; i < a.getPutnici().size(); i++) {
				try {
					Thread.sleep((int)(VRIJEME_PROCESIRANJA_PUTNIKA_AUTOBUS * 1000));
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
				p = ((ArrayList<Putnik>)a.getPutnici()).get(i);
				if(p.isImaKofer()) {
					if(p.getKofer().imaNedozvoljeneStvaeri()) {
						if(a.getVozac() != p) {
							System.out.println("Putnik " + p + " ima nedozvoljene stvari u koferu i ne moze da nastavi dalje !");
							zabraniPrelazakPutnika(a, p, "Putnik " + p.getIme() + " " + p.getPrezime() + ", ID: " + p.getIdDokumenta() + " ima nedozvoljene stvari u koferu.");
							vozilo.getPutnici().remove(p);
						}
					}
				}
			}
			System.out.println(vozilo + " je preslo carinski terminal");
		}
	}
	
	@Override
	public String toString() {
		return "Carinski " + super.toString();				
	}
}
