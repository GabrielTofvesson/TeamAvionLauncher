package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNIFIED);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
        primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                primaryStage.setMaximized(false);
        });
        root.lookup("#exit").setOnMouseClicked(event -> primaryStage.close());                                          // Close program if button is clicked
    }


    public static void main(String[] args) {
        launch(args);
    }
}
