package com.sprint.mission.monew.batch.jobconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.batch.listener.UserCleanupStepListener;
import com.sprint.mission.monew.batch.reader.UserCleanupReader;
import com.sprint.mission.monew.batch.writer.UserCleanupWriter;
import com.sprint.mission.monew.common.config.UserCleanupJobConfig;
import com.sprint.mission.monew.domain.user.repository.UserRepository;
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
    UserCleanupJobConfig.class
})
@TestPropertySource(properties = {
    "batch.user-cleanup.chunk-size=1000"
})
public class UserCleanupJobConfigTest {

  @MockitoBean
  private JobRepository jobRepository;

  @MockitoBean
  private PlatformTransactionManager transactionManager;

  @MockitoBean
  private UserRepository userRepository;

  @MockitoBean
  private UserCleanupReader userCleanupReader;

  @MockitoBean
  private UserCleanupWriter userCleanupWriter;

  @MockitoBean
  private UserCleanupStepListener userCleanupStepListener;

  @Autowired
  private Job userCleanupJob;

  @Autowired
  private Step userCleanupStep;

  @Nested
  @DisplayName("UserCleanupJobConfig Job, Step 테스트")
  class JobStepTest {

    @Test
    @DisplayName("Job, Step 생성 성공")
    void job_step_생성_성공() {
      // then
      assertThat(userCleanupJob).isNotNull();
      assertThat(userCleanupStep).isNotNull();
      assertThat(userCleanupJob.getName()).isEqualTo("userCleanupJob");
      assertThat(userCleanupStep.getName()).isEqualTo("userCleanupStep");
    }
  }

}
