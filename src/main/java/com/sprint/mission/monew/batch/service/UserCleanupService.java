package com.sprint.mission.monew.batch.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCleanupService {

  private final JobLauncher jobLauncher;
  private final Job userCleanupJob;

  public void executeCleanup() throws Exception {
    JobParameters params = new JobParametersBuilder()
        .addLong("time", Instant.now().toEpochMilli())
        .toJobParameters();
    jobLauncher.run(userCleanupJob, params);
  }
}
