/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controladores.ClienteJpaController;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import modelo.Cliente;

/**
 *
 * @author dany
 */
public class MostrarClientes extends Stage{
    private JFXListView<Label> clientList;
    private JFXTextField tfBuscar;
    private List<Cliente> clientes;
    
    private JFXTextField tfNombre, tfCorreo, tfTelefono;
    private JFXTextArea tfDireccion;
    
    private JFXTextField tfNombreConsultar, tfCorreoConsultar, tfTelefonoConsultar;
    private JFXTextArea tfDireccionConsultar;
    
    private AnchorPane root, panelEditar, panelCliente, panelBusqueda, panelConsultar;
    private JFXButton btnAgregar;
    private Label administrarCliente;
    
    private FadeTransition abrirConsultar;
    private boolean agregar = true;
    private String nombre, telefono, direccion, correo;
    private int idCliente;
    
    public MostrarClientes(){
        configurarPanel(); 
    }

    private void configurarPanel() {
        root = new AnchorPane();
        Scene scene = new Scene(root,333,500); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        setScene(scene);

        crearListaClientes();
        crearBarraBusqueda();
        crearPanelConsultarCliente();
        crearPanelEditarCliente();
        crearBotonAgregarCliente();
        
        root.getChildren().addAll(clientList, panelBusqueda, btnAgregar, panelEditar, panelConsultar);
    }
    
    private void cargarClientes(String nombreCliente) {
        clientList.getItems().clear();
        ClienteJpaController controlador = new ClienteJpaController();
        
        if(nombreCliente.equals("")){
            clientes = controlador.findClienteEntities();
            int i = 0;
            for (Cliente c : clientes) {
                    try {
                        Label lbl = new Label(c.getNombre());
                        //lbl.setGraphic(new ImageView(new Image(
                                //getClass().getResourceAsStream("/resources/Avatar_Negro.png"))));
                        lbl.getStyleClass().add("label");
                        clientList.getItems().add(lbl);
                    } catch (Exception ex) {
                        System.err.println("Error: " + ex);
                    }
                    i++;
                }
            System.out.println("Elementos recuperados: "+i);
        } else {
            //clientes = controlador.findCliente(nombreCliente);
        }
        
    }
    
    public void busquedaActiva(KeyEvent e) {
        cargarClientes(tfBuscar.getText());
    }

    private boolean datosInvalidos() {
        return tfNombre.getText().isEmpty() || tfCorreo.getText().isEmpty() || tfTelefono.getText().isEmpty()
                || tfDireccion.getText().isEmpty();
    }
    
    private boolean validarDatos() {
        boolean valido = true;
        String validarNombre = tfNombre.getText();
        String validarCorreo =  tfCorreo.getText();
        String validarTelefono =  tfTelefono.getText();
        String validarDireccion = tfDireccion.getText();
        String errores = "";
        
        if(validarNombre.trim().length() == 0){
            errores += "* El nombre no debe estar vacio \n";
        }
        
        if(validarCorreo.trim().length() == 0){
            errores += "* El correo no debe estar vacio \n";
        }
        
        if(validarTelefono.trim().length() < 10){
            errores += "* El telefono no debe estar vacio \n";
        }
        
        if(validarDireccion.trim().length() == 0){
            errores += "* La direccion no debe estar vacia \n";
        }
        
        if (errores.length() == 0){
//            if (nuevo){
//                alumno = new Alumno();
//            }
//            alumno.setNombre(nombre);
//            alumno.setMatricula(matricula);
//            alumno.setDireccion(direccion);

            panelEditar.setVisible(false);
            this.setWidth(333);
            this.centerOnScreen();
        } else {
            
            mostrarAlerta(errores, "Campos Vacios");
//            Alert dialogo = new Alert(Alert.AlertType.ERROR);
//            dialogo.setTitle("Error");
//            dialogo.setHeaderText("Campos Vacios");
//            dialogo.setContentText(errores);
//            dialogo.show();
            valido = false;
        }
        
        return valido;
    }

