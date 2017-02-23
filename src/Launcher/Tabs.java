package Launcher;

import com.tofvesson.collections.ShiftingList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Tabs {

    private static final ShiftingList<Pair<URL, Node>> loaded = new ShiftingList<>(35);                                 // Memory-efficient list of loaded files


    /**
     * Loads layout from file in layout assets folder. If layout is already loaded,
     * Tabs won't bother with reloading and will instead return the cached version.
     * @param fileName Name of fxml file to load. (Must be located in /assets/layout/ folder or name must define name subfolder in layouts folder)
     * @return Generified object referring to loaded resource.
     */
    public static Node load(String fileName){
        if(!fileName.endsWith(".fxml")) fileName+=".fxml";
        URL file = Main.class.getResource("/assets/layout/"+fileName);
        try {
            final boolean[] b = {false};
            loaded.stream().filter(p ->p!=null && p.getKey().equals(file)).forEach(p->b[0]=true);
            if(!b[0]) loaded.add(new Pair<>(file, FXMLLoader.load(file)));                                              // Load file if it isn't already loaded
            final Node[] p1 = new Node[]{new Pane()};
            loaded.stream().filter(p->p!=null && p.getKey().equals(file)).forEach(p->p1[0]=p.getValue());
            return p1[0];
        } catch (IOException e) {
            e.printStackTrace();
            return new Pane();                                                                                          // Returns empty layout if all else fails
        }
    }

    /**
     * Switches the currently loaded tab in holder pane. Removes current children from holder and adds new tab instead.
     * If holder already contains layout, method simply returns the loaded resource.
     * @param newTabName Name of file containing the new tab data.
     * @param holder Pane where tab should be loaded to.
     * @return Generified object referring to loaded resource.
     */
    public static Node switchTab(String newTabName, Pane holder){
        Node n = load(newTabName);
        if(!holder.getChildren().contains(n)) {
            holder.getChildren().clear();
            holder.getChildren().add(n);
        }
        return n;
    }

    /**
     * Forces unloading of resource to free up resources and/or clear data.
     * @param fileName Name of resource to unload.
     */
    public static void unloadTab(String fileName){
        if(!fileName.endsWith(".fxml")) fileName+=".fxml";
        URL file = Main.class.getResource("/assets/layout/"+fileName);
        loaded.stream().filter(p->p!=null && p.getKey().equals(file)).forEach(loaded::remove);
    }

    /**
     * Forces unloading and the subsequent loading of a layout resource.
     * @param fileName Name of resource to reload.
     * @return Newly loaded layout.
     */
    public static Node reloadTab(String fileName){
        unloadTab(fileName);
        return load(fileName);
    }
    public static List<String> listShit(Class from, String path) throws IOException{
        final File jarFile = new File(from.getProtectionDomain().getCodeSource().getLocation().getPath());
        List<String> l = new ArrayList<>();
        if(jarFile.isFile()) {  // Run with JAR file
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while(entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(path + "/"))  //filter according to the path
                    l.add(name);
            }
            jar.close();
        } else { // Run with IDE
            final URL url = from.getResource("/" + path);
            if (url != null)
                try {
                    final File apps = new File(url.toURI());
                    for (File app : apps.listFiles())
                        l.add(app.getName());
                } catch (URISyntaxException ex) {
                    // never happens
                }
        }
        return l;
    }
}
