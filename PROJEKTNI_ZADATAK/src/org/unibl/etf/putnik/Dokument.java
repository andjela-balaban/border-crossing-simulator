package org.unibl.etf.putnik;

import java.io.Serializable;

public class Dokument implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private boolean vazeciDokument;
	
	public Dokument() {
		super();
	}
	
	public Dokument(String id, boolean vazeciDokument) {
		super();
		this.id = id;
		this.vazeciDokument = vazeciDokument;
	}
	
	@Override
	public String toString() {
		return "Dokument: " + id + "(vazeci: " + (vazeciDokument ? "DA" : "NE") + ")";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isVazeciDokument() {
		return vazeciDokument;
	}

	public void setVazeciDokument(boolean vazeciDokument) {
		this.vazeciDokument = vazeciDokument;
	}
}
