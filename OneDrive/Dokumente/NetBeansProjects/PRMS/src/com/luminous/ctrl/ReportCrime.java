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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 * FXML Controller class
 *
 * @author samth
 */
public class ReportCrime implements Initializable {

    private Start app;
    
     @FXML
    private ComboBox<String> caseCatCheckBox;

    @FXML
    private TextField fullNameField;

    @FXML
    private ComboBox<String> genderCheckBox;

    @FXML
    private TextField pNumberField;

    @FXML
    private FlowPane caseReportContent;

    @FXML
    private ScrollPane caseReportBasePane;

    @FXML
    private TextArea addressField;

    @FXML
    private TextArea statementField;

    @FXML
    private Button rSubmit;

    @FXML
    private Button resetBut;


    ObservableList<String> gender = FXCollections.observableArrayList("Male", "Female");
    ObservableList<String> caseCategory = FXCollections.observableArrayList("Theft", "Assault", "Murder", "Rape" );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        caseReportBasePane.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldVal, Number newVal) -> {
            caseReportContent.setPrefHeight(newVal.doubleValue());
        });
        
        caseReportBasePane.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldVal, Number newVal) -> {
            caseReportContent.setPrefWidth(newVal.doubleValue());
        });
        
        genderCheckBox.setItems(gender);
        caseCatCheckBox.setItems(caseCategory);
    }  

    public void construct(Start aThis) {
        this.app = aThis;
        
        AnchorPane.setTopAnchor(caseReportBasePane, 0D);
        AnchorPane.setLeftAnchor(caseReportBasePane, 0D);
        AnchorPane.setRightAnchor(caseReportBasePane, 0D);
        AnchorPane.setBottomAnchor(caseReportBasePane, 0D);
        
        rSubmit.setOnAction((ae) -> {
            String fname = fullNameField.getText();
            if (fullNameField.getText().isEmpty()) {
                app.util.showMessage("Full Name is Required");
                return;
            }
            
            double phoneNum;
            try {
                phoneNum = Double.parseDouble(pNumberField.getText());
            }
            catch(NumberFormatException ex) {
                app.util.showMessage("Phone Number is Required");
                return;
            }
            
            String genVal = genderCheckBox.getSelectionModel().getSelectedItem();
            
            String add = addressField.getText();
            if (addressField.getText().isEmpty()) {
                app.util.showMessage("Address is Required");
                return;
            }
            
            String caseVal = caseCatCheckBox.getSelectionModel().getSelectedItem();
            
            String rStmt = statementField.getText();
            if (statementField.getText().isEmpty()) {
                app.util.showMessage("Statement is Required");
                return;
            }
            
            boolean rSubmitSuccess = app.db.registerCrimeReport(fname, phoneNum, genVal, add, caseVal, rStmt);
            
            if (rSubmitSuccess) {
                resetFields();
            }
        });
        
        resetBut.setOnAction((ae) -> {
            resetFields();
        });
        
    }
    
    public void resetFields() {
        fullNameField.setText("");
        pNumberField.setText("");
        addressField.setText("");
        genderCheckBox.setPromptText("Select Gender");
        caseCatCheckBox.setPromptText("Select Case");
        statementField.setText("");
    }
    
}
