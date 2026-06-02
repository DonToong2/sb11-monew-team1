package com.sprint.mission.monew.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.monew.domain.user.repository.UserRepository;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.repeat.RepeatStatus;

@ExtendWith(MockitoExtension.class)
public class UserDeleteTaskletTest {

  @InjectMocks
  private UserDeleteTasklet userDeleteTasklet;

  @Mock
  private UserRepository userRepository;

  private Instant threshold;
  @BeforeEach
  void setUp() {
    threshold = Instant.now();
  }

  @Nested
  @DisplayName("execute 호출하기")
  class Execute {
    @Test
    @DisplayName("execute 호출 - deleteAllByDeletedAtBefore")
    void execute_호출_deleteAllByDeletedAtBefore() {
      // given
      threshold = Instant.now();
      when(userRepository.deleteAllByDeletedAtBefore(any())).thenReturn(3); // 3개가 삭제됨

      // when
      // contribution, chunkContext는 실제 로직에서 사용되지 않음
      RepeatStatus result = userDeleteTasklet.execute(null, null);

      // then
      // 1번 호출됐는지 확인
      verify(userRepository, times(1)).deleteAllByDeletedAtBefore(any());

      assertThat(result).isEqualTo(RepeatStatus.FINISHED);
    }
  }

}
