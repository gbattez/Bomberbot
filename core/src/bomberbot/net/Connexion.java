package bomberbot.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * A complete Java class that shows how to open a URL, then read data (text) from that URL,
 * HttpURLConnection class (in combination with an InputStreamReader and BufferedReader).
 *
 * @author alvin alexander, devdaily.com.
 *
 */
public class Connexion
{
    public static void main(String[] args) throws Exception

    {
        Connexion test = new Connexion();
        System.out.print(test.doHttpUrlConnectionAction("Ashmore", "e10adc3949ba59abbe56e057f20f883e"));
    }

    public Connexion()
    {

    }

    /**
     * Returns the output from the given URL.
     *
     * I tried to hide some of the ugliness of the exception-handling
     * in this method, and just return a high level Exception from here.
     * Modify this behavior as desired.
     *
     * @param
     * @return stringBuilder
     * @throws Exception
     */

    public static String doHttpUrlConnectionAction()throws Exception {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try {
            // create the HttpURLConnection
            url = new URL("http://www.bomberbot.comxa.com/rest.php?highscores=true");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            connection.setRequestMethod("GET");

            // uncomment this if you want to write output to this url
            //connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null){
                try{
                    reader.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }


    /**
     * Returns the output from the given URL.
     *
     * I tried to hide some of the ugliness of the exception-handling
     * in this method, and just return a high level Exception from here.
     * Modify this behavior as desired.
     *
     * @param pseudo Une chaîne de caractère du pseudo de l'utilisateur en provenance du launcher.
     * @param pwd Une chaîne de caractère converti en MD5 en provenance du Launcher
     * @return stringBuilder
     * @throws Exception
     */
    public static String doHttpUrlConnectionAction(String pseudo, String pwd) throws Exception {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        try{

            // create the HttpURLConnection
            url = new URL("http://www.bomberbot.comxa.com/rest.php?pseudo="+pseudo+"&password="+pwd);
            //url = new URL("http://www.sfr.fr");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            connection.setRequestMethod("GET");

            // uncomment this if you want to write output to this url
            //connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(150*1000);
            connection.connect();
            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
        catch (Exception e){

            e.printStackTrace();
            throw e;
        }
        finally{

            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null){
                try{
                    reader.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns the output from the given URL.
     *
     * I tried to hide some of the ugliness of the exception-handling
     * in this method, and just return a high level Exception from here.
     * Modify this behavior as desired.
     *
     * @param score Score fait par le joueur envoyé pour voir si il faut le mettre à jour dans la BDD
     * @param pseudo Pseudo de l'utilisateur connecté pour mieux cibler la requête.
     * @return stringBuilder
     * @throws Exception
     */
    public static String doHttpUrlConnectionAction(Integer score, String pseudo) throws Exception
    {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try {
            // create the HttpURLConnection
            url = new URL("http://www.bomberbot.comxa.com/rest.php?pseudo="+pseudo+"&highscores="+String.valueOf(score));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // just want to do an HTTP GET here
            connection.setRequestMethod("GET");

            // uncomment this if you want to write output to this url
            //connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null){
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}