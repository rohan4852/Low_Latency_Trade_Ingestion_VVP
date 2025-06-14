# Low-Latency Trade Ingestion Prototype

### 🛠️ Author: Rohan Adat  

## 📌 Overview

This project is a performance-focused prototype simulating a real-time trade ingestion engine. It explores how concurrency patterns, JVM tuning, and lock-free design can dramatically reduce latency and maximize throughput in ingestion pipelines.

The system was built to model the kind of high-pressure ingestion logic often found in real-time financial infrastructure, where low-latency execution and predictable performance are critical.


## 🚀 Key Features

- Lock-based and Lock-free ingestion pipelines (BlockingQueue vs LMAX Disruptor)  
- JVM tuning for GC and thread pinning  
- Throughput simulation: 6M+ messages/sec  
- Benchmarking with JMH and custom metrics  
- Modular Java backend using concurrency primitives  


## 📊 Performance Metrics

| Ingestion Model     | p99 Latency | Throughput Gain |
|---------------------|-------------|------------------|
| Lock-Based Queue    | ~190μs      | Baseline         |
| Lock-Free Pipeline  | ~45μs       | +30–50%          |

- Garbage-free execution path using off-heap buffers  
- Reduced jitter and lock contention using thread affinity  


## 🧪 Benchmarking Stack

- Java 17 (JMH, Unsafe, Executors)  
- Spring Boot (for base module architecture)  
- CSV output for latency metrics  
- Flamegraphs via async-profiler (optional)


## 🧱 Project Structure

/src/main/java/com/vvp/ingestion/
├── LockBasedIngestion.java
├── LockFreeIngestion.java
├── BenchmarkRunner.java

/benchmarks/
└── latency_results.csv

/docs/
├── architecture.png
├── benchmarks.pdf


## 🖥️ Run the Benchmarks

```bash
git clone https://github.com/rohan4852/Low_Latency_Trade_Ingestion_VVP.git
cd Low_Latency_Trade_Ingestion_VVP
mvn clean package
java -jar target/benchmark.jar

 What This Demonstrates
This project demonstrates how low-level systems tuning — including lock-free queues, GC-awareness, thread pinning, and flamegraph analysis — can lead to massive latency reduction in high-frequency ingestion pipelines.

It is relevant to real-time systems where ingestion and dispatch happen under load, including financial applications, stream processors, and time-critical distributed platforms.


🙌 Acknowledgements
Built as part of a self-directed validation project for high-performance backend engineering roles. All feedback welcome.

© 2025 Rohan Adat
