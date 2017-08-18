/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous;

import com.luminous.ctrl.AddOfficer;
import com.luminous.ctrl.Auth;
import com.luminous.ctrl.ContactDeveloper;
import com.luminous.ctrl.Homepage;
import com.luminous.ctrl.RegisteredOfficers;
import com.luminous.ctrl.ReportCrime;
import com.luminous.domain.DBHandler;
import com.luminous.ctrl.CrimeHistory;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 *
 * @author Luminous
 */
public class Start extends Application {
    public Utilities util;
    public Auth authCtrl;
    public DBHandler db;
    public Homepage homepge;
    public Stage stg, stage;
    private AddOfficer addOff;
    private ReportCrime rcrime;
    public RegisteredOfficers regdOfficers;
    public CrimeHistory crmHistory;
    private ContactDeveloper contDev;
    
    @Override
    public void start(Stage primaryStage) {
        stg = primaryStage;
        util = new Utilities(this);
        db = new DBHandler(this);
        showLoginPage();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void showLoginPage() {
        ObservableMap<String, Object> map = util.createPage("Auth");
        AnchorPane root = (AnchorPane) map.get("view");
        authCtrl = (Auth) map.get("control");
        authCtrl.construct(this);

 
        stg.setTitle("PRMS Authentication");
        stg.setScene(new Scene(root));
        //stg.initStyle(StageStyle.UNDECORATED); //this prevents logout button from working. but works perfectly before separating block of code into a separate method. //leave as it is.
        stg.show();
        
        new Timer(true).schedule(new TimerTask(){
            @Override
            public void run() {
                Platform.runLater(
                        new Runnable() {
                            @Override
                            public void run() {
                                authCtrl.basePane.getChildren().setAll(authCtrl.signInPane);
                            }
                            
                        }
                );
            }
            
        } , 4000);
    }
    
    public void showHomePage(String pagename) {
        stg.close();
        //String page = "ctrl/"+ pagename + ".fxml";
        ObservableMap<String, Object> map = util.createPage(pagename);
        AnchorPane p = (AnchorPane) map.get("view");
        homepge = (Homepage) map.get("control");
        homepge.construct(this);
        
        Scene sce = new Scene(p);
        stage = new Stage();
        stage.setScene(sce);
        stage.show();
    }

    public Node getAddOfficerPage() {
        ObservableMap<String, Object> map = util.createPage("AddOfficer");
        FlowPane page = (FlowPane) map.get("view");
        addOff = (AddOfficer) map.get("control");
        addOff.construct(this);
        return page;
    }

    public Node getRCrimePage() {
       ObservableMap<String, Object> map = util.createPage("ReportCrime");
       ScrollPane page = (ScrollPane) map.get("view");
       rcrime = (ReportCrime) map.get("control");
       rcrime.construct(this);
       return page;
    }

    public Node getRegisteredOfficerPage() {
        ObservableMap<String, Object> map = util.createPage("RegisteredOfficers");
        AnchorPane page = (AnchorPane) map.get("view");
        regdOfficers = (RegisteredOfficers) map.get("control");
        regdOfficers.construct(this);
        return page;
    }

    public Node getCrimeHistoryPage() {
        ObservableMap<String, Object> map = util.createPage("CrimeHistory");
        AnchorPane page = (AnchorPane) map.get("view");
        crmHistory = (CrimeHistory) map.get("control");
        crmHistory.construct(this);
        return page;
    }

    public Node getContactDeveloperPage() {
        ObservableMap<String, Object> map = util.createPage("ContactDeveloper");
        FlowPane page = (FlowPane) map.get("view");
        contDev = (ContactDeveloper) map.get("control");
        contDev.construct(this);
        return page;
    }
}
