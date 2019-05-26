package home.zin;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;

public class UrlHttpClient {

    private static  MultiThreadedHttpConnectionManager connectionManager = null;
    private static  HttpClient client = null;

    static{
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionParams params = connectionManager.getParams();
        params.setConnectionTimeout(1000);
        params.setSoTimeout(1000);
        ((HttpConnectionManagerParams) params).setMaxTotalConnections(10);
        client = new HttpClient(connectionManager);
    }

    public static int processUrl(String url) {
        GetMethod get = new GetMethod(url);
        try {
           return client.executeMethod(get);

            // print response to stdout
        }catch(Exception e){
            return 500;

        } finally {
            // be sure the connection is released back to the connection
            // manager
            get.releaseConnection();
        }
    }
}
