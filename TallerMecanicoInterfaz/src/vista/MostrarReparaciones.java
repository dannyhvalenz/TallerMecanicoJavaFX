/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import controladores.ReparacionJpaController;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import modelo.Automovil;
import modelo.Cliente;
import modelo.Reparacion;

/**
 *
 * @author dany
 */
public class MostrarReparaciones extends Stage{
    private JFXListView<Label> repList;
    private JFXTextField tfBuscar;
    private List<Reparacion> reparaciones;
    private JFXHamburger btnDrawer;

    private JFXComboBox<String> cbTipo;    
    private JFXTextField tfKilometraje, tfCosto;
    private JFXTextArea tfDesFalla, tfDesMantenimiento;
    private JFXDatePicker dpFecha;
    private JFXTimePicker dpHora;
    
    private JFXTextField tfKilometrajeConsultar, tfCostoConsultar;
    private JFXTextArea tfDesFallaConsultar, tfDesMantenimientoConsultar;
    private JFXDatePicker dpFechaConsultar;
    private JFXTimePicker dpHoraConsultar;
    private JFXDrawer drawer;
    
    private AnchorPane root, panelEditar, panelReparacion, panelBusqueda, panelConsultar, panelReparacionConsultar;
    private JFXButton btnAgregar, btnEditar, btnEliminar;
    private Label administrarReparacion, administrarReparacionConsultar;
    
    private boolean editarReparacion = false;
    private Date fecha;
    private Date hora;
    private int costo, idReparacion;
    private String tipo, kilometraje, descripcionFalla, descripcionMantenimiento;
    private String nombreAdministrador;
    private Cliente cliente;
    private Automovil automovil;
    private Reparacion reparacion;

    public MostrarReparaciones(String nombreAdministrador, Cliente cliente, Automovil automovil){
        this.nombreAdministrador = nombreAdministrador;
        System.out.println(this.nombreAdministrador);
        this.cliente = cliente;
        this.automovil = automovil;
        System.out.println(automovil.toString());
        System.out.println(this.cliente.getId() + " --> " + this.automovil.getId());
        configurarPanel(); 
        buscarReparacion();
    }
    
    private void buscarReparacion(){
        tfBuscar.setOnKeyTyped(evt -> {
            cargarReparaciones(tfBuscar.getText());
        });
    }

    private void configurarPanel() {
        root = new AnchorPane();
        Scene scene = new Scene(root,333,500); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        setScene(scene);
        setResizable(false);
        
        crearListaReparaciones();
        crearBarraBusqueda();
        crearPanelConsultarReparacion();
        crearPanelEditarReparacion();
        crearBotonAgregarReparacion();
        crearDrawer();
        setAnimation();
        
        root.getChildren().addAll(repList, panelBusqueda, btnAgregar, panelEditar, panelConsultar,drawer);
    }
    
    private void cargarReparaciones(String tipoRepBuscar) {
        repList.getItems().clear();
        ReparacionJpaController controlador = new ReparacionJpaController();
        
        if(tipoRepBuscar.equals("")){
            reparaciones = controlador.getReparacionesAutomovil(automovil);
            int i = 0;
            for (Reparacion r : reparaciones) {
                    String fechaObtenida  = obtenerFecha(r.getFecha());
                    try {
                        Label lbl = new Label(r.getTipo() + " - " + fechaObtenida);
                        lbl.setGraphic(new ImageView(new Image(
                                getClass().getResourceAsStream("/resources/Rep_Negra.png"))));
                        lbl.getStyleClass().add("label");
                        repList.getItems().add(lbl);
                    } catch (Exception ex) {
                        System.err.println("Error: " + ex);
                    }
                    i++;
                }
            System.out.println(automovil.getId()+ " tiene " + i + " reparaciones");
        } else {
            reparaciones = controlador.findReparaciones(tipoRepBuscar, automovil);
            int i = 0;
            for (Reparacion r : reparaciones) {
                String fechaObtenida  = obtenerFecha(r.getFecha());
                    try {
                        Label lbl = new Label(r.getTipo() + " - " + fechaObtenida);
                        lbl.setGraphic(new ImageView(new Image(
                                getClass().getResourceAsStream("/resources/Rep_Negra.png"))));
                        lbl.getStyleClass().add("label");
                        repList.getItems().add(lbl);
                    } catch (Exception ex) {
                        System.err.println("Error: " + ex);
                    }
                    i++;
                }
            System.out.println("Reparaiones recuperadas: " + i);
        } 
        
    }
    
