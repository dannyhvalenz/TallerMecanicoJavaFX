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
import controladores.AutomovilJpaController;
import controladores.ClienteJpaController;
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

/**
 *
 * @author dany
 */
public class MostrarAutomoviles2 extends Stage{
    private JFXListView<Label> carList;
    private JFXTextField tfBuscar;
    private List<Automovil> automoviles;
    
    private JFXTextField tfMarca, tfModelo, tfLinea, tfMatricula, tfColor;
    
    private JFXTextField tfMarcaConsultar, tfModeloConsultar, tfLineaConsultar, tfColorConsultar;
    
    private AnchorPane root, panelEditar, panelAutomovil, panelBusqueda, panelConsultar, panelAutomovilConsultar;
    private JFXButton btnAgregar, btnEditar, btnEliminar;
    private Label administrarAutomovil, administrarAutomovilConsultar;
    
    private boolean editarAutomovil = false;
    private String matricula, marca, linea, color, modelo, nombreAdministrador;
    private Cliente cliente;
    private Automovil automovil;
    
    public MostrarAutomoviles2(String nombreAdministrador, Cliente cliente){
        this.nombreAdministrador = nombreAdministrador;
        this.cliente = cliente;
        System.out.println("MA --> " + this.cliente.getId());
        configurarPanel(); 
        buscarAutomovil();
    }
    
    private void buscarAutomovil(){
        tfBuscar.setOnKeyTyped(evt -> {
            cargarAutomoviles(tfBuscar.getText());
        });
    }

    private void configurarPanel() {
        root = new AnchorPane();
        Scene scene = new Scene(root,333,500); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        setScene(scene);
        
        crearListaAutomoviles();
        crearBarraBusqueda();
        crearPanelConsultarAutomovil();
        crearPanelEditarAutomovil();
        crearBotonAgregarAutomovil();
        
        root.getChildren().addAll(carList, panelBusqueda, btnAgregar, panelEditar, panelConsultar);
    }
    
    private void cargarAutomoviles(String matriculaBuscar) {
        carList.getItems().clear();
        AutomovilJpaController controlador = new AutomovilJpaController();
        
        if(matriculaBuscar.equals("")){
            System.out.println(cliente.getId());
            automoviles = controlador.getAutomovilesCliente(cliente);
            int i = 0;
            for (Automovil a : automoviles) {
                    try {
                        Label lbl = new Label(a.getMarca() + " - " + a.getId());
                        lbl.setGraphic(new ImageView(new Image(
                                getClass().getResourceAsStream("/resources/Coche_Negro.png"))));
                        lbl.getStyleClass().add("label");
                        carList.getItems().add(lbl);
                    } catch (Exception ex) {
                        System.err.println("Error: " + ex);
                    }
                    i++;
                }
            System.out.println("Elementos recuperados: "+i);
        } else {
            automoviles = controlador.findAutomoviles(matriculaBuscar, cliente);
            int i = 0;
            for (Automovil a : automoviles) {
                    try {
                        Label lbl = new Label(a.getMarca() + " - " + a.getId());
                        lbl.setGraphic(new ImageView(new Image(
                                getClass().getResourceAsStream("/resources/Coche_Negro.png"))));
                        lbl.getStyleClass().add("label");
                        carList.getItems().add(lbl);
                    } catch (Exception ex) {
                        System.err.println("Error: " + ex);
                    }
                    i++;
                }
            System.out.println("Elementos recuperados: "+i);
        } 
        
    }
    
