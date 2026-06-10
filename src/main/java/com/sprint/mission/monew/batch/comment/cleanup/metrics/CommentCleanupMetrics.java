package com.sprint.mission.monew.batch.comment.cleanup.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class CommentCleanupMetrics {

  private static final String DELETED = "monew.comment.deleted";
  private static final String LAST_SUCCESS = "monew.comment.cleanup.last_success.timestamp";

  private final Counter deletedCounter;
  private final AtomicLong lastSuccessEpochSeconds = new AtomicLong(0);

  public CommentCleanupMetrics(MeterRegistry registry) {
    this.deletedCounter = Counter.builder(DELETED)
        .description("만료되어 물리 삭제된 댓글 수")
        .register(registry);
    Gauge.builder(LAST_SUCCESS, lastSuccessEpochSeconds, AtomicLong::get)
        .baseUnit("seconds")
        .description("댓글 물리 삭제 배치가 마지막으로 정상 완료된 시각(epoch seconds)")
        .register(registry);
  }

  public void markSuccess() {
    lastSuccessEpochSeconds.set(Instant.now().getEpochSecond());
  }

  public void countDeleted(long count) {
    deletedCounter.increment(count);
  }
}
