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
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import controladores.ReparacionJpaController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import modelo.Automovil;
import modelo.Cliente;
import modelo.Reparacion;

/**
 *
 * @author dany
 */
public class MostrarReparaciones2 extends Stage{
    private JFXListView<Label> repList;
    private JFXTextField tfBuscar;
    private List<Reparacion> reparaciones;
    
    //private String[] tipoReparaciones = {"Alineacion y balanceo", "Motor", "Aceite"};
    private JFXComboBox<String> cbTipo = new JFXComboBox<>();
    
    private JFXTextField tfTipo, tfKilometraje, tfCosto;
    private JFXTextArea tfDesFalla, tfDesMantenimiento;
    private JFXDatePicker dpFecha;
    private JFXTimePicker dpHora;
    
    private JFXTextField tfKilometrajeConsultar, tfCostoConsultar;
    private JFXTextArea tfDesFallaConsultar, tfDesMantenimientoConsultar;
    private JFXDatePicker dpFechaConsultar;
    private JFXTimePicker dpHoraConsultar;
    
    private AnchorPane root, panelEditar, panelReparacion, panelBusqueda, panelConsultar, panelReparacionConsultar;
    private JFXButton btnAgregar, btnEditar, btnEliminar;
    private Label administrarReparacion, administrarReparacionConsultar;
    
    private boolean editarReparacion = false;
    private Date fecha;
    private Date hora;
    private int costo, idReparacion;
    private String tipo, kilometraje, descripcionFalla, descripcionMantenimiento, nombreAdministrador;
    private Cliente cliente;
    private Automovil automovil;
    private Reparacion reparacion;
    
    public MostrarReparaciones2(String nombreAdministrador, Cliente cliente, Automovil automovil){
        this.nombreAdministrador = nombreAdministrador;
        this.cliente = cliente;
        this.automovil = automovil;
        System.out.println(this.cliente.getId() + " --> " + this.automovil.getId());
        configurarPanel(); 
        buscarAutomovil();
    }
    
    private void buscarAutomovil(){
        tfBuscar.setOnKeyTyped(evt -> {
            cargarReparaciones(tfBuscar.getText());
        });
    }

    private void configurarPanel() {
        root = new AnchorPane();
        Scene scene = new Scene(root,333,500); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        setScene(scene);
        
        crearListaAutomoviles();
        crearBarraBusqueda();
        crearPanelConsultarReparacion();
        crearPanelEditarReparacion();
        crearBotonAgregarReparacion();
        
        root.getChildren().addAll(repList, panelBusqueda, btnAgregar, panelEditar, panelConsultar);
    }
    
