package com.sprint.mission.monew.domain.notification.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.BeforeEach;
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
class NotificationCleanupSchedulerTest {

  @Mock
  private JobLauncher jobLauncher;

  @Mock
  private Job notificationDeleteJob;

  @InjectMocks
  private NotificationCleanupScheduler scheduler;

  @Test
  @DisplayName("스케줄러가 Batch Job을 호출한다")
  void 스케줄러가_Batch_Job의_만료_알림_삭제_메서드를_호출한다() throws Exception {
    // given

    // when
    scheduler.cleanUpExpiredNotifications();

    // then
    ArgumentCaptor<JobParameters> captor = ArgumentCaptor.forClass(JobParameters.class);
    then(jobLauncher).should().run(eq(notificationDeleteJob), captor.capture());
    assertThat(captor.getValue().getParameters()).containsKey("time");
  }
}