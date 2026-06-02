package com.sprint.mission.monew.common.config;

import com.sprint.mission.monew.batch.UserDeleteTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class UserDeleteJobConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  private final UserDeleteTasklet userDeleteTasklet;

  @Bean
  public Job userDeleteJob() {
    return new JobBuilder("userDeleteJob", jobRepository)
        .start(userDeleteStep()).build();
  }

  @Bean
  public Step userDeleteStep() {
    return new StepBuilder("userDeleteStep", jobRepository)
        .tasklet(userDeleteTasklet, transactionManager).build();
  }

}
