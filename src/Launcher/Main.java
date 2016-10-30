package Launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    volatile double[] posOrigin = {0, 0};

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Team-Avion Launcher [WIP]");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();

        System.out.println(getDefaultBrowser());

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.load("google.com");
        System.out.println(webEngine.getDocument());

        Runtime.getRuntime().exec(getDefaultBrowser()+" youtube.com");




        root.lookup("#exit").setOnMouseClicked(event -> primaryStage.close());

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

    public static String getDefaultBrowser()
    {
        try
        {
            // Get registry where we find the default browser
            Process process = Runtime.getRuntime().exec("REG QUERY HKEY_CLASSES_ROOT\\http\\shell\\open\\command");
            Scanner kb = new Scanner(process.getInputStream());
            while (kb.hasNextLine())
            {

                // Get output from the terminal, and replace all '\' with '/' (makes regex a bit more manageable)
                String registry = (kb.nextLine()).replaceAll("\\\\", "/").trim();

                // Extract the default browser
                Matcher matcher = Pattern.compile("\"(.*)\"").matcher(registry);
                if (matcher.find())
                {
                    // Scanner is no longer needed if match is found, so close it
                    kb.close();
                    String defaultBrowser = matcher.group(1);

                    // Capitalize first letter and return String
                    defaultBrowser = defaultBrowser.substring(0, 1).toUpperCase() + defaultBrowser.substring(1, defaultBrowser.length());
                    return defaultBrowser;
                }
            }
            // Match wasn't found, still need to close Scanner
            kb.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        // Have to return something if everything fails
        return "Error: Unable to get default browser";
    }
}
