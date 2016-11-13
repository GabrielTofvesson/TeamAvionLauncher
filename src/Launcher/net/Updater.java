package Launcher.net;

import com.tofvesson.async.Async;
import com.tofvesson.reflection.SafeReflection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Simple thing for updating launcher
 */
public class Updater {

    private static volatile Updater instance;
    private static final Async<Updater> setup = new Async<>(SafeReflection.getFirstConstructor(Updater.class));
    private URLConnection conn;
    public static final URL updateURL;
    private boolean isUpdateAvailable = false;

    static {
        URL u = null;
        try { u = new URL("https://github.com/GabrielTofvesson/TeamAvionLauncher/releases/"); } catch (MalformedURLException e) { e.printStackTrace(); }
        updateURL = u;
    }

    private Updater(){
        try {
            conn = updateURL.openConnection();

        } catch (IOException e) {
            System.out.println("No internet connection available!");
        }
    }

    public static Updater getInstance() {
        return instance==null?instance=setup.await():instance;                                                          // Await async creation
    }
}
