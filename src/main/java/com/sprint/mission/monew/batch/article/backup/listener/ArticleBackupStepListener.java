package com.sprint.mission.monew.batch.article.backup.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleBackupStepListener implements StepExecutionListener {

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    return null;
  }
}