    private boolean validarDatos() {
        boolean valido = true;
        String validarMatricula = tfMatricula.getText();
        String validarMarca = tfMarca.getText();
        String validarModelo =  tfModelo.getText();
        String validarLinea =  tfLinea.getText();
        String validarColor = tfColor.getText();
        String errores = "";
        
        if(validarMatricula.trim().length() == 0){
            errores += "* La matricula no debe estar vacia \n";
        }
        
        if(validarMarca.trim().length() == 0){
            errores += "* La marca no debe estar vacia \n";
        }
        
        if(validarModelo.trim().length() == 0){
            errores += "* El modelo no debe estar vacio \n";
        }
        
        if(validarLinea.trim().length() == 0){
            errores += "* La linea no debe estar vacia \n";
        }
        
        if(validarColor.trim().length() == 0){
            errores += "* La color no debe estar vacio \n";
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

    private void crearPanelConsultarAutomovil() {
        // Panel Consultar Cliente
        panelConsultar = new AnchorPane();
        panelConsultar.setLayoutX(333);
        panelConsultar.setLayoutY(0);
        panelConsultar.setPrefHeight(552);
        panelConsultar.setPrefWidth(333);
        panelConsultar.setVisible(false);
        
        tfMarcaConsultar = new JFXTextField();
        tfMarcaConsultar.setPromptText("Marca");
        tfMarcaConsultar.setLabelFloat(true);
        tfMarcaConsultar.setLayoutX(40);
        tfMarcaConsultar.setLayoutY(207);
        tfMarcaConsultar.setPrefHeight(30);
        tfMarcaConsultar.setPrefWidth(250);
        tfMarcaConsultar.setEditable(false);
        tfMarcaConsultar.getStyleClass().add("TextField");

        tfModeloConsultar = new JFXTextField();
        tfModeloConsultar.setPromptText("Modelo");
        tfModeloConsultar.setLabelFloat(true);
        tfModeloConsultar.setLayoutX(40);
        tfModeloConsultar.setLayoutY(262);
        tfModeloConsultar.setPrefHeight(30);
        tfModeloConsultar.setPrefWidth(250);
        tfModeloConsultar.setEditable(false);
        tfModeloConsultar.getStyleClass().add("TextField");

        tfLineaConsultar = new JFXTextField();
        tfLineaConsultar.setPromptText("Linea");
        tfLineaConsultar.setLabelFloat(true);
        tfLineaConsultar.setLayoutX(40);
        tfLineaConsultar.setLayoutY(317);
        tfLineaConsultar.setPrefHeight(30);
        tfLineaConsultar.setPrefWidth(250);
        tfLineaConsultar.setEditable(false);
        tfLineaConsultar.getStyleClass().add("TextField");
        
        tfColorConsultar = new JFXTextField();
        tfColorConsultar.setPromptText("Color");
        tfColorConsultar.setLabelFloat(true);
        tfColorConsultar.setLayoutX(40);
        tfColorConsultar.setLayoutY(381);
        tfColorConsultar.setPrefHeight(30);
        tfColorConsultar.setPrefWidth(250);
        tfColorConsultar.setEditable(false);
        tfColorConsultar.getStyleClass().add("TextField");
        
        crearPanelAutomovilConsultar();
        
        JFXButton btnVerReparaciones = new JFXButton();
        btnVerReparaciones.setLayoutX(0);
        btnVerReparaciones.setLayoutY(450);
        btnVerReparaciones.setPrefHeight(50);
        btnVerReparaciones.setPrefWidth(340);
        btnVerReparaciones.setText("Ver reparaciones del auto");
        btnVerReparaciones.getStyleClass().add("botonVer");
        btnVerReparaciones.setOnAction(evt -> {
            System.out.println(cliente.getId());
            //MostrarAutomoviles autos = new MostrarAutomoviles(nombreAdministrador, cliente);
            //autos.show();
            this.hide();
        });
        
        panelConsultar.getChildren().addAll(panelAutomovilConsultar, tfMarcaConsultar, tfModeloConsultar, tfLineaConsultar, tfColorConsultar, btnVerReparaciones);
    }

    private void crearPanelEditarAutomovil() {
        // Panel Editar Cliente
        panelEditar = new AnchorPane();
        panelEditar.setLayoutX(333);
        panelEditar.setLayoutY(0);
        panelEditar.setPrefHeight(552);
        panelEditar.setPrefWidth(333);
        panelEditar.setVisible(false);
        
        tfMatricula = new JFXTextField();
        tfMatricula.setPromptText("Matricula");
        tfMatricula.setLabelFloat(true);
        tfMatricula.setLayoutX(40);
        tfMatricula.setLayoutY(173);
        tfMatricula.setPrefHeight(30);
        tfMatricula.setPrefWidth(250);
        tfMatricula.getStyleClass().add("TextField");

        tfMarca = new JFXTextField();
        tfMarca.setPromptText("Marca");
        tfMarca.setLabelFloat(true);
        tfMarca.setLayoutX(40);
        tfMarca.setLayoutY(228);
        tfMarca.setPrefHeight(30);
        tfMarca.setPrefWidth(250);
        tfMarca.getStyleClass().add("TextField");
        
        // Telefono
        tfModelo = new JFXTextField();
        tfModelo.setPromptText("Modelo");
        tfModelo.setLabelFloat(true);
        tfModelo.setLayoutX(40);
        tfModelo.setLayoutY(288);
        tfModelo.setPrefHeight(30);
        tfModelo.setPrefWidth(250);
        tfModelo.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("El modelo debe tener 4 digitos");
            }
        });
        tfModelo.getStyleClass().add("TextField");
        
