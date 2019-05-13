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

import modelo.Automovil;

/**
 *
 * @author dany
 */
public class MostrarAutomoviles extends Stage{
    private JFXListView<Label> listaAutos;
    private JFXTextField tfBuscar;
    private List<Automovil> automoviles;
    private JFXTextField tfMarca, tfModelo, tfLinea, tfMatricula, tfColor;
    private AnchorPane panelEditar, panelAuto, panelBusqueda, panelConsultar;
    private JFXButton btnAgregar;
    private Label administrarAuto;
    //private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoPU");;
    
    public MostrarAutomoviles(){
        configurarPanel(); 
    }

    private void configurarPanel() {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root,415,552); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        setScene(scene);

        crearListaAutos();
        crearBarraBusqueda();
        crearPanelConsultarAuto();
        crearPanelEditarAuto();
        crearBotonAgregarAuto();

        root.getChildren().addAll(listaAutos, panelBusqueda, btnAgregar, panelEditar, panelConsultar);
    }

    private void cargarAutos(String nombreAuto) {
        listaAutos.getItems().clear();
        

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
//                    listaAutos.getItems().add(lbl);
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
        return tfMarca.getText().isEmpty() || tfModelo.getText().isEmpty() || tfLinea.getText().isEmpty()
                || tfColor.getText().isEmpty();
    }
    
    private boolean validarDatos() {
        boolean valido = true;
        String matricula = tfMatricula.getText();
        String marca = tfMarca.getText();
        String modelo =  tfModelo.getText();
        String linea =  tfLinea.getText();
        String color = tfColor.getText();
        String errores = "";
        
        if(matricula.trim().length() == 0){
            errores += "* La matricula no debe estar vacia \n";
        }
        
        if(marca.trim().length() == 0){
            errores += "* La marca no debe estar vacia \n";
        }
        
        if(modelo.trim().length() == 0){
            errores += "* El modelo no debe estar vacio \n";
        }
        
        if(linea.trim().length() < 10){
            errores += "* La linea no debe estar vacia \n";
        }
        
        if(color.trim().length() == 0){
            errores += "* El color no debe estar vacio \n";
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
        
        // Marca
        tfMarca = new JFXTextField();
        tfMarca.setPromptText("Marca del Auto");
        tfMarca.setLabelFloat(true);
        tfMarca.setLayoutX(21);
        tfMarca.setLayoutY(170);
        tfMarca.setPrefHeight(30);
        tfMarca.setPrefWidth(300);
        tfMarca.getStyleClass().add("TextField");
        
        // Modelo
        tfModelo = new JFXTextField();
        tfModelo.setPromptText("Modelo del Auto");
        tfModelo.setLabelFloat(true);
        tfModelo.setLayoutX(21);
        tfModelo.setLayoutY(240);
        tfModelo.setPrefHeight(30);
        tfModelo.setPrefWidth(300);
        tfModelo.getStyleClass().add("TextField");
        
        // Linea
        tfLinea = new JFXTextField();
        tfLinea.setPromptText("Linea del Auto");
        tfLinea.setLabelFloat(true);
        tfLinea.setLayoutX(21);
        tfLinea.setLayoutY(310);
        tfLinea.setPrefHeight(30);
        tfLinea.setPrefWidth(300);
        tfLinea.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("La linea del vehiculo debe tener maximo 4 digitos");
            }
        });
        tfLinea.getStyleClass().add("TextField");
        
        // Color
        tfColor = new JFXTextField();
        tfColor.setPromptText("Color del Auto");
        tfColor.setLabelFloat(true);
        tfColor.setLayoutX(21);
        tfColor.setLayoutY(380);
        tfColor.setPrefHeight(30);
        tfColor.setPrefWidth(300);
        tfColor.getStyleClass().add("TextField");
        
        crearPanelAuto("Matricula");
        
        JFXButton btnVerReparaciones = new JFXButton();
        btnVerReparaciones.setLayoutX(0);
        btnVerReparaciones.setLayoutY(505);
        btnVerReparaciones.setPrefHeight(50);
        btnVerReparaciones.setPrefWidth(340);
        btnVerReparaciones.setText("Ver reparaciones del auto");
        btnVerReparaciones.getStyleClass().add("botonVer");
        btnVerReparaciones.setOnAction(evt -> {
            MostrarReparaciones reparaciones = new MostrarReparaciones();
            reparaciones.show();
            this.hide();
        });
        
        panelConsultar.getChildren().addAll(panelAuto, tfMarca, tfModelo, tfLinea, tfColor, btnVerReparaciones);
    }

    private void crearPanelEditarAuto() {
        // Panel Editar Auto
        panelEditar = new AnchorPane();
        panelEditar.setLayoutX(415);
        panelEditar.setLayoutY(0);
        panelEditar.setPrefHeight(552);
        panelEditar.setPrefWidth(340);
        panelEditar.setVisible(false);
        
        tfMatricula = new JFXTextField();
        tfMatricula.setPromptText("Matricula");
        tfMatricula.setLabelFloat(true);
        tfMatricula.setLayoutX(21);
        tfMatricula.setLayoutY(160);
        tfMatricula.setPrefHeight(30);
        tfMatricula.setPrefWidth(300);
        tfMatricula.getStyleClass().add("TextField");
        
        // Marca
        tfMarca = new JFXTextField();
        tfMarca.setPromptText("Marca");
        tfMarca.setLabelFloat(true);
        tfMarca.setLayoutX(21);
        tfMarca.setLayoutY(220);
        tfMarca.setPrefHeight(30);
        tfMarca.setPrefWidth(300);
        tfMarca.getStyleClass().add("TextField");
        
        // Modelo
        tfModelo = new JFXTextField();
        tfModelo.setPromptText("Modelo");
        tfModelo.setLabelFloat(true);
        tfModelo.setLayoutX(21);
        tfModelo.setLayoutY(280);
        tfModelo.setPrefHeight(30);
        tfModelo.setPrefWidth(300);
        tfModelo.getStyleClass().add("TextField");
        
        // Linea
        tfLinea = new JFXTextField();
        tfLinea.setPromptText("Telefono");
        tfLinea.setLabelFloat(true);
        tfLinea.setLayoutX(21);
        tfLinea.setLayoutY(340);
        tfLinea.setPrefHeight(30);
        tfLinea.setPrefWidth(300);
        tfLinea.setOnKeyTyped(e -> {
            try {
                char input = e.getCharacter().charAt(0);
                if (Character.isDigit(input) != true) {
                    e.consume();
                }
            } catch (Exception ex){
                System.out.println("La linea debe tener 4 digitos");
            }
        });
        tfLinea.getStyleClass().add("TextField");
        
        // Color
        tfColor = new JFXTextField();
        tfColor.setPromptText("Color");
        tfColor.setLabelFloat(true);
        tfColor.setLayoutX(21);
        tfColor.setLayoutY(400);
        tfColor.setPrefHeight(30);
        tfColor.setPrefWidth(300);
        tfColor.getStyleClass().add("TextField");
        
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
        
        crearPanelAuto("Administrar Auto");
        
        panelEditar.getChildren().addAll(panelAuto, tfMatricula, tfMarca, tfModelo, tfLinea, tfColor, btnAceptar, btnCancelar);
    }

    private void crearPanelAuto(String accionAdministrar) {
        panelAuto = new AnchorPane();
        panelAuto.setLayoutX(0);
        panelAuto.setLayoutY(0);
        panelAuto.setPrefHeight(123);
        panelAuto.setPrefWidth(340);
        panelAuto.getStyleClass().add("panelCliente");
        
        administrarAuto = new Label();
        administrarAuto.setLayoutX(97);
        administrarAuto.setLayoutY(46);
        administrarAuto.getStyleClass().add("administrarCliente");
        administrarAuto.setText(accionAdministrar);
        
        ImageView imageAuto = new ImageView();
        imageAuto.setImage(new Image("/resources/avatar_cliente_blanco.png"));
        imageAuto.setLayoutX(26);
        imageAuto.setLayoutY(30);
        imageAuto.setFitHeight(63);
        imageAuto.setFitWidth(63);
        
        panelAuto.getChildren().addAll(administrarAuto, imageAuto);
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
        tfBuscar.setPromptText("Buscar Auto");
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

    private void crearBotonAgregarAuto() {
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
            //administrarAuto.setText("Agregar Auto");
        });
    }

    private void crearListaAutos() {
        listaAutos = new JFXListView();
        listaAutos.setLayoutX(0);
        listaAutos.setLayoutY(0);
        listaAutos.setPrefHeight(552);
        listaAutos.setPrefWidth(415);
    }
}
