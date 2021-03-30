package org.isa.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.isa.App;

import java.io.IOException;

public class PrimaryController {

    public Button btnExo1;
    public Button btnExo2;
    public Button btnExo4;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("exo1jdbc");
    }

    public void switchToExo2(ActionEvent actionEvent) throws IOException {
        App.setRoot("exo2");
    }

    public void switchToExo4(ActionEvent actionEvent) throws IOException {
        App.setRoot("exo4ajout");
    }
}
