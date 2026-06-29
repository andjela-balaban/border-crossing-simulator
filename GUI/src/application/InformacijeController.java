package application;

import static org.unibl.etf.simulacija.MyLogger.logger;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.unibl.etf.vozila.Vozilo;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class InformacijeController implements Initializable {

	@FXML
	ListView<Vozilo> listView;
	@FXML
	TextArea infoVozilo;
	
	static Vozilo trenutnoVozilo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Thread tRedVozila = new Thread(() -> {
			Platform.runLater(() -> {
				infoVozilo.setText(trenutnoVozilo.voziloInfo());
			});
			try {
				Thread.sleep(50);
			}catch(InterruptedException e) {
				logger.log(Level.WARNING, "Exception: ", e);
			}

			
		});
		tRedVozila.setDaemon(true);
		tRedVozila.start();
		
	}

}
