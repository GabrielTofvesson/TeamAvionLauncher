package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    volatile double[] posOrigin = {0, 0};

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
        primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                primaryStage.setMaximized(false);
        });
        root.lookup("#exit").setOnMouseClicked(event -> primaryStage.close());                                          // Close program if button is clicked





        // Drag
        root.lookup("#rectangle").setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX()+(event.getScreenX()-primaryStage.getX()));
            primaryStage.setY(event.getScreenY()+(event.getScreenY()-primaryStage.getY()));
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
