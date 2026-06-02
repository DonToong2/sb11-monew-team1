package com.sprint.mission.monew.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.monew.domain.notification.repository.NotificationRepository;
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
public class NotificationDeleteTaskletTest {

  @InjectMocks
  private NotificationDeleteTasklet notificationDeleteTasklet;

  @Mock
  private NotificationRepository notificationRepository;

  private Instant threshold;

  @BeforeEach
  void setUp() {
    threshold = Instant.now();
  }

  @Nested
  @DisplayName("execute 호출하기")
  class Execute {

    @Test
    @DisplayName("execute 호출 - deleteConfirmedBefore")
    void execute_호출_deleteConfirmedBefore() {
      // given
      when(notificationRepository.deleteConfirmedBefore(any())).thenReturn(3);

      // when
      RepeatStatus result = notificationDeleteTasklet.execute(null, null);

      // then
      verify(notificationRepository, times(1)).deleteConfirmedBefore(any());
      assertThat(result).isEqualTo(RepeatStatus.FINISHED);
    }
  }

}
