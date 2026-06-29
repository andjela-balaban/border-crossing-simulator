package application;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;

import static org.unibl.etf.simulacija.MyLogger.logger;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.logging.Level;

import org.unibl.etf.terminali.*;
import org.unibl.etf.util.Util;
import org.unibl.etf.vozila.*;

import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;


public class SimulationSceneController implements Initializable {

	{
		try {
			slikaPolicija = new Image(new FileInputStream(Util.buildPath("../HelloFx/src/application/police.png")));
			slikaCarina = new Image(new FileInputStream(Util.buildPath("../HelloFx/src/application/carinik.png")));
			slikaAuto = new Image(new FileInputStream(Util.buildPath("../HelloFx/src/application/car.png")));
			slikaBus = new Image(new FileInputStream(Util.buildPath("../HelloFx/src/application/bus.png")));
			slikaBlocked = new Image(new FileInputStream(Util.buildPath("../HelloFx/src/application/blocked2.png")));
		
			slikaKamion = new Image(new FileInputStream(Util.buildPath("../HelloFx/src/application/truck.png")));
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			logger.log(Level.WARNING, "Exception: ", e);
		}
	}
	
	@FXML
	public Label stopwatch;
	@FXML
	public ProgressBar progressBar;
	@FXML 
	public Button pauzaDugme;
	@FXML 
	public Button nastaviDugme;
	@FXML
	private Label brojVozilaKojaSuPreslaGranicu;
	private Image slikaPolicija;
	private Image slikaCarina;
	private Image slikaAuto;
	private Image slikaBus;
	private Image slikaKamion;
	private Image slikaBlocked;
	Stoperica stoperica;
	
	@FXML
	TextArea infoVozilo;
	
	
	//pozicije za kretanje vozila
	@FXML
	private ImageView p1;		// pozicija 1...
	@FXML
	private ImageView p2;
	@FXML
	private ImageView p3;
	@FXML
	private ImageView p4;
	@FXML
	private ImageView p5;
	
	// pozicije terminala
	@FXML
	private ImageView pt1;
	@FXML
	private ImageView pt2;
	@FXML
	private ImageView pt3;
	@FXML
	private ImageView ct1;
	@FXML
	private ImageView ct2;
	
