/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsbtpunico;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 *
 * @author JuanB
 */
public class TsbTpUnico extends Application {
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        
        
        Parent root = FXMLLoader.load(getClass().getResource("PantallaTp.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("Procesador de Archivos de Texto");
        primaryStage.show();
        
        
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
