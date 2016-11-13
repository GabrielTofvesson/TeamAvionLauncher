package Launcher.net;

import com.tofvesson.async.Async;
import com.tofvesson.reflection.SafeReflection;
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
    private static final Async<Updater> setup = new Async<>(SafeReflection.getFirstConstructor(Updater.class));
    public static final Pattern version = Pattern.compile("(?s)<span class=\"css-truncate-target\">.*(\\d).(\\d).(\\d)</span>.*<a href=\"/GabrielTofvesson/TeamAvionLauncher/releases/download/(.*)\\.jar\" rel=\"nofollow\">"); // Pattern to match when finding refs
    private HttpsURLConnection conn;
    public static final URL updateURL;

    static {
        URL u = null;
        try { u = new URL("https://github.com/GabrielTofvesson/TeamAvionLauncher/releases"); } catch (MalformedURLException e) { e.printStackTrace(); }
        updateURL = u;
    }

    private Updater(){
        try {
            conn = (HttpsURLConnection) updateURL.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            if(conn.getResponseCode()!=200) return;                                                                     // Can't get update site
            conn.connect();


            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Matcher m = version.matcher(response.toString());
            String downloadLink = "";
            while(m.find()){
                int     semMaj = Integer.parseInt(m.group(1)),
                        semMin = Integer.parseInt(m.group(2)),
                        semPat = Integer.parseInt(m.group(3));
                if(semMaj < semVerMajor || (semMaj==semVerMajor && semMin<semVerMinor) ||
                        (semMaj==semVerMajor && semMin==semVerMinor && semPat<=semVerPatch)) continue;                  // Version found isn't new
                downloadLink = "https://github.com/GabrielTofvesson/TeamAvionLauncher/releases/download/"+m.group(4)+".jar";
            }
            if(downloadLink.equals("")) return;
            File f = new File("Tal1.jar");
            f.createNewFile();
            OutputStream o = new FileOutputStream(f);
            HttpsURLConnection dl = (HttpsURLConnection) new URL(downloadLink).openConnection();                        // Downloader
            dl.setDoOutput(true);
            dl.setDoInput(true);
            dl.setRequestMethod("GET");
            dl.setRequestProperty("User-Agent", "Mozilla/5.0");
            if(dl.getResponseCode()!=200) return;
            dl.connect();
            InputStream reader = dl.getInputStream();
            int i;
            byte[] buffer = new byte[256];
            while((i=reader.read(buffer))!=-1) o.write(buffer, 0, i);
            reader.close();
            o.close();
            Runtime.getRuntime().exec("java -jar Tal1.jar");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No internet connection available!");
        }
    }

    public static Updater getInstance() {
        return instance==null?instance=setup.await():instance;                                                          // Await async creation
    }
}
