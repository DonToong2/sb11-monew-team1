package com.sprint.mission.monew.batch.notification.cleanup.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationCleanupJobListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {

  }

  @Override
  public void afterJob(JobExecution jobExecution) {

  }
}
