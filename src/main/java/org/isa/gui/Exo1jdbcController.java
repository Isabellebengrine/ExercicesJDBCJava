package org.isa.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.isa.App;

import java.io.IOException;
import java.sql.*;

public class Exo1jdbcController {
    public Label lblsuppliercode;
    public TextField supplierCodeInput;
    public Button btnRechercher;
    public TextField suppliersName;
    public TextField suppliersAddress;
    public TextField suppliersZip;
    public TextField suppliersCity;
    public TextField suppliersContact;
    public Button btnMenu;

    public void search(ActionEvent actionEvent) {

        try
        {
            //établissement de la connexion au lien JDBC
            String url ="jdbc:mysql://localhost:3306/papyrus";
            Connection con= DriverManager.getConnection(url,"root","");

            //preparation de la requête :
            PreparedStatement stm = con.prepareStatement("SELECT * FROM fournis WHERE numfou=?");

            // affectation de valeurs aux paramètres de la requête
            String input = supplierCodeInput.getText();
            stm.setInt(1,Integer.parseInt(input));

            // exécution de la requête
            ResultSet result = stm.executeQuery();

            //message si fournisseur n'existe pas :
            if(result.getFetchSize() == 0){
                suppliersName.setStyle("-fx-text-fill: red");
                suppliersName.setText("Ce fournisseur n'existe pas!!!");
                suppliersContact.clear();
                suppliersCity.clear();
                suppliersZip.clear();
                suppliersAddress.clear();
            }

            // affichage du résultat :
            // next renvoie true lorsqu'il existe un enregistrement supplémentaire
            while(result.next())
            {
                //conversion du résultat dans le bon type
                int num = result.getInt("numfou");
                String nom = result.getString("nomfou");
                String rue = result.getString("ruefou");
                String zipcode = result.getString("posfou");
                String ville = result.getString("vilfou");
                String contact = result.getString("confou");

                //remettre affichage en couleur normale :
                suppliersName.setStyle("");

                //remplir les textfields avec les data :
                suppliersName.setText(nom);
                suppliersAddress.setText(rue);
                suppliersZip.setText(zipcode);
                suppliersCity.setText(ville);
                suppliersContact.setText(contact);

            }
            //il est recommandé de fermer les objets Statement et Resultset
            stm.close();
            result.close();
            //et de fermer la connexion
            con.close ();
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
