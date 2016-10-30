package Launcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public enum Tabs {

    Modpacks(Main.class.getResource("../assets/layout/modpacks.fxml")), Home(Main.class.getResource("../assets/layout/home.fxml"));

    public final URL url;
    Tabs(URL url){
        this.url = url;
    }

    public void switchTab(Pane holder){
        holder.getChildren().clear();
        try {
            holder.getChildren().add(FXMLLoader.load(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
