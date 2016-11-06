package Launcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;

public enum Tabs {

    Modpacks(Tabs.class.getResource("../assets/layout/modpacks.fxml")),
    Home(Tabs.class.getResource("../assets/layout/home.fxml")),
    Settings(Tabs.class.getResource("../assets/layout/settings.fxml")),
    Instance(Tabs.class.getResource("../assets/layout/instance.fxml"));

    /**
     * Url referencing xml.
     */
    public final URL url;

    /**
     * Loaded layout
     */
    public final Parent loaded;

    Tabs(URL url){
        this.url = url;
        Parent p = null;
        try { p = FXMLLoader.load(url); } catch (IOException e) { e.printStackTrace(); }
        loaded = p;
    }

    public void switchTab(Pane holder){
        holder.getChildren().clear();
        holder.getChildren().add(loaded);
    }
}
