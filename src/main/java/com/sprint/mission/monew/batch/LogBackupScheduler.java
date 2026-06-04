package com.sprint.mission.monew.batch;

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
public class LogBackupScheduler {

  private final JobLauncher jobLauncher;

  private final Job logBackupJob;

  @Scheduled(cron = "${scheduler.log-upload.cron}")
  public void uploadLogs() throws Exception {
    log.info("로그 백업 배치 시작");

    JobParameters params = new JobParametersBuilder()
        .addLong("time", Instant.now().toEpochMilli())
        .toJobParameters();

    jobLauncher.run(logBackupJob, params);

    log.info("로그 백업 배치 완료");

  }
}
