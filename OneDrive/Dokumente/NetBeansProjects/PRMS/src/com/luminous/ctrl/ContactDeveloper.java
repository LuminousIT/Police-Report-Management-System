/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous.ctrl;

import com.luminous.Start;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author samth
 */
public class ContactDeveloper implements Initializable {

    private Start app;
    
    @FXML
    private FlowPane regBase;
    
     @FXML
    private VBox regContent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        regBase.heightProperty().addListener((ObservableValue<? extends Number> observable, Number OldValue, Number newValue) -> {
            regContent.setPrefHeight(newValue.doubleValue());
        });
        
        AnchorPane.setLeftAnchor(regBase, 0D);
        AnchorPane.setRightAnchor(regBase, 0D);
        AnchorPane.setTopAnchor(regBase, 0D);
        AnchorPane.setBottomAnchor(regBase, 0D);
    }    

    public void construct(Start aThis) {
        this.app = aThis;
    }
    
}
