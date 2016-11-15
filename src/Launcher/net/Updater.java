package Launcher.net;

import Launcher.Main;
import Launcher.Tabs;
import com.tofvesson.async.Async;
import com.tofvesson.reflection.SafeReflection;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static Launcher.Main.semVerMajor;
import static Launcher.Main.semVerMinor;
import static Launcher.Main.semVerPatch;

/**
 * Simple thing for updating launcher
 */
public class Updater {

    private static volatile Updater instance;
    private static Async<Updater> setup;
    public static final Pattern version = Pattern.compile("(?s)<span class=\"css-truncate-target\">.*?(\\d)\\.(\\d)\\.(\\d)</span>.*?<a href=\"/GabrielTofvesson/TeamAvionLauncher/releases/download/(.*?)\\.jar\" rel=\"nofollow\">"); // Pattern to match when finding refs
    private HttpsURLConnection conn;
    public static final URL updateURL;

    static {
        URL u = null;
        try { u = new URL("https://github.com/GabrielTofvesson/TeamAvionLauncher/releases"); } catch (MalformedURLException e) { e.printStackTrace(); }
        updateURL = u;
    }

    private Updater(Timeline t){
        try {
            conn = (HttpsURLConnection) updateURL.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            if(conn.getResponseCode()!=200){
                t.play();
                return;                                                                                                 // Can't get update site
            }
            conn.connect();


            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) response.append(inputLine);
            in.close();

            Matcher m = version.matcher(response.toString());
            String downloadLink = "";
            int semMajor = semVerMajor, semMinor = semVerMinor, semPatch = semVerPatch;
            while(m.find()){
                int     semMaj = Integer.parseInt(m.group(1)),
                        semMin = Integer.parseInt(m.group(2)),
                        semPat = Integer.parseInt(m.group(3));
                if(semMaj < semMajor || (semMaj==semMajor && semMin<semMinor) ||
                        (semMaj==semMajor && semMin==semMinor && semPat<=semPatch)) continue;                           // Version found isn't new
                downloadLink = "https://github.com/GabrielTofvesson/TeamAvionLauncher/releases/download/"+m.group(4)+".jar";
                semMajor = semMaj;
                semMinor = semMin;
                semPatch = semPat;
            }
            if(downloadLink.equals("")){
                t.play();
                return;
            }
            File f = new File("TAL-"+semMajor+"_"+semMinor+"_"+semPatch+".jar"), f1;
            if((f1=new File(Main.class.getResource("/assets/").getFile())).getParent().contains("!") &&
                    f1.getParent().contains("file:"))                                                                   // Find .jar representation of this program
                f1=new File(f1.getParent().substring(f1.getParent().indexOf("file:")+5, f1.getParent().length()-1));    // Prepare for deletion
            if(f.isFile()) f.renameTo(new File("-"+f.getName()));
            f.createNewFile();
            OutputStream o = new FileOutputStream(f);
            HttpsURLConnection dl = (HttpsURLConnection) new URL(downloadLink).openConnection();                        // Downloader
            dl.setDoOutput(true);
            dl.setDoInput(true);
            dl.setRequestMethod("GET");
            dl.setRequestProperty("User-Agent", "Mozilla/5.0");
            if(dl.getResponseCode()!=200){
                t.play();
                return;
            }
            dl.connect();
            InputStream reader = dl.getInputStream();
            int i;
            byte[] buffer = new byte[256];
            while((i=reader.read(buffer))!=-1) o.write(buffer, 0, i);
            reader.close();
            o.close();
            System.out.println("Starting!");
            Runtime.getRuntime().exec("java -jar "+f.getName()+" "+f1.getAbsolutePath()+" "+false);
            System.exit(0);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("No internet connection available!");
            t.play();
        }
    }
    public static void checkUpdate(Timeline t){ setup = new Async<>(SafeReflection.getFirstConstructor(Updater.class), t); }

    public static Updater getInstance(Timeline t) {
        if(setup==null) checkUpdate(t);
        return instance==null?instance=setup.await():instance;                                                          // Await async creation
    }
}
