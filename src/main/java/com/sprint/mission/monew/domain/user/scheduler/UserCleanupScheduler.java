package com.sprint.mission.monew.domain.user.scheduler;

import com.sprint.mission.monew.domain.user.service.UserService;
import io.micrometer.core.annotation.Timed;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
public class UserCleanupScheduler {

  private final JobLauncher jobLauncher;
  private final Job userDeleteJob;

  @Timed(value = "monew.user.cleanup.duration", description = "만료 사용자 물리 삭제 배치 1회 소요 시간")
  @Scheduled(cron = "${scheduler.user-cleanup.cron}")
  public void cleanUpDeletedUsers() throws Exception {
    log.debug("물리 삭제 스케줄러 실행");

    JobParameters params = new JobParametersBuilder()
        .addLong("time", Instant.now().toEpochMilli()).toJobParameters();

    jobLauncher.run(userDeleteJob, params);

    log.info("물리 삭제 완료");
  }
}
