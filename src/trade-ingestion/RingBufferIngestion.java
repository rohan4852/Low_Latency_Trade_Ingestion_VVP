import java.util.concurrent.atomic.AtomicLong;

public class RingBufferIngestion {
    private final String[] ringBuffer;
    private final int bufferSize;
    private final AtomicLong writeSequence = new AtomicLong(0);
    private final AtomicLong readSequence = new AtomicLong(0);

    public RingBufferIngestion(int size) {
        this.bufferSize = size;
        this.ringBuffer = new String[size];
    }

    public void produce(String trade) {
        long currentWrite = writeSequence.get();
        long nextWrite = (currentWrite + 1) % bufferSize;

        // Wait if buffer is full
        while (nextWrite == readSequence.get()) {
            Thread.yield();
        }

        ringBuffer[(int) currentWrite] = trade;
        writeSequence.set(nextWrite);
    }

    public String consume() {
        long currentRead = readSequence.get();

        // Wait if buffer is empty
        while (currentRead == writeSequence.get()) {
            Thread.yield();
            currentRead = readSequence.get();
        }

        String trade = ringBuffer[(int) currentRead];
        readSequence.set((currentRead + 1) % bufferSize);
        return trade;
    }

    public static void main(String[] args) throws InterruptedException {
        RingBufferIngestion ingestion = new RingBufferIngestion(1024);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                ingestion.produce("Trade-" + i);
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                String trade = ingestion.consume();
                // Process trade (simulated)
            }
        });

        long startTime = System.nanoTime();
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        long endTime = System.nanoTime();

        System.out.println(
                "Lock-Free Ring Buffer ingestion simulation completed in " + (endTime - startTime) / 1_000_000 + " ms");
    }
}
