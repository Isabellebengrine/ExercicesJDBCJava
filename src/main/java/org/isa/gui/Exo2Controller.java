package org.isa.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import org.isa.App;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Exo2Controller  implements Initializable {
    public ComboBox<String> comboBoxSuppliers = new ComboBox<>();
    public TextArea txtCommandes;
    public Button btnMenu;

    ObservableList<String> fournisseurs = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //établissement de la connexion au lien JDBC
            String url ="jdbc:mysql://localhost:3306/papyrus";
            Connection con= DriverManager.getConnection(url,"root","");

            // requête :
            Statement stm = con.createStatement();

            // exécution de la requête
            ResultSet res = stm.executeQuery("SELECT nomfou FROM fournis");

            //entrer première option dans liste : "Tous"
            fournisseurs.add("Tous");

            //Ajouter les noms de fournisseurs 1 à 1
            while (res.next()) {
                String str = res.getString("nomfou");
                fournisseurs.add(str);
            }

            stm.close();
            res.close();
            con.close();

        } catch (SQLException e) {
            System.out.println ("Erreur : la connexion a échoué");
            e.printStackTrace();
        }

        comboBoxSuppliers.setItems(fournisseurs);
    }

    public void afficheCommandes(ActionEvent actionEvent) {
        String choisi = comboBoxSuppliers.getSelectionModel().getSelectedItem();

        try
        {
            //établissement de la connexion au lien JDBC
            String url ="jdbc:mysql://localhost:3306/papyrus";
            Connection con= DriverManager.getConnection(url,"root","");

            if(choisi.equals("Tous")){
                PreparedStatement stm = con.prepareStatement("SELECT * FROM entcom");

                // exécution de la requête
                ResultSet result = stm.executeQuery();

                //utilisation du rést final :
                while(result.next())
                {
                    //conversion du résultat dans le bon type
                    int numcom = result.getInt("numcom");
                    String obscom = result.getString("obscom");
                    Date datcom  = result.getDate("datcom");

                    //remplir les textfields avec les data :
                    txtCommandes.setText(txtCommandes.getText() + "Cde:"  + numcom + "- Date: " + datcom + "- Observations: " + obscom + "\n");

                }

                //il est recommandé de fermer tous objets Statement, connection et Resultset
                stm.close();
                result.close();
                con.close ();
            } else {
                //préparation de la sous-requête :
                PreparedStatement stm1 = con.prepareStatement("SELECT numfou FROM fournis WHERE nomfou = ?");

                // affectation de valeurs aux paramètres de la requête
                stm1.setString(1, choisi);

                // exécution de la requête
                ResultSet result1 = stm1.executeQuery();
                result1.next();//un seul résultat donc je me place au bon endroit (sinon erreur before start...)

                // utilisation du résultat intermédiaire pour récupérer numéro du fournisseur correspondant au choix de combobox ::
                int numfou = result1.getInt("numfou");

                //préparation de la requête finale:
                PreparedStatement stm2 = con.prepareStatement("SELECT * FROM entcom WHERE numfou = ?");
                //pb req imbriquée 08/12 - 23h - syntaxe vérifiée dans heidi ok : SELECT * FROM entcom WHERE numfou = (SELECT numfou FROM fournis WHERE nomfou = ?

                // affectation de valeurs aux paramètres de la requête
                stm2.setInt(1, numfou);

                // exécution de la requête
                ResultSet result2 = stm2.executeQuery();

                //utilisation du rést final :
                while(result2.next())
                {
                    //conversion du résultat dans le bon type
                    int numcom = result2.getInt("numcom");
                    String obscom = result2.getString("obscom");
                    Date datcom  = result2.getDate("datcom");

                    //remplir les textfields avec les data :
                    txtCommandes.setText(txtCommandes.getText() + "Cde:" + numcom + "- Date: " + datcom + "- Observations: " + obscom + "\n");

                }
                //il est recommandé de fermer tous objets Statement, connection et Resultset
                stm1.close();
                stm2.close();
                result1.close();
                result2.close();
                con.close ();
            }
        }
        catch(Exception e)
        {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }

    }


    public void goBack(ActionEvent actionEvent) throws IOException {
        App.setRoot("primary");
    }
}
