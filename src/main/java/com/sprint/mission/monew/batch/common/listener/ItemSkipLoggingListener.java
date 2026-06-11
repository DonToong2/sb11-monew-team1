package com.sprint.mission.monew.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemSkipLoggingListener implements SkipListener<Object, Object> {

  @Override
  public void onSkipInRead(Throwable t) {
    log.error("배치 Reader skip | error={}", t.getMessage());
  }

  @Override
  public void onSkipInProcess(Object item, Throwable t) {
    log.error("배치 Processor skip | item={}, error={}", item, t.getMessage());
  }

  @Override
  public void onSkipInWrite(Object item, Throwable t) {
    log.error("배치 Writer skip | item={}, error={}", item, t.getMessage());
  }

}
