package sample.net;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Simple thing for updating launcher
 */
public class Updater {

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
}
