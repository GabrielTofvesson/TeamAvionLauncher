/*
NOTE TO ANY READERS:
Check out "Rogue - Atlantic". It's a pretty sweet song though I must say that Flo Rida has some pretty good songs too.
Either way, I'd recommend some music if you're considering reading through this hell. Honestly, I feel like even my
not-so-messy code is extremely messy just because of how I work. I mean, I try to make the code readable but people
always tell me that it's virtually unreadable and it doesn't help that it's difficult to explain to them what the code
does without them losing interest. Also, in case you are actually, seriously going to read this crap, do yourself a
favour and pour yourself some nice Jack Daniels. You deserve it if you're going to read through this.



Do not Read Past this point... This is a human health advisory. Anyone reading past this point will risk his or her life.
If you get sick reading, we will not claim responsibility on your health. Please Stay Clear of the Code.
*/

package Launcher;

import Launcher.net.Updater;
import com.tofvesson.async.Async;
import com.tofvesson.joe.Localization;
import com.tofvesson.reflection.SafeReflection;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Do not go further. you risk your life. Read guideline above. Anyone reading past this point is no longer under our responsibility.
Beware the crocodiles on line 100!
*/

public class Main extends Application {

    // Semantic versioning system data
    public static final String  semVerDevState  = "PreDev";                                                             // Development stage
    public static final int     semVerMajor     = 0;                                                                    // Major version
    public static final int     semVerMinor     = 2;                                                                    // Minor version
    public static final int     semVerPatch     = 3;                                                                    // Patch version


    double xOffset = 0, yOffset = 0;                                                                                    // Offsets for dragging
    private static String[] args;
    Button exit, min, Home_btn, Modpack_btn, Settings_btn, Instance_btn, Default_theme, Dark_theme, Light_theme;        // Define buttons
    private ImageView icon;
    private TextField Search_modpacks;
    private Image appIcon;
    private Rectangle dragBar;                                                                                          // Draggable top bar
    Pane root, tab;
    Node activeTab, settings_activeTab;
    private Label dialog_changer;

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
        Default_theme = (Button) root.lookup("#default-theme");
        Light_theme = (Button) root.lookup("#light-theme");
        Dark_theme = (Button) root.lookup("#dark-theme");

        dialog_changer = (Label) root.lookup("#dialog-changer");

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
                Tabs.load("modpacks").lookup("#download-modpack").setOnMouseClicked(event1 -> {
                    System.out.println("Downloading Modpack");
                });
                Tabs.load("modpacks").lookup("#view-modpack").setOnMouseClicked(event1 -> {
                    System.out.println("Viewing Modpack");
                });
            }
        });

        Instance_btn.setOnMouseClicked(event -> {
            if(!activeTab.equals(Instance_btn)){
                updateTabSelection(Instance_btn, TabType.MAIN);
                Tabs.switchTab("instance", tab);
                Tabs.load("instance").lookup("#Launch-VM").setOnMouseClicked(event1 -> {
                    System.out.println("Launching Minecraft");
                });
            }
        });

        Settings_btn.setOnMouseClicked(event ->{
            if(!activeTab.equals(Settings_btn)){
                updateTabSelection(Settings_btn, TabType.MAIN);
                Node n = Tabs.switchTab("settings", tab), tmp;                                                          // Sets the active tab to the settings tab unless it's already active

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
                if((tmp=Tabs.load("settings_generic").lookup("#default-theme")).getOnMouseClicked()==null) {
                    tmp.setOnMouseClicked(event2 -> {
                        Theme.Default.switchTo(root);
                        System.out.println("Changing Theme to Default");
                    });
                    Tabs.load("settings_generic").lookup("#light-theme").setOnMouseClicked(event2 -> {
                        Theme.Light.switchTo(root);
                        System.out.println("Changing Theme to Light");
                    });
                    Tabs.load("settings_generic").lookup("#dark-theme").setOnMouseClicked(event1 -> {
                        Theme.Dark.switchTo(root);
                        System.out.println("Changing Theme to Dark");
                    });
                }
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
        Localization l = new Localization(new File(Main.class.getResource("/assets/lang").getFile()));                  // Create a localization with aggressive loading
        System.out.println(Arrays.toString(l.getLanguageNames()));
        System.out.println("Success: "+l.get("du_label"));
        Main.args = args;
        if (args.length > 0) {
            File f = new File(args[0]);
            if (f.isFile()) while(!f.delete()) Thread.sleep(50);                                                        // Delete previous jar
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

    public static List<Node> getFlatRepresentation(Parent root){
        List<Node> l = new ArrayList<>();
        l.add(root);
        for(Node n : root.getChildrenUnmodifiable()){
            if(n instanceof Parent)
                l.addAll(getFlatRepresentation((Parent)n));
            else l.add(n);
        }
        return l;
    }

    public void processStyleData(Node n){

    }

    enum TabType{
        SETTINGS, MAIN
    }

    enum Theme{
        Default(""), Dark(Main.class.getResource("/assets/style/dark-theme.css").toExternalForm()), Light(Main.class.getResource("/assets/style/light-theme.css").toExternalForm());

        public final String style;
        Theme(String style){ this.style = style; }

        public void switchTo(Pane root){
            ObservableList<String> l = root.getStylesheets();
            if(l.contains(Light.style)) l.remove(Light.style);
            if(l.contains(Dark.style)) l.remove(Dark.style);
            if(this!=Default) l.add(style);
        }
    }
}
