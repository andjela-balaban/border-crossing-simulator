package application;

import static org.unibl.etf.simulacija.MyLogger.logger;

import java.net.URL;

import org.unibl.etf.vozila.Vozilo;

import java.util.*;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;

public class RedVozilaController implements Initializable {

	@FXML
	ListView<Vozilo> listView;
	@FXML
	TextArea infoVozilo;
	
	Queue<Vozilo> vozila = IzvrsavacSimulacije.simulacija.getQueueVozila();
	Vozilo trenutnoVozilo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Thread tRedVozila = new Thread(() -> {
			
			Platform.runLater(() -> {
				listView.getItems().addAll(vozila);
			});
			Queue<Vozilo> ispisana = new LinkedList<>();
			while(true) {
				Queue<Vozilo> razlika = new LinkedList<>(IzvrsavacSimulacije.simulacija.getQueueVozila());
				ispisana.removeAll(razlika);
				Platform.runLater(() -> {
					listView.getItems().removeAll(ispisana);
					listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Object>() {
						
						@Override
						public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
							
							trenutnoVozilo = listView.getSelectionModel().getSelectedItem();
							if(trenutnoVozilo != null)
								infoVozilo.setText(trenutnoVozilo.voziloInfo());
							
							
	//				lebalaInfo.setText(trenutnoVozilo.voziloInfo());
							
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
		tRedVozila.setDaemon(true);
		tRedVozila.start();
		
	}

}
