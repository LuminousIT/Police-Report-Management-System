/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luminous.ctrl;

import com.luminous.Start;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Luminous
 */
public class Auth implements Initializable {

    private Start app;
    
    @FXML
    public AnchorPane signInPane;

    @FXML
    public AnchorPane basePane;

    @FXML
    public FlowPane welcomePane;
    
    @FXML
    public TextField policeidField;
    
    @FXML
    public PasswordField passwordField;
    
    @FXML
    private FlowPane statusPane;
    
    @FXML
    public Label statusLab;
   
    public StringProperty status = new SimpleStringProperty();
    
    TranslateTransition transIn, transOut;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        transIn = new TranslateTransition(Duration.millis(500), statusPane);
        //transIn.setFromY(-75);
        transIn.setToY(0);
        transIn.setInterpolator(Interpolator.EASE_IN);
        
        transOut = new TranslateTransition(Duration.millis(500), statusPane);
        transOut.setFromY(0);
        transOut.setToY(75);
        transOut.setInterpolator(Interpolator.EASE_OUT);
        transOut.setDelay(Duration.seconds(5));
        
//        transIn.setOnFinished(a -> transOut.play());
//        
//        status.addListener((ob, ov, nv) -> {
//            if (!Platform.isFxApplicationThread()) {
//                Platform.runLater(() -> {
//                    updateStatus(nv, transIn, transOut);
//                });
//            } else {
//                updateStatus(nv, transIn, transOut);
//            }
//        });
    }    

    public void construct(Start appth) {
        this.app = appth;

        statusPane.setOpacity(0);
//        transOut.play();
    }
    
    @FXML
    void login(ActionEvent ae) {
        if (!policeidField.getText().isEmpty()) {
            if (app.db.userExists(policeidField.getText().trim(), passwordField.getText()) ) {
                app.showHomePage("Homepage");
            }
            else {
                System.out.println("wrong username/password");
                //app.util.showMessage("wrong username/password");
                //updateStat("wrong username/password"); //it'll clash with DB. //FEAR NOT! code taking care of already from DBHandler.
            }
        } else {
             System.out.println("Please Enter USername");
             updateStat("Please Enter USername");
        }
    }
    
    public void updateStat(String stat){
        statusLab.setText(stat);
        statusPane.setOpacity(1);
        transIn.play();
        transIn.setOnFinished(a -> transOut.play());
        //transIn.setDelay(Duration.seconds(5));
        //transOut.play();
    }

//    private void updateStatus(String nv, TranslateTransition transIn, TranslateTransition transOut) {
//       if (nv.isEmpty()) {
//           return;
//       } 
//       
//       statusPane.setOpacity(0);
//       if (transIn.getStatus() == Animation.Status.RUNNING) {
//           transIn.stop();
//           transOut.play();
//       }
//       
//       if (transOut.getStatus() == Animation.Status.RUNNING) {
//           transOut.stop();
//       }
//       
//        statusLab.setText(nv);
//        statusPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
//        statusPane.setOpacity(1);
//        statusPane.toFront();
//        transIn.playFromStart();
//        status.setValue("");
//    }
//    
    
    
}
