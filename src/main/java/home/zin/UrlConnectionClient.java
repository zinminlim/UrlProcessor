package home.zin;



import java.net.HttpURLConnection;
import java.net.URL;

public class UrlConnectionClient {

    public static int processUrl(String url) {
        try {

            URL oracle = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)oracle.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            if (code != 200){
                return 500;
            }else{
                return 200;
            }
        }catch (Exception e) {
            return 500;
        }

    }



}