        // Direccion
        tfLinea = new JFXTextField();
        tfLinea.setPromptText("Linea");
        tfLinea.setLabelFloat(true);
        tfLinea.setLayoutX(40);
        tfLinea.setLayoutY(349);
        tfLinea.setPrefHeight(30);
        tfLinea.setPrefWidth(250);
        tfLinea.getStyleClass().add("TextField");
        
        tfColor = new JFXTextField();
        tfColor.setPromptText("Color");
        tfColor.setLabelFloat(true);
        tfColor.setLayoutX(40);
        tfColor.setLayoutY(410);
        tfColor.setPrefHeight(30);
        tfColor.setPrefWidth(250);
        tfColor.getStyleClass().add("TextField");
        
        // Boton Aceptar
        JFXButton btnAceptar = new JFXButton();
        ImageView imageAceptar = new ImageView();
        imageAceptar.setImage(new Image("/resources/Aceptar.png"));
        imageAceptar.setFitHeight(28);
        imageAceptar.setFitWidth(126);
        btnAceptar.setGraphic(imageAceptar);
        btnAceptar.setLayoutX(174);
        btnAceptar.setLayoutY(457);
        btnAceptar.setPrefWidth(126);
        
        btnAceptar.setOnAction(evt -> {
            if (validarDatos()) {
                matricula = tfMatricula.getText();
                matricula = matricula.toUpperCase();
                marca = tfMarca.getText();
                modelo = tfModelo.getText();
                linea = tfLinea.getText();
                color = tfColor.getText();
                
                AutomovilJpaController controlador = new AutomovilJpaController();
                
                if (editarAutomovil == false){
                    try {
                        automovil = new Automovil (matricula, marca, modelo, linea , color, cliente);
                        controlador.create(automovil);
                        cargarAutomoviles("");
                    } catch (Exception ex){
                        System.out.println(ex);
                    }
                } else {
                    try {
                        automovil = new Automovil (matricula, marca, modelo, linea , color, cliente);
                        controlador.actualizar(automovil);
                        cargarAutomoviles("");
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            limpiarCamposEditar();
            }
        });
        
        // Boton Cancelar
        JFXButton btnCancelar = new JFXButton();
        ImageView imageCancelar = new ImageView();
        imageCancelar.setImage(new Image("/resources/Cancelar.png"));
        imageCancelar.setFitHeight(28);
        imageCancelar.setFitWidth(126);
        btnCancelar.setGraphic(imageCancelar);
        btnCancelar.setLayoutX(34);
        btnCancelar.setLayoutY(457);
        btnCancelar.setPrefWidth(126);
        
        btnCancelar.setOnAction(evt -> {
            if (editarAutomovil == true){
                panelConsultar.setVisible(true);
                panelEditar.setVisible(false);
                this.centerOnScreen();
                
                administrarAutomovilConsultar.setText(automovil.getId());
                tfMarcaConsultar.setText(automovil.getMarca());
                tfModeloConsultar.setText(automovil.getModelo());
                tfLineaConsultar.setText(automovil.getLinea());
                tfColorConsultar.setText(automovil.getColor());
            } else {
                panelEditar.setVisible(false);
                this.setWidth(333);
                this.centerOnScreen();
            }
            limpiarCamposEditar();
        });
        
        crearPanelAutomovil();
        panelEditar.getChildren().addAll(panelAutomovil, tfMatricula, tfMarca, tfModelo, tfLinea, tfColor, btnAceptar, btnCancelar);
    }

    private void crearPanelAutomovilConsultar() {
        panelAutomovilConsultar = new AnchorPane();
        panelAutomovilConsultar.setLayoutX(0);
        panelAutomovilConsultar.setLayoutY(0);
        panelAutomovilConsultar.setPrefHeight(143);
        panelAutomovilConsultar.setPrefWidth(333);
        panelAutomovilConsultar.getStyleClass().add("panelCliente");
        
        administrarAutomovilConsultar = new Label();
        administrarAutomovilConsultar.setLayoutX(140);
        administrarAutomovilConsultar.setLayoutY(38);
        administrarAutomovilConsultar.getStyleClass().add("administrarCliente");
        administrarAutomovilConsultar.setText(matricula);
        
        ImageView imageAutomovil = new ImageView();
        imageAutomovil.setImage(new Image("/resources/Coche_Blanco.png"));
        imageAutomovil.setLayoutX(22);
        imageAutomovil.setLayoutY(24);
        imageAutomovil.setFitHeight(96);
        imageAutomovil.setFitWidth(96);
        
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
            
            tfMatricula.setText(administrarAutomovilConsultar.getText());
            tfMarca.setText(tfMarcaConsultar.getText());
            tfModelo.setText(tfModeloConsultar.getText());
            tfLinea.setText(tfLineaConsultar.getText());
            tfColor.setText(tfColorConsultar.getText());
           
            editarAutomovil = true;
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
        
        panelAutomovilConsultar.getChildren().addAll(administrarAutomovilConsultar, imageAutomovil, btnEditar, btnEliminar);   
    }
    
    private void crearPanelAutomovil() {
        panelAutomovil = new AnchorPane();
        panelAutomovil.setLayoutX(0);
        panelAutomovil.setLayoutY(0);
        panelAutomovil.setPrefHeight(143);
        panelAutomovil.setPrefWidth(333);
        panelAutomovil.getStyleClass().add("panelCliente");
        
        administrarAutomovil = new Label();
        administrarAutomovil.setLayoutX(140);
        administrarAutomovil.setLayoutY(38);
        administrarAutomovil.getStyleClass().add("administrarCliente");
        administrarAutomovil.setText("Administrar \n Automovil");
        
        ImageView imageAutomovil = new ImageView();
        imageAutomovil.setImage(new Image("/resources/Coche_Blanco.png"));
        imageAutomovil.setLayoutX(22);
        imageAutomovil.setLayoutY(24);
        imageAutomovil.setFitHeight(96);
        imageAutomovil.setFitWidth(96);
        
        panelAutomovil.getChildren().addAll(administrarAutomovil, imageAutomovil);
        
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

    private void crearBotonAgregarAutomovil() {
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
            administrarAutomovil.setText("Agregar \n Cliente");
        });
    }

