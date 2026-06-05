package com.sprint.mission.monew.batch.scheduler;

import io.micrometer.core.annotation.Timed;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("prod")
@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCollectScheduler {

  private final JobLauncher jobLauncher;
  private final Job newsCollectJob;

  @Timed(value = "monew.news.collect.job.duration", description = "뉴스 수집 배치 Job 전체 소요 시간")
  @Scheduled(cron = "${scheduler.news-collect.cron}")
  public void collect() throws Exception {
    log.info("뉴스 수집 배치 시작");
    JobParameters params = new JobParametersBuilder()
        .addLong("time", Instant.now().toEpochMilli())
        .toJobParameters();

    jobLauncher.run(newsCollectJob, params);
    log.info("뉴스 수집 배치 완료");
  }
}
