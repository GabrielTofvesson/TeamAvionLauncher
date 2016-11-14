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

import Launcher.net.Updater;
import com.tofvesson.reflection.SafeReflection;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import com.tofvesson.async.*;
import javafx.util.Duration;

public class Main extends Application {

    // Semantic versioning system data
    public static final String  semVerDevState  = "PreDev";                                                             // Development stage
    public static final int     semVerMajor     = 0;                                                                    // Major version
    public static final int     semVerMinor     = 2;                                                                    // Minor version
    public static final int     semVerPatch     = 2;                                                                    // Patch version

    private double xOffset = 0, yOffset = 0;                                                                            // Offsets for dragging
    private static String[] args;
    private Button exit, min, Home_btn, Modpack_btn, Settings_btn, Instance_btn;                                        // Define buttons
    private ImageView icon;
    private TextField Search_modpacks;
    private Image appIcon;
    private Rectangle dragBar;                                                                                          // Draggable top bar
    private Pane root, tab;
    private Node activeTab, settings_activeTab;
    Async stringUpdater;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.initStyle(StageStyle.UNDECORATED);

        if(args.length<2 || !args[1].equals("false")){
            Stage d = new Stage();
            Timeline t = new Timeline();
            t.getKeyFrames().add(new KeyFrame(Duration.millis(1), event ->{ d.close(); primaryStage.show(); }));
            d.initStyle(StageStyle.UNDECORATED);
            Pane n = (Pane) Tabs.load("dialog_update");
            d.setScene(new Scene(n));
            d.show();
            Thread t1 = new Thread(()->{
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                Updater.getInstance(t);
            });
            t1.setDaemon(true);
            t1.start();
        } else primaryStage.show();                                                               // Remove ugly trash

        root = (Pane) Tabs.load("main");                                                                                // Load via layout loader
        ((Label)root.lookup("#version")).setText(((Label) root.lookup("#version"))                                      // Dynamically set version label
                .getText().replace("$v", semVerDevState+"-"+semVerMajor+"."+semVerMinor+"."+semVerPatch));              // Use variables to define version
        primaryStage.setTitle("Team-Avion Launcher [WIP]");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.getIcons().clear();
        primaryStage.getIcons().add(appIcon = new Image(getClass().getResourceAsStream("/assets/icons/app.png")));

        // Field initialization
        exit = (Button) root.lookup("#exit");
        min = (Button) root.lookup("#min");

        dragBar = (Rectangle) root.lookup("#rectangle");

        Home_btn = (Button) root.lookup("#Home-btn");
        Modpack_btn = (Button) root.lookup("#Modpacks-btn");
        Settings_btn = (Button) root.lookup("#Settings-btn");
        Instance_btn = (Button) root.lookup("#Instance-btn");

        tab = (Pane) root.lookup("#tab");

        icon = (ImageView) root.lookup("#icon");

        Search_modpacks = (TextField) root.lookup("#search-modpacks");

        // Infrastructural navigation
        exit.setOnMouseClicked(event -> primaryStage.close());                                                          // Closes the program if exit button is clicked
        min.setOnMouseClicked(event -> primaryStage.setIconified(true));                                                // Minimizes the program if minimize button is clicked

        Home_btn.setOnMouseClicked(event ->{
            if(!activeTab.equals(Home_btn)){
                updateTabSelection(Home_btn, TabType.MAIN);
                Tabs.switchTab("home", tab);
            }
        });                                                                                                             // Sets the active tab to the home tab unless it's already active

        Modpack_btn.setOnMouseClicked(event ->{
            if(!activeTab.equals(Modpack_btn)){
                updateTabSelection(Modpack_btn, TabType.MAIN);
                Tabs.switchTab("modpacks", tab);
                if(stringUpdater!=null && stringUpdater.isAlive()) stringUpdater.cancel();
                stringUpdater = new Async(SafeReflection.getFirstMethod(Main.class, "detectStringUpdate"), Tabs.load("modpacks").lookup("#search-modpacks"));
            }
        });

        Instance_btn.setOnMouseClicked(event -> {
            if(!activeTab.equals(Instance_btn)){
                updateTabSelection(Instance_btn, TabType.MAIN);
                Tabs.switchTab("instance", tab);
                Tabs.load("instance").lookup("#Launch-VM").setOnMouseClicked(event1 -> {

                });
            }
        });

        Settings_btn.setOnMouseClicked(event ->{
            if(!activeTab.equals(Settings_btn)){
                updateTabSelection(Settings_btn, TabType.MAIN);
                Node n = Tabs.switchTab("settings", tab);                                                               // Sets the active tab to the settings tab unless it's already active

                if(settings_activeTab==null) settings_activeTab = n.lookup("#Settings-Gen-btn");                        // First time stuff

                n.lookup("#Settings-Gen-btn").setOnMouseClicked(event1 -> {
                    // Generic Settings Sub-tab
                    if(!settings_activeTab.getId().equals(n.lookup("#Settings-Gen-btn").getId())){                      // Use id to identify layouts
                        updateTabSelection(n.lookup("#Settings-Gen-btn"), TabType.SETTINGS);
                        Node genericLayout = Tabs.switchTab("settings_generic", (Pane) n.lookup("#Settings-Pane"));


                    }
                });

                n.lookup("#Settings-Mine-btn").setOnMouseClicked(event1 -> {
                    // Minecraft Settings Sub-tab
                    if(!settings_activeTab.getId().equals(n.lookup("#Settings-Mine-btn").getId())){                     // Use id to identify layouts
                        updateTabSelection(n.lookup("#Settings-Mine-btn"), TabType.SETTINGS);
                        Node minecraftLayout = Tabs.switchTab("settings_minecraft", (Pane) n.lookup("#Settings-Pane"));

                    }
                });

                Tabs.switchTab(settings_activeTab.getId().equals("Settings-Gen-btn") ? "settings_generic" : "settings_minecraft", (Pane) n.lookup("#Settings-Pane"));
            }
        });

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
        activeTab = Home_btn;                                                                                           // Update selected tab
        Tabs.switchTab("home", tab);
        icon.setImage(appIcon);
    }

    public static void main(String[] args) throws Exception{
        Main.args = args;
        if (args.length > 0) {
            File f = new File(args[0]);
            if (f.isFile()) f.delete();                                                                                 // Delete previous jar
        }
        launch(args);
    }

    /**
     * Search for packs with an 80% match compared to detected string.
     * @param toRead TextField to read from.
     */
    public static void detectStringUpdate(TextField toRead){
        String s = "";
        while(true) if(!s.equals(toRead.getText())) System.out.println(s = toRead.getText());

    }

    void updateTabSelection(Node newTab, TabType t){
        Node n = t==TabType.MAIN?activeTab:settings_activeTab;
        n.getStyleClass().remove("selected");
        n.getStyleClass().add("tab");
        if(t==TabType.MAIN) activeTab = newTab;
        else settings_activeTab = newTab;
        newTab.getStyleClass().remove("tab");
        newTab.getStyleClass().add("selected");
    }

    enum TabType{
        SETTINGS, MAIN
    }
}
