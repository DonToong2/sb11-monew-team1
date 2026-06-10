package com.sprint.mission.monew.batch.user.cleanup.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * 사용자 커스텀 메트릭을 집계한다. (만료 사용자 물리 삭제 건수)
 */
@Component
public class UserCleanupMetrics {

  private static final String DELETED = "monew.user.deleted";
  private static final String LAST_SUCCESS = "monew.user.cleanup.last_success.timestamp";

  private final Counter deletedCounter;
  private final AtomicLong lastSuccessEpochSeconds = new AtomicLong(0);

  public UserCleanupMetrics(MeterRegistry registry) {
    this.deletedCounter = Counter.builder(DELETED)
        .description("만료되어 물리 삭제된 사용자 수")
        .register(registry);
    Gauge.builder(LAST_SUCCESS, lastSuccessEpochSeconds, AtomicLong::get)
        .baseUnit("seconds")
        .description("사용자 물리 삭제 배치가 마지막으로 정상 완료된 시각(epoch seconds)")
        .register(registry);
  }

  public void markSuccess() {
    lastSuccessEpochSeconds.set(Instant.now().getEpochSecond());
  }

  public void countDeleted(long count) {
    deletedCounter.increment(count);
  }
}
