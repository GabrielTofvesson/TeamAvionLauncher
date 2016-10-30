package Launcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;

public enum Tabs {

    Modpacks(Main.class.getResource("../assets/layout/modpacks.fxml")), Home(Main.class.getResource("../assets/layout/home.fxml")), Settings(Main.class.getResource("../assets/layout/settings.fxml"));

    public final URL url;
    private Parent loaded;
    Tabs(URL url){
        this.url = url;
    }

    public void switchTab(Pane holder){
        holder.getChildren().clear();
        try {
            holder.getChildren().add(loaded==null?loaded=FXMLLoader.load(url):loaded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
