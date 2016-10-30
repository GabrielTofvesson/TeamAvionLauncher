package Launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.awt.*;
import java.net.URI;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    volatile double[] posOrigin = {0, 0};

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("Main_Launcher.fxml"));
        primaryStage.setTitle("Team-Avion Launcher [WIP]");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();

        root.lookup("#exit").setOnMouseClicked(event -> primaryStage.close());
        root.lookup("#min").setOnMouseClicked(event -> primaryStage.setIconified(true));

        // Drag
        root.lookup("#rectangle").setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.lookup("#rectangle").setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
