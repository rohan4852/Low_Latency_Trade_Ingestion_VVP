import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DisruptorIngestion {

    public static class TradeEvent {
        private String trade;

        public String getTrade() {
            return trade;
        }

        public void setTrade(String trade) {
            this.trade = trade;
        }
    }

    public static class TradeEventFactory implements EventFactory<TradeEvent> {
        @Override
        public TradeEvent newInstance() {
            return new TradeEvent();
        }
    }

    public static class TradeEventHandler implements EventHandler<TradeEvent> {
        @Override
        public void onEvent(TradeEvent event, long sequence, boolean endOfBatch) {
            // Process trade event (simulated)
            String trade = event.getTrade();
            // No-op for now
        }
    }

    private final Disruptor<TradeEvent> disruptor;
    private final RingBuffer<TradeEvent> ringBuffer;

    public DisruptorIngestion(int bufferSize) {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        disruptor = new Disruptor<>(new TradeEventFactory(), bufferSize, threadFactory, ProducerType.SINGLE,
                new com.lmax.disruptor.BlockingWaitStrategy());
        disruptor.handleEventsWith(new TradeEventHandler());
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
    }

    public void produce(String trade) {
        long sequence = ringBuffer.next();
        try {
            TradeEvent event = ringBuffer.get(sequence);
            event.setTrade(trade);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public void shutdown() {
        disruptor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        DisruptorIngestion ingestion = new DisruptorIngestion(1024);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                ingestion.produce("Trade-" + i);
            }
        });

        // Consumer is handled by the event handler internally

        long startTime = System.nanoTime();
        producer.start();
        producer.join();
        ingestion.shutdown();
        long endTime = System.nanoTime();

        System.out.println("Disruptor ingestion simulation completed in " + (endTime - startTime) / 1_000_000 + " ms");
    }
}
