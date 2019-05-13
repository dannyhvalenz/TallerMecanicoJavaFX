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
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import modelo.Reparacion;

/**
 *
 * @author dany
 */
public class MostrarReparaciones extends Stage{
    private JFXListView<Label> listaReparaciones;
    private JFXTextField tfBuscar;
    private List<Reparacion> reparaciones;
    private JFXTextField tfKilometraje, tfCosto, tfTipoReparacion;
    private JFXTextArea tfDescripcionRep,tfDescripcionFalla ;
    private AnchorPane panelEditar, panelReparacion, panelBusqueda, panelConsultar;
    private JFXButton btnAgregar;
    private Label administrarReparacion;
    //private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoPU");;
    
    public MostrarReparaciones(){
        configurarPanel(); 
    }

    private void configurarPanel() {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root,415,552); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        setScene(scene);

        crearlistaReparaciones();
        crearBarraBusqueda();
        crearPanelConsultarAuto();
        crearPanelEditarAuto();
        crearBotonAgregarReparacion();

        root.getChildren().addAll(listaReparaciones, panelBusqueda, btnAgregar, panelEditar, panelConsultar);
    }

    private void cargarAutos(String nombreAuto) {
        listaReparaciones.getItems().clear();
        

//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        
//        if (nombreAuto == null) {
//            Autos = em.createNamedQuery("Auto.findAll").getResultList();
//        } else {
//            Autos = em.createNamedQuery("Auto.findByNombreLike").setParameter("nombre", nombreAuto).getResultList();
//        }
//        if (Autos != null) {
//            for (Auto c : Autos) {
//                try {
//                    Label lbl = new Label(c.getNombre());
//                    lbl.setGraphic(new ImageView(new Image(
//                            getClass().getResourceAsStream("/resources/avatar_Auto.png"))));
//                    listaReparaciones.getItems().add(lbl);
//                } catch (Exception ex) {
//                    System.err.println("Error: " + ex);
//                }
//            }
//        }
    }
    
    public void busquedaActiva(KeyEvent e) {
        cargarAutos(tfBuscar.getText());
    }

    private boolean datosInvalidos() {
        return tfKilometraje.getText().isEmpty() || tfCosto.getText().isEmpty() || tfDescripcionFalla.getText().isEmpty()
                || tfDescripcionRep.getText().isEmpty();
    }
    
    private boolean validarDatos() {
        boolean valido = true;
        String tipoReparacion = tfTipoReparacion.getText();
        String kilometraje = tfKilometraje.getText();
        String costo =  tfCosto.getText();
        String descripcionFalla =  tfDescripcionFalla.getText();
        String descripcionReparacion = tfDescripcionRep.getText();
        String errores = "";
        
        if(tipoReparacion.trim().length() == 0){
            errores += "* El tipo de reparacion no debe estar vacio \n";
        }
        
        if(kilometraje.trim().length() == 0){
            errores += "* El kilometraje no debe estar vacio \n";
        }
        
        if(costo.trim().length() == 0){
            errores += "* El costo no debe estar vacio \n";
        }
        
        if(descripcionFalla.trim().length() < 10){
            errores += "* La descripcion de la falla no debe estar vacia \n";
        }
        
        if(descripcionReparacion.trim().length() == 0){
            errores += "* La descripcion de la reparacion no debe estar vacia \n";
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

    private void crearPanelConsultarAuto() {
        // Panel Consultar Auto
        panelConsultar = new AnchorPane();
        panelConsultar.setLayoutX(415);
        panelConsultar.setLayoutY(0);
        panelConsultar.setPrefHeight(552);
        panelConsultar.setPrefWidth(340);
        panelConsultar.setVisible(false);
        
        // Tipo de reparacion
        tfTipoReparacion = new JFXTextField();
        tfTipoReparacion.setPromptText("Tipo de Reparacion");
        tfTipoReparacion.setLabelFloat(true);
        tfTipoReparacion.setLayoutX(21);
        tfTipoReparacion.setLayoutY(155);
        tfTipoReparacion.setPrefHeight(30);
        tfTipoReparacion.setPrefWidth(300);
        tfTipoReparacion.getStyleClass().add("TextField");
        
        // kilometraje
        tfKilometraje = new JFXTextField();
        tfKilometraje.setPromptText("Kilometraje");
        tfKilometraje.setLabelFloat(true);
        tfKilometraje.setLayoutX(21);
        tfKilometraje.setLayoutY(210);
        tfKilometraje.setPrefHeight(30);
        tfKilometraje.setPrefWidth(300);
        tfKilometraje.getStyleClass().add("TextField");
        
        // Modelo
        tfCosto = new JFXTextField();
        tfCosto.setPromptText("Costo");
        tfCosto.setLabelFloat(true);
        tfCosto.setLayoutX(21);
        tfCosto.setLayoutY(270);
        tfCosto.setPrefHeight(30);
        tfCosto.setPrefWidth(300);
        tfCosto.getStyleClass().add("TextField");
        
        // Descripcion de la Falla
        tfDescripcionFalla = new JFXTextArea();
        tfDescripcionFalla.setPromptText("Descripcion de la Falla");
        tfDescripcionFalla.setLabelFloat(true);
        tfDescripcionFalla.setLayoutX(21);
        tfDescripcionFalla.setLayoutY(330);
        tfDescripcionFalla.setPrefHeight(60);
        tfDescripcionFalla.setPrefWidth(300);
        tfDescripcionFalla.getStyleClass().add("TextField");
        
        // Descripcion de la Reparacion
        tfDescripcionRep = new JFXTextArea();
        tfDescripcionRep.setPromptText("Descripcion de la Reparacion");
        tfDescripcionRep.setLabelFloat(true);
        tfDescripcionRep.setLayoutX(21);
        tfDescripcionRep.setLayoutY(420);
        tfDescripcionRep.setPrefHeight(60);
        tfDescripcionRep.setPrefWidth(300);
        tfDescripcionRep.getStyleClass().add("TextField");
        
        crearPanelReparacion("Reparacion");
        
        panelConsultar.getChildren().addAll(panelReparacion, tfTipoReparacion, tfKilometraje, tfCosto, tfDescripcionFalla, tfDescripcionRep);
    }

    private void crearPanelEditarAuto() {
        // Panel Editar Auto
        panelEditar = new AnchorPane();
        panelEditar.setLayoutX(410);
        panelEditar.setLayoutY(0);
        panelEditar.setPrefHeight(552);
        panelEditar.setPrefWidth(340);
        panelEditar.setVisible(false);
        
        // Tipo de reparacion
        tfTipoReparacion = new JFXTextField();
        tfTipoReparacion.setPromptText("Tipo de Reparacion");
        tfTipoReparacion.setLabelFloat(true);
        tfTipoReparacion.setLayoutX(21);
        tfTipoReparacion.setLayoutY(155);
        tfTipoReparacion.setPrefHeight(30);
        tfTipoReparacion.setPrefWidth(300);
        tfTipoReparacion.getStyleClass().add("TextField");
        
        // kilometraje
        tfKilometraje = new JFXTextField();
        tfKilometraje.setPromptText("Kilometraje");
        tfKilometraje.setLabelFloat(true);
        tfKilometraje.setLayoutX(21);
        tfKilometraje.setLayoutY(210);
        tfKilometraje.setPrefHeight(30);
        tfKilometraje.setPrefWidth(300);
        tfKilometraje.getStyleClass().add("TextField");
        tfKilometraje.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("El kilometraje debe tener al menos 2 digitos");
            }
        });
        
        // Modelo
        tfCosto = new JFXTextField();
        tfCosto.setPromptText("Costo");
        tfCosto.setLabelFloat(true);
        tfCosto.setLayoutX(21);
        tfCosto.setLayoutY(270);
        tfCosto.setPrefHeight(30);
        tfCosto.setPrefWidth(300);
        tfCosto.getStyleClass().add("TextField");
        tfCosto.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("El costo debe tener al menos 2 digitos");
            }
        });
        
        // Descripcion de la Falla
        tfDescripcionFalla = new JFXTextArea();
        tfDescripcionFalla.setPromptText("Descripcion de la Falla");
        tfDescripcionFalla.setLabelFloat(true);
        tfDescripcionFalla.setLayoutX(21);
        tfDescripcionFalla.setLayoutY(330);
        tfDescripcionFalla.setPrefHeight(60);
        tfDescripcionFalla.setPrefWidth(300);
        tfDescripcionFalla.getStyleClass().add("TextField");
        
        // Descripcion de la Reparacion
        tfDescripcionRep = new JFXTextArea();
        tfDescripcionRep.setPromptText("Descripcion de la Reparacion");
        tfDescripcionRep.setLabelFloat(true);
        tfDescripcionRep.setLayoutX(21);
        tfDescripcionRep.setLayoutY(420);
        tfDescripcionRep.setPrefHeight(60);
        tfDescripcionRep.setPrefWidth(300);
        tfDescripcionRep.getStyleClass().add("TextField");
        
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
        
        crearPanelReparacion("Reparacion");
        
        panelEditar.getChildren().addAll(panelReparacion, tfTipoReparacion, tfKilometraje, tfCosto, tfDescripcionFalla, tfDescripcionRep, btnAceptar, btnCancelar);
    }

    private void crearPanelReparacion(String accionAdministrar) {
        panelReparacion = new AnchorPane();
        panelReparacion.setLayoutX(0);
        panelReparacion.setLayoutY(0);
        panelReparacion.setPrefHeight(123);
        panelReparacion.setPrefWidth(340);
        panelReparacion.getStyleClass().add("panelCliente");
        
        administrarReparacion = new Label();
        administrarReparacion.setLayoutX(97);
        administrarReparacion.setLayoutY(46);
        administrarReparacion.getStyleClass().add("administrarCliente");
        administrarReparacion.setText(accionAdministrar);
        
        ImageView imageReparacion = new ImageView();
        imageReparacion.setImage(new Image("/resources/avatar_cliente_blanco.png"));
        imageReparacion.setLayoutX(26);
        imageReparacion.setLayoutY(30);
        imageReparacion.setFitHeight(63);
        imageReparacion.setFitWidth(63);
        
        panelReparacion.getChildren().addAll(administrarReparacion, imageReparacion);
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
        tfBuscar.setPromptText("Buscar Reparacion");
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

    private void crearBotonAgregarReparacion() {
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
            //panelEditar.setVisible(true);
            //administrarReparacion.setText("Agregar Reparacion");
        });
    }

    private void crearlistaReparaciones() {
        listaReparaciones = new JFXListView();
        listaReparaciones.setLayoutX(0);
        listaReparaciones.setLayoutY(0);
        listaReparaciones.setPrefHeight(552);
        listaReparaciones.setPrefWidth(415);
    }
}
