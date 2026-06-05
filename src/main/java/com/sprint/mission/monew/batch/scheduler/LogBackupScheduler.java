package com.sprint.mission.monew.batch.scheduler;

import com.sprint.mission.monew.batch.service.LogBackupService;
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

  private final LogBackupService logBackupService;

  @Scheduled(cron = "${scheduler.log-upload.cron}")
  public void upload() throws Exception {
    log.info("로그 백업 배치 시작");
    logBackupService.executeBackup();
    log.info("로그 백업 배치 완료");
  }
}
