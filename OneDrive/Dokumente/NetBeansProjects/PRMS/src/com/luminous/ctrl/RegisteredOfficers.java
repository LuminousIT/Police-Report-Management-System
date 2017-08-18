/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous.ctrl;

import com.luminous.Start;
import com.luminous.domain.OfficerData;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Luminous
 */
public class RegisteredOfficers implements Initializable {

    private Start app;
    
     @FXML
    private TableColumn<OfficerData, String> phoneCol;

    @FXML
    private TableColumn<OfficerData, String> nameCol;

    @FXML
    private TableColumn<OfficerData, String> rankCol;

    @FXML
    private TableColumn<OfficerData, Node> detailActCol;

    @FXML
    private TableColumn<OfficerData, Integer> serialCol;
    
    @FXML
    private TableView<OfficerData> officerTable;


    @FXML
    private TextField filterField;

    @FXML
    private AnchorPane detailsPane;

    @FXML
    private AnchorPane regdOffBase;
    
    ObservableList<ObservableMap<String, Object>> result = FXCollections.observableArrayList();
    ObservableList<String> rankList = FXCollections.observableArrayList("Inspector General", "Deputy Insp-General", "Constable");
    private int x;
    
    TranslateTransition transIn;
    TranslateTransition transOut;
    
    
    @FXML
    private ComboBox<String> updateRankCB;
    
    @FXML
    private TextField updatePhoneNumField;
    
     @FXML
    private TextField updateLnameField;
     
    @FXML
    private TextField updateFnameField;
        
    @FXML
    private TextArea updateAddressField;
            
    @FXML
    private Button update;
    
    @FXML
    private Button delete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AnchorPane.setBottomAnchor(regdOffBase, 0D);
        AnchorPane.setTopAnchor(regdOffBase, 0D);
        AnchorPane.setLeftAnchor(regdOffBase, 0D);
        AnchorPane.setRightAnchor(regdOffBase, 0D);
        
        serialCol.setCellValueFactory(new PropertyValueFactory("sn"));
        rankCol.setCellValueFactory(new PropertyValueFactory("Rank"));
        nameCol.setCellValueFactory(new PropertyValueFactory("Name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory("Phone"));
        detailActCol.setCellValueFactory(new PropertyValueFactory("details"));
        
        transIn = new TranslateTransition(Duration.millis(2000), detailsPane);
        transOut = new TranslateTransition(Duration.millis(4000), detailsPane);
        
        transIn.setFromX(600);
        transIn.setToX(0);
        transIn.setInterpolator(Interpolator.EASE_IN);
        
        transOut.setToX(600);
        transOut.setInterpolator(Interpolator.EASE_OUT);
        transOut.setDelay(Duration.seconds(3));
        //transIn.setOnFinished((ae) -> transOut.play());
        officerTable.setOnMouseClicked((me) -> {
            hideDetailsPane();
            me.consume();
        });
        
        updateRankCB.setItems(rankList);
        updateFnameField.setDisable(true);
        updateLnameField.setDisable(true);
    }    

    public void construct(Start aThis) {
        this.app = aThis;
        hideDetailsPane();
        
        filterField.textProperty().addListener((ob, ov, nv) -> refreshOffTable(filterField.getText().trim()));
        
        delete.setOnAction((ae) -> {
            boolean delSuccess = app.db.deleteOffRow((int) mapp.get("id"));
            if (delSuccess) {
                app.util.showMessage("Officer Data Deleted Successfully");
            } else {
                app.util.showMessage("Unable to delete Officer");
            }
            
        });
        
        update.setOnAction((ae) -> {
            
        String rankSelected = updateRankCB.getSelectionModel().getSelectedItem();
        
        String fname = updateFnameField.getText().trim();
        if (updateFnameField.getText().trim().isEmpty()) {
            System.out.println("First Name Required");
            return;
        }
        
        String lname = updateLnameField.getText().trim();
        if (updateLnameField.getText().trim().isEmpty()) {
            System.out.println("Last Name Required");
            return;
        }
        
        double pNumber = 0D;
        if (updatePhoneNumField.getText().trim().isEmpty()) {
            System.out.println("Phone Number Required");
            return;
        }
        try{
        pNumber = Double.parseDouble(updatePhoneNumField.getText().trim());
        }catch(NumberFormatException ex) {
            app.util.showMessage("Digit required as Phone number");
        }
        String addFld = updateAddressField.getText();
       
        System.out.println("All UserData collected Successful");
            
        boolean suc = app.db.updateInfo(rankSelected, fname, lname, pNumber, addFld, (int) mapp.get("id"));
        
        if (suc) {
            app.util.showMessage("Update Successful");
        }
        
        });
        
        refreshOffTable();
    }
    
    public void refreshOffTable() {
        result = app.db.getRegOfficerData();
        officerTable.getItems().clear();
        
        if (result != null) {
            x = 1;
            result.stream().forEach((ObservableMap<String, Object> officers) -> {
                officerTable.getItems().add(new OfficerData(app, x++, officers));
            });
        }
        
    }
    
    public void refreshOffTable(String filter) {
        result = app.db.getRegOfficerData(filter);
        officerTable.getItems().clear();
        
        if (result != null) {
            x = 1;
            result.stream().forEach((ObservableMap<String, Object> officers) -> {
                officerTable.getItems().add(new OfficerData(app, x++, officers));
            });
        }
        
    }

    ObservableMap<String, Object> mapp;
    public void showDetailsPane(int x) {
        detailsPane.setOpacity(1);
        if (Animation.Status.STOPPED != transOut.getStatus()) {
            transOut.stop();
        }
        transIn.play();
        
        if (result.size() > x) {
            //ObservableMap<String, Object> mapp = result.get(x);
            mapp = result.get(x);
            updateFnameField.setText(mapp.get("fname").toString());
            updateLnameField.setText(mapp.get("lname").toString());
            updatePhoneNumField.setText(mapp.get("phone").toString());
            updateAddressField.setText(mapp.get("address").toString());
       } else {
            return;
        }
    }
    
    public void hideDetailsPane() {
        if (Animation.Status.STOPPED != transIn.getStatus()) {
            transIn.stop();
        }
        transOut.play();
        detailsPane.setOpacity(0);
    }
    
   
}
