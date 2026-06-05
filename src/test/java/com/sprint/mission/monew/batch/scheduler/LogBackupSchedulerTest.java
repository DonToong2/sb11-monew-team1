package com.sprint.mission.monew.batch.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class LogBackupSchedulerTest {

  @Mock
  private JobLauncher jobLauncher;

  @Mock
  private Job logBackupJob;

  @InjectMocks
  private LogBackupScheduler logBackupScheduler;

  @Nested
  @DisplayName("로그 파일 S3 업로드 스케줄러")
  class UploadLogs {

    @Test
    @DisplayName("uploadLogs 호출 시 LogBackupService에 위임한다")
    void uploadLogs_호출_시_서비스에_위임한다() throws Exception {
      // when
      logBackupScheduler.upload();

      // then
      ArgumentCaptor<JobParameters> captor = ArgumentCaptor.forClass(JobParameters.class);
      then(jobLauncher).should().run(eq(logBackupJob), captor.capture());
      assertThat(captor.getValue().getParameters()).containsKey("time");
    }
  }
}
