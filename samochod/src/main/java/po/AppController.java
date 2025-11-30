package po;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class AppController {

    public void handleButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JavaFX");
        alert.setHeaderText(null);
        alert.setContentText("Kliknięto przycisk!");
        alert.showAndWait();
    }
}

