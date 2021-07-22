package com.gshs.servertools.about;

import java.net.URL;
import java.util.ResourceBundle;

import com.gshs.servertools.RootController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

public class AboutController implements Initializable {

//    @FXML
//    private Circle btnCloseAbout;
    @FXML
    private AnchorPane pnlAbout;
    @FXML
    private void handleMouseEvent(MouseEvent event) {

//        if(event.getSource() == btnCloseAbout) {
//        	
//            //new animatefx.animation.FadeOut(pnlAbout).play();
//            Stage stage = (Stage) btnCloseAbout.getScene().getWindow();
//            stage.close();
//            RootController.setAbout(false);
//        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    }
}