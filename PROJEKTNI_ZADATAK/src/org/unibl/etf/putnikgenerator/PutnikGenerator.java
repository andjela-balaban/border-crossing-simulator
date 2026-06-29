package org.unibl.etf.putnikgenerator;

import static org.unibl.etf.util.SimulacioniParametri.VJEROVATNOCA_DA_PUTNIK_IMA_KOFER;

import java.util.*;

import org.unibl.etf.putnik.Dokument;
import org.unibl.etf.putnik.Kofer;
import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.util.Util;

public class PutnikGenerator {
	private static String[] ime = {"Marko", "Janko", "Petar", "Mirko", "Slavko", "Milan", "Goran", "Sanja", "Nikolina", "Sofija",
							"Nikola", "Marija", "Ana", "Martina"};
	private static String[] prezime = {"Markovic", "Popovic", "Bajic", "Savic", "Petrovic", "Milenkovic", "Saric", "Andric",
								"Novakovic", "Ilic"};
	
	public static Putnik generatePutnik() {	//generisanje jednog putnika
		int x = Util.generateRandomInt(0, ime.length - 1);
		int y = Util.generateRandomInt(0, prezime.length - 1);
		String imePutnika = ime[x];
		String prezimePutnika = prezime[y];
		Kofer kofer;
	
		boolean imaKofer = Util.generateVjerovatnoca(VJEROVATNOCA_DA_PUTNIK_IMA_KOFER);
		
		if(imaKofer == true)
			kofer = new Kofer();
		else
			kofer = null;
		
		Dokument id = new Dokument();
		
		Putnik putnik = new Putnik(imePutnika, prezimePutnika, id, kofer);
		return putnik;
	}
	
	public static ArrayList<Putnik> generateNPutnika(int brojPutnika) {	// generisanje n putnika
		ArrayList<Putnik> putnici = new ArrayList<>();
		for(int i = 0; i < brojPutnika; i++) {
			Putnik p = generatePutnik();
			putnici.add(p);
		}
		
		int imaKofer = 0;	// provjera koliko od tih putnika ima kofer
		for(Putnik p : putnici) {	
			if(p.isImaKofer() == true)
				imaKofer++;
		}
		
		ArrayList<Kofer> koferi = KoferGenerator.generateNKofera(imaKofer);	// generisanje kofera za putnike
		ArrayList<Dokument> dokumenti = DokumentGenerator.generateNDokumenata(brojPutnika);	// generisanje dokumenata za putnike
		int i = 0;
		for(Putnik p : putnici) {	
			if(p.isImaKofer() == true) {
				p.setKofer(koferi.get(i));
				i++;
			}	
		}

		
		for(int j = 0; j < brojPutnika; j++)
			putnici.get(j).setIdDokumenta(dokumenti.get(j));
 		
		return putnici;
	}
	
}

