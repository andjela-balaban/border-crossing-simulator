package org.unibl.etf.putnik;

import java.io.*;

public class Putnik extends Thread implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ime;
	private String prezime;
	private Dokument id;
	private boolean imaKofer;
	private Kofer kofer;

	public Putnik() {
		super();
	}
	
	public Putnik(String ime, String prezime, Dokument id, Kofer kofer) {
		this.ime = ime;
		this.prezime = prezime;
		this.id = id;
		this.kofer = kofer;
		if(kofer == null) 
			imaKofer = false;
		else
			imaKofer = true;
	}

	public boolean isImaKofer() {
		return imaKofer;
	}

	public void setImaKofer(boolean imaKofer) {
		this.imaKofer = imaKofer;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Dokument getIdDokumenta() {
		return id;
	}

	public void setIdDokumenta(Dokument id) {
		this.id = id;
	}
	
	public Kofer getKofer() {
		return kofer;
	}
	
	public void setKofer(Kofer kofer) {
		this.kofer = kofer;
	}
	
	@Override
	public String toString() {
		return "Putnik: " + ime + " " + prezime + "(" + id + ")" + (imaKofer == true ? kofer.toString() : "nema kofer");
	}

}