    private void crearListaAutomoviles() {
        carList = new JFXListView();
        carList.setLayoutX(0);
        carList.setLayoutY(86);
        carList.setPrefHeight(414);
        carList.setPrefWidth(333);
        
        carList.setOnMouseClicked((MouseEvent click) -> {
            if (click.getClickCount() == 2) {
                Automovil a = automoviles.get(carList.getSelectionModel().getSelectedIndex());
                this.setWidth(666);
                this.centerOnScreen();
                panelConsultar.setVisible(true);
                panelEditar.setVisible(false);
                
                automovil = new Automovil (a.getId(), a.getMarca(), a.getModelo(), a.getLinea(), a.getColor(), cliente);
                
                // Recuperar automovil
                administrarAutomovilConsultar.setText(automovil.getId());
                tfMarcaConsultar.setText(automovil.getMarca());
                tfModeloConsultar.setText(automovil.getModelo());
                tfLineaConsultar.setText(automovil.getLinea());
                tfColorConsultar.setText(automovil.getColor());
                
            } 
        });
        
        cargarAutomoviles("");
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
            System.out.println("Cliente eliminado");
            dialog.close();
            AutomovilJpaController controlador = new AutomovilJpaController();
            try {
                controlador.destroy(matricula);
                cargarAutomoviles("");
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
        tfMatricula.setText("");
        tfMarca.setText("");
        tfModelo.setText("");
        tfLinea.setText("");
        tfColor.setText("");
    }
    
}
