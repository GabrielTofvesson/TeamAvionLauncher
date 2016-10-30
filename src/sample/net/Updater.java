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

    public void download(){
        //TODO: Download lots of
    }

    public void downloadMore(){
        //TODO: Download more
    }

    public void downloadEvenMore(){
        //TODO: Download even more
    }

    public static Updater getInstance(String url) throws IOException {
        if(instance==null) instance = new Updater(url);
        return instance;
    }
}