    private boolean validarDatos() {
        boolean valido = true;
        String validarTipo = cbTipo.getSelectionModel().getSelectedItem().toString();
        String validarKilometraje = tfKilometraje.getText();
        String validarCosto =  tfCosto.getText();
        String validarFecha = dpFecha.getValue().toString();
        String validarHora = dpHora.getValue().toString();
        String validarFalla =  tfDesFalla.getText();
        String validarMantenimiento = tfDesMantenimiento.getText();
        String errores = "";
        
        if(validarTipo.trim().length() == 0){
            errores += "* El tipo de reparacion no debe estar vacio \n";
        }
        
        if(validarKilometraje.trim().length() == 0){
            errores += "* El kilometraje no debe estar vacio \n";
        }
        
        if(validarCosto.trim().length() == 0){
            errores += "* El costo no debe estar vacio \n";
        }
        
        if(validarFalla.trim().length() == 0){
            errores += "* La descripcion de la falla no debe estar vacia \n";
        }
        
        if(validarMantenimiento.trim().length() == 0){
            errores += "* La descripcion del mantenimiento no debe estar vacia \n";
        }
        if(validarFecha.trim().length() == 0){
            errores += "* La fecha no debe estar vacia \n";
        }
        if(validarHora.trim().length() == 0){
            errores += "* La hora no debe estar vacia \n";
        }
        
        if (errores.length() != 0){
            mostrarAlerta(errores, "Campos Vacios");
            valido = false;
        }
        
        return valido;
    }

    private void crearPanelConsultarReparacion() {
        panelConsultar = new AnchorPane();
        panelConsultar.setLayoutX(333);
        panelConsultar.setLayoutY(0);
        panelConsultar.setPrefHeight(552);
        panelConsultar.setPrefWidth(455);
        panelConsultar.setVisible(false);
        
        tfKilometrajeConsultar = new JFXTextField();
        tfKilometrajeConsultar.setPromptText("Kilometraje");
        tfKilometrajeConsultar.setLabelFloat(true);
        tfKilometrajeConsultar.setLayoutX(40);
        tfKilometrajeConsultar.setLayoutY(228);
        tfKilometrajeConsultar.setPrefHeight(30);
        tfKilometrajeConsultar.setPrefWidth(250);
        tfKilometrajeConsultar.setEditable(false);
        tfKilometrajeConsultar.getStyleClass().add("TextField");

        tfCostoConsultar = new JFXTextField();
        tfCostoConsultar.setPromptText("Costo");
        tfCostoConsultar.setLabelFloat(true);
        tfCostoConsultar.setLayoutX(324);
        tfCostoConsultar.setLayoutY(323);
        tfCostoConsultar.setPrefHeight(30);
        tfCostoConsultar.setPrefWidth(110);
        tfCostoConsultar.setEditable(false);
        tfCostoConsultar.getStyleClass().add("TextField");
        
        tfDesFallaConsultar = new JFXTextArea();
        tfDesFallaConsultar.setPromptText("Descripcion de la Falla");
        tfDesFallaConsultar.setLabelFloat(true);
        tfDesFallaConsultar.setLayoutX(40);
        tfDesFallaConsultar.setLayoutY(288);
        tfDesFallaConsultar.setPrefHeight(60);
        tfDesFallaConsultar.setPrefWidth(250);
        tfDesFallaConsultar.setEditable(false);
        tfDesFallaConsultar.getStyleClass().add("TextField");
        
        tfDesMantenimientoConsultar = new JFXTextArea();
        tfDesMantenimientoConsultar.setPromptText("Descripcion de la Mantenimiento");
        tfDesMantenimientoConsultar.setLabelFloat(true);
        tfDesMantenimientoConsultar.setLayoutX(40);
        tfDesMantenimientoConsultar.setLayoutY(383);
        tfDesMantenimientoConsultar.setPrefHeight(60);
        tfDesMantenimientoConsultar.setPrefWidth(250);
        tfDesMantenimientoConsultar.setEditable(false);
        tfDesMantenimientoConsultar.getStyleClass().add("TextField");
        
        dpHoraConsultar = new JFXTimePicker();
        dpHoraConsultar.setPromptText("Hora");
        dpHoraConsultar.setLayoutX(324);
        dpHoraConsultar.setLayoutY(228);
        dpHoraConsultar.setPrefHeight(30);
        dpHoraConsultar.setPrefWidth(110);
        dpHoraConsultar.setEditable(false);
        //dpHoraConsultar.getStyleClass().add("TextField");
        
        dpFechaConsultar = new JFXDatePicker();
        dpFechaConsultar.setPromptText("Fecha");
        dpFechaConsultar.setLayoutX(324);
        dpFechaConsultar.setLayoutY(168);
        dpFechaConsultar.setPrefHeight(30);
        dpFechaConsultar.setPrefWidth(110);
        dpFechaConsultar.setEditable(false);
        //dpFechaConsultar.getStyleClass().add("TextField");
        
        crearPanelReparacionConsultar();
        
        panelConsultar.getChildren().addAll(panelReparacionConsultar, tfKilometrajeConsultar, tfCostoConsultar, tfDesFallaConsultar, tfDesMantenimientoConsultar, dpHoraConsultar, dpFechaConsultar);
    }

