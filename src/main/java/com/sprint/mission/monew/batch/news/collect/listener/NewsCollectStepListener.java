package com.sprint.mission.monew.batch.news.collect.listener;

import com.sprint.mission.monew.batch.news.collect.metrics.NewsCollectMetrics;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCollectStepListener implements StepExecutionListener {

  private final NewsCollectMetrics newsCollectMetrics;

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {

    Duration duration = Duration.between(
        Objects.requireNonNull(stepExecution.getStartTime()),
        Objects.requireNonNull(stepExecution.getEndTime())
    );

    newsCollectMetrics.recordCollectDuration(duration);

    log.info("News Collect Step 완료 | duration={}", duration);

    return stepExecution.getExitStatus();
  }

}
