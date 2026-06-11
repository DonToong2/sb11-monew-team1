package com.sprint.mission.monew.batch.common.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
public class CleanupSkipLoggingListener implements SkipListener<Object, Object> {

  @Override
  public void onSkipInRead(Throwable t) {

  }

  @Override
  public void onSkipInProcess(Object item, Throwable t) {

  }

  @Override
  public void onSkipInWrite(Object item, Throwable t) {

  }

}
