package Launcher;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0, yOffset = 0;                                                                            // Offsets for dragging
    private Button exit, min;                                                                                           // Define buttons
    private Rectangle dragBar;                                                                                          // Draggable top bar

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("Main_Launcher.fxml"));
        primaryStage.setTitle("Team-Avion Launcher [WIP]");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();


        // Field initialization
        exit = (Button) root.lookup("#exit");
        min = (Button) root.lookup("#min");
        dragBar = (Rectangle) root.lookup("#rectangle");



        // Infrastructural navigation
        exit.setOnMouseClicked(event -> primaryStage.close());
        min.setOnMouseClicked(event -> primaryStage.setIconified(true));


        // Drag
        dragBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        dragBar.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