	@FXML
	private ImageView blockedPt1;
	@FXML
	private ImageView blockedPt2;
	@FXML
	private ImageView blockedPt3;
	@FXML
	private ImageView blockedCt1;
	@FXML
	private ImageView blockedCt2;
	
	
	@FXML
	private void incidentiButtonClick(ActionEvent e)  {
		Parent root;
	    try {
	    	root = FXMLLoader.load(getClass().getResource("SceneIncidenti.fxml"));
	    	Stage stage = new Stage();
	    	stage.setTitle("Incidenti");
	    	stage.setScene(new Scene(root, 800, 400));
	    	stage.show();
	    	System.out.println("Scenes switched - scena incidenata");	    	
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	   
	}
	
	@FXML
	private void redVozilaButtonClick(ActionEvent e) {
		Parent root;
		try {
	    	root = FXMLLoader.load(getClass().getResource("SceneVozilaURedu.fxml"));
	    	Stage stage = new Stage();
	    	stage.setTitle("Red vozila");
	    	stage.setScene(new Scene(root, 800, 400));
	    	stage.show();
	   
	    	System.out.println("Scenes switched - scena vozila u redu");
		}catch(IOException exc) {
		logger.log(Level.WARNING, "Exception: ", exc);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<ImageView> pozicije = new ArrayList<>();
		pozicije.add(p1);
		pozicije.add(p2);
		pozicije.add(p3);
		pozicije.add(p4);
		pozicije.add(p5);
		Object mapa[][] = IzvrsavacSimulacije.simulacija.getMapa();
		
//		azuriranje staze
		Thread tPozicije = new Thread(() -> {
			while(true) {
				for(int i = 0; i < 5; i++) {
					if(mapa[1][i] instanceof LicnoVozilo) {
						int j = i;
						Platform.runLater(() -> {
							pozicije.get(j).setImage(slikaAuto);
						});
					}
					else if(mapa[1][i] instanceof Kamion) {
						int j = i;
						Platform.runLater(() -> {
							pozicije.get(j).setImage(slikaKamion);
						});
					}
					else if(mapa[1][i] instanceof Autobus) {
						int j = i;
						Platform.runLater(() -> {
							pozicije.get(j).setImage(slikaBus);
						});
					}
					else {
						int j = i;
						Platform.runLater(() -> {
							pozicije.get(j).setImage(null);
						});
					}
				}
				try {
					Thread.sleep(50);
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		});
		tPozicije.setDaemon(true);
		tPozicije.start();
		
		ArrayList<PolicijskiTerminal> ptKamioni = IzvrsavacSimulacije.simulacija.getPTKamioni();
		ArrayList<PolicijskiTerminal> ptVozila = IzvrsavacSimulacije.simulacija.getPTVozila();
		ArrayList<CarinskiTerminal> ctKamioni = IzvrsavacSimulacije.simulacija.getCTKamioni();
		ArrayList<CarinskiTerminal> ctVozila = IzvrsavacSimulacije.simulacija.getCTVozila();
		
//		azuriranje prikaza vozila na terminalu
		Thread tPTKamioni = new Thread(() -> {		// policijski terminal za kamione
			while(true) {
				for(int i = 0; i < ptKamioni.size(); i++) {
					if(!ptKamioni.get(i).daLiRadi()) {
						Platform.runLater(() -> {
							blockedPt3.setImage(slikaBlocked);
							pt3.setImage(null);
						});
					}
					else {
						Platform.runLater(() -> {
							blockedPt3.setImage(null);
						});
						if(ptKamioni.get(i).getTrenutnoVozilo() != null) {
							Platform.runLater(() -> {
								pt3.setImage(slikaKamion);
							});
						}
						else {
							if(ptKamioni.get(i).daLiJeSlobodno()) {
								Platform.runLater(() -> {
									pt3.setImage(null);
								});
							}
						}						
					}
				}
				try {
					Thread.sleep(50);
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		});
		tPTKamioni.setDaemon(true);
		tPTKamioni.start();
		
		Thread tCTKamioni = new Thread(() -> {		// carinski terminal za kamione
			while(true) {
				for(int i = 0; i < ctKamioni.size(); i++) {
					if(!ctKamioni.get(i).daLiRadi()) {
						Platform.runLater(() -> {
							blockedCt2.setImage(slikaBlocked);
							ct2.setImage(null);
						});
					}
					else {
						Platform.runLater(() -> {
							blockedCt2.setImage(null);
						});
						if(ptKamioni.get(i).getTrenutnoVozilo() != null) {
							Platform.runLater(() -> {
								ct2.setImage(slikaKamion);
							});
						}
						else {
							if(ctKamioni.get(i).daLiJeSlobodno()) {
								Platform.runLater(() -> {
									ct2.setImage(null);
								});
							}
						}
					}
				}
				try {
					Thread.sleep(50);
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		});
		tCTKamioni.setDaemon(true);
		tCTKamioni.start();
		
		
		List<ImageView> pozicijePTVozila = new ArrayList<>(); 
		pozicijePTVozila.add(pt1);
		pozicijePTVozila.add(pt2);
		List<ImageView> blockedPozicije = new ArrayList<>(); 
		blockedPozicije.add(blockedPt1);
		blockedPozicije.add(blockedPt2);
		
		Thread tPTVozila = new Thread(() -> {
			while(true) {
				for(int i = 0; i < ptVozila.size(); i++) {
					int j = i;
					if(!ptVozila.get(i).daLiRadi()) {
						Platform.runLater(() -> {
							blockedPozicije.get(j).setImage(slikaBlocked);
							pozicijePTVozila.get(j).setImage(null);
						});
					}
					else {
						Platform.runLater(() -> {
							blockedPozicije.get(j).setImage(null);
						});
						Vozilo v = ptVozila.get(i).getTrenutnoVozilo();
						if(v != null) {
							if(v instanceof LicnoVozilo) {
								Platform.runLater(() -> {
									pozicijePTVozila.get(j).setImage(slikaAuto);
								});
							}
							else {
								Platform.runLater(() -> {
									pozicijePTVozila.get(j).setImage(slikaBus);
								});
							}
						}
						else {
							if(ptVozila.get(i).daLiJeSlobodno()) {
								Platform.runLater(() -> {
									pozicijePTVozila.get(j).setImage(null);
								});
							}
						}
					}
				}
				try {
					Thread.sleep(50);
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		});
		tPTVozila.setDaemon(true);
		tPTVozila.start();
		
		
		Thread tCTVozila = new Thread(() -> {
			while(true) {
				for(int i = 0; i < ctVozila.size(); i++) {
					if(!ctVozila.get(i).daLiRadi()) {
						Platform.runLater(() -> {
							blockedCt1.setImage(slikaBlocked);
							ct1.setImage(null);
						});
					}
					else {
						Platform.runLater(() -> {
							blockedCt1.setImage(null);
						});
						Vozilo v = ctVozila.get(i).getTrenutnoVozilo();
						if(v != null) {
							if(v instanceof LicnoVozilo) {
								Platform.runLater(() -> {
									ct1.setImage(slikaAuto);
								});
							}
							else {
								Platform.runLater(() -> {
									ct1.setImage(slikaBus);
								});
							}
							
						}
						
						else {
							if(ctVozila.get(i).daLiJeSlobodno()) {
								Platform.runLater(() -> {
									ct1.setImage(null);
								});
							}
						}
					}
				}
				try {
					Thread.sleep(50);
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		});
		tCTVozila.setDaemon(true);
		tCTVozila.start();
		
		
		// azuriranje broja predjenih vozila
		Thread tPredjenaVozila = new Thread(() -> {
			int broj = 0;
			int brojPredjenih = 0;
			while(true) {
				brojPredjenih = IzvrsavacSimulacije.simulacija.getBrojPredjenihVozila();
				if(broj != brojPredjenih) {
					int b = brojPredjenih;
					Platform.runLater(() -> {
						brojVozilaKojaSuPreslaGranicu.setText(b + "");
					});
					broj = brojPredjenih;
				}
				try {
					Thread.sleep(50);	
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		});
		tPredjenaVozila.setDaemon(true);
		tPredjenaVozila.start();
		
		// azuriranje progessBar-a
		Thread tProgressBar = new Thread(() -> {
			Platform.runLater(() -> {
				progressBar.setStyle("-fx-accent: #66B2FF;");
			});
			int broj = 0;
			int brojPredjenih = 0;
			int brojNepredjenih = 0;
			int ukupno = IzvrsavacSimulacije.simulacija.getUkupanBrojVozila();
			while(true) {
				brojPredjenih = IzvrsavacSimulacije.simulacija.getBrojPredjenihVozila();
				brojNepredjenih =  IzvrsavacSimulacije.simulacija.getBrojNepredjenihVozila();
				if(broj != (brojPredjenih + brojNepredjenih)) {
					int b = (brojPredjenih + brojNepredjenih);
					Platform.runLater(() -> {
						progressBar.setProgress((double)b/ukupno);
					});
					broj = b;
				}
				try {
					Thread.sleep(50);	
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
			}
		});
		tProgressBar.setDaemon(true);
		tProgressBar.start();
		
		stoperica = new Stoperica(stopwatch);
		stoperica.start();
		
		Thread tZaustavljanjeStoperice = new Thread(() -> {
			synchronized(IzvrsavacSimulacije.simulacija.getKraj()) {
				while(IzvrsavacSimulacije.simulacija.getKraj().get() !=  IzvrsavacSimulacije.simulacija.getUkupanBrojVozila()) {
					try {
						IzvrsavacSimulacije.simulacija.getKraj().wait();
					}catch(InterruptedException e) {
						logger.log(Level.WARNING, "Exception: ", e);
					}
				}
			}
			
			stoperica.stopStopwatch();
			Platform.runLater(() -> {
				pauzaDugme.setDisable(true);
				nastaviDugme.setDisable(true);
			});
		});
		tZaustavljanjeStoperice.setDaemon(true);
		tZaustavljanjeStoperice.start();
	}
	
	
	@FXML
	public void nastaviButtonClicked(ActionEvent e) {	
		IzvrsavacSimulacije.simulacija.pokreniVozila();
		Platform.runLater(() -> {
			nastaviDugme.setDisable(true);
			pauzaDugme.setDisable(false);			
		});
		// nastavi stopericu
		stoperica.resumeStopwatch();
	}
	
	@FXML
	public void pauzirajButtonClicked(ActionEvent e) {	
		IzvrsavacSimulacije.simulacija.pauzirajVozila();
		Platform.runLater(() -> {
			nastaviDugme.setDisable(false);
			pauzaDugme.setDisable(true);			
		});
		// zaustavi stopericu
		stoperica.pauseStopwatch();
	}
	
	
	@FXML
	private void prikazPozicije1(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = (Vozilo) IzvrsavacSimulacije.simulacija.getMapa()[1][0];
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicije2(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = (Vozilo) IzvrsavacSimulacije.simulacija.getMapa()[1][1];
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicije3(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = (Vozilo) IzvrsavacSimulacije.simulacija.getMapa()[1][2];
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicije4(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = (Vozilo) IzvrsavacSimulacije.simulacija.getMapa()[1][3];
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicije5(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = (Vozilo) IzvrsavacSimulacije.simulacija.getMapa()[1][4];
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicijePT1(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = ((Terminal)IzvrsavacSimulacije.simulacija.getMapa()[0][5]).getTrenutnoVozilo();
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicijePT2(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = ((Terminal)IzvrsavacSimulacije.simulacija.getMapa()[1][5]).getTrenutnoVozilo();
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicijePT3(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = ((Terminal)IzvrsavacSimulacije.simulacija.getMapa()[2][5]).getTrenutnoVozilo();
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicijeCT1(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = ((Terminal)IzvrsavacSimulacije.simulacija.getMapa()[0][6]).getTrenutnoVozilo();
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	@FXML
	private void prikazPozicijeCT2(MouseEvent e)  {
		Parent root;
	    try {
	    	Vozilo v = ((Terminal)IzvrsavacSimulacije.simulacija.getMapa()[2][6]).getTrenutnoVozilo();
	    	if(v != null) {
	    		InformacijeController.trenutnoVozilo = v;
	    		root = FXMLLoader.load(getClass().getResource("InformacijeOIzabranomVozilu.fxml"));
	    		Stage stage = new Stage();
	    		stage.setTitle("Informacije o izabranom vozilu");
	    		stage.setScene(new Scene(root, 600, 400));
	    		stage.show();
	    		System.out.println("Scenes switched - scena izabrano vozilo");	    	
	    	}
	    }catch(IOException exc) {
			logger.log(Level.WARNING, "Exception: ", exc);
		}
	}
	
	
}
