/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallermecanico;

import javafx.application.Application;
import javafx.stage.Stage;
import vista.IniciarSesion;

/**
 *
 * @author dany
 */
public class TallerMecanico extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        IniciarSesion root = new IniciarSesion();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
