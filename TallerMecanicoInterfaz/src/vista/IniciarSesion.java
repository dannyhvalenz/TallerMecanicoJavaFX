/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author dany
 */
public class IniciarSesion extends Stage{
    //private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoPU");
    private ImageView logo;
    
    public IniciarSesion() {
        configurarPanel();
        setTitle("Taller Mecanico");
        show();
    }

    private void configurarPanel() {
        
        VBox raiz = new VBox();
        raiz.setSpacing(40);
        raiz.setPadding(new Insets(10));
        Scene scene = new Scene(raiz,300,500); 
        setScene(scene);
        
        // LOGO
        Label logo = new Label("Taller Mecanico");
        logo.setStyle("-fx-font-size: 20pt");
        logo.setPadding(new Insets (20));

        //logo.setImage(new Image("../resources/avatar_cliente.png"));

        JFXTextField usuario = new JFXTextField();
        usuario.setPromptText("Usuario");
        usuario.setLabelFloat(true);
        usuario.setPrefHeight(30);
        usuario.setPadding(new Insets (10));
        
        JFXTextField contrasena = new JFXTextField();
        contrasena.setPromptText("Contraseña");
        contrasena.setLabelFloat(true);
        contrasena.setPrefHeight(30);
        contrasena.setPadding(new Insets (10));
        
        JFXButton iniciarSesion = new JFXButton();
        iniciarSesion.setText("Iniciar Sesion");
        iniciarSesion.setMinHeight(30);
        iniciarSesion.setPadding(new Insets (10));
        iniciarSesion.setStyle("-fx-background-color: #1156CB; -fx-text-fill: white");
        iniciarSesion.setOnAction(evt -> {
            // TODO: VALIDACION DE USUARIO
//            if(){
              MostrarClientes clientes = new MostrarClientes();
              clientes.show();
              this.hide();
//            }else {
//            
//            }
        });
        
        raiz.getChildren().addAll(logo, usuario,contrasena, iniciarSesion);
        raiz.setAlignment(Pos.CENTER);
    }
}
