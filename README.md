
# ⚡ Low Latency Trade Ingestion – VVP Prototype for Tower Research Capital

A Value Validation Project (VVP) targeting the real-world challenge of reducing ingestion latency in high-frequency trading systems.

## 🧠 Problem

In HFT environments, trade ingestion pipelines are often bottlenecked by lock contention and cache misses. This leads to unacceptable latency spikes (p99 > 100μs).

## 💡 Solution

This VVP explores an optimized ingestion design using:
- Lock-free ring buffers (Disruptor pattern)
- Memory-mapped I/O
- JVM tuning
- CPU affinity pinning
- Low GC pressure techniques

## 📈 Benchmark Results

| Method        | p99 Latency | Throughput     |
|---------------|-------------|----------------|
| Lock-based    | 190μs       | 800K msgs/sec  |
| Lock-free     | 45μs        | 1.2M msgs/sec  |

## 📊 Live Demo Dashboard

Explore the benchmark results visually with our interactive web-based dashboard:

- **Latency Over Time**: View p50, p90, and p99 latency trends.
- **Throughput Comparison**: Compare lock-based vs lock-free throughput.

To access the dashboard, run the Spring Boot application and open [http://localhost:8080/dashboard](http://localhost:8080/dashboard) in your browser.

## 🧱 Architecture

- Java 17, Docker, JMH
- RingBuffer-based ingestion
- Custom dispatcher threads
- Affinity + low-latency GC options

## 📂 Directory Structure

```
/src
  └── trade-ingestion
       ├── RingBufferIngestion.java
       ├── BenchmarkTest.java
       └── ...
```

## 👨‍💻 Author

**Rohan Adat**  
[LinkedIn](https://linkedin.com/in/roh...adatat) | roh...@gmail.com

---

## 🚀 Interested?

This repo is part of my Value Validation Project to solve relevant system challenges for companies like Tower Research Capital. If this resonates with your team, I’d love to collaborate or go deeper into implementation.
