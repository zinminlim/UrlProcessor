package home.zin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPInputStream;

public class UrlProcessor {

    private int NUM_CONSUMERS = 200;
    private BlockingQueue<String> queue = null;
    private ExecutorService threadPoolExecutor = null;
    ScheduledExecutorService scheduledExecutorService = null;

    AtomicLong successCount = new AtomicLong();
    AtomicLong errorCount = new AtomicLong();

    public void initThreadPools(){
        queue = new ArrayBlockingQueue<String>(50);
        threadPoolExecutor = Executors.newFixedThreadPool(NUM_CONSUMERS);
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleWithFixedDelay(new StatusPrinter(this), 3, 3, TimeUnit.SECONDS);
        for (int i=0; i < NUM_CONSUMERS; i++){
            threadPoolExecutor.submit(new UrlConsumer(queue, this));
        }
    }


    public void shutdownPools(){
        threadPoolExecutor.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPoolExecutor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                    System.exit(1);
                }

            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            System.err.println("Pool did not terminate in exception");
            threadPoolExecutor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

        scheduledExecutorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate 1");
                    System.exit(1);
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            System.err.println("Pool did not terminate in exception 1");
            scheduledExecutorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }


    public void processUrls(){

        long startTime = System.nanoTime();
        String sourcePath = "/Users/zlim/training/TakeHome/URLFileProcessorAssignment/inputData";

        long numUrls = 0;

        try {
            File folder = new File(sourcePath);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                   numUrls = numUrls + processEachFile(file);
                }
            }
            queue.add("POISON");
        }catch (Exception e){
            //
        }



        System.out.println("Number of URLs:" + numUrls);
       // System.out.println("Final Num Success:"+successCount + " Num Failure:"+errorCount);
        System.out.println("Total Time:" + (System.nanoTime() - startTime)/1000000 + " ms");
    }

    public static void main (String[] args){
        UrlProcessor urlProcessor = new UrlProcessor();
        urlProcessor.initThreadPools();
        urlProcessor.processUrls();
    }

    private long processEachFile(File file) {
        BufferedReader br = null;
        long count = 0;
        try{
            br = new BufferedReader(
                    new InputStreamReader(
                            new GZIPInputStream(new FileInputStream(file)), "UTF8"));

            String str;
            while ((str = br.readLine()) != null) {
               // System.out.println(str);
                queue.put(str);
                count++;
            }

        }catch (Exception e){
            e.printStackTrace(System.out);
        }finally {
            try {
                br.close();
            }catch (Exception e){
                //
            }
        }
        return count;
    }
}
