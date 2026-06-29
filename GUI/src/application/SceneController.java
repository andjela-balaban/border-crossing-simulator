package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	public void switchToSceneSimulationScene(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneSimulacijaPrelaza.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		IzvrsavacSimulacije.pokreni();
		
		System.out.println("Scenes switched - scena simulacije");
	}
}
