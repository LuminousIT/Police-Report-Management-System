/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

/**
 *
 * @author Luminous
 */
public class Utilities {

    private final Start app;
    
    public Utilities(Start appt) {
        this.app = appt;
    }
    
    public ObservableMap<String, Object> createPage(String pagename) {
        String page = "view/" + pagename + ".fxml";
        ObservableMap<String, Object> map = FXCollections.observableHashMap();
        FXMLLoader loader = new FXMLLoader(Start.class.getResource(page), null, new JavaFXBuilderFactory());
        InputStream in = Start.class.getResourceAsStream(page);
        try {
            map.put("view", loader.load(in));
            map.put("control", loader.getController());
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return map;
    }
    
    public void showMessage(String message) {
        app.homepge.status.setValue(message);
    }
    
    public void alertMessage(String message) {
        
    }
}
