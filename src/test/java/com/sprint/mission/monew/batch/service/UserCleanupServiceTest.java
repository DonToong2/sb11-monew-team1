package com.sprint.mission.monew.batch.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

@ExtendWith(MockitoExtension.class)
class UserCleanupServiceTest {

  @Mock
  private JobLauncher jobLauncher;

  @Mock
  private Job userCleanupJob;

  @InjectMocks
  private NotificationCleanupService userCleanupService;

  @Test
  @DisplayName("logBackupJob이 JobLauncher를 통해 정상 실행된다")
  void executeBackup_success() throws Exception {

    // given
    when(jobLauncher.run(any(Job.class), any(JobParameters.class)))
        .thenReturn(null);

    // when
    userCleanupService.executeCleanup();

    // then
    verify(jobLauncher, times(1))
        .run(eq(userCleanupJob), any(JobParameters.class));
  }
}