package com.sprint.mission.monew.batch;

import com.sprint.mission.monew.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationDeleteTasklet implements Tasklet {

  private final NotificationRepository notificationRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    throw new UnsupportedOperationException();
  }

}
