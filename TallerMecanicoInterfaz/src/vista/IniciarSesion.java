/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controladores.AdministradorJpaController;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.Administrador;

/**
 *
 * @author dany
 */
public class IniciarSesion extends Stage{
    //private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoPU");
    private ImageView logo;
    private Label etiquetaLogo;
    private JFXTextField correo;
    private JFXPasswordField contrasena;
    private JFXButton iniciarSesion;
    private AnchorPane root;
    private StackPane rootPane;
    
    public IniciarSesion() {
        configurarPanel();
        setTitle("Taller Mecanico");
        show();
    }

    private void configurarPanel() {
        root = new AnchorPane();
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
        
        contrasena = new JFXPasswordField();
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
            if (validarCampos()){
                if(validarUsuario()){
                MostrarClientes clientes = new MostrarClientes();
                clientes.show();
                this.hide();
                } else {
                    mostrarAlerta("Correo o contraseña incorrectos", "Datos inválidos");
                }
            } else {
                mostrarAlerta("Hay campos vacios", "Campos vacíos");
            }
        });
    }
    
    private boolean validarCampos(){
        return correo.getText() != null && contrasena.getText() != null;
    }
    
    private boolean validarUsuario() {
        boolean valido = false;
        AdministradorJpaController controlador = new AdministradorJpaController();
        Administrador a = controlador.getAdmin(correo.getText(), contrasena.getText());
        if (a != null){
            valido = true;
        }
        return valido;
    }

    private void mostrarMensaje(String mensaje) {
        rootPane = new StackPane();
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton boton = new JFXButton("Okay");
        JFXDialog dialogo = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        boton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent ) -> {
            dialogo.close();
        });
        dialogLayout.setBody(new Label(mensaje));
        dialogLayout.setActions(boton);
        dialogo.show();
    }
    
    
    public void mostrarAlerta(String mensaje, String titulo){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(titulo));
        content.setBody(new Text(mensaje));
        content.setPrefSize(250, 100);
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.LEFT, true);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                        dialog.close();
                }
        });
        button.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
        button.setPrefHeight(32);
        button.getStyleClass().add("btnAlerta");
        //button.setStyle(dialogBtnStyle);
        content.setActions(button);
        root.getChildren().add(stackPane);
        AnchorPane.setTopAnchor(stackPane, (root.getHeight() - content.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(stackPane, (root.getWidth() - content.getPrefWidth()) / 2);
        dialog.show();  
    }
}

   
