import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkTest {

    private LockBasedIngestion lockBasedIngestion;
    private RingBufferIngestion ringBufferIngestion;
    private DisruptorIngestion disruptorIngestion;
    private ExecutorService executor;

    @Setup(Level.Iteration)
    public void setup() {
        lockBasedIngestion = new LockBasedIngestion();
        ringBufferIngestion = new RingBufferIngestion(1024);
        disruptorIngestion = new DisruptorIngestion(1024);
        executor = Executors.newFixedThreadPool(2);
    }

    @Benchmark
    public void benchmarkLockBased() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        executor.submit(() -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    lockBasedIngestion.produce("Trade-" + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    lockBasedIngestion.consume();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        latch.await();
    }

    @Benchmark
    public void benchmarkRingBuffer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        executor.submit(() -> {
            for (int i = 0; i < 1000; i++) {
                ringBufferIngestion.produce("Trade-" + i);
            }
            latch.countDown();
        });

        executor.submit(() -> {
            for (int i = 0; i < 1000; i++) {
                ringBufferIngestion.consume();
            }
            latch.countDown();
        });

        latch.await();
    }

    @Benchmark
    public void benchmarkDisruptor() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        executor.submit(() -> {
            for (int i = 0; i < 1000; i++) {
                disruptorIngestion.produce("Trade-" + i);
            }
            latch.countDown();
        });

        latch.await();
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
        executor.shutdown();
        disruptorIngestion.shutdown();
    }
}
