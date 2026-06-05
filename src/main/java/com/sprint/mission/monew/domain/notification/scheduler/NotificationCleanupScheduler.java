package com.sprint.mission.monew.domain.notification.scheduler;

import com.sprint.mission.monew.domain.notification.service.NotificationService;
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
@RequiredArgsConstructor
@Component
public class NotificationCleanupScheduler {

  private final JobLauncher jobLauncher;
  private final Job notificationCleanupJob;

  @Timed(value = "monew.notification.cleanup.job.duration", description = "만료 알림 정리 배치 Job 전체 소요 시간")
  @Scheduled(cron = "${scheduler.notification-cleanup.cron}")
  public void cleanUpExpiredNotifications() throws Exception {
    log.debug("만료 알림 삭제 스케줄러 실행");

    JobParameters params = new JobParametersBuilder()
        .addLong("time", Instant.now().toEpochMilli()).toJobParameters();

    jobLauncher.run(notificationCleanupJob, params);

    log.info("만료 알림 삭제 완료");
  }
}
