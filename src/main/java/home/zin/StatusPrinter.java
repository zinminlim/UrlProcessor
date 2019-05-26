package home.zin;


public class StatusPrinter implements Runnable {

    private UrlProcessor urlProcessor = null;

    @Override
    public void run() {


            System.out.println("Num Success:"+urlProcessor.successCount + " Num Failure:"+urlProcessor.errorCount);

    }


    public StatusPrinter(UrlProcessor urlProcessor) {
        this.urlProcessor = urlProcessor;
    }
}

