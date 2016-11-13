package Launcher.net;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Simple thing for updating launcher
 */
public class Updater {

    private static Updater instance;
    private URLConnection conn;

    private Updater(String URL) throws IOException {
        conn = new URL(URL).openConnection();
    }

    public void downloadStuff(){
        //TODO:
    }

    public void downloadMoreStuff(){

    }

    public void downloadEvenMoreStuff(){

    }

    public static Updater getInstance(String url) throws IOException {
        if(instance==null) instance = new Updater(url);
        return instance;
    }
}
