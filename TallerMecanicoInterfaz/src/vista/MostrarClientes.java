/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controladores.ClienteJpaController;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

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
    private AnchorPane panelEditar, panelCliente, panelBusqueda, panelConsultar;
    private JFXButton btnAgregar;
    private Label administrarCliente;
    
    public MostrarClientes(){
        configurarPanel(); 
    }

    private void configurarPanel() {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root,415,552); 
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
                        lbl.setGraphic(new ImageView(new Image(
                                getClass().getResourceAsStream("/resources/avatar_cliente.png"))));
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
        String nombre = tfNombre.getText();
        String correo =  tfCorreo.getText();
        String telefono =  tfTelefono.getText();
        String direccion = tfDireccion.getText();
        String errores = "";
        
        if(nombre.trim().length() == 0){
            errores += "* El nombre no debe estar vacio \n";
        }
        
        if(correo.trim().length() == 0){
            errores += "* El correo no debe estar vacio \n";
        }
        
        if(telefono.trim().length() < 10){
            errores += "* El telefono no debe estar vacio \n";
        }
        
        if(direccion.trim().length() == 0){
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
            this.setWidth(415);
        } else {
            Alert dialogo = new Alert(Alert.AlertType.ERROR);
            dialogo.setTitle("Error");
            dialogo.setHeaderText("Campos Vacios");
            dialogo.setContentText(errores);
            dialogo.show();
            valido = false;
        }
        
        return valido;
    }

    private void crearPanelConsultarCliente() {
        // Panel Consultar Cliente
        panelConsultar = new AnchorPane();
        panelConsultar.setLayoutX(415);
        panelConsultar.setLayoutY(0);
        panelConsultar.setPrefHeight(552);
        panelConsultar.setPrefWidth(340);
        panelConsultar.setVisible(false);
        
        // Nombre
        tfNombre = new JFXTextField();
        tfNombre.setPromptText("Nombre del cliente");
        tfNombre.setLabelFloat(true);
        tfNombre.setLayoutX(21);
        tfNombre.setLayoutY(170);
        tfNombre.setPrefHeight(30);
        tfNombre.setPrefWidth(300);
        tfNombre.getStyleClass().add("TextField");
        
        // Correo
        tfCorreo = new JFXTextField();
        tfCorreo.setPromptText("Correo electronico");
        tfCorreo.setLabelFloat(true);
        tfCorreo.setLayoutX(21);
        tfCorreo.setLayoutY(240);
        tfCorreo.setPrefHeight(30);
        tfCorreo.setPrefWidth(300);
        tfCorreo.getStyleClass().add("TextField");
        
        // Telefono
        tfTelefono = new JFXTextField();
        tfTelefono.setPromptText("Telefono");
        tfTelefono.setLabelFloat(true);
        tfTelefono.setLayoutX(21);
        tfTelefono.setLayoutY(310);
        tfTelefono.setPrefHeight(30);
        tfTelefono.setPrefWidth(300);
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
        tfDireccion.setLayoutX(21);
        tfDireccion.setLayoutY(390);
        tfDireccion.setPrefHeight(60);
        tfDireccion.setPrefWidth(300);
        tfDireccion.getStyleClass().add("TextField");
        
        crearPanelCliente("Cliente");
        
        JFXButton btnVerAutos = new JFXButton();
        btnVerAutos.setLayoutX(0);
        btnVerAutos.setLayoutY(505);
        btnVerAutos.setPrefHeight(50);
        btnVerAutos.setPrefWidth(340);
        btnVerAutos.setText("Ver autos del cliente");
        btnVerAutos.getStyleClass().add("botonVer");
        btnVerAutos.setOnAction(evt -> {
            MostrarAutomoviles autos = new MostrarAutomoviles();
            autos.show();
            this.hide();
        });
        
        panelConsultar.getChildren().addAll(panelCliente, tfNombre, tfCorreo, tfTelefono, tfDireccion, btnVerAutos);
    }

    private void crearPanelEditarCliente() {
        // Panel Editar Cliente
        panelEditar = new AnchorPane();
        panelEditar.setLayoutX(415);
        panelEditar.setLayoutY(0);
        panelEditar.setPrefHeight(552);
        panelEditar.setPrefWidth(340);
        panelEditar.setVisible(false);
        
        // Nombre
        tfNombre = new JFXTextField();
        tfNombre.setPromptText("Nombre del cliente");
        tfNombre.setLabelFloat(true);
        tfNombre.setLayoutX(21);
        tfNombre.setLayoutY(170);
        tfNombre.setPrefHeight(30);
        tfNombre.setPrefWidth(300);
        tfNombre.getStyleClass().add("TextField");
        
        // Correo
        tfCorreo = new JFXTextField();
        tfCorreo.setPromptText("Correo electronico");
        tfCorreo.setLabelFloat(true);
        tfCorreo.setLayoutX(21);
        tfCorreo.setLayoutY(240);
        tfCorreo.setPrefHeight(30);
        tfCorreo.setPrefWidth(300);
        tfCorreo.getStyleClass().add("TextField");
        
        // Telefono
        tfTelefono = new JFXTextField();
        tfTelefono.setPromptText("Telefono");
        tfTelefono.setLabelFloat(true);
        tfTelefono.setLayoutX(21);
        tfTelefono.setLayoutY(310);
        tfTelefono.setPrefHeight(30);
        tfTelefono.setPrefWidth(300);
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
        tfDireccion.setLayoutX(21);
        tfDireccion.setLayoutY(390);
        tfDireccion.setPrefHeight(60);
        tfDireccion.setPrefWidth(300);
        tfDireccion.getStyleClass().add("TextField");
        
        // Boton Aceptar
        JFXButton btnAceptar = new JFXButton();
        ImageView imageAceptar = new ImageView();
        imageAceptar.setImage(new Image("/resources/aceptar.png"));
        imageAceptar.setFitHeight(20);
        imageAceptar.setFitWidth(20);
        btnAceptar.setGraphic(imageAceptar);
        btnAceptar.setText("Aceptar");
        btnAceptar.setLayoutX(180);
        btnAceptar.setLayoutY(500);
        btnAceptar.setPrefWidth(120);
        btnAceptar.getStyleClass().add("aceptar");
        
        btnAceptar.setOnAction(evt -> {
            validarDatos();
        });
        
        // Boton Cancelar
        JFXButton btnCancelar = new JFXButton();
        ImageView imageCancelar = new ImageView();
        imageCancelar.setImage(new Image("/resources/cancelar.png"));
        imageCancelar.setFitHeight(20);
        imageCancelar.setFitWidth(20);
        btnCancelar.setGraphic(imageCancelar);
        btnCancelar.setText("Cancelar");
        btnCancelar.setLayoutX(40);
        btnCancelar.setLayoutY(500);
        btnCancelar.setPrefWidth(120);
        btnCancelar.getStyleClass().add("cancelar");
        
        btnCancelar.setOnAction(evt -> {
            panelEditar.setVisible(false);
            this.setWidth(415);
        });
        
        crearPanelCliente("Administrar Cliente");
        
        panelEditar.getChildren().addAll(panelCliente, tfNombre, tfCorreo, tfTelefono, tfDireccion, btnAceptar, btnCancelar);
    }

    private void crearPanelCliente(String accionAdministrar) {
        panelCliente = new AnchorPane();
        panelCliente.setLayoutX(0);
        panelCliente.setLayoutY(0);
        panelCliente.setPrefHeight(123);
        panelCliente.setPrefWidth(340);
        panelCliente.getStyleClass().add("panelCliente");
        
        administrarCliente = new Label();
        administrarCliente.setLayoutX(97);
        administrarCliente.setLayoutY(46);
        administrarCliente.getStyleClass().add("administrarCliente");
        administrarCliente.setText(accionAdministrar);
        
        ImageView imageCliente = new ImageView();
        imageCliente.setImage(new Image("/resources/avatar_cliente_blanco.png"));
        imageCliente.setLayoutX(26);
        imageCliente.setLayoutY(30);
        imageCliente.setFitHeight(63);
        imageCliente.setFitWidth(63);
        
        panelCliente.getChildren().addAll(administrarCliente, imageCliente);
    }

    private void crearBarraBusqueda() {
        // Panel de busqueda
        panelBusqueda = new AnchorPane();
        panelBusqueda.setPrefHeight(86);
        panelBusqueda.setPrefWidth(415);
        panelBusqueda.getStyleClass().add("panelBusqueda");
        panelBusqueda.setLayoutX(0);
        panelBusqueda.setLayoutY(0);
        
        // Panel de busqueda - Barra de busqueda
        AnchorPane barraBusqueda = new AnchorPane();
        barraBusqueda.setLayoutX(70);
        barraBusqueda.setLayoutY(26);
        barraBusqueda.setPrefHeight(37);
        barraBusqueda.setPrefWidth(322);
        barraBusqueda.getStyleClass().add("barraBusqueda");
        
        // TextField Buscar
        tfBuscar = new JFXTextField();
        tfBuscar.setPromptText("Buscar Cliente");
        tfBuscar.setLayoutX(34);
        tfBuscar.setLayoutY(3);
        tfBuscar.setPrefHeight(31);
        tfBuscar.setPrefWidth(252);
        tfBuscar.setFocusColor(Color.WHITE);
        tfBuscar.setUnFocusColor(Color.WHITE);
        tfBuscar.getStyleClass().add("TextField");
        
        // Imagen Lupa
        ImageView imageBuscar = new ImageView();
        imageBuscar.setImage(new Image("/resources/buscar.png"));
        imageBuscar.setLayoutX(5);
        imageBuscar.setLayoutY(4);
        imageBuscar.setFitHeight(28);
        imageBuscar.setFitWidth(25);
        
        // Boton Salir
        JFXButton btnSalir = new JFXButton();
        ImageView imageSalir = new ImageView();
        imageSalir.setImage(new Image("/resources/salir.png"));
        imageSalir.setFitHeight(44);
        imageSalir.setFitWidth(42);
        imageSalir.setLayoutX(20);
        imageSalir.setLayoutY(24);
        btnSalir.setGraphic(imageSalir);
        btnSalir.setLayoutX(5);
        btnSalir.setLayoutY(20);
        btnSalir.setOnAction(evt -> {
            IniciarSesion iniciarSesion = new IniciarSesion();
            iniciarSesion.show();
            this.close();
        });
        
        barraBusqueda.getChildren().addAll(tfBuscar, imageBuscar);
        panelBusqueda.getChildren().addAll(barraBusqueda, btnSalir);
    }

    private void crearBotonAgregarCliente() {
        btnAgregar = new JFXButton();
        btnAgregar.setText("+");
        btnAgregar.getStyleClass().add("agregar");
        btnAgregar.setPrefHeight(50);
        btnAgregar.setPrefWidth(50);
        btnAgregar.setLayoutX(345);
        btnAgregar.setLayoutY(477);
        
        btnAgregar.setOnAction(evt -> {
            this.setWidth(754);
            panelEditar.setVisible(true);
            administrarCliente.setText("Agregar Cliente");
        });
    }

    private void crearListaClientes() {
        clientList = new JFXListView();
        clientList.setLayoutX(0);
        clientList.setLayoutY(86);
        clientList.setPrefHeight(552);
        clientList.setPrefWidth(415);
        
        clientList.setOnMouseClicked((MouseEvent click) -> {
            if (click.getClickCount() == 2) {
                panelEditar.setVisible(true);
                Cliente c = clientes.get(clientList.getSelectionModel().getSelectedIndex());
                System.out.println(c.getNombre());
            } 
        });
   
        
        cargarClientes("");
    }
}
