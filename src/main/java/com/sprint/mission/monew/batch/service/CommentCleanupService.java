package com.sprint.mission.monew.batch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentCleanupService {

  private final JobLauncher jobLauncher;

  @Qualifier("commentCleanupJob")
  private final Job commentCleanupJob;

  public void executeCleanup() {

  }

}
