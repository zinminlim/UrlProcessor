package home.zin;


import java.io.IOException;
import java.net.URI;
/*
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

 */
import java.time.Duration;

public class JavaHttpClient {
    /*
    private static HttpClient client = HttpClient.newBuilder().build();

    public static int processUrl(String url) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofMillis(5000))
                .build();


        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if(response.statusCode() != 200) {
                return 500;
            }else {
                return 200;
            }

        } catch (IOException | InterruptedException e) {
            return 500;
        }
    }*/
}
