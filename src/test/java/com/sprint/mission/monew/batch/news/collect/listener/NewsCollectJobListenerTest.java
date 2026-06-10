package com.sprint.mission.monew.batch.news.collect.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.monew.batch.news.collect.metrics.NewsCollectMetrics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

@ExtendWith(MockitoExtension.class)
public class NewsCollectJobListenerTest {

  @Mock
  NewsCollectMetrics newsCollectMetrics;

  @InjectMocks
  NewsCollectJobListener listener;

  @Test
  @DisplayName("Job 실패 시 markSuccess는 호출되지 않는다")
  void job_실패하면_markSuccess_미호출() {
    // given
    JobExecution jobExecution = mock(JobExecution.class);
    when(jobExecution.getStatus()).thenReturn(BatchStatus.FAILED);

    // when
    listener.afterJob(jobExecution);

    // then
    verify(newsCollectMetrics, never()).markSuccess();
  }

  @Test
  @DisplayName("Job 성공 시 markSuccess가 호출된다")
  void job_성공하면_markSuccess_호출() {
    // given
    JobExecution jobExecution = mock(JobExecution.class);
    when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);

    // when
    listener.afterJob(jobExecution);

    // then
    verify(newsCollectMetrics, times(1)).markSuccess();
  }
}
