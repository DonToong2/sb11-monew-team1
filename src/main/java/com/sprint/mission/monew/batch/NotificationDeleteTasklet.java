package com.sprint.mission.monew.batch;

import com.sprint.mission.monew.domain.notification.repository.NotificationRepository;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDeleteTasklet implements Tasklet {

  private final NotificationRepository notificationRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

    Instant cutoff = Instant.now().minus(Duration.ofDays(7));

    log.info("만료 알림 삭제 실행: 기준시점={}", cutoff);

    int deleted = notificationRepository.deleteConfirmedBefore(cutoff);

    log.info("만료 알림 삭제 완료: {}건 삭제", deleted);

    return RepeatStatus.FINISHED;
  }

}
