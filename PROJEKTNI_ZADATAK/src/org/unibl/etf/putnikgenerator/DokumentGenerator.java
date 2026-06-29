package org.unibl.etf.putnikgenerator;

import static org.unibl.etf.util.SimulacioniParametri.*;

import java.util.*;

import org.unibl.etf.putnik.Dokument;

public class DokumentGenerator {
	
	private DokumentGenerator() {
		super();
	}
	
	public static ArrayList<Dokument> generateNDokumenata(int ukupanBrojDokumenata) {
		Random rand = new Random();
		
		ArrayList<Dokument> dokumenti = new ArrayList<>();
		int brojNeispavnihDokumenata = (int)(ukupanBrojDokumenata * ((double)VJEROVATNOCA_NEISPRAVNOG_DOKUMENTA)/100); 
		long id;
		for(int i = 0; i < brojNeispavnihDokumenata; i++) {
			id = rand.nextLong(1_000_000_000);
			dokumenti.add(new Dokument(String.valueOf(id), false));
		}
		
		for(int i = 0; i < ukupanBrojDokumenata - brojNeispavnihDokumenata; i++) {
			id = rand.nextLong(1_000_000_000);
			dokumenti.add(new Dokument(String.valueOf(id), true));
		}
		return dokumenti;
	}
	
}
