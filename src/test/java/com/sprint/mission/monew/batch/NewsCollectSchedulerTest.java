package com.sprint.mission.monew.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
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
class NewsCollectSchedulerTest {

  @Mock
  private JobLauncher jobLauncher;

  @Mock
  private Job newsCollectJob;

  @InjectMocks
  private NewsCollectScheduler scheduler;
  
  @Nested
  @DisplayName("뉴스 수집 스케줄러")
  class Collect {

    @Test
    @DisplayName("스케줄러가 Batch Job을 호출한다")
    void 스케줄러가_Batch_Job의_collect_메서드를_호출한다() throws Exception {
      // given

      // when
      scheduler.collect();

      // then
      ArgumentCaptor<JobParameters> captor = ArgumentCaptor.forClass(JobParameters.class);
      then(jobLauncher).should().run(eq(newsCollectJob), captor.capture());
      assertThat(captor.getValue().getParameters()).containsKey("time");
    }
  }
}
