package com.sprint.mission.monew.batch.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

@ExtendWith(MockitoExtension.class)
public class CommentCleanupServiceTest {

  @Mock
  private JobLauncher jobLauncher;

  @Mock
  private Job commentCleanupJob;

  @InjectMocks
  private CommentCleanupService commentCleanupService;

  @Test
  @DisplayName("commentCleanupJob이 JobLauncher를 통해 정상 실행된다")
  void commentCleanupJob이_JobLauncher를_통해_정상_실행된다() throws Exception {
    // given
    when(jobLauncher.run(any(Job.class), any(JobParameters.class)))
        .thenReturn(null);

    // when
    commentCleanupService.executeCleanup();

    // then
    ArgumentCaptor<JobParameters> paramsCaptor = ArgumentCaptor.forClass(JobParameters.class);
    verify(jobLauncher, times(1)).run(eq(commentCleanupJob), paramsCaptor.capture());
    assertThat(paramsCaptor.getValue().getParameters()).containsKey("time");
  }
}
