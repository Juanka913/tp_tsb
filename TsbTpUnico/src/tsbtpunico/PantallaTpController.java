/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsbtpunico;

import Clases.LogicaPantallas;
import Clases.TSB_OAHashtable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javax.swing.table.DefaultTableModel;

/**
 * FXML Controller class
 *
 * @author JuanB
 */
public class PantallaTpController implements Initializable {
    TSB_OAHashtable tabla = new TSB_OAHashtable();
    @FXML
    private TableView<Map.Entry> lista;
    @FXML
    private TableColumn<Map.Entry, String> palabra;
    @FXML
    private TableColumn<Map.Entry, String> cantidad;
    @FXML
    private Button buscar;
    @FXML
    private Label totalPalabras;
    @FXML
    private Button cargar;
    @FXML
    private Button Agregar;
    @FXML
    private Button guardar;
    @FXML
    private Label cantidadLabel;
    @FXML
    private Label palabraLabel;
    @FXML
    private TextField itemBusqueda;
    @FXML
    private CheckBox checkBox;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         File f = new File("lista.dat");
         boolean flag = true;
         
        palabra.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().toString()));
        palabra.setSortType(TableColumn.SortType.ASCENDING);
        cantidad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));
        
        if(f.exists() && !f.isDirectory()) { 
            LogicaPantallas logica = new LogicaPantallas();
        tabla = logica.cargar();
        
        ObservableList<Map.Entry> b = observableArrayList();
        
        Iterator a = tabla.entrySet().iterator();
        while(a.hasNext())
        {
            Map.Entry entrada = (Map.Entry) a.next();
            if (entrada != null)
            {
                b.add(entrada);
                
            }
            
        }
        
        this.lista.setItems(b);
        lista.getSortOrder().add(palabra);
        this.totalPalabras.setText(String.valueOf(tabla.size()) + " " + "Palabras");
        
        
        new Alert(Alert.AlertType.INFORMATION, "Se cargo los archivos por defecto con exito, se cargo desde una tabla previamente guardada en : " + new File(".").getAbsolutePath() + "\n" + "\n" + "Presione aceptar para continuar").showAndWait();
        

        } 
        else
        {
        LogicaPantallas logica = new LogicaPantallas();
        Alert z = new Alert(Alert.AlertType.INFORMATION, "No se encontro una tabla con los archivos por defecto previamente guardada, se procesaran los archivos por defecto esto puede tomar unos momentos, por favor espere.");   
        z.show();
        try{
        tabla.addFromList(logica.agregarArchivos("16082-8.txt"));
        tabla.addFromList(logica.agregarArchivos("18166-8.txt"));
        tabla.addFromList(logica.agregarArchivos("22975-8.txt"));
        tabla.addFromList(logica.agregarArchivos("41575-8.txt"));}
        
        
        catch(FileNotFoundException ex)
        {
            flag = false;
            new Alert(Alert.AlertType.INFORMATION, "No se encontro los archivos de texto predeterminados en el directorio de ejecución, asegurese de que esten el directorio del ejecutable.").showAndWait();   
        }           
        catch (UnsupportedEncodingException ex) 
        {
                flag = false;
        }
            this.actualizarTabla();
            
            z.close();
            
            if (flag == true)
            {
                logica.guardar(tabla);
                new Alert(Alert.AlertType.INFORMATION, "Se cargo los archivos por defecto con exito, se guardo una copia de la tabla automáticamente en : " + new File(".").getAbsolutePath()).show();
            }
        }
    }    

    @FXML
    private void Buscar(ActionEvent event) {
        LogicaPantallas logica = new LogicaPantallas();
        String[] t = logica.buscar(tabla, this.itemBusqueda.getText());
        this.palabraLabel.setText(t[0]); 
        this.cantidadLabel.setText(t[1]);
        this.totalPalabras.setText(String.valueOf(tabla.size()) + " " + "palabras" );
        
        if (this.checkBox.isSelected() == true)
        {
        for (Map.Entry a : lista.getItems())
        {
            if (a.getKey().toString().equals(this.itemBusqueda.getText()))
            {
                lista.requestFocus();
                lista.getSelectionModel().select(a);
                lista.scrollTo(a);
            }
        }
        }
    }

    private void actualizarTabla()
    {
        ObservableList<Map.Entry> b = observableArrayList();
        Iterator a = tabla.entrySet().iterator();
        
            while(a.hasNext())
            {
                Map.Entry entrada = (Map.Entry) a.next();
                if (entrada != null)
                {
                     b.add(entrada);
                }

            }

            this.lista.setItems(b);
            lista.getSortOrder().add(palabra);
            this.totalPalabras.setText(String.valueOf(tabla.size()) + " " + "Palabras");
    }
    
    @FXML
    private void Cargar(ActionEvent event) {
        LogicaPantallas logica = new LogicaPantallas();
        tabla = logica.cargar();
        this.actualizarTabla();
        
        new Alert(Alert.AlertType.INFORMATION, "Se cargo la tabla de hash con exito").showAndWait();
    }

    @FXML
    private void Agregar(ActionEvent event) throws UnsupportedEncodingException, FileNotFoundException {
        LogicaPantallas logica = new LogicaPantallas();
                   FileChooser fileChooser = new FileChooser();
                   fileChooser.setTitle("Open Resource File");
                   fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Mis Archivos", "*.txt"));
                   
                   
                   File selectedFile = fileChooser.showOpenDialog(null);
                   if (selectedFile != null) {
                       try {
                           tabla.addFromList(logica.agregarArchivos(selectedFile.getAbsolutePath()));
                           this.actualizarTabla();
                           new Alert(Alert.AlertType.INFORMATION, "Se cargo el nuevo archivo de texto con exito").showAndWait();
                       } catch (FileNotFoundException ex) {
                           throw ex;
                       } catch (UnsupportedEncodingException ex) {
                           throw ex;
                       }
                    }
    }

    @FXML
    private void Guardar(ActionEvent event) {
        LogicaPantallas logica = new LogicaPantallas();
        logica.guardar(tabla);
        new Alert(Alert.AlertType.INFORMATION, "Se guardo el archivo con exito en: " + new File(".").getAbsolutePath()).showAndWait();
    }

    @FXML
    private void Restaurar(ActionEvent event) {
        
        boolean flag = true;
        tabla.clear();
        LogicaPantallas logica = new LogicaPantallas();
        Alert z = new Alert(Alert.AlertType.INFORMATION, "Se procesaran los archivos por defecto para restaurar, esto puede tomar unos momentos, por favor espere.");   
        z.show();
        try{
        tabla.addFromList(logica.agregarArchivos("16082-8.txt"));
        tabla.addFromList(logica.agregarArchivos("18166-8.txt"));
        tabla.addFromList(logica.agregarArchivos("22975-8.txt"));
        tabla.addFromList(logica.agregarArchivos("41575-8.txt"));}
        
        
        catch(FileNotFoundException ex)
        {
            flag = false;
            new Alert(Alert.AlertType.INFORMATION, "No se encontro los archivos de texto predeterminados en el directorio de ejecución, asegurese de que esten el directorio del ejecutable.").showAndWait();   
        }           
        catch (UnsupportedEncodingException ex) 
        {
                flag = false;
        }
            this.actualizarTabla();
            
            z.close();
            
            if (flag == true)
            {
                logica.guardar(tabla);
                new Alert(Alert.AlertType.INFORMATION, "Se cargo los archivos por defecto con exito, se guardo una copia de la nueva tabla automáticamente en : " + new File(".").getAbsolutePath()).show();
            }
        
    }
    
}