    private void cargarReparaciones(String tipoRepBuscar) {
        repList.getItems().clear();
        ReparacionJpaController controlador = new ReparacionJpaController();
        
        if(tipoRepBuscar.equals("")){
            System.out.println(automovil.getId());
            reparaciones = controlador.getReparacionesAutomovil(automovil);
            int i = 0;
            for (Reparacion r : reparaciones) {
                    try {
                        Label lbl = new Label(r.getTipo());
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
                    try {
                        Label lbl = new Label(r.getTipo());
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
        String validarTipo = tfTipo.getText();
        String validarKilometraje = tfKilometraje.getText();
        String validarCosto =  tfCosto.getText();
        LocalDate validarFecha =  dpFecha.getValue();
        LocalTime validarHora = dpHora.getValue();
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
            errores += "* La descripcion del mantenimiento no debe estar vacio \n";
        }
        
        if (errores.length() == 0){
            panelEditar.setVisible(false);
            this.setWidth(333);
            this.centerOnScreen();
        } else {
            mostrarAlerta(errores, "Campos Vacios");
            valido = false;
        }
        
        return valido;
    }

    private void crearPanelConsultarReparacion() {
        // Panel Consultar Cliente
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
        tfKilometrajeConsultar.setLayoutY(262);
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
        
        tfTipo = new JFXTextField();
        tfTipo.setPromptText("Tipo de reparacion");
        tfTipo.setLabelFloat(true);
        tfTipo.setLayoutX(40);
        tfTipo.setLayoutY(159);
        tfTipo.setPrefHeight(30);
        tfTipo.setPrefWidth(250);
        tfTipo.setEditable(false);
        tfTipo.getStyleClass().add("TextField");

        tfKilometraje = new JFXTextField();
        tfKilometraje.setPromptText("Kilometraje");
        tfKilometraje.setLabelFloat(true);
        tfKilometraje.setLayoutX(40);
        tfKilometraje.setLayoutY(228);
        tfKilometraje.setPrefHeight(30);
        tfKilometraje.setPrefWidth(250);
        tfKilometraje.setEditable(false);
        tfKilometraje.getStyleClass().add("TextField");

        tfCosto = new JFXTextField();
        tfCosto.setPromptText("Costo");
        tfCosto.setLabelFloat(true);
        tfCosto.setLayoutX(324);
        tfCosto.setLayoutY(323);
        tfCosto.setPrefHeight(30);
        tfCosto.setPrefWidth(110);
        tfCosto.setEditable(false);
        tfCosto.getStyleClass().add("TextField");
        
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
        //dpHora.getStyleClass().add("TextField");
        
        dpFecha = new JFXDatePicker();
        dpFecha.setPromptText("Fecha");
        dpFecha.setLayoutX(324);
        dpFecha.setLayoutY(168);
        dpFecha.setPrefHeight(30);
        dpFecha.setPrefWidth(110);
        //dpHora.getStyleClass().add("TextField");
        
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
                
                hora = Date.valueOf(dpHora.getValue().toString());
                costo = Integer.parseInt(tfCosto.getText());
                tipo = tfTipo.getText();
                kilometraje = tfKilometraje.getText();
                descripcionFalla = tfDesFalla.getText();
                descripcionMantenimiento = tfDesMantenimiento.getText();
                
                if (editarReparacion == false){
                    try {
                        idReparacion = controlador.getReparacionCount() + 1;
                        reparacion = new Reparacion (fecha, hora, costo, idReparacion, tipo, kilometraje, descripcionFalla, descripcionMantenimiento, automovil);
                        controlador.create(reparacion);
                        cargarReparaciones("");
                    } catch (Exception ex){
                        System.out.println(ex);
                    }
                } else {
                    try {
                        reparacion = new Reparacion (fecha, hora, costo, idReparacion, tipo, kilometraje, descripcionFalla, descripcionMantenimiento, automovil);
                        controlador.edit(reparacion);
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
        panelEditar.getChildren().addAll(panelReparacion, tfTipo, tfKilometraje, tfCosto, tfDesFalla, tfDesMantenimiento, dpHora, dpFecha, btnAceptar, btnCancelar);
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
            
            tfTipo.setText(administrarReparacionConsultar.getText());
            tfKilometraje.setText(reparacion.getKilometraje());
            tfCosto.setText(Integer.toString(reparacion.getCosto()));
            tfDesFalla.setText(reparacion.getDescripcionFalla());
            tfDesMantenimiento.setText(reparacion.getDescripcionMantenimiento());
            
            //Fecha
            Calendar cal = Calendar.getInstance();
            cal.setTime(reparacion.getFecha());
            LocalDate date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
            dpFecha.setValue(date);

            // Hora
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
        tfBuscar.setPromptText("Buscar Automovil");
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
        
        // Boton Drawer
        JFXButton btnDrawer = new JFXButton();
        ImageView imageDrawer = new ImageView();
        imageDrawer.setImage(new Image("/resources/Menu.png"));
        imageDrawer.setFitHeight(17);
        imageDrawer.setFitWidth(26);
        btnDrawer.setGraphic(imageDrawer);
        btnDrawer.setLayoutX(7);
        btnDrawer.setLayoutY(31);
        btnDrawer.setOnAction(evt -> {
            IniciarSesion iniciarSesion = new IniciarSesion();
            iniciarSesion.show();
            this.close();
        });
        
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
        });
    }

    private void crearListaAutomoviles() {
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
                
//                automovil = new Automovil (a.getId(), a.getMarca(), a.getModelo(), a.getLinea(), a.getColor(), cliente);
//                
//                // Recuperar automovil
//                administrarAutomovilConsultar.setText(automovil.getId());
//                tfMarcaConsultar.setText(automovil.getMarca());
//                tfModeloConsultar.setText(automovil.getModelo());
//                tfLineaConsultar.setText(automovil.getLinea());
//                tfColorConsultar.setText(automovil.getColor());
                
            } 
        });
        
        cargarReparaciones("");
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
    
    public void mostrarAlertaEliminar(){
        JFXDialogLayout content = new JFXDialogLayout();
        
        ImageView error = new ImageView();
        error.setImage(new Image("/resources/Error.png"));
        error.setFitHeight(30);
        error.setFitWidth(30);
        
        Label header = new Label();
        header.setText("  Eliminar automovil");
        header.setGraphic(error);
        content.setHeading(header);
        
        content.getStyleClass().add("mensaje");
        content.setBody(new Text("¿Desea eliminar este automovil?"));
        content.setPrefSize(250, 100);
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.LEFT, true);
        JFXButton btnSi = new JFXButton("Si");
        btnSi.setOnAction((ActionEvent event) -> {
            System.out.println("Reparacion eliminada");
            dialog.close();
            ReparacionJpaController controlador = new ReparacionJpaController();
            try {
                controlador.destroy(idReparacion);
                cargarReparaciones("");
                panelConsultar.setVisible(false);
                this.setWidth(333);
                this.centerOnScreen();
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
        AnchorPane.setLeftAnchor(stackPane, (panelEditar.getWidth() + (panelEditar.getWidth() - content.getPrefWidth()) / 2));
        dialog.show();  
    }

    private void limpiarCamposEditar() {
        tfTipo.setText("");
        tfKilometraje.setText("");
        tfCosto.setText("");
        tfDesFalla.setText("");
        tfDesMantenimiento.setText("");
    }
    
}
