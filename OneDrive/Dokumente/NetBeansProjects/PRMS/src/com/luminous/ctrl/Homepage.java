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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author samth
 */
public class Homepage implements Initializable {

    private Start app;
    
    @FXML
    private AnchorPane contentPane;
    
    @FXML
    private FlowPane loadingPane;
    
    @FXML
    private AnchorPane statusPane;
    
    @FXML
    private Label statusLabel;
    
    public StringProperty status = new SimpleStringProperty();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        TranslateTransition transIn = new TranslateTransition(Duration.millis(400), statusPane);
        TranslateTransition transOut = new TranslateTransition(Duration.millis(4000), statusPane);
        
        transIn.setFromX(-600);
        transIn.setToX(0);
        transIn.setInterpolator(Interpolator.EASE_IN);
        
        transOut.setFromX(0);
        transOut.setToX(-600);
        transOut.setInterpolator(Interpolator.EASE_OUT);
        
        transOut.setDelay(Duration.seconds(3));
        transIn.setOnFinished((a) -> transOut.play());
        
        status.addListener((ob, ov, nv) -> {
            if (!Platform.isFxApplicationThread()) {
                Platform.runLater(() -> updateStatus(nv, transIn, transOut)); 
            } else {
                updateStatus(nv, transIn, transOut);
            }
        });
    }    

    public void construct(Start aThis) {
        app = aThis;
        app.util.showMessage("Welcome IG Muhammed");
    }
    
    private void updateStatus(String nv, TranslateTransition transIn, TranslateTransition transOut) {
        if (nv.isEmpty()) {
            return;
        }
        
        statusPane.setOpacity(0);
        if (transIn.getStatus() == Animation.Status.RUNNING) {
            transIn.stop();
            transOut.play();
        }
        
        if (transOut.getStatus() == Animation.Status.RUNNING) {
            transOut.stop();
        }
        
        statusLabel.setText(nv);
        statusPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        statusPane.toFront();
        statusPane.setOpacity(1);
        transIn.playFromStart();
        status.setValue("");
    }
    
    
    @FXML
    void gotoNavHandler(MouseEvent me) {
        Label lab = (Label) me.getSource();
        switch (lab.getText().toLowerCase()) {
            case "register officer":
                showRegPage();
                break;
            case "report case":
                showRCrimePage();
                break;
            case "crime history":
                showCrimeHistory();
                break;
            case "registered officers":
                showRegisteredOfficerPage();
                break;
            case "logout":
                app.stage.close();
                app.showLoginPage();
                break;
            case "contact developer":
                showContactDeveloperPage();
                break;
        }
    }

    private void showRegPage() {
        showLoading();
        Platform.runLater(() -> contentPane.getChildren().setAll(app.getAddOfficerPage()) );
//directt approach. works.
//      contentPane.getChildren().removeAll();
//      contentPane.getChildren().setAll(app.getAddOfficerPage());
    }
    
    private void showRCrimePage() {
        showLoading();
        Platform.runLater(() -> contentPane.getChildren().setAll(app.getRCrimePage()));
    }
    
    private void showLoading() {
        if (!contentPane.getChildren().contains(loadingPane)) {
            contentPane.getChildren().setAll(loadingPane);
        } else {
            loadingPane.toFront();
        }
    }

    private void showRegisteredOfficerPage() {
        showLoading();
        Platform.runLater(() -> contentPane.getChildren().setAll(app.getRegisteredOfficerPage()) );
    }

    private void showCrimeHistory() {
        Platform.runLater(() -> contentPane.getChildren().setAll(app.getCrimeHistoryPage()));
    }

    private void showContactDeveloperPage() {
        showLoading();
        Platform.runLater(() -> contentPane.getChildren().setAll(app.getContactDeveloperPage()) );
    }

    
    
}
