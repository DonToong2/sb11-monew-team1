package com.sprint.mission.monew.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationDeleteJobConfig {

  @Bean
  public Job notificationDeleteJob() {
    return null;
  }

  @Bean
  public Step notificationDeleteStep() {
    return null;
  }

}
