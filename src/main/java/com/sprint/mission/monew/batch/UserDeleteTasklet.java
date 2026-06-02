package com.sprint.mission.monew.batch;

import com.sprint.mission.monew.domain.user.repository.UserRepository;
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
public class UserDeleteTasklet implements Tasklet {

  private final UserRepository userRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

    Instant threshold = Instant.now().minus(Duration.ofDays(1));

    log.info("물리 삭제 실행: 기준시점={}", threshold);

    int deleted = userRepository.deleteAllByDeletedAtBefore(threshold);

    log.info("물리 삭제 완료: {}건 삭제", deleted);

    return RepeatStatus.FINISHED;

  }


}
