package com.sprint.mission.monew.batch;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.common.config.UserDeleteJobConfig;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    UserDeleteJobConfig.class,
    UserDeleteTasklet.class
})
public class UserDeleteJobConfigTest {

  @MockitoBean
  private JobRepository jobRepository;

  @MockitoBean
  private PlatformTransactionManager transactionManager;

  @MockitoBean
  private UserRepository userRepository;

  @Autowired
  private Job userDeleteJob;

  @Autowired
  private Step userDeleteStep;

  @Nested
  @DisplayName("UserDeleteJobConfig Job, Step 테스트")
  class JobStepTest {

    @Test
    @DisplayName("Job, Step 생성 성공")
    void job_step_생성_성공() {
      // then
      assertThat(userDeleteJob).isNotNull();
      assertThat(userDeleteStep).isNotNull();
      assertThat(userDeleteJob.getName()).isEqualTo("userDeleteJob");
      assertThat(userDeleteStep.getName()).isEqualTo("userDeleteStep");
    }
  }

}
