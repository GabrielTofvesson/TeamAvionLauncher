package sample.net;

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

    public void downloadPorn(){
        //TODO: Download lots of porn
    }

    public void downloadMorePorn(){
        //TODO: Download more porn
    }

    public void downloadEvenMorePorn(){
        //TODO: Download even more porn
    }

    public static Updater getInstance(String url) throws IOException {
        if(instance==null) instance = new Updater(url);
        return instance;
    }
}
