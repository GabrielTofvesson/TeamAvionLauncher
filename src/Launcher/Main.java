/*
NOTE TO ANY READERS:
Check out "Rogue - Atlantic". It's a pretty sweet song though I must say that Flo Rida has some pretty good songs too.
Either way, I'd recommend some music if you're considering reading through this hell. Honestly, I feel like even my
not-so-messy code is extremely messy just because of how I work. I mean, I try to make the code readable but people
always tell me that it's virtually unreadable and it doesn't help that it's difficult to explain to them what the code
does without them losing interest. Also, in case you are actually, seriously going to read this crap, do yourself a
favour and pour yourself some nice Jack Daniels. You deserve it if you're going to read through this.
 */

package Launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;

public class Main extends Application {

    public static final URL mainLauncher = Main.class.getResource("../assets/layout/main.fxml");                        // Launcher body

    private double xOffset = 0, yOffset = 0;                                                                            // Offsets for dragging
    private Button exit, min, Home_btn, Modpack_btn, Settings_btn, Search_modpacks_btn;                                 // Define buttons
    private ImageView icon;
    private TextField Search_modpacks;
    private Image appIcon;
    private Rectangle dragBar;                                                                                          // Draggable top bar
    private Pane root, tab;
    private Tabs activeTab = Tabs.Home;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.initStyle(StageStyle.UNDECORATED);                                                                 // Remove ugly trash

        root = FXMLLoader.load(mainLauncher);
        primaryStage.setTitle("Team-Avion Launcher [WIP]");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
        primaryStage.getIcons().clear();
        primaryStage.getIcons().add(appIcon = new Image(getClass().getResourceAsStream("../assets/icons/app.png")));

        // Field initialization
        exit = (Button) root.lookup("#exit");
        min = (Button) root.lookup("#min");
        dragBar = (Rectangle) root.lookup("#rectangle");
        Home_btn = (Button) root.lookup("#Home-btn");
        Modpack_btn = (Button) root.lookup("#Modpacks-btn");
        Settings_btn = (Button) root.lookup("#Settings-btn");
        tab = (Pane) root.lookup("#tab");
        icon = (ImageView) root.lookup("#icon");
        Search_modpacks_btn = (Button) root.lookup("#search-modpacks-btn");
        Search_modpacks = (TextField) root.lookup("#search-modpacks");

        // Infrastructural navigation
        exit.setOnMouseClicked(event -> primaryStage.close());                                                          // Closes the program if exit button is clicked
        min.setOnMouseClicked(event -> primaryStage.setIconified(true));                                                // Minimizes the program if minimize button is clicked
        Home_btn.setOnMouseClicked(event -> {if(activeTab!=Tabs.Home)(activeTab=Tabs.Home).switchTab(tab);});           // Sets the active tab to the home tab unless it's already active
        Modpack_btn.setOnMouseClicked(event -> {if(activeTab!=Tabs.Modpacks)(activeTab=Tabs.Modpacks).switchTab(tab);});// Sets the active tab to the modpacks tab unless it's already active
        Settings_btn.setOnMouseClicked(event -> {if(activeTab!=Tabs.Settings)(activeTab=Tabs.Settings).switchTab(tab);});

        // Drag
        dragBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        dragBar.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        // Set up default layout
        Tabs.Home.switchTab(tab);
        icon.setImage(appIcon);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
