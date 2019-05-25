/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 *
 * @author dany
 */
public class Drawer extends JFXDrawer{
    private AnchorPane drawer, panelSuperior;
    private Label nombreAdmin, ruta;
    private String nombreAdministrador, path;
    private JFXButton btnBuscarCliente, btnBuscarAutomovil, btnSalir;
    private AnchorPane root;
    
    public Drawer(){}
    
    public Drawer(String nombreAdministrador, String cliente, AnchorPane root){
        this.nombreAdministrador = nombreAdministrador;
        this.path = cliente;
        this.root = root;
        configurarDrawer();
    }
    
    public Drawer(String nombreAdministrador, String cliente, String auto){
        this.nombreAdministrador = nombreAdministrador;
        this.path = cliente + " -- " + auto;
        configurarDrawer();
    }

    public String getNombreAdministrador() {
        return nombreAdministrador;
    }

    public void setNombreAdministrador(String nombreAdministrador) {
        this.nombreAdministrador = nombreAdministrador;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private void configurarDrawer() {
        setPrefHeight(500);
        setPrefWidth(279);
        //drawer.getStyleClass().add(""); 
        
        panelSuperior = new AnchorPane();
        panelSuperior.setPrefHeight(147);
        panelSuperior.setPrefWidth(279);
        //panelSuperior.getStyleClass().add(""); 
        
        nombreAdmin = new Label();
        nombreAdmin.setText(nombreAdministrador);
        nombreAdmin.setPrefWidth(135);
        nombreAdmin.setLayoutX(120);
        nombreAdmin.setLayoutY(33);
        //nombreAdmin.getStyleClass().add(""); 
        
        ImageView imagenAdmin = new ImageView();
        imagenAdmin.setImage(new Image("/resources/Avatar_Blanco.png"));
        imagenAdmin.setFitHeight(96);
        imagenAdmin.setFitWidth(96);
        imagenAdmin.setLayoutX(12);
        imagenAdmin.setLayoutY(13);
        
        ruta = new Label();
        ruta.setText(path);
        ruta.setPrefWidth(190);
        ruta.setLayoutX(45);
        ruta.setLayoutY(127);
        //ruta.getStyleClass().add("");
        
        btnBuscarCliente = new JFXButton();
        btnBuscarCliente.setGraphic(new ImageView(new Image("/resources/Avatar_Drawer.png")));
        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.setPrefHeight(30);
        btnBuscarCliente.setPrefWidth(150);
        btnBuscarCliente.setLayoutX(11);
        btnBuscarCliente.setLayoutY(167);
        
        btnBuscarAutomovil = new JFXButton();
        btnBuscarAutomovil.setGraphic(new ImageView(new Image("/resources/Coche_Drawer.png")));
        btnBuscarAutomovil.setText("Buscar Automovil");
        btnBuscarAutomovil.setPrefHeight(25);
        btnBuscarAutomovil.setPrefWidth(177);
        btnBuscarAutomovil.setLayoutX(11);
        btnBuscarAutomovil.setLayoutY(211);
        
        btnSalir = new JFXButton();
        btnSalir.setGraphic(new ImageView(new Image("/resources/Salir.png")));
        btnSalir.setText("Cerrar Sesion");
        btnSalir.setPrefHeight(30);
        btnSalir.setPrefWidth(156);
        btnSalir.setLayoutX(11);
        btnSalir.setLayoutY(456);
        
        panelSuperior.getChildren().addAll(imagenAdmin, nombreAdmin, ruta);       
        getChildren().addAll(panelSuperior, btnBuscarCliente, btnBuscarAutomovil, btnSalir);
        
    }
            
    public void clickDrawer() {
        
    }
    
    public void fadeOutTransition() {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(300));
        transition.setNode(root);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();
    }
    
    public void fadeInTransition() {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(300));
        transition.setNode(root);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }
    
}
