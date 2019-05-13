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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author dany
 */
public class IniciarSesion extends Stage{
    //private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoPU");
    private ImageView logo;
    private Label etiquetaLogo;
    private JFXTextField correo, contrasena;
    private JFXButton iniciarSesion;
    
    
    public IniciarSesion() {
        configurarPanel();
        setTitle("Taller Mecanico");
        show();
    }

    private void configurarPanel() {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root,300,500); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        root.getStyleClass().add("background");
        setScene(scene);
        
        crearComponentes();

        root.getChildren().addAll(logo, etiquetaLogo, correo,contrasena, iniciarSesion);
    }

    private void crearComponentes() {
        logo = new ImageView();
        logo.setImage(new Image("/resources/car.png"));
        logo.setLayoutX(30);
        logo.setLayoutY(20);
        logo.setFitHeight(122);
        logo.setFitWidth(250);
        
        etiquetaLogo = new Label("Taller Mecanico");
        etiquetaLogo.getStyleClass().add("logo");
        etiquetaLogo.setLayoutX(20);
        etiquetaLogo.setLayoutY(137);
        
        correo = new JFXTextField();
        correo.setPromptText("Correo Electronico");
        correo.setLabelFloat(true);
        correo.setMinWidth(250);
        correo.getStyleClass().add("TextField");
        correo.setLayoutX(25);
        correo.setLayoutY(218);
        
        contrasena = new JFXTextField();
        contrasena.setPromptText("Contraseña");
        contrasena.setLabelFloat(true);
        contrasena.setMinWidth(250);
        contrasena.getStyleClass().add("TextField");
        contrasena.setLayoutX(25);
        contrasena.setLayoutY(308);
        
        iniciarSesion = new JFXButton();
        iniciarSesion.setText("Iniciar Sesion");
        iniciarSesion.setMinHeight(40);
        iniciarSesion.setMinWidth(200);
        iniciarSesion.setLayoutX(50);
        iniciarSesion.setLayoutY(399);
        iniciarSesion.getStyleClass().add("iniciarSesion");
        iniciarSesion.setOnAction(evt -> {
            // TODO: VALIDACION
//            if(validarUsuario()){
              MostrarClientes clientes = new MostrarClientes();
              clientes.show();
              this.hide();
//            }else {
//            
//            }
        });
    }
    
    private boolean validarUsuario() {
        boolean valido = true;
        return valido;
    }
    
}

   
