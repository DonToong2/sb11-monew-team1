package com.sprint.mission.monew.batch.log.backup.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.monew.batch.log.backup.metrics.LogBackupMetrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;

@ExtendWith(MockitoExtension.class)
public class LogBackupJobListenerTest {


  @Mock
  LogBackupMetrics logBackupMetrics;

  @InjectMocks
  LogBackupJobListener listener;

  @Mock
  JobExecution jobExecution;

  @Test
  @DisplayName("Job 실패 시 markSuccess는 호출되지 않는다")
  void job_failed_does_not_mark_success() {

    // given
    JobExecution jobExecution = mock(JobExecution.class);
    when(jobExecution.getStatus()).thenReturn(BatchStatus.FAILED);

    // when
    listener.afterJob(jobExecution);

    // then
    verify(logBackupMetrics, never()).markSuccess();
  }

  @Test
  @DisplayName("Job 성공 시 markSuccess가 호출된다")
  void job_success_marks_metrics() {

    // given
    JobExecution jobExecution = mock(JobExecution.class);
    when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);

    // when
    listener.afterJob(jobExecution);

    // then
    verify(logBackupMetrics, times(1)).markSuccess();
  }
}