    private void crearPanelConsultarCliente() {
        // Panel Consultar Cliente
        panelConsultar = new AnchorPane();
        panelConsultar.setLayoutX(333);
        panelConsultar.setLayoutY(0);
        panelConsultar.setPrefHeight(552);
        panelConsultar.setPrefWidth(333);
        panelConsultar.setVisible(false);
        
        // Nombre
        tfNombreConsultar = new JFXTextField();
        tfNombreConsultar.setPromptText("Nombre del cliente");
        tfNombreConsultar.setLabelFloat(true);
        tfNombreConsultar.setLayoutX(40);
        tfNombreConsultar.setLayoutY(177);
        tfNombreConsultar.setPrefHeight(30);
        tfNombreConsultar.setPrefWidth(250);
        tfNombreConsultar.getStyleClass().add("TextField");
        
        // Correo
        tfCorreoConsultar = new JFXTextField();
        tfCorreoConsultar.setPromptText("Correo electronico");
        tfCorreoConsultar.setLabelFloat(true);
        tfCorreoConsultar.setLayoutX(40);
        tfCorreoConsultar.setLayoutY(238);
        tfCorreoConsultar.setPrefHeight(30);
        tfCorreoConsultar.setPrefWidth(250);
        tfCorreoConsultar.getStyleClass().add("TextField");
        
        // Telefono
        tfTelefonoConsultar = new JFXTextField();
        tfTelefonoConsultar.setPromptText("Telefono");
        tfTelefonoConsultar.setLabelFloat(true);
        tfTelefonoConsultar.setLayoutX(40);
        tfTelefonoConsultar.setLayoutY(298);
        tfTelefonoConsultar.setPrefHeight(30);
        tfTelefonoConsultar.setPrefWidth(250);
        tfTelefonoConsultar.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("El telefono debe tener mas de 0 digitos");
            }
        });
        tfTelefonoConsultar.getStyleClass().add("TextField");
        
        // Direccion
        tfDireccionConsultar = new JFXTextArea();
        tfDireccionConsultar.setPromptText("Direccion");
        tfDireccionConsultar.setLabelFloat(true);
        tfDireccionConsultar.setLayoutX(40);
        tfDireccionConsultar.setLayoutY(361);
        tfDireccionConsultar.setPrefHeight(60);
        tfDireccionConsultar.setPrefWidth(250);
        tfDireccionConsultar.getStyleClass().add("TextField");
        
        crearPanelCliente("Administrar \n Cliente");
        
        JFXButton btnVerAutos = new JFXButton();
        btnVerAutos.setLayoutX(0);
        btnVerAutos.setLayoutY(450);
        btnVerAutos.setPrefHeight(50);
        btnVerAutos.setPrefWidth(340);
        btnVerAutos.setText("Ver autos del cliente");
        btnVerAutos.getStyleClass().add("botonVer");
        btnVerAutos.setOnAction(evt -> {
            MostrarAutomoviles autos = new MostrarAutomoviles();
            autos.show();
            this.hide();
        });
        
        panelConsultar.getChildren().addAll(panelCliente, tfNombreConsultar, tfCorreoConsultar, tfTelefonoConsultar, tfDireccionConsultar, btnVerAutos);
    }

    private void crearPanelEditarCliente() {
        // Panel Editar Cliente
        panelEditar = new AnchorPane();
        panelEditar.setLayoutX(333);
        panelEditar.setLayoutY(0);
        panelEditar.setPrefHeight(552);
        panelEditar.setPrefWidth(333);
        panelEditar.setVisible(false);
        
        // Nombre
        tfNombre = new JFXTextField();
        tfNombre.setPromptText("Nombre del cliente");
        tfNombre.setLabelFloat(true);
        tfNombre.setLayoutX(40);
        tfNombre.setLayoutY(177);
        tfNombre.setPrefHeight(30);
        tfNombre.setPrefWidth(250);
        tfNombre.getStyleClass().add("TextField");
        
        // Correo
        tfCorreo = new JFXTextField();
        tfCorreo.setPromptText("Correo electronico");
        tfCorreo.setLabelFloat(true);
        tfCorreo.setLayoutX(40);
        tfCorreo.setLayoutY(238);
        tfCorreo.setPrefHeight(30);
        tfCorreo.setPrefWidth(250);
        tfCorreo.getStyleClass().add("TextField");
        
        // Telefono
        tfTelefono = new JFXTextField();
        tfTelefono.setPromptText("Telefono");
        tfTelefono.setLabelFloat(true);
        tfTelefono.setLayoutX(40);
        tfTelefono.setLayoutY(298);
        tfTelefono.setPrefHeight(30);
        tfTelefono.setPrefWidth(250);
        tfTelefono.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("El telefono debe tener mas de 0 digitos");
            }
        });
        tfTelefono.getStyleClass().add("TextField");
        
        // Direccion
        tfDireccion = new JFXTextArea();
        tfDireccion.setPromptText("Direccion");
        tfDireccion.setLabelFloat(true);
        tfDireccion.setLayoutX(40);
        tfDireccion.setLayoutY(361);
        tfDireccion.setPrefHeight(60);
        tfDireccion.setPrefWidth(250);
        tfDireccion.getStyleClass().add("TextField");
        
        // Boton Aceptar
        JFXButton btnAceptar = new JFXButton();
        ImageView imageAceptar = new ImageView();
        imageAceptar.setImage(new Image("/resources/Aceptar.png"));
        imageAceptar.setFitHeight(28);
        imageAceptar.setFitWidth(126);
        btnAceptar.setGraphic(imageAceptar);
        //btnAceptar.setText("Aceptar");
        btnAceptar.setLayoutX(174);
        btnAceptar.setLayoutY(457);
        btnAceptar.setPrefWidth(126);
        //btnAceptar.getStyleClass().add("aceptar");
        
        btnAceptar.setOnAction(evt -> {
            if (validarDatos()) {
                nombre = tfNombre.getText();
                correo = tfCorreo.getText();
                direccion = tfDireccion.getText();
                telefono = tfTelefono.getText();
                
                ClienteJpaController controlador = new ClienteJpaController();
                
                if (agregar == true){
                    try {
                        Cliente c = new Cliente (nombre, telefono, direccion, correo);
                        controlador.create(c);
                    } catch (Exception ex){
                        System.out.println(ex);
                    }
                } else {
                    try {
                        Cliente c = new Cliente (idCliente, nombre, telefono, direccion, correo);
                        controlador.edit(c);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        });
        
        // Boton Cancelar
        JFXButton btnCancelar = new JFXButton();
        ImageView imageCancelar = new ImageView();
        imageCancelar.setImage(new Image("/resources/Cancelar.png"));
        imageCancelar.setFitHeight(28);
        imageCancelar.setFitWidth(126);
        btnCancelar.setGraphic(imageCancelar);
        //btnCancelar.setText("Cancelar");
        btnCancelar.setLayoutX(34);
        btnCancelar.setLayoutY(457);
        btnCancelar.setPrefWidth(126);
        //btnCancelar.getStyleClass().add("cancelar");
        
        btnCancelar.setOnAction(evt -> {
            panelEditar.setVisible(false);
            this.setWidth(333);
            this.centerOnScreen();
        });
        
        crearPanelCliente("Administrar \n Cliente");
        
        panelEditar.getChildren().addAll(panelCliente, tfNombre, tfCorreo, tfTelefono, tfDireccion, btnAceptar, btnCancelar);
    }

    private void crearPanelCliente(String accionAdministrar) {
        panelCliente = new AnchorPane();
        panelCliente.setLayoutX(0);
        panelCliente.setLayoutY(0);
        panelCliente.setPrefHeight(143);
        panelCliente.setPrefWidth(333);
        panelCliente.getStyleClass().add("panelCliente");
        
        administrarCliente = new Label();
        administrarCliente.setLayoutX(140);
        administrarCliente.setLayoutY(38);
        administrarCliente.getStyleClass().add("administrarCliente");
        administrarCliente.setText(accionAdministrar);
        
        ImageView imageCliente = new ImageView();
        imageCliente.setImage(new Image("/resources/Avatar_Blanco.png"));
        imageCliente.setLayoutX(22);
        imageCliente.setLayoutY(24);
        imageCliente.setFitHeight(96);
        imageCliente.setFitWidth(96);
        
        panelCliente.getChildren().addAll(administrarCliente, imageCliente);
    }

    private void crearBarraBusqueda() {
        // Panel de busqueda
        panelBusqueda = new AnchorPane();
        panelBusqueda.setPrefHeight(87);
        panelBusqueda.setPrefWidth(333);
        panelBusqueda.getStyleClass().add("panelBusqueda");
        panelBusqueda.setLayoutX(0);
        panelBusqueda.setLayoutY(0);
        
        // Panel de busqueda - Barra de busqueda
        AnchorPane barraBusqueda = new AnchorPane();
        barraBusqueda.setLayoutX(58);
        barraBusqueda.setLayoutY(27);
        barraBusqueda.setPrefHeight(34);
        barraBusqueda.setPrefWidth(260);
        barraBusqueda.getStyleClass().add("barraBusqueda");
        
        // TextField Buscar
        tfBuscar = new JFXTextField();
        tfBuscar.setPromptText("Buscar Cliente");
        tfBuscar.setLayoutX(34);
        tfBuscar.setLayoutY(3);
        tfBuscar.setPrefHeight(21);
        tfBuscar.setPrefWidth(218);
        tfBuscar.setFocusColor(Color.WHITE);
        tfBuscar.setUnFocusColor(Color.WHITE);
        tfBuscar.getStyleClass().add("Buscar");
        
        // Imagen Lupa
        ImageView imageBuscar = new ImageView();
        imageBuscar.setImage(new Image("/resources/Buscar2.png"));
        imageBuscar.setLayoutX(6);
        imageBuscar.setLayoutY(8);
        imageBuscar.setFitHeight(22);
        imageBuscar.setFitWidth(22);
        
        // Boton Salir
        JFXButton btnSalir = new JFXButton();
        ImageView imageSalir = new ImageView();
        imageSalir.setImage(new Image("/resources/Menu.png"));
        imageSalir.setFitHeight(17);
        imageSalir.setFitWidth(26);
        //imageSalir.setLayoutX(20);
        //imageSalir.setLayoutY(35);
        btnSalir.setGraphic(imageSalir);
        btnSalir.setLayoutX(7);
        btnSalir.setLayoutY(31);
        btnSalir.setOnAction(evt -> {
            IniciarSesion iniciarSesion = new IniciarSesion();
            iniciarSesion.show();
            this.close();
        });
        
        barraBusqueda.getChildren().addAll(tfBuscar, imageBuscar);
        panelBusqueda.getChildren().addAll(barraBusqueda, btnSalir);
    }

    private void crearBotonAgregarCliente() {
        ImageView imageAgregar = new ImageView();
        imageAgregar.setImage(new Image("/resources/Agregar.png"));
        imageAgregar.setFitHeight(60);
        imageAgregar.setFitWidth(60);
        
        btnAgregar = new JFXButton();
        btnAgregar.setGraphic(imageAgregar);
        btnAgregar.getStyleClass().add("agregar");
        btnAgregar.setPrefHeight(60);
        btnAgregar.setPrefWidth(60);
        btnAgregar.setLayoutX(248);
        btnAgregar.setLayoutY(411);
        
        btnAgregar.setOnAction(evt -> {
            this.setWidth(666);
            this.centerOnScreen();
            panelEditar.setVisible(true);
            panelConsultar.setVisible(false);
            administrarCliente.setText("Agregar Cliente");
            //animacionAbrirConsultar();
            //abrirConsultar.play();
        });
    }

    private void crearListaClientes() {
        clientList = new JFXListView();
        clientList.setLayoutX(0);
        clientList.setLayoutY(86);
        clientList.setPrefHeight(552);
        clientList.setPrefWidth(333);
        
        clientList.setOnMouseClicked((MouseEvent click) -> {
            if (click.getClickCount() == 2) {
                Cliente c = clientes.get(clientList.getSelectionModel().getSelectedIndex());
                System.out.println(c.getNombre());
                this.setWidth(666);
                this.centerOnScreen();
                panelConsultar.setVisible(true);
                panelEditar.setVisible(false);
                
                // Recuperar cliente
                idCliente = c.getId();
                tfNombreConsultar.setText(c.getNombre());
                tfTelefonoConsultar.setText(c.getTelefono());
                tfDireccionConsultar.setText(c.getDireccion());
                tfCorreoConsultar.setText(c.getCorreo());
                
                crearPanelCliente(c.getNombre());
            } 
        });
   
        
        cargarClientes("");
    }
    
    private void animacionAbrirConsultar(){
        
    }
    
    private void animacionPane(String nombrePanel, String accion) {
        
        if (nombrePanel.equals("Consultar")){
            if (accion.equals("abrir")){
                
                
            } else if (accion.equals("cerrar")){
                TranslateTransition cerrarConsultar =new TranslateTransition(new Duration(350), panelConsultar);
                cerrarConsultar.setToX(-(panelConsultar.getWidth()));
                cerrarConsultar.play();
            }
        } else if (nombrePanel.equals("Editar")){
            if (accion.equals("abrir")){
                TranslateTransition abrirEditar = new TranslateTransition(new Duration(350), panelEditar);
                abrirEditar.setToX(0);
                abrirEditar.play();
            } else if (accion.equals("cerrar")){
                TranslateTransition cerrarEditar = new TranslateTransition(new Duration(350), panelEditar);
                cerrarEditar.setToX(-(panelConsultar.getWidth()));
                cerrarEditar.play();
            }
        }
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
        AnchorPane.setTopAnchor(stackPane, (500 - content.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(stackPane, (panelEditar.getWidth() + (panelEditar.getWidth() - content.getPrefWidth()) / 2));
        dialog.show();  
    }
    
}
