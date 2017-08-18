/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous.ctrl;

import com.luminous.Start;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Luminous
 */
public class AddOfficer implements Initializable {

    private Start app;
    
    @FXML
    private VBox regContent;

    @FXML
    private FlowPane regBase;
    
    @FXML
    public ComboBox<String> rankCB;
    
    @FXML
    public ComboBox<String> genderCB;
    
    ObservableList<String> rankOptions = FXCollections.observableArrayList("Inspector General", "Constable");
    ObservableList<String> genderOptions = FXCollections.observableArrayList("Male", "Female");
    
    @FXML
    private ImageView imgView;
        
    @FXML
    private Label passportLab;
    
    @FXML
    private Button submit;
    
     @FXML
    void uploadPics(MouseEvent event) {
        f = fileChooser.showOpenDialog(app.stage);
        if (f != null) {
            try {
                Image imgs = new Image(new FileInputStream(f), 100, 100, true, true);
                imgView.setImage(imgs);
                passportLab.setGraphic(imgView);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AddOfficer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    @FXML
    private TextField fnameField;
    
    @FXML
    private TextField lnameField;
    
    @FXML 
    private TextField phoneNumField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextArea addressField;
    
    FileChooser fileChooser = new FileChooser();
    File f;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        rankCB.getItems().addAll(rankOptions);
        genderCB.getItems().addAll(genderOptions);
        
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
        
        
        passwordField.textProperty().addListener((ob, ov, nv) -> submitButn());
        //Okay, Right here.. This next two Lines of code are unnecessary bcos the Button would only enable when the name and phone field are filled with text sequentially.
        //jargons explanatin eh. //even i dont understand myself sometimes.
//        fnameField.textProperty().addListener((ob, ov, nv) -> submitButn());
//        phoneNumField.textProperty().addListener((ob, ov, nv) -> submitButn());
        submitButn();

    }
    
    
    @FXML
    void submitButton(ActionEvent ae) {
        String passportPath = "";
        passportPath = f.getAbsolutePath();
        
        String rankSelected = rankCB.getSelectionModel().getSelectedItem();
        
        String fname = fnameField.getText().trim();
        if (fnameField.getText().trim().isEmpty()) {
            System.out.println("First Name Required");
            return;
        }
        
        String lname = lnameField.getText().trim();
        if (lnameField.getText().trim().isEmpty()) {
            System.out.println("Last Name Required");
            return;
        }
        
        String genderSelected = genderCB.getSelectionModel().getSelectedItem();
        
        String pNumber = phoneNumField.getText().trim();
        if (phoneNumField.getText().trim().isEmpty()) {
            System.out.println("Phone Number Required");
            return;
        }
        
        String pwd = passwordField.getText();
        if (passwordField.getText().trim().isEmpty()) {
            System.out.println("Password Required");
            return;
        }
        
        String addFld = addressField.getText();
       
        System.out.println("All UserData collected Successful");
        
        boolean suc = app.db.registerUser(passportPath, rankSelected, fname, lname, genderSelected, pNumber, pwd, addFld);
        System.out.println(suc);
        if (suc) clearFields();
        
    }
    
    public void clearFields() {
        fnameField.clear();
        lnameField.clear();
        genderCB.setPromptText("Select Gender");
        phoneNumField.clear();
        passwordField.clear();
        addressField.clear();
        rankCB.setPromptText("Select Rank");
    }
    
    @FXML
    void resetButton(ActionEvent event) {
        clearFields();
    }
    
    public void submitButn() {
        boolean validSubmission = true;
        if (!fnameField.getText().isEmpty() && !phoneNumField.getText().isEmpty() && !passwordField.getText().isEmpty() ) {
            validSubmission = false;
            
        }
        submit.setDisable(validSubmission);
    }
}
