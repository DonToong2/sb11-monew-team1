package com.sprint.mission.monew.batch.log.backup.listener;

import com.sprint.mission.monew.batch.log.backup.metrics.LogBackupMetrics;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogBackupStepListener implements StepExecutionListener {

  private final LogBackupMetrics logBackupMetrics;

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {

    Duration duration = Duration.between(
        Objects.requireNonNull(stepExecution.getStartTime()),
        Objects.requireNonNull(stepExecution.getEndTime())
    );

    logBackupMetrics.recordDuration(duration);

    return stepExecution.getExitStatus();
  }

}
