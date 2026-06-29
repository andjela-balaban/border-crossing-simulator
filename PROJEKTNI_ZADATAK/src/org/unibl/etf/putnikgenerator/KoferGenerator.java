package org.unibl.etf.putnikgenerator;

import static org.unibl.etf.util.SimulacioniParametri.*;

import java.util.*;

import org.unibl.etf.putnik.Kofer;

public class KoferGenerator {
	
	private KoferGenerator() {
		super();
	}
	
	public static ArrayList<Kofer> generateNKofera(int ukupanBrojKofera) {	//generisanje N kofera
		ArrayList<Kofer> koferi = new ArrayList<>();
//		(vjerovatnoća da putnik ima kofer je 70%, pri čemu u 10% kofera su nedozvoljene stvari)
		int brojKoferaSaNedozvoljenimStvarima = (int)(ukupanBrojKofera * (((double)VJEROVATNOCA_NEDOZVOLJENIH_STVARI_U_KOFERU)/100)); 
		for(int i = 0; i < brojKoferaSaNedozvoljenimStvarima; i++) {
			koferi.add(new Kofer(true));
		}
		for(int i = 0; i < ukupanBrojKofera - brojKoferaSaNedozvoljenimStvarima; i++) {
			koferi.add(new Kofer(false));
		}
		return koferi;
	}
}
