package com.sprint.mission.monew.batch;

import com.sprint.mission.monew.domain.user.repository.UserRepository;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeleteTasklet implements Tasklet {

  private final UserRepository userRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    Instant threshold = Instant.now().minus(Duration.ofDays(1));

    userRepository.deleteAllByDeletedAtBefore(threshold);

    return RepeatStatus.FINISHED;

  }


}
