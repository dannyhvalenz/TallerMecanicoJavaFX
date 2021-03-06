package vista;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controladores.ClienteJpaController;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private JFXDrawer drawer;
    private JFXHamburger btnDrawer;
    
    private JFXTextField tfNombre, tfCorreo, tfTelefono;
    private JFXTextArea tfDireccion;
    
    private JFXTextField tfNombreConsultar, tfCorreoConsultar, tfTelefonoConsultar;
    private JFXTextArea tfDireccionConsultar;
    
    private AnchorPane root, panelEditar, panelCliente, panelBusqueda, panelConsultar, panelClienteConsultar;
    private JFXButton btnAgregar, btnEditar, btnEliminar, btnBuscarCliente;
    private Label administrarCliente, administrarClienteConsultar;
    
    private boolean editarCliente = false;
    private String nombre, telefono, direccion, correo, nombreAdministrador;
    private int idCliente;
    private Cliente cliente;
    
    public MostrarClientes(String nombreAdministrador){
        this.nombreAdministrador = nombreAdministrador;
        configurarPanel(); 
        buscarCliente();
    }
    
    private void buscarCliente(){
        tfBuscar.setOnKeyTyped(evt -> {
            cargarClientes(tfBuscar.getText());
        });
    }

    private void configurarPanel() {
        root = new AnchorPane();
        Scene scene = new Scene(root,333,500); 
        scene.getStylesheets().add(getClass().getResource("/vista/styles.css").toExternalForm());
        setScene(scene);
        setResizable(false);
        
        crearListaClientes();
        crearBarraBusqueda();
        crearPanelConsultarCliente();
        crearPanelEditarCliente();
        crearBotonAgregarCliente();
        crearDrawer();
        setAnimation();
        
        root.getChildren().addAll(clientList, panelBusqueda, btnAgregar, panelEditar, panelConsultar, drawer);
    }
    
    private void cargarClientes(String nombreCliente) {
        clientList.getItems().clear();
        ClienteJpaController controlador = new ClienteJpaController();
        
        if(nombreCliente.equals("")){
            clientes = controlador.findAllClientes();
            int i = 0;
            for (Cliente c : clientes) {
                    try {
                        Label lbl = new Label(c.getNombre());
                        lbl.setGraphic(new ImageView(new Image(
                                getClass().getResourceAsStream("/resources/Avatar_Negro.png"))));
                        lbl.getStyleClass().add("label");
                        clientList.getItems().add(lbl);
                    } catch (Exception ex) {
                        System.err.println("Error: " + ex);
                    }
                    i++;
                }
            System.out.println("Clientes recuperados: " + i);
        } else {
            clientes = controlador.findClientes(nombreCliente);
            int i = 0;
            for (Cliente c : clientes) {
                    try {
                        Label lbl = new Label(c.getNombre());
                        lbl.setGraphic(new ImageView(new Image(
                                getClass().getResourceAsStream("/resources/Avatar_Negro.png"))));
                        lbl.getStyleClass().add("label");
                        clientList.getItems().add(lbl);
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
        
        if(validarTelefono.trim().length() == 0){
            errores += "* El telefono no debe estar vacio \n";
        } else if (validarTelefono.trim().length() < 10){
            errores += "* El telefono no debe tener menos \n de 10 dígitos \n";
        } else if (validarTelefono.trim().length() > 10){
            errores += "* El telefono no debe tener mas \n de 10 dígitos \n";
        }
        
        if(validarDireccion.trim().length() == 0){
            errores += "* La direccion no debe estar vacia \n";
        }
        
        if (errores.length() != 0){
            mostrarAlerta(errores, "Campos Vacios");
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
        
        // Correo
        tfCorreoConsultar = new JFXTextField();
        tfCorreoConsultar.setPromptText("Correo electronico");
        tfCorreoConsultar.setLabelFloat(true);
        tfCorreoConsultar.setLayoutX(40);
        tfCorreoConsultar.setLayoutY(214);
        tfCorreoConsultar.setPrefHeight(30);
        tfCorreoConsultar.setPrefWidth(250);
        tfCorreoConsultar.setEditable(false);
        tfCorreoConsultar.getStyleClass().add("TextField");
        
        // Telefono
        tfTelefonoConsultar = new JFXTextField();
        tfTelefonoConsultar.setPromptText("Telefono");
        tfTelefonoConsultar.setLabelFloat(true);
        tfTelefonoConsultar.setLayoutX(40);
        tfTelefonoConsultar.setLayoutY(279);
        tfTelefonoConsultar.setPrefHeight(30);
        tfTelefonoConsultar.setPrefWidth(250);
        tfTelefonoConsultar.setEditable(false);
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
        tfDireccionConsultar.setLayoutY(344);
        tfDireccionConsultar.setPrefHeight(60);
        tfDireccionConsultar.setPrefWidth(250);
        tfDireccionConsultar.setEditable(false);
        tfDireccionConsultar.getStyleClass().add("TextField");
        
        crearPanelClienteConsultar();
        
        JFXButton btnVerAutos = new JFXButton();
        btnVerAutos.setLayoutX(0);
        btnVerAutos.setLayoutY(450);
        btnVerAutos.setPrefHeight(50);
        btnVerAutos.setPrefWidth(340);
        btnVerAutos.setText("Ver autos del cliente");
        btnVerAutos.getStyleClass().add("botonVer");
        btnVerAutos.setOnAction(evt -> {
            System.out.println(cliente.getId());
            MostrarAutomoviles autos = new MostrarAutomoviles(nombreAdministrador, cliente);
            autos.show();
            this.hide();
        });
        
        panelConsultar.getChildren().addAll(panelClienteConsultar, tfCorreoConsultar, tfTelefonoConsultar, tfDireccionConsultar, btnVerAutos);
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
        btnAceptar.setLayoutX(174);
        btnAceptar.setLayoutY(457);
        btnAceptar.setPrefWidth(126);
        
        btnAceptar.setOnAction(evt -> {
            if (validarDatos()) {
                nombre = tfNombre.getText();
                correo = tfCorreo.getText();
                direccion = tfDireccion.getText();
                telefono = tfTelefono.getText();
                
                ClienteJpaController controlador = new ClienteJpaController();
                
                if (editarCliente == false){
                    try {
                        System.out.println("Agregar Cliente");
                        idCliente = controlador.getLastId() + 1;
                        System.out.println(idCliente);
                        cliente = new Cliente (idCliente, nombre, telefono, direccion, correo);
                        controlador.crearCliente(cliente);
                        mostrarAlertaExito("Se ha registrado el cliente en \n la base de datos", "Cliente Registrado");
                        cargarClientes("");
                    } catch (Exception ex){
                        System.out.println(ex);
                    }
                } else {
                    System.out.println("Editar Cliente: " + nombre);
                    try {
                        cliente = new Cliente (idCliente, nombre, telefono, direccion, correo);
                        controlador.editarCliente(cliente);
                        mostrarAlertaExito("Se ha actualizado el cliente en \n la base de datos", "Cliente Actualizado");
                        cargarClientes("");
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
            if (editarCliente == true){
                panelConsultar.setVisible(true);
                panelEditar.setVisible(false);
                this.centerOnScreen();
                
                tfDireccionConsultar.setText(tfDireccion.getText());
                tfTelefonoConsultar.setText(tfTelefono.getText());
                tfCorreoConsultar.setText(tfCorreo.getText());
                nombre = tfNombre.getText();

                nombre = separarNombre(nombre);
                nombre =  nombre.replaceAll("\\s+", "\n");
                administrarClienteConsultar.setText(nombre);
            } else {
                panelEditar.setVisible(false);
                this.setWidth(333);
                this.centerOnScreen();
            }
            limpiarCamposEditar();
        });
        
        crearPanelCliente();
        panelEditar.getChildren().addAll(panelCliente, tfNombre, tfCorreo, tfTelefono, tfDireccion, btnAceptar, btnCancelar);
    }

    private void crearPanelClienteConsultar() {
        panelClienteConsultar = new AnchorPane();
        panelClienteConsultar.setLayoutX(0);
        panelClienteConsultar.setLayoutY(0);
        panelClienteConsultar.setPrefHeight(143);
        panelClienteConsultar.setPrefWidth(333);
        panelClienteConsultar.getStyleClass().add("panelCliente");
        
        administrarClienteConsultar = new Label();
        administrarClienteConsultar.setLayoutX(140);
        administrarClienteConsultar.setLayoutY(38);
        administrarClienteConsultar.getStyleClass().add("administrarCliente");
        administrarClienteConsultar.setText(nombre);
        
        ImageView imageCliente = new ImageView();
        imageCliente.setImage(new Image("/resources/Avatar_Blanco.png"));
        imageCliente.setLayoutX(22);
        imageCliente.setLayoutY(24);
        imageCliente.setFitHeight(96);
        imageCliente.setFitWidth(96);
        
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
            nombre = separarNombre(nombre);
            tfNombre.setText(nombre);
            tfTelefono.setText(tfTelefonoConsultar.getText());
            tfCorreo.setText(tfCorreoConsultar.getText());
            tfDireccion.setText(tfDireccionConsultar.getText());
            editarCliente = true;
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
        
        panelClienteConsultar.getChildren().addAll(administrarClienteConsultar, imageCliente, btnEditar, btnEliminar);   
    }
    
    private void crearPanelCliente() {
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
        administrarCliente.setText("Administrar \n Cliente");
        
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
        
        btnDrawer = new JFXHamburger();
        btnDrawer.getStyleClass().add("jfx-hamburger");
        btnDrawer.setLayoutX(15);
        btnDrawer.setLayoutY(35);
        btnDrawer.setPrefSize(26, 17);
        
        barraBusqueda.getChildren().addAll(tfBuscar, imageBuscar);
        panelBusqueda.getChildren().addAll(barraBusqueda, btnDrawer);
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
            administrarCliente.setText("Agregar \n Cliente");
            editarCliente = false;
        });
    }

    private void crearListaClientes() {
        clientList = new JFXListView();
        clientList.setLayoutX(0);
        clientList.setLayoutY(86);
        clientList.setPrefHeight(414);
        clientList.setPrefWidth(333);
        
        clientList.setOnMouseClicked((MouseEvent click) -> {
            if (click.getClickCount() == 2) {
                Cliente c = clientes.get(clientList.getSelectionModel().getSelectedIndex());
                this.setWidth(666);
                this.centerOnScreen();
                panelConsultar.setVisible(true);
                panelEditar.setVisible(false);
                
                cliente = new Cliente (c.getId(), c.getNombre(), c.getTelefono(), c.getDireccion(), c.getCorreo());
                
                
                // Recuperar cliente
                idCliente = cliente.getId();
                tfTelefonoConsultar.setText(cliente.getTelefono());
                tfDireccionConsultar.setText(cliente.getDireccion());
                tfCorreoConsultar.setText(cliente.getCorreo());
                
                nombre = cliente.getNombre();
                nombre =  nombre.replaceAll("\\s+", "\n");
                administrarClienteConsultar.setText(nombre);
            } 
        });
        
        cargarClientes("");
    }
    
    public String separarNombre(String nombre){
        String nombre_nuevo = "";
        for (int i = 0; i < nombre.length(); i++){
            char c = nombre.charAt(i);
            if (i >= 1){
                if(Character.isUpperCase(c)){               
                    nombre_nuevo = nombre_nuevo + " " + c;
                } else {
                    nombre_nuevo = nombre_nuevo + c;
                }
            } else {
                nombre_nuevo = nombre_nuevo + c;
            }
        }
        return nombre_nuevo;
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
        content.setPrefSize(300, 100);
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
        AnchorPane.setLeftAnchor(stackPane, 345.00);
        dialog.show();  
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
        header.setText("  Eliminar cliente");
        header.setGraphic(error);
        content.setHeading(header);
        
        content.getStyleClass().add("mensaje");
        content.setBody(new Text("¿Desea eliminar este cliente?"));
        content.setPrefSize(250, 100);
        StackPane stackPane = new StackPane();
        stackPane.autosize();
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.LEFT, true);
        JFXButton btnSi = new JFXButton("Si");
        btnSi.setOnAction((ActionEvent event) -> {
            System.out.println("Cliente eliminado");
            dialog.close();
            ClienteJpaController controlador = new ClienteJpaController();
            try {
                controlador.destroy(cliente.getId());
                mostrarAlertaExito("Se ha eliminado el cliente de \n la base de datos", "Cliente Eliminado");
                cargarClientes("");
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
        tfNombre.setText("");
        tfCorreo.setText("");
        tfDireccion.setText("");
        tfTelefono.setText("");
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
        ruta.setPrefWidth(190);
        ruta.setLayoutX(45);
        ruta.setLayoutY(127);
        ruta.setStyle("-fx-font-size:9pt; -fx-font-family: 'Fira Code', monospace; -fx-text-fill:#ffffff;");
        
        btnBuscarCliente = new JFXButton();
        btnBuscarCliente.setGraphic(new ImageView(new Image("/resources/Avatar_Drawer.png")));
        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.setPrefHeight(30);
        btnBuscarCliente.setPrefWidth(150);
        btnBuscarCliente.setLayoutX(11);
        btnBuscarCliente.setLayoutY(167);
        btnBuscarCliente.getStyleClass().add("botonesDrawer");
        
        
        JFXButton btnBuscarAutomovil = new JFXButton();
        btnBuscarAutomovil.setGraphic(new ImageView(new Image("/resources/Coche_Drawer.png")));
        btnBuscarAutomovil.setText("Buscar Automovil");
        btnBuscarAutomovil.setPrefHeight(25);
        btnBuscarAutomovil.setPrefWidth(177);
        btnBuscarAutomovil.setLayoutX(11);
        btnBuscarAutomovil.setLayoutY(211);
        btnBuscarAutomovil.getStyleClass().add("botonesDrawer");
        btnBuscarAutomovil.setDisable(true);

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
        
        btnBuscarCliente.setOnAction(evt -> {
            menuTranslation.setRate(-1);
            menuTranslation.play();
        });
        
        drawer.setOnMouseExited(evt -> {
            menuTranslation.setRate(-1);
            menuTranslation.play();
        });
    }
}
