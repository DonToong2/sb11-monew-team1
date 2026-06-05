package com.sprint.mission.monew.batch.jobconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.batch.listener.NotificationCleanupStepListener;
import com.sprint.mission.monew.batch.reader.NotificationCleanupReader;
import com.sprint.mission.monew.batch.writer.NotificationCleanupWriter;
import com.sprint.mission.monew.common.config.NotificationCleanupJobConfig;
import com.sprint.mission.monew.domain.notification.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    NotificationCleanupJobConfig.class
})
@TestPropertySource(properties = {
    "batch.notification-cleanup.chunk-size=1000"
})
public class NotificationCleanupJobConfigTest {
  @MockitoBean
  private JobRepository jobRepository;

  @MockitoBean
  private PlatformTransactionManager transactionManager;

  @MockitoBean
  private NotificationRepository notificationRepository;

  @MockitoBean
  private NotificationCleanupReader notificationCleanupReader;

  @MockitoBean
  private NotificationCleanupWriter notificationCleanupWriter;

  @MockitoBean
  private NotificationCleanupStepListener notificationCleanupStepListener;

  @Autowired
  private Job notificationCleanupJob;

  @Autowired
  private Step notificationCleanupStep;

  @Nested
  @DisplayName("NotificationCleanupJobConfig Job, Step 테스트")
  class JobStepTest {

    @Test
    @DisplayName("Job, Step 생성 성공")
    void job_step_생성_성공() {
      // then
      assertThat(notificationCleanupJob).isNotNull();
      assertThat(notificationCleanupStep).isNotNull();
      assertThat(notificationCleanupJob.getName()).isEqualTo("notificationCleanupJob");
      assertThat(notificationCleanupStep.getName()).isEqualTo("notificationCleanupStep");
    }
  }
}
