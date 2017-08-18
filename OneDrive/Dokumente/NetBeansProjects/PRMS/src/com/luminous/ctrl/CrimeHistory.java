/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous.ctrl;

import com.luminous.Start;
import com.luminous.domain.CrimeHistoryData;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.print.PrinterJob.JobStatus;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Luminous
 */
public class CrimeHistory implements Initializable {

    private Start app;
    
    @FXML
    private TableColumn<CrimeHistoryData, String> chCrimeCol;

    @FXML
    private ScrollPane filterScrollPane;

    @FXML
    private TableColumn<CrimeHistoryData, Node> chStatementCol;

    @FXML
    private TableColumn<CrimeHistoryData, String> chPhoneCol;

    @FXML
    private TableColumn<CrimeHistoryData, String> chNameCol;

    @FXML
    private TableColumn<CrimeHistoryData, String> chDateCol;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private TableColumn<CrimeHistoryData, Integer> chSnCol;
    
    @FXML
    private TableColumn<CrimeHistoryData, String> chCaseIdCol;

    @FXML
    private FlowPane filterFlowPane;
    
    @FXML
    private TableView crimeHistoryTable;
    
     @FXML
    private DatePicker toDateFld;
     
      @FXML
    private DatePicker fromDateFld;

    @FXML
    private ComboBox chCrimeCat;

    @FXML
    private TextField chFilterField;
    
     @FXML
    private TextField chCaseIdFld;
     
    @FXML
    private AnchorPane printablePane;
      
    @FXML
    private FlowPane printableDetailsPane;
    
    @FXML
    private Label detailsName;

    @FXML
    private Label detailsPhone;
    
    @FXML
    private Label detailsCaseId;
    
    @FXML
    private Label detailsDate;
    
    @FXML
    private Label detailsCrimeCat;
    
    @FXML
    private Label detailsStatement;
    
    @FXML
    private Button closeBut;
    
    @FXML
    private Button printBut;
    /**
     * Initializes the controller class.
     */
    
    ObservableList<ObservableMap<String, Object>> res = FXCollections.observableArrayList();
    ObservableList<ObservableMap<String, Object>> res2 = FXCollections.observableArrayList();
    ObservableList<String> caseCategory = FXCollections.observableArrayList("Theft", "Assault", "Murder", "Rape" );
    private int x;
    
    ScaleTransition scaleIn, scaleOut;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        AnchorPane.setLeftAnchor(contentPane, 0D);
        AnchorPane.setRightAnchor(contentPane, 0D);
        AnchorPane.setTopAnchor(contentPane, 0D);
        AnchorPane.setBottomAnchor(contentPane, 0D);
        
        filterScrollPane.heightProperty().addListener((ob, ov, nv) -> {
            filterFlowPane.setPrefHeight(nv.doubleValue());
        });
        
        filterScrollPane.widthProperty().addListener((ob, ov, nv) -> {
            filterFlowPane.setPrefWidth(nv.doubleValue());
        });
        
        chSnCol.setCellValueFactory(new PropertyValueFactory("sn"));
        chNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        chPhoneCol.setCellValueFactory(new PropertyValueFactory("phone"));
        chStatementCol.setCellValueFactory(new PropertyValueFactory("action"));
        chDateCol.setCellValueFactory(new PropertyValueFactory("date"));
        chCaseIdCol.setCellValueFactory(new PropertyValueFactory("caseId"));
        chCrimeCol.setCellValueFactory(new PropertyValueFactory("caseType"));
        
        chCrimeCat.setItems(caseCategory);
        
        scaleIn = new ScaleTransition(Duration.millis(400), printablePane);
        scaleOut = new ScaleTransition(Duration.millis(400), printablePane);
        
        scaleIn.setToX(1);
        scaleIn.setToY(1);
         
