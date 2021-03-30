package org.isa.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.isa.App;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Exo4ajoutController {
    public TextField inputSuppliersName;
    public TextField inputSuppliersAddress;
    public TextField inputSuppliersZip;
    public TextField inputSuppliersCity;
    public TextField inputSuppliersContact;
    public Button btnAjout;
    public Button btnAnnuler;
    public Label pbnom;
    public Label pbAddress;
    public Label pbZip;
    public Label pbCity;
    public Label pbContact;
    public Button btnMenu;

    public void ajouterFournisseur(ActionEvent actionEvent) {

        try {

            String url = "jdbc:mysql://localhost:3306/papyrus";
            Connection con = DriverManager.getConnection(url, "root", "");

            //1e req pou trouver max des numfou :
            PreparedStatement stmnumfou = con.prepareStatement("SELECT MAX(numfou) FROM fournis");
            // exécution de la requête
            ResultSet maxnumfou = stmnumfou.executeQuery();

            //utilisation du rést pour fixer prochain numfou pour nouveau fournisseur :
            maxnumfou.next();
            int nvonumfou = (maxnumfou.getInt(1)) + 1;

            //requête préparée pour insertion d'un nouveau fournisseur :
            PreparedStatement stm = con.prepareStatement("INSERT INTO fournis (numfou, nomfou, ruefou, vilfou, posfou, confou) VALUES (?, ?, ?, ?, ?, ?)");

            //vérification des inputs avant envoi en bdd :

            // String to be scanned to find the pattern
            //we allow digits and usual letters and spaces on all inputs except zip which is only 5 digits:
            String inputNom = inputSuppliersName.getText();
            String inputAddress = inputSuppliersAddress.getText();
            String inputCity = inputSuppliersCity.getText();
            String inputContact = inputSuppliersContact.getText();
            String inputZip = inputSuppliersZip.getText();

            //3 regex différentes selon inputs (mots, phrase ou nombre à 5 chiffres) :
            String patternmot = "^[a-zA-Z]+$";
            String patternaddress = "^[a-zA-Z0-9\\s]+$";
            String nb = "^[0-9]{5}$";

            // Create a Pattern object for each pattern :
            Pattern rmot = Pattern.compile(patternmot);
            Pattern raddress = Pattern.compile(patternaddress);
            Pattern patternNb = Pattern.compile(nb);

            // Now create a matcher object for each input :
            Matcher mNom = rmot.matcher(inputNom);
            Matcher mAdd = raddress.matcher(inputAddress);
            Matcher mCi = rmot.matcher(inputCity);
            Matcher mCo = rmot.matcher(inputContact);
            Matcher mZip = patternNb.matcher(inputZip);

            //si tous inputs ok alors exécuter requête :

            if (mNom.find( ) && mAdd.find() && mCi.find() && mCo.find() && mZip.find()) {

                stm.setInt(1, nvonumfou);
                stm.setString(2, inputNom);
                stm.setString(3, inputAddress);
                stm.setString(4, inputCity);
                stm.setString(5, inputZip);
                stm.setString(6, inputContact);

                stm.execute();

                //affiche fenêtre de confirmation de succès:
                showAlert();

                stm.close();
                con.close();

            }else {
                //afficher alerte :
                showWarning();

                //il y a au moins une erreur, peut-être plusieurs, donc mettre en évidence le champ où se trouve l'erreur :
                //pb 09/12 - 15h: montre erreurs partout jusqu'à l'erreur même pour champs corrects:
                if (!mNom.find()) {
                    pbnom.setVisible(true);
                    inputSuppliersName.setStyle("-fx-border-color: red");
                }
                if (!mAdd.find()) {
                    pbAddress.setVisible(true);
                    inputSuppliersAddress.setStyle("-fx-border-color: red");
                }
                if (!mZip.find()) {
                    pbZip.setVisible(true);
                    inputSuppliersZip.setStyle("-fx-border-color: red");
                }
                if(!mCi.find()){
                    pbCity.setVisible(true);
                    inputSuppliersCity.setStyle("-fx-border-color: red");
                }
                if(!mCo.find()){
                    pbContact.setVisible(true);
                    inputSuppliersContact.setStyle("-fx-border-color: red");
                }

            }

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }

    }

    // Show a Information Alert if insert succeeded :
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Succès :");
        alert.setContentText("Le fournisseur a bien été ajouté!");
        alert.showAndWait();
    }

    // Show a Warning Alert if insert failed:
    private void showWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention!");
        alert.setHeaderText("Echec :");
        alert.setContentText("L'ajout de nouveau fournisseur a échoué!");
        alert.showAndWait();
    }

    public void annuler(ActionEvent actionEvent) {
        //je vide tous champs :
        inputSuppliersAddress.clear();
        inputSuppliersCity.clear();
        inputSuppliersContact.clear();
        inputSuppliersName.clear();
        inputSuppliersZip.clear();

        //je remets tous styles à 0:
        inputSuppliersAddress.setStyle("");
        inputSuppliersCity.setStyle("");
        inputSuppliersContact.setStyle("");
        inputSuppliersName.setStyle("");
        inputSuppliersZip.setStyle("");

        //je rends à nouveau invisible les labels "*" qui servent si erreur d'input:
        pbnom.setVisible(false);
        pbAddress.setVisible(false);
        pbZip.setVisible(false);
        pbCity.setVisible(false);
        pbContact.setVisible(false);
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        App.setRoot("primary");
    }
}
