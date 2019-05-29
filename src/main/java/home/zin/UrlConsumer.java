package home.zin;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;

public class UrlConsumer implements Runnable {

    private BlockingQueue<String> queue = null;
    private UrlProcessor urlProcessor = null;

    @Override
    public void run() {

        try {
            while (true) {
                String take = queue.take();
                if ("POISON".equalsIgnoreCase(take)){
                    urlProcessor.shutdownPools();
                }
                process(take);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void process(String take) throws InterruptedException {
     //   makeHttpRequestSync(take);
       // int status = JavaHttpClient.processUrl(take);
        int status = MultiThreadedHttpClient.processUrl(take);
        //int status = UrlConnectionClient.processUrl(take);
        //int status = UrlConnectionClient.processUrl(take);
        if (status == 200){
            urlProcessor.successCount.incrementAndGet();
        }else{
            urlProcessor.errorCount.incrementAndGet();
        }
    }

    public UrlConsumer(BlockingQueue<String> queue, UrlProcessor urlProcessor) {
        this.queue = queue;
        this.urlProcessor = urlProcessor;
    }



}


