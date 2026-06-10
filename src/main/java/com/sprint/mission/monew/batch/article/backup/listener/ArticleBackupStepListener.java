package com.sprint.mission.monew.batch.article.backup.listener;

import com.sprint.mission.monew.batch.article.backup.metrics.ArticleBackupMetrics;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleBackupStepListener implements StepExecutionListener {

  private final ArticleBackupMetrics articleBackupMetrics;

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {

    Duration duration = Duration.between(
        Objects.requireNonNull(stepExecution.getStartTime()),
        Objects.requireNonNull(stepExecution.getEndTime())
    );

    articleBackupMetrics.recordDuration(duration);

    return stepExecution.getExitStatus();
  }
}
