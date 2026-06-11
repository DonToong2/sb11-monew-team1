package com.sprint.mission.monew.batch.news.collect.config;

import com.sprint.mission.monew.batch.common.listener.ItemSkipLoggingListener;
import com.sprint.mission.monew.batch.news.collect.dto.NewsCollectItem;
import com.sprint.mission.monew.batch.news.collect.listener.NewsCollectJobListener;
import com.sprint.mission.monew.batch.news.collect.listener.NewsCollectStepListener;
import com.sprint.mission.monew.batch.news.collect.reader.NewsCollectReader;
import com.sprint.mission.monew.batch.news.collect.writer.NewsCollectWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class NewsCollectJobConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  private final NewsCollectJobListener newsCollectJobListener;
  private final ItemSkipLoggingListener itemSkipLoggingListener;
  private final NewsCollectReader newsCollectReader;
  private final NewsCollectWriter newsCollectWriter;
  private final NewsCollectStepListener newsCollectStepListener;

  @Value("${batch.news-collect.chunk-size}")
  private int chunkSize;

  @Bean(name = "newsCollectJob")
  public Job newsCollectJob() {
    return new JobBuilder("newsCollectJob", jobRepository)
        .listener(newsCollectJobListener)
        .start(newsCollectStep()).build();
  }

  @Bean
  public Step newsCollectStep() {
    return new StepBuilder("newsCollectStep", jobRepository)
        .<NewsCollectItem, NewsCollectItem>chunk(chunkSize, transactionManager)
        .reader(newsCollectReader)
        .writer(newsCollectWriter)
        .faultTolerant()
        .skip(Exception.class)
        .noSkip(OutOfMemoryError.class)
        .skipLimit(200)
        .retryLimit(3)
        .retry(TransientDataAccessException.class)
        .listener(itemSkipLoggingListener)
        .listener(newsCollectStepListener)
        .build();
  }
}
