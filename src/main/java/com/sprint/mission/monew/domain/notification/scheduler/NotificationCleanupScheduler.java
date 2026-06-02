package com.sprint.mission.monew.domain.notification.scheduler;

import com.sprint.mission.monew.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
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
  private final Job notificationDeleteJob;

  @Scheduled(cron = "${scheduler.notification-cleanup.cron}")
  public void cleanUpExpiredNotifications() {
    log.debug("만료 알림 삭제 스케줄러 실행");

    log.info("만료 알림 삭제 완료");
  }
}
