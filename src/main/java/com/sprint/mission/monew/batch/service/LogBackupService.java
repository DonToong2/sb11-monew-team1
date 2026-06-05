package com.sprint.mission.monew.batch.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogBackupService {

  private final JobLauncher jobLauncher;
  private final Job logBackupJob;

  public void executeBackup() throws Exception {
    JobParameters params = new JobParametersBuilder()
        .addLong("time", Instant.now().toEpochMilli())
        .toJobParameters();
    jobLauncher.run(logBackupJob, params);
  }
}