package com.sprint.mission.monew.batch.log.backup.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.monew.batch.log.backup.metrics.LogBackupMetrics;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;

@ExtendWith(MockitoExtension.class)
public class LogBackupStepListenerTest {

  @Mock
  LogBackupMetrics logBackupMetrics;

  @InjectMocks
  LogBackupStepListener listener;

  @Mock
  StepExecution stepExecution;

  @Test
  @DisplayName("StepExecution recordDuration를 metrics로 전달한다")
  void 스텝_실행_후_메트릭스가_기록된다() {

    // given
    LocalDateTime start = LocalDateTime.of(2026, 6, 10, 10, 0, 0);
    LocalDateTime end = LocalDateTime.of(2026, 6, 10, 10, 0, 5);

    given(stepExecution.getStartTime()).willReturn(start);
    given(stepExecution.getEndTime()).willReturn(end);
    given(stepExecution.getExitStatus()).willReturn(ExitStatus.COMPLETED);

    // when
    ExitStatus result = listener.afterStep(stepExecution);

    // then
    then(logBackupMetrics).should().recordDuration(Duration.ofSeconds(5));
    assertThat(result).isEqualTo(ExitStatus.COMPLETED);
  }
}
