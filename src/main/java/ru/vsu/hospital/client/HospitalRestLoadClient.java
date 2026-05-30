package ru.vsu.hospital.client;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.vsu.hospital.model.dto.BulkOperationResultDto;
import ru.vsu.hospital.model.request.BulkCreateRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class HospitalRestLoadClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String DEFAULT_ENTITY = "doctors";
    private static final long DEFAULT_COUNT = 1_000_000L;
    private static final int DEFAULT_THREADS = 1;
    private static final int DEFAULT_BATCH_SIZE = 1_000;
    private static final String DEFAULT_PREFIX = "load";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ClientConfig config = ClientConfig.fromArgs(args);
        RestClient restClient = RestClient.builder()
                .baseUrl(config.baseUrl())
                .build();

        System.out.printf("Начало загрузки: entity=%s, count=%d, threads=%d, batchSize=%d%n",
                config.entity(), config.count(), config.threads(), config.batchSize());

        Instant startedAt = Instant.now();
        long processed = runLoad(restClient, config);
        Duration duration = Duration.between(startedAt, Instant.now());

        System.out.printf("Загружено %d записей в %d мс, потоков: %d, размер батча: %d%n",
                processed, duration.toMillis(), config.threads(), config.batchSize());
    }

    private static long runLoad(RestClient restClient, ClientConfig config)
            throws InterruptedException, ExecutionException {
        AtomicLong nextIndex = new AtomicLong(0);
        AtomicLong processed = new AtomicLong(0);
        ExecutorService executor = Executors.newFixedThreadPool(config.threads());
        String uri = "/" + config.entity() + "/bulk";

        try {
            Future<?>[] futures = new Future<?>[config.threads()];

            for (int i = 0; i < config.threads(); i++) {
                futures[i] = executor.submit(() -> {
                    while (true) {
                        long start = nextIndex.getAndAdd(config.batchSize());
                        if (start >= config.count()) {
                            return;
                        }

                        int currentBatchSize = (int) Math.min(config.batchSize(), config.count() - start);
                        BulkOperationResultDto result = restClient.post()
                                .uri(uri)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BulkCreateRequest.builder()
                                        .namePrefix(config.prefix())
                                        .startIndex(start)
                                        .count(currentBatchSize)
                                        .build())
                                .retrieve()
                                .body(BulkOperationResultDto.class);

                        processed.addAndGet(Objects.requireNonNull(result).getProcessed());
                    }
                });
            }

            for (Future<?> future : futures) {
                future.get();
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        }

        return processed.get();
    }

    private record ClientConfig(
            String baseUrl,
            String entity,
            long count,
            int threads,
            int batchSize,
            String prefix
    ) {
        private static ClientConfig fromArgs(String[] args) {
            String baseUrl = DEFAULT_BASE_URL;
            String entity = DEFAULT_ENTITY;
            long count = DEFAULT_COUNT;
            int threads = DEFAULT_THREADS;
            int batchSize = DEFAULT_BATCH_SIZE;
            String prefix = DEFAULT_PREFIX;

            for (String arg : args) {
                if (arg.startsWith("--base-url=")) {
                    baseUrl = arg.substring("--base-url=".length());
                } else if (arg.startsWith("--entity=")) {
                    entity = arg.substring("--entity=".length());
                } else if (arg.startsWith("--count=")) {
                    count = Long.parseLong(arg.substring("--count=".length()));
                } else if (arg.startsWith("--threads=")) {
                    threads = Integer.parseInt(arg.substring("--threads=".length()));
                } else if (arg.startsWith("--batch-size=")) {
                    batchSize = Integer.parseInt(arg.substring("--batch-size=".length()));
                } else if (arg.startsWith("--prefix=")) {
                    prefix = arg.substring("--prefix=".length());
                }
            }

            if (count <= 0) throw new IllegalArgumentException("count must be positive");
            if (threads <= 0) throw new IllegalArgumentException("threads must be positive");
            if (batchSize <= 0) throw new IllegalArgumentException("batchSize must be positive");

            return new ClientConfig(baseUrl, entity, count, threads, batchSize, prefix);
        }
    }
}
