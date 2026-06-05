package com.sprint.mission.monew.batch.jobconfig;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.monew.batch.reader.NewsCollectReader;
import com.sprint.mission.monew.batch.writer.NewsCollectWriter;
import com.sprint.mission.monew.common.config.NewsCollectJobConfig;
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
    NewsCollectJobConfig.class
})
@TestPropertySource(properties = {
    "batch.news-collect.chunk-size=1000"
})
public class NewsCollectJobConfigTest {

  @MockitoBean
  private JobRepository jobRepository;

  @MockitoBean
  private PlatformTransactionManager transactionManager;

  @MockitoBean
  private NewsCollectReader newsCollectReader;

  @MockitoBean
  private NewsCollectWriter newsCollectWriter;

  @Autowired
  private Job newsCollectJob;

  @Autowired
  private Step newsCollectStep;

  @Nested
  @DisplayName("NewsCollectJobConfig Job, Step 테스트")
  class JobStepTest {

    @Test
    @DisplayName("Job, Step 생성 성공")
    void job_step_생성_성공() {
      // then
      assertThat(newsCollectJob).isNotNull();
      assertThat(newsCollectStep).isNotNull();
      assertThat(newsCollectJob.getName()).isEqualTo("newsCollectJob");
      assertThat(newsCollectStep.getName()).isEqualTo("newsCollectStep");
    }
  }

}
