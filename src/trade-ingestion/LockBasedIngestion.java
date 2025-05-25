import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LockBasedIngestion {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public void produce(String trade) throws InterruptedException {
        queue.put(trade);
    }

    public String consume() throws InterruptedException {
        return queue.take();
    }

    public static void main(String[] args) throws InterruptedException {
        LockBasedIngestion ingestion = new LockBasedIngestion();

        // Simple simulation of producing and consuming trades
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    ingestion.produce("Trade-" + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    String trade = ingestion.consume();
                    // Process trade (simulated)
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        long startTime = System.nanoTime();
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        long endTime = System.nanoTime();

        System.out.println("Lock-Based ingestion simulation completed in " + (endTime - startTime) / 1_000_000 + " ms");
    }
}
