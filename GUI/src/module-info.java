module HelloFX {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires PROJEKTNI_ZADATAK;
	requires java.logging;
	
	opens application to javafx.graphics, javafx.fxml;
}