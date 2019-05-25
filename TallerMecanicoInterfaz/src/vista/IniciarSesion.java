/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controladores.AdministradorJpaController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ImageView logo;
    private Label etiquetaLogo;
    private JFXTextField correo;
    private JFXPasswordField contrasena;
    private JFXButton iniciarSesion;
    private AnchorPane root;
    private StackPane rootPane;
    private String nombreAdmin;
    
    public IniciarSesion() {
        configurarPanel();
        setTitle("Taller Mecanico");
        show();
    }

    private void configurarPanel() {
        root = new AnchorPane();
        Scene scene = new Scene(root,333,500); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        root.getStyleClass().add("background");
        setScene(scene);
        
        crearComponentes();

        root.getChildren().addAll(logo, etiquetaLogo, correo,contrasena, iniciarSesion);
    }

    private void crearComponentes() {
        logo = new ImageView();
        logo.setImage(new Image("/resources/Logo.png"));
        logo.setLayoutX(42);
        logo.setLayoutY(52);
        logo.setFitHeight(103);
        logo.setFitWidth(250);
        
        etiquetaLogo = new Label("Taller Mecanico");
        etiquetaLogo.getStyleClass().add("logo");
        etiquetaLogo.setLayoutX(32);
        etiquetaLogo.setLayoutY(163);
        
        correo = new JFXTextField();
        correo.setPromptText("Correo Electronico");
        correo.setLabelFloat(true);
        correo.setMinWidth(250);
        correo.getStyleClass().add("TextField");
        correo.setLayoutX(42);
        correo.setLayoutY(248);
        
        contrasena = new JFXPasswordField();
        contrasena.setPromptText("Contraseña");
        contrasena.setLabelFloat(true);
        contrasena.setMinWidth(250);
        contrasena.getStyleClass().add("TextField");
        contrasena.setLayoutX(42);
        contrasena.setLayoutY(325);
        
        
        iniciarSesion = new JFXButton();
        iniciarSesion.setText("Iniciar Sesion");
        iniciarSesion.setMinHeight(40);
        iniciarSesion.setMinWidth(200);
        iniciarSesion.setLayoutX(67);
        iniciarSesion.setLayoutY(408);
        iniciarSesion.getStyleClass().add("iniciarSesion");
        iniciarSesion.setOnAction(evt -> {
            if (validarCampos()){
                if(validarUsuario()){
                MostrarClientes clientes = new MostrarClientes(nombreAdmin);
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
        return !correo.getText().isEmpty() &&  !contrasena.getText().isEmpty();
    }
    
    private boolean validarUsuario() {
        boolean valido = false;
        AdministradorJpaController controlador = new AdministradorJpaController();
        Administrador a = controlador.getAdmin(correo.getText(), contrasena.getText());
        if (a != null){
            nombreAdmin = a.getNombre();
            valido = true;
        }
        return valido;
    }
    
    public void mostrarAlerta(String mensaje, String titulo){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(titulo));
        content.getStyleClass().add("mensaje");
        content.setBody(new Text(mensaje));
        content.setPrefSize(250, 100);
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.LEFT, true);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        button.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
        button.setPrefHeight(32);
        button.getStyleClass().add("btnAlerta");
        content.setActions(button);
        root.getChildren().add(stackPane);
        AnchorPane.setTopAnchor(stackPane, (root.getHeight() - content.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(stackPane, (root.getWidth() - content.getPrefWidth()) / 2);
        dialog.show();  
    }
}

   