    private void crearPanelEditarReparacion() {
        // Panel Editar Cliente
        panelEditar = new AnchorPane();
        panelEditar.setLayoutX(333);
        panelEditar.setLayoutY(0);
        panelEditar.setPrefHeight(552);
        panelEditar.setPrefWidth(455);
        panelEditar.setVisible(false);
        
        ObservableList lista = FXCollections.observableArrayList("Mecanico", "Electrico", "Lubricacion", 
            "Hojalateria", "Llantas", "Vestiduras", "Mofles");
        
        cbTipo = new JFXComboBox();
        cbTipo.setItems(lista);
        cbTipo.getSelectionModel().selectFirst();
        cbTipo.setLabelFloat(true);
        cbTipo.setPromptText("Tipo de reparacion");
        cbTipo.setLabelFloat(true);
        cbTipo.setLayoutX(40);
        cbTipo.setLayoutY(165);
        cbTipo.setPrefHeight(30);
        cbTipo.setPrefWidth(250);
        cbTipo.getStyleClass().add("TextField");

        tfKilometraje = new JFXTextField();
        tfKilometraje.setPromptText("Kilometraje");
        tfKilometraje.setLabelFloat(true);
        tfKilometraje.setLayoutX(40);
        tfKilometraje.setLayoutY(228);
        tfKilometraje.setPrefHeight(30);
        tfKilometraje.setPrefWidth(250);
        tfKilometraje.getStyleClass().add("TextField");
        tfKilometraje.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("El kilometraje solo puede contener digitos");
            }
        });

        tfCosto = new JFXTextField();
        tfCosto.setPromptText("Costo");
        tfCosto.setLabelFloat(true);
        tfCosto.setLayoutX(324);
        tfCosto.setLayoutY(323);
        tfCosto.setPrefHeight(30);
        tfCosto.setPrefWidth(110);
        tfCosto.getStyleClass().add("TextField");
        tfCosto.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("El costo solo puede contener digitos");
            }
        });
        
        tfDesFalla = new JFXTextArea();
        tfDesFalla.setPromptText("Descripcion de la Falla");
        tfDesFalla.setLabelFloat(true);
        tfDesFalla.setLayoutX(40);
        tfDesFalla.setLayoutY(288);
        tfDesFalla.setPrefHeight(60);
        tfDesFalla.setPrefWidth(250);
        tfDesFalla.getStyleClass().add("TextField");
        
        tfDesMantenimiento = new JFXTextArea();
        tfDesMantenimiento.setPromptText("Descripcion de la Mantenimiento");
        tfDesMantenimiento.setLabelFloat(true);
        tfDesMantenimiento.setLayoutX(40);
        tfDesMantenimiento.setLayoutY(383);
        tfDesMantenimiento.setPrefHeight(60);
        tfDesMantenimiento.setPrefWidth(250);
        tfDesMantenimiento.getStyleClass().add("TextField");
        
        dpHora = new JFXTimePicker();
        dpHora.setPromptText("Hora");
        dpHora.setLayoutX(324);
        dpHora.setLayoutY(228);
        dpHora.setPrefHeight(30);
        dpHora.setPrefWidth(110);
        dpHora.getStyleClass().add("jfx-time-picker");
        
        dpFecha = new JFXDatePicker();
        dpFecha.setPromptText("Fecha");
        dpFecha.setLayoutX(324);
        dpFecha.setLayoutY(168);
        dpFecha.setPrefHeight(30);
        dpFecha.setPrefWidth(110);
        dpFecha.getStyleClass().add("jfx-date-picker");
        
        // Boton Aceptar
        JFXButton btnAceptar = new JFXButton();
        ImageView imageAceptar = new ImageView();
        imageAceptar.setImage(new Image("/resources/Aceptar.png"));
        imageAceptar.setFitHeight(28);
        imageAceptar.setFitWidth(126);
        btnAceptar.setGraphic(imageAceptar);
        btnAceptar.setLayoutX(311);
        btnAceptar.setLayoutY(457);
        btnAceptar.setPrefWidth(126);
        
        btnAceptar.setOnAction(evt -> {
            if (validarDatos()) {
                ReparacionJpaController controlador = new ReparacionJpaController();
                
                fecha = Date.valueOf(dpFecha.getValue());

                Calendar calendar = Calendar.getInstance();
                calendar.set(0, 0, 0, dpHora.getValue().getHour(), dpHora.getValue().getMinute(), dpHora.getValue().getSecond());
                
                java.util.Date d = calendar.getTime();
                java.sql.Date sd = new java.sql.Date(d.getTime());
                
                hora = sd;
                costo = Integer.parseInt(tfCosto.getText());
                tipo = cbTipo.getSelectionModel().getSelectedItem();
                kilometraje = tfKilometraje.getText();
                descripcionFalla = tfDesFalla.getText();
                descripcionMantenimiento = tfDesMantenimiento.getText();
                
                if (editarReparacion == false){
                    try {
                        idReparacion = controlador.getLastId() + 1;
                        reparacion = new Reparacion (idReparacion, fecha, hora, costo, tipo, kilometraje, descripcionFalla, descripcionMantenimiento, automovil);
                        controlador.crear(reparacion);
                        mostrarAlertaExito("Se ha registrado la reparacion en \n la base de datos", "Reparacion Registrada");
                        cargarReparaciones("");
                    } catch (Exception ex){
                        System.out.println(ex);
                    }
                } else {
                    try {
                        reparacion = new Reparacion (reparacion.getId(), fecha, hora, costo, tipo, kilometraje, descripcionFalla, descripcionMantenimiento, automovil);
                        controlador.actualizar(reparacion);
                        mostrarAlertaExito("Se ha actualizado la reparacion en \n la base de datos", "Reparacion Actualizada");
                        cargarReparaciones("");
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            limpiarCamposEditar();
            }
        });

        JFXButton btnCancelar = new JFXButton();
        ImageView imageCancelar = new ImageView();
        imageCancelar.setImage(new Image("/resources/Cancelar.png"));
        imageCancelar.setFitHeight(28);
        imageCancelar.setFitWidth(126);
        btnCancelar.setGraphic(imageCancelar);
        btnCancelar.setLayoutX(171);
        btnCancelar.setLayoutY(457);
        btnCancelar.setPrefWidth(126);
        
        btnCancelar.setOnAction(evt -> {
            if (editarReparacion == true){
                panelConsultar.setVisible(true);
                panelEditar.setVisible(false);
                this.centerOnScreen();
                
                administrarReparacionConsultar.setText(reparacion.getTipo());
                
                tfKilometrajeConsultar.setText(reparacion.getKilometraje());
                tfCostoConsultar.setText(Integer.toString(reparacion.getCosto()));
                tfDesFallaConsultar.setText(reparacion.getDescripcionFalla());
                tfDesMantenimientoConsultar.setText(reparacion.getDescripcionMantenimiento());

                //Fecha
                Calendar cal = Calendar.getInstance();
                cal.setTime(reparacion.getFecha());
                LocalDate date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                dpFechaConsultar.setValue(date);
                
                // Hora                
                Instant instant = Instant.ofEpochMilli(reparacion.getHora().getTime());
                LocalTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
                dpHoraConsultar.setValue(res);
            } else {
                panelEditar.setVisible(false);
                this.setWidth(333);
                this.centerOnScreen();
            }
            limpiarCamposEditar();
        });
        
        crearPanelReparacion();
        panelEditar.getChildren().addAll(panelReparacion, cbTipo, tfKilometraje, tfCosto, tfDesFalla, tfDesMantenimiento, dpHora, dpFecha, btnAceptar, btnCancelar);
    }

    private void crearPanelReparacionConsultar() {
        panelReparacionConsultar = new AnchorPane();
        panelReparacionConsultar.setLayoutX(0);
        panelReparacionConsultar.setLayoutY(0);
        panelReparacionConsultar.setPrefHeight(143);
        panelReparacionConsultar.setPrefWidth(455);
        panelReparacionConsultar.getStyleClass().add("panelCliente");
        
        administrarReparacionConsultar = new Label();
        administrarReparacionConsultar.setLayoutX(140);
        administrarReparacionConsultar.setLayoutY(38);
        administrarReparacionConsultar.getStyleClass().add("administrarCliente");
        administrarReparacionConsultar.setText(tipo);
        
        ImageView imageReparacion = new ImageView();
        imageReparacion.setImage(new Image("/resources/Rep_Blanca.png"));
        imageReparacion.setLayoutX(22);
        imageReparacion.setLayoutY(24);
        imageReparacion.setFitHeight(96);
        imageReparacion.setFitWidth(96);
        
        ImageView imageEditar = new ImageView();
        imageEditar.setImage(new Image("/resources/Editar.png"));
        imageEditar.setFitHeight(20);
        imageEditar.setFitWidth(20);
        
        btnEditar = new JFXButton();
        btnEditar.setGraphic(imageEditar);
        btnEditar.setLayoutX(249);
        btnEditar.setLayoutY(120);
        btnEditar.setPrefHeight(30);
        btnEditar.setPrefWidth(30);
        btnEditar.setOnAction(evt -> {
            panelEditar.setVisible(true);
            panelConsultar.setVisible(false);
            
            cbTipo.getSelectionModel().select(administrarReparacionConsultar.getText());
            tfKilometraje.setText(reparacion.getKilometraje());
            tfCosto.setText(Integer.toString(reparacion.getCosto()));
            tfDesFalla.setText(reparacion.getDescripcionFalla());
            tfDesMantenimiento.setText(reparacion.getDescripcionMantenimiento());
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(reparacion.getFecha());
            LocalDate date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
            dpFecha.setValue(date);

            Instant instant = Instant.ofEpochMilli(reparacion.getHora().getTime());
            LocalTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
            dpHora.setValue(res);
           
            editarReparacion = true;
        });
        
        ImageView imageEliminar = new ImageView();
        imageEliminar.setImage(new Image("/resources/Eliminar.png"));
        imageEliminar.setFitHeight(20);
        imageEliminar.setFitWidth(15);
        
        btnEliminar = new JFXButton();
        btnEliminar.setGraphic(imageEliminar);
        btnEliminar.setLayoutX(290);
        btnEliminar.setLayoutY(120);
        btnEliminar.setPrefHeight(30);
        btnEliminar.setPrefWidth(25);
        btnEliminar.setOnAction(evt -> {
            mostrarAlertaEliminar();
        });
        
        panelReparacionConsultar.getChildren().addAll(administrarReparacionConsultar, imageReparacion, btnEditar, btnEliminar);   
    }
    
    private void crearPanelReparacion() {
        panelReparacion = new AnchorPane();
        panelReparacion.setLayoutX(0);
        panelReparacion.setLayoutY(0);
        panelReparacion.setPrefHeight(143);
        panelReparacion.setPrefWidth(455);
        panelReparacion.getStyleClass().add("panelCliente");
        
        administrarReparacion = new Label();
        administrarReparacion.setLayoutX(140);
        administrarReparacion.setLayoutY(38);
        administrarReparacion.getStyleClass().add("administrarCliente");
        administrarReparacion.setText("Administrar \n Reparacion");
        
        ImageView imageReparacion = new ImageView();
        imageReparacion.setImage(new Image("/resources/Rep_Blanca.png"));
        imageReparacion.setLayoutX(22);
        imageReparacion.setLayoutY(24);
        imageReparacion.setFitHeight(96);
        imageReparacion.setFitWidth(96);
        
        panelReparacion.getChildren().addAll(administrarReparacion, imageReparacion);
    }

    private void crearBarraBusqueda() {
        panelBusqueda = new AnchorPane();
        panelBusqueda.setPrefHeight(87);
        panelBusqueda.setPrefWidth(333);
        panelBusqueda.getStyleClass().add("panelBusqueda");
        panelBusqueda.setLayoutX(0);
        panelBusqueda.setLayoutY(0);
        
        AnchorPane barraBusqueda = new AnchorPane();
        barraBusqueda.setLayoutX(58);
        barraBusqueda.setLayoutY(27);
        barraBusqueda.setPrefHeight(34);
        barraBusqueda.setPrefWidth(260);
        barraBusqueda.getStyleClass().add("barraBusqueda");
        
        tfBuscar = new JFXTextField();
        tfBuscar.setPromptText("Buscar Reparacion");
        tfBuscar.setLayoutX(34);
        tfBuscar.setLayoutY(3);
        tfBuscar.setPrefHeight(21);
        tfBuscar.setPrefWidth(218);
        tfBuscar.setFocusColor(Color.WHITE);
        tfBuscar.setUnFocusColor(Color.WHITE);
        tfBuscar.getStyleClass().add("Buscar");
        
        ImageView imageBuscar = new ImageView();
        imageBuscar.setImage(new Image("/resources/Buscar2.png"));
        imageBuscar.setLayoutX(6);
        imageBuscar.setLayoutY(8);
        imageBuscar.setFitHeight(22);
        imageBuscar.setFitWidth(22);
        
        btnDrawer = new JFXHamburger();
        btnDrawer.getStyleClass().add("jfx-hamburger");
        btnDrawer.setLayoutX(15);
        btnDrawer.setLayoutY(35);
        btnDrawer.setPrefSize(26, 17);
        
        barraBusqueda.getChildren().addAll(tfBuscar, imageBuscar);
        panelBusqueda.getChildren().addAll(barraBusqueda, btnDrawer);
    }

    private void crearBotonAgregarReparacion() {
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
            this.setWidth(787);
            this.centerOnScreen();
            panelEditar.setVisible(true);
            panelConsultar.setVisible(false);
            administrarReparacion.setText("Agregar \n Reparacion");
            cbTipo.getSelectionModel().selectFirst();
            editarReparacion = false;
        });
    }

    private void crearListaReparaciones() {
        repList = new JFXListView();
        repList.setLayoutX(0);
        repList.setLayoutY(86);
        repList.setPrefHeight(414);
        repList.setPrefWidth(333);
        
        repList.setOnMouseClicked((MouseEvent click) -> {
           
            if (click.getClickCount() == 2) {
                Reparacion r = reparaciones.get(repList.getSelectionModel().getSelectedIndex());
                this.setWidth(787);
                this.centerOnScreen();
                panelConsultar.setVisible(true);
                panelEditar.setVisible(false);
                
                reparacion = r;
                
                reparacion.setId(r.getId());
                System.out.println("Id de la reparacion: " + reparacion.getId());
                reparacion.setTipo(r.getTipo());
                reparacion.setKilometraje(r.getKilometraje());
                reparacion.setDescripcionFalla(r.getDescripcionFalla());
                reparacion.setDescripcionMantenimiento(r.getDescripcionMantenimiento());
                reparacion.setIdAutomovil(r.getIdAutomovil());
                
                administrarReparacionConsultar.setText(reparacion.getTipo());
                tfCostoConsultar.setText(Integer.toString(reparacion.getCosto()));
                tfKilometrajeConsultar.setText(reparacion.getKilometraje());
                tfDesFallaConsultar.setText(reparacion.getDescripcionFalla());
                tfDesMantenimientoConsultar.setText(reparacion.getDescripcionMantenimiento());
                
                reparacion.setFecha(r.getFecha());
                reparacion.setHora(r.getHora());
                
                //Fecha
                Calendar cal = Calendar.getInstance();
                cal.setTime(reparacion.getFecha());
                LocalDate date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                dpFechaConsultar.setValue(date);

                // Hora
                Instant instant = Instant.ofEpochMilli(reparacion.getHora().getTime());
                LocalTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
                dpHoraConsultar.setValue(res);
            } 
        });
        
        cargarReparaciones("");
    }
    
    public void crearDrawer(){
        drawer = new JFXDrawer();
        drawer.setPrefHeight(500);
        drawer.setPrefWidth(279);
        drawer.setEffect(new DropShadow());
        
        AnchorPane panelDrawer = new AnchorPane();
        panelDrawer.setPrefHeight(500);
        panelDrawer.setPrefWidth(279);
        panelDrawer.setLayoutX(147);
        panelDrawer.setLayoutY(279);
        panelDrawer.getStyleClass().add("background");
        
        AnchorPane panelSuperior = new AnchorPane();
        panelSuperior.setPrefHeight(147);
        panelSuperior.setPrefWidth(279);
        panelSuperior.setPrefHeight(147);
        panelSuperior.setPrefWidth(279);
        panelSuperior.getStyleClass().add("panelSuperior");
        
        Label nombreAdmin = new Label();
        nombreAdmin.setText(nombreAdministrador);
        nombreAdmin.setPrefWidth(135);
        nombreAdmin.setLayoutX(120);
        nombreAdmin.setLayoutY(33);
        nombreAdmin.getStyleClass().add("administrarCliente");
        
        ImageView imagenAdmin = new ImageView();
        imagenAdmin.setImage(new Image("/resources/Avatar_Blanco.png"));
        imagenAdmin.setFitHeight(96);
        imagenAdmin.setFitWidth(96);
        imagenAdmin.setLayoutX(12);
        imagenAdmin.setLayoutY(13);
        
        Label ruta = new Label();
        ruta.setText(cliente.getNombre() + " --> " + automovil.getId());
        ruta.setPrefWidth(250);
        ruta.setLayoutX(10);
        ruta.setLayoutY(127);
        ruta.setStyle("-fx-font-size:9pt; -fx-font-family: 'Fira Code', monospace; -fx-text-fill:#ffffff;");
        //ruta.getStyleClass().add("ruta");
        
        JFXButton btnBuscarCliente = new JFXButton();
        btnBuscarCliente.setGraphic(new ImageView(new Image("/resources/Avatar_Drawer.png")));
        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.setPrefHeight(30);
        btnBuscarCliente.setPrefWidth(150);
        btnBuscarCliente.setLayoutX(11);
        btnBuscarCliente.setLayoutY(167);
        btnBuscarCliente.getStyleClass().add("botonesDrawer");
        btnBuscarCliente.setOnAction(evt -> {
            MostrarClientes ventanaClientes = new MostrarClientes(nombreAdministrador);
            ventanaClientes.show();
            this.hide();
        });
        
        JFXButton btnBuscarAutomovil = new JFXButton();
        btnBuscarAutomovil.setGraphic(new ImageView(new Image("/resources/Coche_Drawer.png")));
        btnBuscarAutomovil.setText("Buscar Automovil");
        btnBuscarAutomovil.setPrefHeight(25);
        btnBuscarAutomovil.setPrefWidth(177);
        btnBuscarAutomovil.setLayoutX(11);
        btnBuscarAutomovil.setLayoutY(211);
        btnBuscarAutomovil.getStyleClass().add("botonesDrawer");
        btnBuscarAutomovil.setOnAction(evt -> {
            MostrarAutomoviles ventanaAutomoviles = new MostrarAutomoviles(nombreAdministrador, cliente);
            ventanaAutomoviles.show();
            this.hide();
        });
        
        JFXButton btnSalir = new JFXButton();
        btnSalir.setGraphic(new ImageView(new Image("/resources/Salir.png")));
        btnSalir.setText("Cerrar Sesion");
        btnSalir.setPrefHeight(30);
        btnSalir.setPrefWidth(156);
        btnSalir.setLayoutX(11);
        btnSalir.setLayoutY(456);
        btnSalir.getStyleClass().add("botonesDrawer");
        btnSalir.setOnAction(evt -> {
            IniciarSesion inicio = new IniciarSesion();
            inicio.show();
            this.hide();
        });
        
        panelSuperior.getChildren().addAll(nombreAdmin, imagenAdmin, ruta);
        panelDrawer.getChildren().addAll(panelSuperior, btnBuscarCliente, btnBuscarAutomovil, btnSalir);
        drawer.getChildren().add(panelDrawer);
    }
    
    public void mostrarAlerta(String mensaje, String titulo){
        JFXDialogLayout content = new JFXDialogLayout();
        ImageView error = new ImageView();
        error.setImage(new Image("/resources/Error.png"));
        error.setFitHeight(30);
        error.setFitWidth(30);
        
        Label header = new Label();
        header.setText("  " + titulo);
        header.setGraphic(error);
        content.setHeading(header);
        content.getStyleClass().add("mensaje");
        content.setBody(new Text(mensaje));
        content.setPrefSize(300, 100);
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
        AnchorPane.setLeftAnchor(stackPane, 410.50);
        dialog.show();  
    }
    
    public void mostrarAlertaEliminar(){
        JFXDialogLayout content = new JFXDialogLayout();
        
        ImageView error = new ImageView();
        error.setImage(new Image("/resources/Error.png"));
        error.setFitHeight(30);
        error.setFitWidth(30);
        
        Label header = new Label();
        header.setText("  Eliminar Reparacion");
        header.setGraphic(error);
        content.setHeading(header);
        
        content.getStyleClass().add("mensaje");
        content.setBody(new Text("¿Desea eliminar esta reparacion?"));
        content.setPrefSize(300, 100);
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.LEFT, true);
        JFXButton btnSi = new JFXButton("Si");
        btnSi.setOnAction((ActionEvent event) -> {
            
            dialog.close();
            ReparacionJpaController controlador = new ReparacionJpaController();
            try {
                System.out.println("idReparacion " + reparacion.getId());
                controlador.destroy(reparacion.getId());
                mostrarAlertaExito("Se ha eliminado la reparacion de \n la base de datos", "Reparacion Eliminada");
                cargarReparaciones("");
            } catch (Exception ex) {
                mostrarAlerta("Error de conexión con la base de datos", "Error de conexión");
                System.out.println(ex);
            }
        });
        btnSi.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
        btnSi.setPrefHeight(32);
        btnSi.setPrefWidth(50);
        btnSi.getStyleClass().add("btnAlerta");
        
        JFXButton btnNo = new JFXButton("No");
        btnNo.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        btnNo.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
        btnNo.setPrefHeight(32);
        btnNo.setPrefWidth(50);
        btnNo.getStyleClass().add("btnAlerta");
        
        content.setActions(btnSi, btnNo);
        root.getChildren().add(stackPane);
        AnchorPane.setTopAnchor(stackPane, (500 - content.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(stackPane, 410.50);
        dialog.show();
    }

    private void limpiarCamposEditar() {
        tfKilometraje.setText("");
        tfCosto.setText("");
        tfDesFalla.setText("");
        tfDesMantenimiento.setText("");
    }
       
    private void setAnimation(){
        drawer.prefHeightProperty().bind(root.heightProperty());
        drawer.setPrefWidth(279);
        drawer.setTranslateX(-300);
        TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), drawer);

        menuTranslation.setFromX(-300);
        menuTranslation.setToX(0);
        
        btnDrawer.setOnMouseClicked(evt -> {
            menuTranslation.setRate(1);
            menuTranslation.play();
        });
        
        drawer.setOnMouseExited(evt -> {
            menuTranslation.setRate(-1);
            menuTranslation.play();
        });
    }

    private String obtenerFecha(java.util.Date fecha) {
        java.util.Date dat = fecha;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        
        String[] meses = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"}; 
        
        String fechaActualizada = day + " / " + meses[month] + " / " + year;
                
        return fechaActualizada;
    }
    
    public void mostrarAlertaExito(String mensaje, String titulo){
        JFXDialogLayout content = new JFXDialogLayout();
        ImageView check = new ImageView();
        check.setImage(new Image("/resources/Check.png"));
        check.setFitHeight(30);
        check.setFitWidth(30);
        
        Label header = new Label();
        header.setText("  " + titulo);
        header.setGraphic(check);
        content.setHeading(header);
        content.getStyleClass().add("mensaje");
        content.setBody(new Text(mensaje));
        content.setPrefSize(350, 100);
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.LEFT, true);
        JFXButton button = new JFXButton("Okay");
        button.setOnAction((ActionEvent event) -> {
            dialog.close();
            panelEditar.setVisible(false);
            this.setWidth(333);
            this.centerOnScreen();
        });
        button.setButtonType(com.jfoenix.controls.JFXButton.ButtonType.RAISED);
        button.setPrefHeight(32);
        button.getStyleClass().add("btnAlerta");
        content.setActions(button);
        root.getChildren().add(stackPane);
        AnchorPane.setTopAnchor(stackPane, (500 - content.getPrefHeight()) / 2);
        AnchorPane.setLeftAnchor(stackPane, 385.00);
        dialog.show();  
    }
    
}
