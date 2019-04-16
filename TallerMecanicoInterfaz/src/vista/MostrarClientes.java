/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
    //private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TallerMecanicoPU");;
    
    public MostrarClientes(){;
        configurarPanel(); 
    }

    private void configurarPanel() {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root,415,552); 
        setScene(scene);
        
        // Lista de clientes
        clientList = new JFXListView();
        clientList.setLayoutX(0);
        clientList.setLayoutY(0);
        clientList.setPrefHeight(552);
        clientList.setPrefWidth(415);

        // Panel de busqueda
        AnchorPane panelBusqueda = new AnchorPane();
        panelBusqueda.setPrefHeight(86);
        panelBusqueda.setPrefWidth(415);
        panelBusqueda.setStyle("-fx-background-color: #003c8f");
        panelBusqueda.setLayoutX(0);
        panelBusqueda.setLayoutY(0);
        
        // Panel de busqueda - Barra de busqueda
        AnchorPane barraBusqueda = new AnchorPane();
        barraBusqueda.setLayoutX(70);
        barraBusqueda.setLayoutY(26);
        barraBusqueda.setPrefHeight(37);
        barraBusqueda.setPrefWidth(322);
        barraBusqueda.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 30; -fx-border-width: 5");
        
        // TextField Buscar
        tfBuscar = new JFXTextField();
        tfBuscar.setPromptText("Buscar Cliente");
        tfBuscar.setLayoutX(34);
        tfBuscar.setLayoutY(3);
        tfBuscar.setPrefHeight(31);
        tfBuscar.setPrefWidth(252);
        tfBuscar.setFocusColor(Color.WHITE);
        tfBuscar.setUnFocusColor(Color.WHITE);
        
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
        
        // Boton Agregar
        JFXButton btnAgregar = new JFXButton();
        btnAgregar.setText("+");
        btnAgregar.setStyle("-fx-background-color: #FFC107; -fx-background-radius: 50; -fx-text-fill: #ffffff; -fx-font-size: 20pt");
        btnAgregar.setPrefHeight(50);
        btnAgregar.setPrefWidth(50);
        btnAgregar.setLayoutX(345);
        btnAgregar.setLayoutY(477);
        
        // Panel Consultar Cliente
        AnchorPane panelConsultar = new AnchorPane();
        panelConsultar.setLayoutX(70);
        panelConsultar.setLayoutY(26);
        panelConsultar.setPrefHeight(37);
        panelConsultar.setPrefWidth(322);
        panelConsultar.setVisible(false);
        
        
        // Panel Consultar - Elementos
        
        
        // Panel Agregar - Editar Cliente
        AnchorPane panelEditar = new AnchorPane();
        panelEditar.setLayoutX(415);
        panelEditar.setLayoutY(0);
        panelEditar.setPrefHeight(552);
        panelEditar.setPrefWidth(340);
        panelEditar.setVisible(false);
        panelEditar.setStyle("-fx-background-color: #1565c0");
        
        // Panel Editar - Elementos
        AnchorPane panelCliente = new AnchorPane();
        panelCliente.setLayoutX(0);
        panelCliente.setLayoutY(0);
        panelCliente.setPrefHeight(123);
        panelCliente.setPrefWidth(340);
        panelCliente.setStyle("-fx-background-color: #1565c0");
        
        
        panelEditar.getChildren().addAll(panelCliente);
        barraBusqueda.getChildren().addAll(tfBuscar, imageBuscar);
        panelBusqueda.getChildren().addAll(barraBusqueda, btnSalir);
        root.getChildren().addAll(clientList, panelBusqueda, btnAgregar);
        
        // Acciones de los botones
        btnAgregar.setOnAction(evt -> {
            this.setMinWidth(754);
            panelEditar.setVisible(true);
        });
        
    }

    private void cargarClientes(String nombreCliente) {
        clientList.getItems().clear();
        

//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        
//        if (nombreCliente == null) {
//            clientes = em.createNamedQuery("Cliente.findAll").getResultList();
//        } else {
//            clientes = em.createNamedQuery("Cliente.findByNombreLike").setParameter("nombre", nombreCliente).getResultList();
//        }
//        if (clientes != null) {
//            for (Cliente c : clientes) {
//                try {
//                    Label lbl = new Label(c.getNombre());
//                    lbl.setGraphic(new ImageView(new Image(
//                            getClass().getResourceAsStream("/resources/avatar_cliente.png"))));
//                    listaClientes.getItems().add(lbl);
//                } catch (Exception ex) {
//                    System.err.println("Error: " + ex);
//                }
//            }
//        }
    }
    
    public void busquedaActiva(KeyEvent e) {
        cargarClientes(tfBuscar.getText());
    }
    
    
}
