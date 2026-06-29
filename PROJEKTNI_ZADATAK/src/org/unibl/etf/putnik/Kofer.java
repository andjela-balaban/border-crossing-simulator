package org.unibl.etf.putnik;

import java.io.Serializable;

public class Kofer implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean nedozvoljneStvari;
	
	public Kofer() {
		super();
	}
	
	public Kofer(boolean nedozvoljenjeStvari) {
		super();
		this.nedozvoljneStvari = nedozvoljenjeStvari;
	}
	
	public boolean imaNedozvoljeneStvaeri() {
		return nedozvoljneStvari;
	}
	
	@Override
	public String toString() {
		return "Nedozvoljene stvari u koferu: " + ((nedozvoljneStvari == true) ? "DA" : "NE");
	}
}