        scaleOut.setToX(.1);
        scaleOut.setToY(.1);
       
        
        contentPane.getChildren().remove(printablePane);
    }    

    public void construct(Start aThis) {
        this.app = aThis;
        fromDateFld.setValue(LocalDate.now().minusYears(1));
        toDateFld.setValue(LocalDate.now());
        
        fromDateFld.valueProperty().addListener((ob, ov, nv) -> {
            updateTable();
        });
        toDateFld.valueProperty().addListener((ob, ov, nv) -> {
            updateTable();
        });
        
        chFilterField.textProperty().addListener((ob, ov, nv) -> {
            updateTable(chFilterField.getText().trim());
        });
        chCaseIdFld.textProperty().addListener((ob, ov, nv) -> {
            updateTable2(chCaseIdFld.getText().trim());
        });
        
        chCrimeCat.valueProperty().addListener((ob, ov, nv) -> {
            updateTable3(chCrimeCat.getSelectionModel().getSelectedItem().toString());
        });
        
        
        closeBut.setOnAction(ae -> {
            contentPane.getChildren().remove(printablePane);
            crimeHistoryTable.setDisable(false);
        });
        
        PrinterJob pj = PrinterJob.createPrinterJob(Printer.getDefaultPrinter());
        printBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                
                if (pj.showPageSetupDialog(app.stage)) {
                    printBut.setDisable(true);
                    if (pj.showPrintDialog(app.stage)) {
                        if(pj.printPage(printableDetailsPane)) {
                            pj.endJob();
                        }
                    }
                }
            }
        });
        
        pj.jobStatusProperty().addListener(new ChangeListener<PrinterJob.JobStatus>() {
            @Override
            public void changed(ObservableValue<? extends PrinterJob.JobStatus> observable, PrinterJob.JobStatus oldValue, PrinterJob.JobStatus newValue) {
                app.util.showMessage(newValue.toString());
            }
        });
        
        updateTable();
        
    }
    
    //for date
    public void updateTable() {

        Timestamp fromDate = Timestamp.valueOf(LocalDateTime.of(fromDateFld.getValue(), LocalTime.MIN));
        Timestamp toDate = Timestamp.valueOf(LocalDateTime.of(toDateFld.getValue(), LocalTime.MAX));
        
        res = app.db.getCrimeHistoryData(fromDate, toDate);
        crimeHistoryTable.getItems().clear();
        if (res != null) {
            x = 1;
            res.stream().forEach((ObservableMap<String, Object> crimeData) -> {
                crimeHistoryTable.getItems().add(new CrimeHistoryData(app, x++, crimeData));
            });
        }
        
    }
    
    //for nameField
    public void updateTable(String filter) {
        
        res = app.db.getCrimeHistoryData(filter);
        crimeHistoryTable.getItems().clear();
        if (res != null) {
            x = 1;
            res.stream().forEach((ObservableMap<String, Object> crimeData) -> {
                crimeHistoryTable.getItems().add(new CrimeHistoryData(app, x++, crimeData));
            });
        }
        
    }
    
    //for caseidFiled
     public void updateTable2(String filter) {
        res = app.db.getCrimeHistoryData2(filter);
        crimeHistoryTable.getItems().clear();
        if (res != null) {
            x = 1;
            res.stream().forEach((ObservableMap<String, Object> crimeData) -> {
                crimeHistoryTable.getItems().add(new CrimeHistoryData(app, x++, crimeData));
            });
        }
    }
     
     //for crimecategory
     public void updateTable3(String filter) {
        res = app.db.getCrimeHistoryData3(filter);
        crimeHistoryTable.getItems().clear();
        if (res != null) {
            x = 1;
            res.stream().forEach((ObservableMap<String, Object> crimeData) -> {
                crimeHistoryTable.getItems().add(new CrimeHistoryData(app, x++, crimeData));
            });
        }
    }
 
     ObservableMap<String, Object> mappp = FXCollections.observableHashMap();
     public void viewStatement(int x) {
        //mappp =  res.get(crimeHistoryTable.getSelectionModel().getSelectedIndex() + 1);
        
        if (res.size() > x) {
        /*NOTE: res is a list of Maps(each map contain a specific offcicer info). 
        //the viewStatement method stores the officer info of a officer x (from result map) into mappp
        mapp contains info details of a particular officer at every instant*/
        mappp =  res.get(x); 
        System.out.println(mappp.get("name"));
        detailsName.setText("Name:" + mappp.get("name"));
        detailsPhone.setText("Phone: " + mappp.get("phone"));
        detailsCrimeCat.setText("Crime Category: " + mappp.get("casetype"));
        detailsCaseId.setText("CaseID: " + mappp.get("caseId"));
        detailsDate.setText("Date: " + mappp.get("cDate"));
        detailsStatement.setText("STATEMENT\n " + mappp.get("statement"));
        contentPane.getChildren().add(printablePane);
        crimeHistoryTable.setDisable(true);
        }
        else {return;} 
     }
}
