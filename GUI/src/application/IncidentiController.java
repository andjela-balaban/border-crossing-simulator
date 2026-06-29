package application;

import static org.unibl.etf.simulacija.MyLogger.logger;

import java.net.URL;
import java.util.*;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.putnik.Putnik;
import org.unibl.etf.vozila.Autobus;
import org.unibl.etf.vozila.Kamion;
import org.unibl.etf.vozila.LicnoVozilo;
import org.unibl.etf.vozila.Vozilo;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;

public class IncidentiController implements Initializable {
	
	@FXML
	ListView<Vozilo> listView;
	@FXML
	TextArea infoVozilo;
	
	Map<Vozilo, List<String>> vozilaIncidenti = IzvrsavacSimulacije.simulacija.getIncidentiLog();
	Vozilo trenutnoVozilo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

		Thread tIncidenti = new Thread(() -> {
//			listView.getItems().addAll(putnici);
			Set<Vozilo> ispisana = new LinkedHashSet<>();
			
			while(true) {
				Set<Vozilo> razlika = new LinkedHashSet<>(vozilaIncidenti.keySet());
				razlika.removeAll(ispisana);
				Platform.runLater(() -> {
					listView.getItems().addAll(razlika);
					listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Object>() {
						
						@Override
						public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
							
							trenutnoVozilo = listView.getSelectionModel().getSelectedItem();
							
//							infoVozilo.setText(trenutnoVozilo.voziloInfo());
							StringBuilder sb = new StringBuilder();
							for(int i = 0; i < vozilaIncidenti.get(trenutnoVozilo).size(); i++) {
								sb.append(vozilaIncidenti.get(trenutnoVozilo).get(i) + " "  + "\n");
							}
							infoVozilo.setText(sb.toString());
						}

					});
				});
				try {
					Thread.sleep(50);
				}catch(InterruptedException e) {
					logger.log(Level.WARNING, "Exception: ", e);
				}
				
				ispisana.addAll(razlika);
				
				
			}
		});
		tIncidenti.setDaemon(true);
		tIncidenti.start();
		
	}
}
