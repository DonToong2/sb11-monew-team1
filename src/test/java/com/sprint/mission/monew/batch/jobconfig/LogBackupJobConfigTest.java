package com.sprint.mission.monew.batch.jobconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.batch.processor.LogBackupProcessor;
import com.sprint.mission.monew.batch.reader.LogBackupReader;
import com.sprint.mission.monew.batch.writer.LogBackupWriter;
import com.sprint.mission.monew.common.config.LogBackupJobConfig;
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
    LogBackupJobConfig.class
})
@TestPropertySource(properties = {
    "batch.log-backup.chunk-size=1000"
})
public class LogBackupJobConfigTest {

  @MockitoBean
  private JobRepository jobRepository;

  @MockitoBean
  private PlatformTransactionManager transactionManager;

  @MockitoBean
  private LogBackupReader logBackupReader;

  @MockitoBean
  private LogBackupProcessor logBackupProcessor;

  @MockitoBean
  private LogBackupWriter logBackupWriter;

  @Autowired
  private Job logBackupJob;

  @Autowired
  private Step logBackupStep;

  @Nested
  @DisplayName("logBackupJobConfig Job, Step 테스트")
  class JobStepTest {

    @Test
    @DisplayName("Job, Step 생성 성공")
    void job_step_생성_성공() {
      // then
      assertThat(logBackupJob).isNotNull();
      assertThat(logBackupStep).isNotNull();
      assertThat(logBackupJob.getName()).isEqualTo("logBackupJob");
      assertThat(logBackupStep.getName()).isEqualTo("logBackupStep");
    }
  }
}
