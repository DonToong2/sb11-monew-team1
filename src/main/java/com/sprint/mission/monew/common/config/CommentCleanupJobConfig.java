package com.sprint.mission.monew.common.config;

import com.sprint.mission.monew.batch.reader.CommentCleanupReader;
import com.sprint.mission.monew.batch.writer.CommentCleanupWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CommentCleanupJobConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  private final CommentCleanupReader commentCleanupReader;
  private final CommentCleanupWriter commentCleanupWriter;

  @Value("${batch.comment-cleanup.chunk-size}")
  private int chunkSize;

  @Bean(name = "commentCleanupJob")
  public Job commentCleanupJob() {
    return null;
  }

  @Bean
  public Step commentCleanupStep() {
    return null;
  }

}
