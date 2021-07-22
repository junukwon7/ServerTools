package com.gshs.servertools.about;
 
import java.io.IOException;

import com.gshs.servertools.RootController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class About extends Application {
    
    Stage stage=new Stage();
    private double xOffset = 0;
    private double yOffset = 0;
    public boolean isOn = false;
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        RootController.setAbout(true);
        Parent root = FXMLLoader.load(getClass().getResource("About.fxml"));

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        
        stage.setOnCloseRequest(event -> {
          RootController.setAbout(false);
        });
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("About ServerTools");
        stage.getIcons().add(new Image("file:src/files/linux_400px_trans.png"));
        stage.setResizable(false);
        stage.setScene(new Scene(root, 500, 400));
        stage.show(); 
    }
    public void bringUp()
    {
        stage.setAlwaysOnTop(true);
        stage.setAlwaysOnTop(false);
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    public void  showWindow() throws Exception {
        start(stage);
    }
}
    
